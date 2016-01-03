"use strict";

var oid_extnID="2.5.29.14";
var oid_type="1.2.840.113549.1.9.14";

var oid={
    cn: "2.5.4.3",
    c: "2.5.4.6",
    e: "1.2.840.113549.1.9.1",
    o: "2.5.4.10",
    ou: "2.5.4.11",
    l: "2.5.4.7",
    st: "2.5.4.8",
    street: "2.5.4.9"
};

var oid_syntax={
    cn: "UTF8STRING",
    c: "PRINTABLESTRING",
    e: "UTF8STRING",
    o: "UTF8STRING",
    ou: "UTF8STRING",
    l: "UTF8STRING",
    st: "UTF8STRING",
    street: "UTF8STRING"
};


function buildSubject(pkcs10_simpl) {
    $.each(schema,function(k,v){
        var fv=$("#"+k).val();
        if (typeof fv!=="undefined") {
            var syntax=oid_syntax[k];
            switch (syntax) {
                case "PRINTABLESTRING":
                    pkcs10_simpl.subject.types_and_values.push(
                        new org.pkijs.simpl.ATTR_TYPE_AND_VALUE(
                            {type: oid[k], value: new org.pkijs.asn1.PRINTABLESTRING({value: fv})}
                        ));
                    break;
                case "UTF8STRING":
                    pkcs10_simpl.subject.types_and_values.push(
                        new org.pkijs.simpl.ATTR_TYPE_AND_VALUE(
                            {type: oid[k], value: new org.pkijs.asn1.UTF8STRING({value: fv})}
                        ));
                    break;
            }
        }
    });

    return pkcs10_simpl;
}


