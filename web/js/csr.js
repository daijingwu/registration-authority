/* jshint browser: true */
/* jshint jquery */

"use strict";


const signCsr = function () {
    const div_create = $('#create');
    const lbl_password = $('#lbl_password');
    const div_password = $('#password');
    const labelPassword="Your Password:";
    const friendlyName="ip6li Demo PKI";

    const ajaxData={};
    ajaxData.pkcs10=window.csr.pkcs10;
    JSON.stringify(ajaxData);

    div_create.hide();
    openDialog();
    const jqxhr = $.ajax({
        url: "req",
        method: "POST",
        data: JSON.stringify(ajaxData)
    })
        .done(function(json) {
            window.csr.certificateChain=JSON.parse(json).certificateChain;
            closeDialog();
            div_create.hide();

            genPKCS12();
            lbl_password.empty().append(labelPassword);
            div_password.empty().append(password);
        })
        .fail(function() {
            alert( "error" );
        })
        .always(function() {
            alert( "complete" );
        });
};


const createCSR = function () {
    window.csr = {};
    window.csr.subjectAltNames = {};
    window.csr.c = $("#c").val();
    window.csr.cn = $("#cn").val();
    window.csr.e = $("#e").val();
    window.csr.subjectAltNames.rfc822Name = window.csr.e;

    console.log ("create pkcs#10: %o", window.csr);

    createPKCS10(function() {
        debugPkcs10Data();
        signCsr();
    });
};


function genPKCS12() {
    window.csr.password=genPassword();
    passwordPrivacy();
}


function genPassword() {
    let text="";
    const possible="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!§$%&/()=?#'+*~-_.:,;|";
    for (let i=0;i<32;i++) {
        text+=possible.charAt(Math.floor(Math.random()*possible.length));
    }
    return text;
}


function arrayBufferToBase64String(arrayBuffer) {
    const byteArray = new Uint8Array(arrayBuffer)
    let byteString = "";
    for (let i=0; i<byteArray.byteLength; i++) {
        byteString += String.fromCharCode(byteArray[i]);
    }
    return btoa(byteString);
}


function openDialog() {
    $("#spinner").dialog("open");
    setTimeout(
        function(){
            closeDialog();
        } , 10000);
}


function closeDialog() {
    const spinner = $("#spinner");
    spinner.dialog("close");
}


const loadCountries = function () {
    $.get("json/countries.min.json")
        .then(function(data) {
            const countryfield=$("#c");
            const countries=data.countries;
            $.each(countries,function(key,value) {
                countryfield
                    .append($("<option></option>")
                        .attr("value",key)
                        .text(value.name));
            })

        });
};


const debugPkcs10Data = function () {
    console.log ("PKCS#10");
    console.log ("=======");
    console.log ("%o", window.csr.pkcs10);
    console.log ("=====================================================================");
    console.log ("private key");
    console.log ("===========");
    console.log ("%o", window.csr.privateKey);
    console.log ("=====================================================================");
};


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

    window.createCSR = createCSR;

});
