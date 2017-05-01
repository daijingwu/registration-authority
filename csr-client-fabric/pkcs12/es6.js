import * as asn1js from "asn1js";
import {stringToArrayBuffer, fromBase64} from "pvutils";
import {getRandomValues, setEngine} from "../PKI.js/src/common";
import Certificate from "../PKI.js/src/Certificate";
import PrivateKeyInfo from "../PKI.js/src/PrivateKeyInfo";
import AuthenticatedSafe from "../PKI.js/src/AuthenticatedSafe";
import SafeContents from "../PKI.js/src/SafeContents";
import SafeBag from "../PKI.js/src/SafeBag";
import CertBag from "../PKI.js/src/CertBag";
import PFX from "../PKI.js/src/PFX";
import Attribute from "../PKI.js/src/Attribute";
import PKCS8ShroudedKeyBag from "../PKI.js/src/PKCS8ShroudedKeyBag";
//*********************************************************************************
//region Auxiliary functions
//*********************************************************************************
function destroyClickedElement(event) {
    document.body.removeChild(event.target);
}
//*********************************************************************************
const getBase64FromPEM = function (pem) {
    const startChars = /-----BEGIN CERTIFICATE-----|-----BEGIN PRIVATE KEY-----/;
    const endChars = /-----END CERTIFICATE-----|-----END PRIVATE KEY-----/;
    const endLineChars = /(?:\r\n|\r|\n)/g;

    let b64 = pem.replace (startChars, "");
    b64 = b64.replace (endChars, "");
    b64 = b64.replace (endLineChars, "");
    console.log ("pkcs12 pem: %o", b64);

    return b64;
};
//*********************************************************************************
const generateSafeContents = function (keyLocalIDBuffer, certLocalIDBuffer) {
    const asn1 = asn1js.fromBER(stringToArrayBuffer(fromBase64(getBase64FromPEM(window.csr.privateKey))));
    const pkcs8Simpl = new PrivateKeyInfo({schema: asn1.result});

    const keyLocalIDBuffer = new ArrayBuffer(4);
    const keyLocalIDView = new Uint8Array(keyLocalIDBuffer);
    getRandomValues(keyLocalIDView);

    const bitArray = new ArrayBuffer(1);
    const bitView = new Uint8Array(bitArray);

    bitView[0] = bitView[0] | 0x80;

    const keyUsage = new asn1js.BitString({
        valueHex: bitArray,
        unusedBits: 7
    });

    pkcs8Simpl.attributes = [
        new Attribute({
            type: "2.5.29.15",
            values: [
                keyUsage
            ]
        })
    ];

    const safeContents = [
        {
            privacyMode: 0, // "No-privacy" Protection Mode
            value: new SafeContents({
                safeBags: [
                    new SafeBag({
                        bagId: "1.2.840.113549.1.12.10.1.2",
                        bagValue: new PKCS8ShroudedKeyBag({
                            parsedValue: pkcs8Simpl
                        }),
                        bagAttributes: [
                            new Attribute({
                                type: "1.2.840.113549.1.9.20", // friendlyName
                                values: [
                                    new asn1js.BmpString({value: window.csr.friendlyName})
                                ]
                            }),
                            new Attribute({
                                type: "1.2.840.113549.1.9.21", // localKeyID
                                values: [
                                    new asn1js.OctetString({valueHex: keyLocalIDBuffer})
                                ]
                            }),
                            new Attribute({
                                type: "1.3.6.1.4.1.311.17.1", // pkcs12KeyProviderNameAttr
                                values: [
                                    new asn1js.BmpString({value: "http://www.pkijs.org"})
                                ]
                            })
                        ]
                    })
                ]
            })
        }
    ];

    //const numCerts = window.csr.certificateChain.length;
    const numCerts = 1;
    for (let i=0;i<numCerts;i++) {
        const asn1 = asn1js.fromBER(stringToArrayBuffer(fromBase64(getBase64FromPEM(window.csr.certificateChain[i]))));
        const certSimpl = new Certificate({schema: asn1.result});

        safeContents.push(
            {
                privacyMode: 1, // Password-Based Privacy Protection Mode
                value: new SafeContents({
                    safeBags: [
                        new SafeBag({
                            bagId: "1.2.840.113549.1.12.10.1.3",
                            bagValue: new CertBag({
                                parsedValue: certSimpl
                            }),
                            bagAttributes: [
                                new Attribute({
                                    type: "1.2.840.113549.1.9.20", // friendlyName
                                    values: [
                                        new asn1js.BmpString({value: window.csr.friendlyName})
                                    ]
                                }),
                                new Attribute({
                                    type: "1.2.840.113549.1.9.21", // localKeyID
                                    values: [
                                        new asn1js.OctetString({valueHex: certLocalIDBuffer})
                                    ]
                                }),
                                new Attribute({
                                    type: "1.3.6.1.4.1.311.17.1", // pkcs12KeyProviderNameAttr
                                    values: [
                                        new asn1js.BmpString({value: "http://www.pkijs.org"})
                                    ]
                                })
                            ]
                        })
                    ]
                })
            }
        );
    }

    return safeContents;
};
//*********************************************************************************
//endregion 
//*********************************************************************************
function openSSLLikeInternal(password) {
    //region Initial variables
    let sequence = Promise.resolve();

    const passwordConverted = stringToArrayBuffer(password);
    //endregion

    //region Put initial values for PKCS#12 structures
    const pkcs12 = new PFX({
        parsedValue: {
            integrityMode: 0, // Password-Based Integrity Mode
            authenticatedSafe: new AuthenticatedSafe({
                parsedValue: {
                    safeContents: generateSafeContents()
                }
            })
        }
    });
    //endregion

    //region Encode internal values for "PKCS8ShroudedKeyBag"
    sequence = sequence.then(
        () => pkcs12.parsedValue.authenticatedSafe.parsedValue.safeContents[0].value.safeBags[0].bagValue.makeInternalValues({
            password: passwordConverted,
            contentEncryptionAlgorithm: {
                name: "AES-CBC", // OpenSSL can handle AES-CBC only
                length: 128
            },
            hmacHashAlgorithm: "SHA-1", // OpenSSL can handle SHA-1 only
            iterationCount: 100000
        })
    );
    //endregion

    //region Encode internal values for all "SafeContents" firts (create all "Privacy Protection" envelopes)
    sequence = sequence.then(
        () => pkcs12.parsedValue.authenticatedSafe.makeInternalValues({
            safeContents: [
                {
                    // Empty parameters for first SafeContent since "No Privacy" protection mode there
                },
                {
                    password: passwordConverted,
                    contentEncryptionAlgorithm: {
                        name: "AES-CBC", // OpenSSL can handle AES-CBC only
                        length: 128
                    },
                    hmacHashAlgorithm: "SHA-1", // OpenSSL can handle SHA-1 only
                    iterationCount: 100000
                }
            ]
        })
    );
    //endregion

    //region Encode internal values for "Integrity Protection" envelope
    sequence = sequence.then(
        () => pkcs12.makeInternalValues({
            password: passwordConverted,
            iterations: 100000,
            pbkdf2HashAlgorithm: "SHA-256", // OpenSSL can not handle usage of PBKDF2, only PBKDF1
            hmacHashAlgorithm: "SHA-256"
        })
    );
    //endregion

    //region Save encoded data
    sequence = sequence.then(() => pkcs12.toSchema().toBER(false));
    //endregion

    return sequence;
}
//*********************************************************************************
function openSSLLike() {
    return Promise.resolve().then(() => openSSLLikeInternal(window.csr.password)).then(result => {
        console.log ("pkcs#12 password: %o", window.csr.password);
        const pkcs12AsBlob = new Blob([result], {type: "application/x-pkcs12"});
        const downloadLink = document.createElement("a");
        downloadLink.download = "pkijs_pkcs12.p12";
        downloadLink.innerHTML = "Download File";

        downloadLink.href = window.URL.createObjectURL(pkcs12AsBlob);
        downloadLink.onclick = destroyClickedElement;
        downloadLink.style.display = "none";
        document.body.appendChild(downloadLink);

        downloadLink.click();
    });
}
//*********************************************************************************
context("Hack for Rollup.js", () => {
    return;

    //noinspection UnreachableCodeJS
    openSSLLike();
    setEngine();
});
//**********************************************************************************
