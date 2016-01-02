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

    var hash_algorithm;
    //var hash_option = document.getElementById("hash_alg").value;
    var hash_option = "alg_SHA256";
    switch (hash_option) {
        case "alg_SHA1":
            hash_algorithm = "sha-1";
            break;
        case "alg_SHA256":
            hash_algorithm = "sha-256";
            break;
        case "alg_SHA384":
            hash_algorithm = "sha-384";
            break;
        case "alg_SHA512":
            hash_algorithm = "sha-512";
            break;
        default:
            ;
    }

    var signature_algorithm_name;
    //var sign_option = document.getElementById("sign_alg").value;
    var sign_option = "alg_RSA15";
    switch (sign_option) {
        case "alg_RSA15":
            signature_algorithm_name = "RSASSA-PKCS1-V1_5";
            break;
        case "alg_RSA2":
            signature_algorithm_name = "RSA-PSS";
            break;
        case "alg_ECDSA":
            signature_algorithm_name = "ECDSA";
            break;
        default:
            ;
    }
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
    pkcs10_simpl.subject.types_and_values.push(
        new org.pkijs.simpl.ATTR_TYPE_AND_VALUE(
            {type: "2.5.4.6", value: new org.pkijs.asn1.PRINTABLESTRING({value: country})}
        ));
    pkcs10_simpl.subject.types_and_values.push(
        new org.pkijs.simpl.ATTR_TYPE_AND_VALUE(
            {type: "2.5.4.3", value: new org.pkijs.asn1.UTF8STRING({value: cn})}
        ));
    pkcs10_simpl.subject.types_and_values.push(
        new org.pkijs.simpl.ATTR_TYPE_AND_VALUE(
            {type: "1.2.840.113549.1.9.1", value: new org.pkijs.asn1.UTF8STRING({value: email})}
        ));

    pkcs10_simpl.attributes = new Array();
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
            console.log("Fucked up while export private key: " + error);
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
                type: "1.2.840.113549.1.9.14", // pkcs-9-at-extensionRequest
                values: [(new org.pkijs.simpl.EXTENSIONS({
                    extensions_array: [
                        new org.pkijs.simpl.EXTENSION({
                            extnID: "2.5.29.14",
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
        function (result) {
            var pkcs10_schema = pkcs10_simpl.toSchema();
            var pkcs10_encoded = pkcs10_schema.toBER(false);

            var result_string = "-----BEGIN CERTIFICATE REQUEST-----\r\n";
            result_string = result_string + formatPEM(window.btoa(arrayBufferToString(pkcs10_encoded)));
            result_string = result_string + "\r\n-----END CERTIFICATE REQUEST-----\r\n";
            //$('#pem-text-block').val(result_string);
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
                .done(function(certificateChain) {
                    closeDialog();
                    $('#create').hide();
                    var password=genPassword();
                    var uriContent=genPKCS12(privateKeyPem,certificateChain,password);
                    var labelPassword="Your Password:";
                    $('#lbl_password').empty().append(labelPassword);
                    $('#password').empty().append(password);

                    var a = document.createElement("a");
                    a.download = "certificate.p12";
                    a.filename = "certificate.p12";
                    a.title = "download certificate";
                    a.href = uriContent;

                    //window.location.href=uriContent;
                    window.location.href=a;
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


function genPKCS12(privateKey,certificateChain,password) {

    var pki = forge.pki;
    var privateKey = pki.privateKeyFromPem(privateKey);

    // generate a p12 using AES (default)
    //var p12Asn1 = forge.pkcs12.toPkcs12Asn1(
    //  privateKey, certificateChain, 'password');
    var p12Asn1 = forge.pkcs12.toPkcs12Asn1(
        privateKey, certificateChain, password);

    // generate a p12 that can be imported by Chrome/Firefox
    // (requires the use of Triple DES instead of AES)
    var p12Asn1 = forge.pkcs12.toPkcs12Asn1(
        privateKey, certificateChain, password,
        {algorithm: '3des'});

    // base64-encode p12
    var p12Der = forge.asn1.toDer(p12Asn1).getBytes();
    var p12b64 = forge.util.encode64(p12Der);

    var uriContent = "data:application/x-pkcs12;charset=utf-8;base64," + p12b64;

    return uriContent;
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
            var countryfield=$("#country");
            var countries=data.countries;
            $.each(countries,function(key,value) {
                countryfield
                    .append($("<option></option>")
                        .attr("value",key)
                        .text(value.name));
            })

        });
}

