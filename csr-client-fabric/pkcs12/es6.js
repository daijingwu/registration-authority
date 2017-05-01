import * as asn1js from "asn1js";
//import {stringToArrayBuffer, arrayBufferToString, fromBase64, toBase64} from "pvutils";
import {stringToArrayBuffer, fromBase64} from "pvutils";
//import {getCrypto, getAlgorithmParameters, getRandomValues, setEngine} from "../PKI.js/src/common";
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
//region Global variables
//*********************************************************************************
//const certificateBASE64 = `MIIDRDCCAi6gAwIBAgIBATALBgkqhkiG9w0BAQswODE2MAkGA1UEBhMCVVMwKQYDVQQDHiIAUABlAGMAdQBsAGkAYQByACAAVgBlAG4AdAB1AHIAZQBzMB4XDTEzMDEzMTIxMDAwMFoXDTE2MDEzMTIxMDAwMFowODE2MAkGA1UEBhMCVVMwKQYDVQQDHiIAUABlAGMAdQBsAGkAYQByACAAVgBlAG4AdAB1AHIAZQBzMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4qEnCuFxZqTEM/8cYcaYxexT6+fAHan5/eGCFOe1Yxi0BjRuDooWBPX71+hmWK/MKrKpWTpA3ZDeWrQR2WIcaf/ypd6DAEEWWzlQgBYpEUj/o7cykNwIvZReU9JXCbZu0EmeZXzBm1mIcWYRdk17UdneIRUkU379wVJcKXKlgZsx8395UNeOMk11G5QaHzAafQ1ljEKB/x2xDgwFxNaKpSIq3LQFq0PxoYt/PBJDMfUSiWT5cFh1FdKITXQzxnIthFn+NVKicAWBRaSZCRQxcShX6KHpQ1Lmk0/7QoCcDOAmVSfUAaBl2w8bYpnobFSStyY0RJHBqNtnTV3JonGAHwIDAQABo10wWzAMBgNVHRMEBTADAQH/MAsGA1UdDwQEAwIA/zAdBgNVHQ4EFgQU5QmA6U960XL4SII2SEhCcxij0JYwHwYDVR0jBBgwFoAU5QmA6U960XL4SII2SEhCcxij0JYwCwYJKoZIhvcNAQELA4IBAQAikQls3LhY8rYQCZ+8jXrdaRTY3L5J3S2xzoAofkEnQNzNMClaWrZbY/KQ+gG25MIFwPOWZn/uYUKB2j0yHTRMPEAp/v5wawSqM2BkdnkGP4r5Etx9pe3mog2xNUBqSeopNNto7QgV0o1yYHtuMKQhNAzcFB1CGz25+lXv8VuuU1PoYNrTjiprkjLDgPurNXUjUh9AZl06+Cakoe75LEkuaZKuBQIMNLJFcM2ZSK/QAAaI0E1DovcsCctW8x/6Qk5fYwNu0jcIdng9dzKYXytzV53+OGxdK5mldyBBkyvTrbO8bWwYT3c+weB1huNpgnpRHJKMz5xVj0bbdnHir6uc`;

