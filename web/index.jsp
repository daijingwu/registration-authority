<%--
  Created by IntelliJ IDEA.
  User: cf
  Date: 23.08.15
  Time: 06:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="hdr" class="net.felsing.client_cert.utilities.CsrBeans"/>

<html lang="en" xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta charset="utf-8"/>
    <title>${hdr.title}</title>

    <link rel="stylesheet" type="text/css" href="css/csr.css"/>
    <link rel="stylesheet" href="css/jquery-ui.min.css">
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript">
        let schema =${hdr.formSchema};
    </script>
</head>

<body>
<div class="wrapper">
    <div id="add-pkcs10-block">
        <div id="ra_form">
            ${hdr.form}
        </div>

        <div id="div_create">
            <button id="create" type="button" onclick="create_PKCS10($('#cn').val(),$('#e').val(),$('#c').val());">Create PKCS#12 Keystore</button>
        </div>

        <div id="div_password">
            <div id="lbl_password" class="label"></div>
            <div id="password"></div>
        </div>

        <div id="div_certificate" style="display:none">

        </div>

        <div id="add-pkcs10-block">
            <p>
                <label for="hashAlg" style="font-weight:bold">Hashing algorithm:</label>
                <select id="hashAlg" onchange="handleHashAlgOnChange()">
                    <option value="alg_SHA1">SHA-1</option>
                    <option value="alg_SHA256">SHA-256</option>
                    <option value="alg_SHA384">SHA-384</option>
                    <option value="alg_SHA512">SHA-512</option>
                </select>
            </p>
            <p>
                <label for="signAlg" style="font-weight:bold">Signature algorithm:</label>
                <select id="signAlg" onchange="handleSignAlgOnChange()">
                    <option value="alg_RSA15">RSASSA-PKCS1-v1_5</option>
                    <option value="alg_RSA2">RSA-PSS</option>
                    <option value="alg_ECDSA">ECDSA</option>
                </select>
            </p>
            <textarea id="pem-text-block"></textarea>
            <a onclick="createPKCS10();">Create</a>
            <a onclick="parsePKCS10();">Parse</a>
            <a onclick="verifyPKCS10();">Verify</a>
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

</div>

<div id="spinner"></div>

<script type="application/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/pkcs10.js"></script>
<script type="text/javascript" src="js/pkcs12.js"></script>
<script type="text/javascript" src="js/csr.js"></script>

</body>
</html>