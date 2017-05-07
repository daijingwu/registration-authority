/* jshint browser: true */
/* jshint jquery */

"use strict";


let configuration = null;

function destroyClickedElement(event) {
    document.body.removeChild(event.target);
}


const signCsr = function () {
    const div_create = $('#create');
    const lbl_password = $('#lbl_password');
    const div_password = $('#password');
    const labelPassword = "Your Password:";
    const friendlyName = configuration.downloadName;

    const ajaxData = {};
    ajaxData.pkcs10 = window.csr.pkcs10;
    JSON.stringify(ajaxData);

    div_create.hide();
    openDialog();
    const jqxhr = $.ajax({
        url: "req",
        method: "POST",
        data: JSON.stringify(ajaxData)
    })
        .done(function (json) {
            window.csr.certificateChain = JSON.parse(json).certificateChain;
            closeDialog();
            div_create.hide();
            window.csr.password = genPassword();
            window.csr.friendlyName = friendlyName;
            genPKCS12();
            lbl_password.empty().append(labelPassword);
            div_password.empty().append(window.csr.password);
        })
        .fail(function () {
            alert("error");
        })
        .always(function () {
            alert("complete");
        });
};


const createCSR = function () {
    window.csr = {};
    window.csr.subjectAltNames = {};
    window.csr.c = $("#c").val();
    window.csr.cn = $("#cn").val();
    window.csr.e = $("#e").val();
    window.csr.subjectAltNames.rfc822Name = window.csr.e;

    createPKCS10(function () {
        signCsr();
    });
};

function b64toBlob(b64Data, contentType, sliceSize) {
    contentType = contentType || '';
    sliceSize = sliceSize || 512;

    const byteCharacters = atob(b64Data);
    const byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        const slice = byteCharacters.slice(offset, offset + sliceSize);

        const byteNumbers = new Array(slice.length);
        for (let i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }

        const byteArray = new Uint8Array(byteNumbers);

        byteArrays.push(byteArray);
    }

    return new Blob(byteArrays, {type: contentType});
}


function genPKCS12() {
    const pki = forge.pki;
    const p12PrivateKey = pki.privateKeyFromPem(window.csr.privateKey);

    // generate a p12 that can be imported by Chrome/Firefox
    // (requires the use of Triple DES instead of AES)
    const p12Asn1 = forge.pkcs12.toPkcs12Asn1(
        p12PrivateKey,
        window.csr.certificateChain,
        window.csr.password,
        {
            algorithm: '3des',
            friendlyName: window.csr.friendlyName
        });

    // base64-encode p12
    let p12Der = forge.asn1.toDer(p12Asn1).getBytes();
    const downloadLink = document.createElement("a");
    downloadLink.download = configuration.downloadName + ".p12";
    downloadLink.innerHTML = "Download File";
    downloadLink.href = window.URL.createObjectURL(b64toBlob(forge.util.encode64(p12Der), {type: "application/x-pkcs12"}));
    downloadLink.onclick = destroyClickedElement;
    downloadLink.style.display = "none";
    document.body.appendChild(downloadLink);

    downloadLink.click();
}


function genPassword() {
    let text = "";
    //const possible="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!§$%&/()=?#'+*~-_.:,;|";
    const possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for (let i = 0; i < 32; i++) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
}


function openDialog() {
    $("#spinner").dialog("open");
    setTimeout(
        function () {
            closeDialog();
        }, 10000);
}


function closeDialog() {
    const spinner = $("#spinner");
    spinner.dialog("close");
}


const loadCountries = function () {
    $.get("json/countries.min.json")
        .then(function (data) {
            const countryfield = $("#c");
            const countries = data.countries;
            $.each(countries, function (key, value) {
                countryfield
                    .append($("<option></option>")
                        .attr("value", key)
                        .text(value.name));
            })

        });
};


const fillLocalizedText = function () {
    const langData = configuration.bundle;
    for (const key in langData) {
        if (langData.hasOwnProperty(key)) {
            const div = $("#" + key);
            const v = langData[key];
            div.empty().append(v);
        }
    }
};


$(document).ready(function () {
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

    const body = $("body");
    configuration = JSON.parse(body.attr("jsp_configurationString"));
    //fillLocalizedText();
    loadCountries();

    window.createCSR = createCSR;

});
