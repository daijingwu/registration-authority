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
    <script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript">
        var schema =${hdr.formSchema};
        $("#test").empty().text(schema);
    </script>
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

        <div id="ra_form">
            ${hdr.form}
        </div>

        <div id="div_create">
            <button id="create" type="button" onclick="create_PKCS10($('#cn').val(),$('#e').val(),$('#country').val());">Create PKCS#12 Keystore</button>
        </div>

        <div id="div_password">
            <div id="lbl_password" class="label"></div>
            <div id="password"></div>
        </div>

        <div id="div_certificate" style="display:none">

        </div>
    </div>

</div>

<div id="spinner"></div>

<script type="application/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/PKI.js/org/pkijs/common.js"></script>
<script type="text/javascript" src="js/ASN1.js/org/pkijs/asn1.js"></script>
<script type="text/javascript" src="js/PKI.js/org/pkijs/x509_schema.js"></script>
<script type="text/javascript" src="js/PKI.js/org/pkijs/x509_simpl.js"></script>
<script type="text/javascript" src="js/forge.min.js"></script>
<script type="text/javascript" src="js/csr.js"></script>

</body>
</html>