<%--
/*
 * Copyright (c) 2016. by Christian Felsing
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="hdr" class="net.felsing.client_cert.utilities.CsrBeans"/>

<html>

<head>
    <meta charset="utf-8"/>
    <title>${hdr.title}</title>

    <link rel="stylesheet" type="text/css" href="css/csr.css"/>
    <link rel="stylesheet" href="css/jquery-ui.min.css">
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript">
        let schema =${hdr.formSchema};
        let loginStatus = ${hdr.loginStatus};
        console.log("loginStatus: %o", loginStatus);
    </script>
</head>

<body>
<c:set var="loginStatus" value="${hdr.loginStatus}"/>
<c:choose>
    <c:when test="${hdr.loginStatus eq false}">
        <div class="wrapper">
            <form id="login" action="/login" method="post">
                <div id="div_loginusername" class="div_input">
                    <label for="loginusername" class="label">Username:</label>
                    <input type="text" id="loginusername" name="loginusername"/>
                </div>
                <div id="div_loginpassword" class="div_input">
                    <label for="loginpassword" class="label">Password:</label>
                    <input type="password" id="loginpassword" name="loginpassword"/>
                </div>
                <div id="div_loginbutton" class="div_input">
                    <button id="loginbutton" type="submit">Send</button>
                </div>
            </form>
        </div>
    </c:when>
    <c:otherwise>
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
    </c:otherwise>
</c:choose>

<div id="spinner"></div>

<script type="application/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/pkcs10.js"></script>
<script type="text/javascript" src="js/forge.min.js"></script>
<script type="text/javascript" src="js/csr.js"></script>

</body>
</html>