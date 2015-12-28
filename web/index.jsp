<%--
  Created by IntelliJ IDEA.
  User: cf
  Date: 23.08.15
  Time: 06:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta charset="utf-8"/>
    <title>PKCS#10 complex example</title>

    <link rel="stylesheet" type="text/css" href="css/csr.css"/>
    <link rel="stylesheet" href="css/jquery-ui.min.css">
</head>

<body>
<div class="wrapper">
    <div id="add-pkcs10-block">
        <p style="display: none">
            <label for="hash_alg" style="font-weight:bold">Hashing algorithm:</label>
            <select id="hash_alg">
                <option value="alg_SHA1">SHA-1</option>
                <option value="alg_SHA256">SHA-256</option>
                <option value="alg_SHA384">SHA-384</option>
                <option value="alg_SHA512">SHA-512</option>
            </select>
        </p>
        <p style="display: none">
            <label for="sign_alg" style="font-weight:bold">Signature algorithm:</label>
            <select id="sign_alg">
                <option value="alg_RSA15">RSASSA-PKCS1-v1_5</option>
                <option value="alg_RSA2">RSA-PSS</option>
                <option value="alg_ECDSA">ECDSA</option>
            </select>
        </p>

        <div>

            <div id="email_group" class="input_group">
                <label for="email" style="font-weight:bold">email:</label>
                <input type="text" id="email"/>
            </div>

            <div id="cn_group" class="input_group">
                <label for="cn" style="font-weight:bold">cn:</label>
                <input type="text" id="cn"/>
            </div>

            <div id="c_group" class="input_group">
                <label for="country" style="font-weight:bold">Country:</label>
                <select id="country"></select>
            </div>

        </div>

        <div id="div_create">
            <a id="create" onclick="create_PKCS10($('#cn').val(),$('#email').val(),$('#country').val());">Create</a>
        </div>

        <a id="parse" style="display: none;" onclick="parse_PKCS10();">Parse</a>
        <a id="verify" style="display: none;" onclick="verify_PKCS10();">Verify</a>

        <div id="password"></div>

    </div>
    <div id="pkcs10-data-block" style="display:none;">
        <h2 id="pkcs10-subject-cn"></h2>

        <div class="two-col">
            <p class="subject">Subject:</p>
            <ul id="pkcs10-subject"></ul>
        </div>
        <p><span class="type">Public Key Size (Bits):</span> <span id="keysize">key size</span></p>

        <p><span class="type">Signature Algorithm:</span> <span id="sig-algo">signature algorithm</span></p>

        <div id="pkcs10-attributes" class="two-col" style="display:none;">
            <p class="subject">Attributes:</p>
            <ul id="pkcs10-exten"></ul>
        </div>

    </div>
</div>
<div id="spinner"></div>

<script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
<script type="application/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/PKI.js/org/pkijs/common.js"></script>
<script type="text/javascript" src="js/ASN1.js/org/pkijs/asn1.js"></script>
<script type="text/javascript" src="js/PKI.js/org/pkijs/x509_schema.js"></script>
<script type="text/javascript" src="js/PKI.js/org/pkijs/x509_simpl.js"></script>
<script type="text/javascript" src="js/forge.min.js"></script>
<script type="text/javascript" src="js/csr.js"></script>

</body>
</html>