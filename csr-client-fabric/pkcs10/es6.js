import * as asn1js from "asn1js";
//import {arrayBufferToString, stringToArrayBuffer, toBase64, fromBase64} from "pvutils";
import {arrayBufferToString, toBase64} from "pvutils";
import {getCrypto, getAlgorithmParameters, setEngine} from "../PKI.js/src/common";
import CertificationRequest from "../PKI.js/src/CertificationRequest";
import AttributeTypeAndValue from "../PKI.js/src/AttributeTypeAndValue";
import Attribute from "../PKI.js/src/Attribute";
import Extension from "../PKI.js/src/Extension";
import Extensions from "../PKI.js/src/Extensions";
//import RSAPublicKey from "../PKI.js/src/RSAPublicKey";

//*********************************************************************************
let pkcs10Buffer = new ArrayBuffer(0);

//const hashAlg = "SHA-1";
const hashAlg = "SHA-256";
const signAlg = "RSASSA-PKCS1-V1_5";
//const signAlg = "RSA-PSS";
//const signAlg = "ECDSA";

const pkcs_9_at_extensionRequest = "1.2.840.113549.1.9.14";
const extnID = "2.5.29.14";

// see http://www.umich.edu/~x509/ssleay/asn1-oids.html
const oid={
    cn: { type: "2.5.4.3", asn1type: asn1js.Utf8String },
    sn: { type: "2.5.4.4", asn1type: asn1js.Utf8String },
    c: { type: "2.5.4.6", asn1type: asn1js.PrintableString },
    l: { type: "2.5.4.7", asn1type: asn1js.Utf8String },
    st: { type: "2.5.4.8", asn1type: asn1js.Utf8String },
    street: { type: "2.5.4.9", asn1type: asn1js.Utf8String },
    o: { type: "2.5.4.10", asn1type: asn1js.Utf8String },
    ou: { type: "2.5.4.11", asn1type: asn1js.Utf8String },
    t: { type: "2.5.4.12", asn1type: asn1js.Utf8String },
    gn: { type: "2.5.4.42", asn1type: asn1js.Utf8String },
    i: { type: "2.5.4.43", asn1type: asn1js.Utf8String },
    e: { type: "1.2.840.113549.1.9.1", asn1type: asn1js.PrintableString }
};

const oidAltNames={
    rfc822Name: { type: "2.5.29.17.1", asn1type: asn1js.Utf8String },
    dNSName: { type: "2.5.29.17.2", asn1type: asn1js.Utf8String }
};