//*********************************************************************************
// #region Auxiliary functions
//*********************************************************************************
function formatPEM(pem_string) {
    /// <summary>Format string in order to have each line with length equal to 63</summary>
    /// <param name="pem_string" type="String">String to format</param>

    var string_length = pem_string.length;
    var result_string = "";

    for (var i = 0, count = 0; i < string_length; i++, count++) {
        if (count > 63) {
            result_string = result_string + "\r\n";
            count = 0;
        }

        result_string = result_string + pem_string[i];
    }

    return result_string;
}
//*********************************************************************************
function arrayBufferToString(buffer) {
    /// <summary>Create a string from ArrayBuffer</summary>
    /// <param name="buffer" type="ArrayBuffer">ArrayBuffer to create a string from</param>

    var result_string = "";
    var view = new Uint8Array(buffer);

    for (var i = 0; i < view.length; i++)
        result_string = result_string + String.fromCharCode(view[i]);

    return result_string;
}
//*********************************************************************************
function stringToArrayBuffer(str) {
    /// <summary>Create an ArrayBuffer from string</summary>
    /// <param name="str" type="String">String to create ArrayBuffer from</param>

    var stringLength = str.length;

    var resultBuffer = new ArrayBuffer(stringLength);
    var resultView = new Uint8Array(resultBuffer);

    for (var i = 0; i < stringLength; i++)
        resultView[i] = str.charCodeAt(i);

    return resultBuffer;
}
//*********************************************************************************
// #endregion
//*********************************************************************************
// #region Create PKCS#10
//*********************************************************************************
function create_PKCS10(cn, email, country) {
    // #region Initial variables
    var sequence = Promise.resolve();

    var pkcs10_simpl = new org.pkijs.simpl.PKCS10();

    var publicKey;
    var privateKey;
    var privateKeyPem;
    var csr=null;

    //var hash_algorithm = "sha-1"; // broken
    var hash_algorithm = "sha-256"; // works best
    //var hash_algorithm = "sha-384";
    //var hash_algorithm = "sha-512";

    var signature_algorithm_name = "RSASSA-PKCS1-V1_5"; // Works with all
    //var signature_algorithm_name = "RSA-PSS"; // Chrome ok, FF: nok
    //var signature_algorithm_name = "ECDSA"; // Anyone?
    // #endregion

    // #region Get a "crypto" extension
    var crypto = org.pkijs.getCrypto();
    if (typeof crypto == "undefined") {
        alert("No WebCrypto extension found");
        return;
    }
    // #endregion

    // #region Put a static values
    pkcs10_simpl.version = 0;

    $.each(schema,function(k,v){
        var fv=$("#"+k).val();
        if (typeof fv!=="undefined") {
            var syntax=oid_syntax[k];
            var subj_oid=oid[k];
            switch (syntax) {
                case "PRINTABLESTRING":
                    pkcs10_simpl.subject.types_and_values.push(
                        new org.pkijs.simpl.ATTR_TYPE_AND_VALUE(
                            {type: subj_oid, value: new org.pkijs.asn1.PRINTABLESTRING({value: fv})}
                        ));
                    break;
                case "UTF8STRING":
                    pkcs10_simpl.subject.types_and_values.push(
                        new org.pkijs.simpl.ATTR_TYPE_AND_VALUE(
                            {type: subj_oid, value: new org.pkijs.asn1.UTF8STRING({value: fv})}
                        ));
                    break;
            }
        }
    });

    pkcs10_simpl.attributes = [];
    // #endregion

    // #region Create a new key pair
    sequence = sequence.then(
        function () {
            // #region Get default algorithm parameters for key generation
            var algorithm = org.pkijs.getAlgorithmParameters(signature_algorithm_name, "generatekey");
            if ("hash" in algorithm.algorithm)
                algorithm.algorithm.hash.name = hash_algorithm;
            // #endregion

            return crypto.generateKey(algorithm.algorithm, true, algorithm.usages);
        }
    );
    // #endregion

    // #region Store new key in an interim variables
    sequence = sequence.then(
        function (keyPair) {
            publicKey = keyPair.publicKey;
            privateKey = keyPair.privateKey;
            return exportPrivateKey(keyPair);
        },
        function (error) {
            alert("Error during key generation: " + error);
        }
    );
    // #endregion

    sequence = sequence.then(
        function (result) {
            privateKeyPem=result;
        },
        function (error) {
            alert("Fucked up while export private key: " + error);
        }
    );

    // #region Exporting public key into "subjectPublicKeyInfo" value of PKCS#10
    sequence = sequence.then(
        function (result) {
            return pkcs10_simpl.subjectPublicKeyInfo.importKey(publicKey);
        },
        function (error) {
            alert("Fucked up while export public key");
        }
    );
    // #endregion

    // #region SubjectKeyIdentifier
    sequence = sequence.then(
        function (result) {
            return crypto.digest({name: "SHA-1"}, pkcs10_simpl.subjectPublicKeyInfo.subjectPublicKey.value_block.value_hex);
        }
    ).then(
        function (result) {
            pkcs10_simpl.attributes.push(new org.pkijs.simpl.ATTRIBUTE({
                type: oid_type, // pkcs-9-at-extensionRequest
                values: [(new org.pkijs.simpl.EXTENSIONS({
                    extensions_array: [
                        new org.pkijs.simpl.EXTENSION({
                            extnID: oid_extnID,
                            critical: false,
                            extnValue: (new org.pkijs.asn1.OCTETSTRING({value_hex: result})).toBER(false)
                        })
                    ]
                })).toSchema()]
            }));
        }
    );
    // #endregion

    // #region Signing final PKCS#10 request
    sequence = sequence.then(
        function () {
            return pkcs10_simpl.sign(privateKey, hash_algorithm);
        },
        function (error) {
            alert("Error during exporting public key: " + error);
        }
    );
    // #endregion

    sequence = sequence.then(
        function () {
            var pkcs10_schema = pkcs10_simpl.toSchema();
            var pkcs10_encoded = pkcs10_schema.toBER(false);

            var result_string = "-----BEGIN CERTIFICATE REQUEST-----\r\n";
            result_string = result_string + formatPEM(window.btoa(arrayBufferToString(pkcs10_encoded)));
            result_string = result_string + "\r\n-----END CERTIFICATE REQUEST-----\r\n";
            return result_string;
        },
        function (error) {
            alert("Error signing PKCS#10: " + error);
        }
    );

    sequence.then(
        function(result) {
            var ajaxData={};
            ajaxData.pkcs10=result;
            JSON.stringify(ajaxData);

            $('create').hide();
            openDialog();
            var jqxhr = $.ajax({
                    url: "req",
                    method: "POST",
                    data: JSON.stringify(ajaxData)
                })
                .done(function(json) {
                    var certificateChain=JSON.parse(json).certificateChain;
                    closeDialog();
                    $('#create').hide();
                    var password=genPassword();
                    var friendlyName="ip6li Demo PKI";
                    var uriContent=genPKCS12(privateKeyPem,certificateChain,password,friendlyName);
                    var labelPassword="Your Password:";
                    $('#lbl_password').empty().append(labelPassword);
                    $('#password').empty().append(password);

                    saveData(b64toBlob(uriContent,"application/x-pkcs12"),"certificate.p12");
                })
                .fail(function() {
                    alert( "error" );
                })
                .always(function() {
                    //alert( "complete" );
                });
        }
    )
}


