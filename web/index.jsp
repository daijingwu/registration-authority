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
            <button id="create" type="button" onclick="createCSR();">Create PKCS#12 Keystore</button>
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
<script type="text/javascript" src="js/pkcs10.js"></script>
<script type="text/javascript" src="js/forge.min.js"></script>
<script type="text/javascript" src="js/csr.js"></script>

</body>
</html>