//*********************************************************************************
//region Auxiliary functions
//*********************************************************************************
function formatPEM(pemString) {
    /// <summary>Format string in order to have each line with length equal to 63</summary>
    /// <param name="pemString" type="String">String to format</param>

    const stringLength = pemString.length;
    let resultString = "";

    for (let i = 0, count = 0; i < stringLength; i++, count++) {
        if (count > 63) {
            resultString = `${resultString}\r\n`;
            count = 0;
        }

        resultString = `${resultString}${pemString[i]}`;
    }

    return resultString;
}
//*********************************************************************************
//endregion
//*********************************************************************************
//region Create PKCS#10
//*********************************************************************************
function createPKCS10Internal() {
    //region Initial variables
    let sequence = Promise.resolve();

    const pkcs10 = new CertificationRequest();

    let publicKey;
    let privateKey;
    //endregion

    //region Get a "crypto" extension
    const crypto = getCrypto();
    if (typeof crypto === "undefined")
        return Promise.reject("No WebCrypto extension found");
    //endregion

    //region Put a static values
    pkcs10.version = 0;
    console.log ("subject: %o", window.csr);
    for (let key in window.csr) {
        if (window.csr.hasOwnProperty(key) && typeof oid[key] !== "undefined") {
            //noinspection JSPotentiallyInvalidConstructorUsage
            pkcs10.subject.typesAndValues.push(new AttributeTypeAndValue({
                type: oid[key].type,
                value: new oid[key].asn1type({value: window.csr[key]})
            }));
        }
    }

    for (let key in window.csr.subjectAltNames) {
        if (window.csr.subjectAltNames.hasOwnProperty(key) && typeof oidAltNames[key] !== "undefined") {

            console.log ("WARNING: SubjectAltNames not supported yet: %o", key);
        }
    }

    pkcs10.attributes = [];
    //endregion

    //region Create a new key pair
    sequence = sequence.then(() => {
            //region Get default algorithm parameters for key generation
            const algorithm = getAlgorithmParameters(signAlg, "generatekey");
            if ("hash" in algorithm.algorithm)
                algorithm.algorithm.hash.name = hashAlg;
            //endregion

            return crypto.generateKey(algorithm.algorithm, true, algorithm.usages);
        }
    );
    //endregion

    //region Store new key in an interim variables
    sequence = sequence.then(keyPair => {
            publicKey = keyPair.publicKey;
            privateKey = keyPair.privateKey;
        },
        error => Promise.reject((`Error during key generation: ${error}`))
    );
    //endregion

    //region Exporting private key
    sequence = sequence.then(() =>
        crypto.exportKey("pkcs8", privateKey)
    );
    //endregion

    //region Store exported key on Web page
    sequence = sequence.then(result =>
    {
        const privateKeyString = String.fromCharCode.apply(null, new Uint8Array(result));
        let privateKeyResultString = "";
        privateKeyResultString = `${privateKeyResultString}\r\n-----BEGIN PRIVATE KEY-----\r\n`;
        privateKeyResultString = `${privateKeyResultString}${formatPEM(window.btoa(privateKeyString))}`;
        privateKeyResultString = `${privateKeyResultString}\r\n-----END PRIVATE KEY-----\r\n`;

        window.csr.privateKey = privateKeyResultString;
    }, error => Promise.reject(`Error during exporting of private key: ${error}`));
    //endregion

    //region Exporting public key into "subjectPublicKeyInfo" value of PKCS#10
    sequence = sequence.then(() => pkcs10.subjectPublicKeyInfo.importKey(publicKey));
    //endregion

    //region SubjectKeyIdentifier
    sequence = sequence.then(() => crypto.digest({name: hashAlg}, pkcs10.subjectPublicKeyInfo.subjectPublicKey.valueBlock.valueHex))
        .then(result => {
                pkcs10.attributes.push(new Attribute({
                    type: pkcs_9_at_extensionRequest,
                    values: [(new Extensions({
                        extensions: [
                            new Extension({
                                extnID: extnID,
                                critical: false,
                                extnValue: (new asn1js.OctetString({valueHex: result})).toBER(false)
                            })
                        ]
                    })).toSchema()]
                }));
            }
        );
    //endregion

    //region Signing final PKCS#10 request
    sequence = sequence.then(() => pkcs10.sign(privateKey, hashAlg), error => Promise.reject(`Error during exporting public key: ${error}`));
    //endregion

    return sequence.then(() => {
        pkcs10Buffer = pkcs10.toSchema().toBER(false);

    }, error => Promise.reject(`Error signing PKCS#10: ${error}`));
}
//*********************************************************************************
function createPKCS10(callback) {
    return Promise.resolve().then(() => createPKCS10Internal()).then(() => {
        let resultString = "-----BEGIN CERTIFICATE REQUEST-----\r\n";
        resultString = `${resultString}${formatPEM(toBase64(arrayBufferToString(pkcs10Buffer)))}`;
        resultString = `${resultString}\r\n-----END CERTIFICATE REQUEST-----\r\n`;

        window.csr.pkcs10 = resultString;

        if (typeof callback === "function") {
            callback();
        } else {
            console.log ("callback is not a function");
        }
    });
}
//*********************************************************************************
//endregion
//*********************************************************************************
context("Hack for Rollup.js", () => {
    return;

    //noinspection UnreachableCodeJS
    createPKCS10();
    setEngine();
});