//const privateKeyBASE64 = `MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDioScK4XFmpMQz/xxhxpjF7FPr58Adqfn94YIU57VjGLQGNG4OihYE9fvX6GZYr8wqsqlZOkDdkN5atBHZYhxp//Kl3oMAQRZbOVCAFikRSP+jtzKQ3Ai9lF5T0lcJtm7QSZ5lfMGbWYhxZhF2TXtR2d4hFSRTfv3BUlwpcqWBmzHzf3lQ144yTXUblBofMBp9DWWMQoH/HbEODAXE1oqlIirctAWrQ/Ghi388EkMx9RKJZPlwWHUV0ohNdDPGci2EWf41UqJwBYFFpJkJFDFxKFfooelDUuaTT/tCgJwM4CZVJ9QBoGXbDxtimehsVJK3JjREkcGo22dNXcmicYAfAgMBAAECggEBANMO1fdyIVRAWmE6UspUU+7vuvBWMjruE9126NhjOjABz5Z/uYdc3kjcdSCMVNR/VBrnrINmlwZBZnL+hCj5EBE/xlDnOwU/mHx4khnXiYOJglqLwFHcOV+lD3vsxhZLikP8a8GEQCJXbZR+RADzA8gkqJQSxnPkLpqeAyqulKhviQ2lq2ZxeCXI+iZvURQPTSm86+szClwgzr2uW6NSlNKKeeLHMILed4mrwbPOdyhutnqvV79GUYH3yYdzbEbbw5GOat77+xPLt33cfLCL7pg5lGDrKEomu6V1d5KmBOhv0K8gGPKfxPrpeUG5n1q58k/2ouCiyAaKWpVoOWmnbzECgYEA/UzAGZ2N8YE+kC85Nl0wQof+WVm+RUDsv6C3L2vPUht3GwnbxSTMl4+NixbCWG46udVhsM2x7ZzYY1eB7LtnBnjvXZTYU4wqZtGR/+X2Rw5ou+oWm16/OgcEuFjP2zpQtr9r/bpKhyBV+IdSngnLy00RueKGUL6nvtecRklEhQ0CgYEA5Quek+c12qMtrmg5znHPQC7uuieZRzUL9jTlQtuZM5m4B3AfB/N/0qIQS06PHS1ijeHQ9SxEmG72weamUYC0SPi8GxJioFzaJEDVit0Ra38gf0CXQvcYT0XD1CwY/m+jDXDWL5L1CCIr60AzNjM3WEfGO4VHaNsovVLn1Fvy5tsCgYEA4ZOEUEubqUOsb8NedCexXs61mOTvKcWUEWQTP0wHqduDyrSQ35TSDvds2j0+fnpMGksJYOcOWcmge3fm4OhT69Ovd+uia2UcLczc9MPa+5S9ePwTffJ24jp13aZaFaZtUxJOHfvVe1k0tsvsq4mV0EumSaCOdUIVKUPijEWbm9ECgYBpFa+nxAidSwiGYCNFaEnh9KZqmghk9x2J1DLrPb1IQ1p/bx2NlFYs2VYIdv6KMGxrFBO+qJTAKwjjZWMhOZ99a0FCWmkNkgwzXdubXlnDrAvI1mWPv7ZTiHqUObct5SI15HMgWJg7JxJnWIkmcNEPm76DSF6+6O4EDql2cMk8yQKBgF5roj+l90lfwImr6V1NJo3J5VCi9wTT5x9enPY9WRcfSyRjqU7JWy6h0C+Jq+AYAxrkQVjQuv1AOhO8Uhc6amM5FA+gfg5HKKPnwuOe7r7B48LFF8eRjYRtHmrQUrFY0jH6O+t12dEQI+7qE+SffUScsZWCREX7QYEK/tuznv/U`;
//*********************************************************************************
//endregion
//*********************************************************************************
//region Auxiliary functions
//*********************************************************************************
function destroyClickedElement(event) {
    document.body.removeChild(event.target);
}
//*********************************************************************************
//endregion 
//*********************************************************************************
function openSSLLikeInternal(password) {
    //region Initial variables
    let sequence = Promise.resolve();

    const keyLocalIDBuffer = new ArrayBuffer(4);
    const keyLocalIDView = new Uint8Array(keyLocalIDBuffer);

    getRandomValues(keyLocalIDView);

    const certLocalIDBuffer = new ArrayBuffer(4);
    const certLocalIDView = new Uint8Array(certLocalIDBuffer);

    getRandomValues(certLocalIDView);

    //region "KeyUsage" attribute
    const bitArray = new ArrayBuffer(1);
    const bitView = new Uint8Array(bitArray);

    bitView[0] = bitView[0] | 0x80;

    const keyUsage = new asn1js.BitString({
        valueHex: bitArray,
        unusedBits: 7
    });
    //endregion

    const passwordConverted = stringToArrayBuffer(password);
    //endregion

    //region Create simplified structures for certificate and private key
    const asn1 = asn1js.fromBER(stringToArrayBuffer(fromBase64(window.csr.privateKey)));
    const pkcs8Simpl = new PrivateKeyInfo({schema: asn1.result});

    //region Add "keyUsage" attribute
    pkcs8Simpl.attributes = [
        new Attribute({
            type: "2.5.29.15",
            values: [
                keyUsage
            ]
        })
    ];
    //endregion

    const numCerts = window.csr.certificateChain.length;
    const safeContent = {};
    for (let i=0;i<numCerts;i++) {
        const asn1 = asn1js.fromBER(stringToArrayBuffer(fromBase64(window.csr.certificateChain[i])));
        const certSimpl = new Certificate({schema: asn1.result});

        safeContent.safeBags.push(
            new SafeBag({
                bagId: "1.2.840.113549.1.12.10.1.3",
                bagValue: new CertBag({
                    parsedValue: certSimpl
                }),
                bagAttributes: [
                    new Attribute({
                        type: "1.2.840.113549.1.9.20", // friendlyName
                        values: [
                            new asn1js.BmpString({value: "CertBag from PKIjs"})
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
        )
    }

    //region Put initial values for PKCS#12 structures
    const pkcs12 = new PFX({
        parsedValue: {
            integrityMode: 0, // Password-Based Integrity Mode
            authenticatedSafe: new AuthenticatedSafe({
                parsedValue: {
                    safeContents: [
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
                                                    new asn1js.BmpString({value: window.csr.friendlyname})
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
                        },
                        {
                            privacyMode: 1, // Password-Based Privacy Protection Mode
                            value: new SafeContents(safeContent)
                        }
                    ]
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