var saveData = (function () {
    var a = document.createElement("a");
    document.body.appendChild(a);
    try {
        a.style = "display: none";
    } catch (e) {
        // do nothing
    }
    return function (data, fileName) {
        var url = window.URL.createObjectURL(data);
        a.href = url;
        a.download = fileName;
        a.id="certificate";
        a.click();
    };
}());




function b64toBlob(b64Data, contentType, sliceSize) {
    contentType = contentType || '';
    sliceSize = sliceSize || 512;

    var byteCharacters = atob(b64Data);
    var byteArrays = [];

    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        var slice = byteCharacters.slice(offset, offset + sliceSize);

        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }

        var byteArray = new Uint8Array(byteNumbers);

        byteArrays.push(byteArray);
    }

    return new Blob(byteArrays, {type: contentType});
}


function genPKCS12(privateKey, certificateChain, password, name) {

    var pki = forge.pki;
    var p12PrivateKey = pki.privateKeyFromPem(privateKey);

    // generate a p12 using AES (default)
    //var p12Asn1 = forge.pkcs12.toPkcs12Asn1(
    //  privateKey, certificateChain, 'password');

    // generate a p12 that can be imported by Chrome/Firefox
    // (requires the use of Triple DES instead of AES)
    var p12Asn1 = forge.pkcs12.toPkcs12Asn1(
        p12PrivateKey, certificateChain, password,
        {algorithm: '3des',friendlyName: name});

    // base64-encode p12
    var p12Der = forge.asn1.toDer(p12Asn1).getBytes();
    return forge.util.encode64(p12Der);
}


function genPassword() {
    var text="";
    var possible="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!§$%&/()=?#'+*~-_.:,;|";
    for (var i=0;i<32;i++) {
        text+=possible.charAt(Math.floor(Math.random()*possible.length));
    }
    return text;
}


function arrayBufferToBase64String(arrayBuffer) {
    var byteArray = new Uint8Array(arrayBuffer)
    var byteString = '';
    for (var i=0; i<byteArray.byteLength; i++) {
        byteString += String.fromCharCode(byteArray[i]);
    }
    return btoa(byteString);
}


function convertBinaryToPem(binaryData, label) {
    var base64Cert = arrayBufferToBase64String(binaryData);
    var pemCert = "-----BEGIN " + label + "-----\r\n";
    var nextIndex = 0;
    var lineLength;
    while (nextIndex < base64Cert.length) {
        if (nextIndex + 64 <= base64Cert.length) {
            pemCert += base64Cert.substr(nextIndex, 64) + "\r\n";
        } else {
            pemCert += base64Cert.substr(nextIndex) + "\r\n";
        }
        nextIndex += 64;
    }
    pemCert += "-----END " + label + "-----\r\n";
    return pemCert;
}


function exportPrivateKey(keys) {
    return new Promise(function(resolve) {
        var expK=window.crypto.subtle.exportKey('pkcs8',keys.privateKey);
        expK.then(function(pkcs8) {
            resolve(convertBinaryToPem(pkcs8,"RSA PRIVATE KEY"));
        });
    });
}


function openDialog() {
    $("#spinner").dialog("open");
    setTimeout(
        function(){
            closeDialog();
        } , 10000);
}


function closeDialog() {
    $("#spinner").dialog("close");
}


$(document).ready(function() {
    $("#spinner").dialog({
        draggable: false,
        resizable: false,
        hide: false,
        show: false,
        autoOpen: false,
        modal: true,
        height: 256,
        width: 256,
    });

    loadCountries();

});


function loadCountries () {
    $.get("json/countries.min.json")
        .then(function(data) {
            var countryfield=$("#c");
            var countries=data.countries;
            $.each(countries,function(key,value) {
                countryfield
                    .append($("<option></option>")
                        .attr("value",key)
                        .text(value.name));
            })

        });
}

