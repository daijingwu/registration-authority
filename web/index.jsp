<%@ page import="java.util.Locale" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page import="net.felsing.client_cert.utilities.CsrBeans" %>
<%@ page import="net.felsing.client_cert.utilities.BackgroundProcesses" %>
<%@ page import="net.felsing.client_cert.utilities.Constants" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
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
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="hdr" class="net.felsing.client_cert.utilities.CsrBeans"/>
<%
    Locale locale = request.getLocale();
    session.setAttribute("locale", locale);
    hdr.getLang(request.getHeader("Accept-Language"));
%>
<c:set var="loginStatus" value="${hdr.loginStatus}"/>
<% if (!BackgroundProcesses.isEjbcaRunning()) { response.sendError(500, "Backend is dead" ); } %>
${hdr.dummyLogin}
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge;chrome=1">
    <title>${hdr.title}</title>
    <link rel="stylesheet" type="text/css" href="css/csr.css"/>
    <link rel="stylesheet" href="css/jquery-ui.min.css">
</head>
<body jsp_configurationString='${hdr.jsConfiguration}'>
<div class="wrapper">
    <div id="title" class="div_title">${hdr.title}</div>
    <shiro:guest>
        ${hdr.cleanUp}
        <form id="login" action="login" method="post">
            <div id="div_loginusername" class="div_input">
                <label id="lbl_loginusername" for="<%= Constants.formUsername %>"
                       class="left">${hdr.bundleEntry("lbl_loginusername")}</label>
                <span class="left2">
                    <input type="text" id="<%= Constants.formUsername %>" name="<%= Constants.formUsername %>"/>
                </span>
            </div>
            <div id="div_loginpassword" class="div_input">
                <label id="lbl_loginpassword" for="<%= Constants.formPassword %>"
                       class="left">${hdr.bundleEntry("lbl_loginpassword")}</label>
                <span class="left2">
                    <input type="password" id="<%= Constants.formPassword %>" name="<%= Constants.formPassword %>"/>
                </span>
            </div>
            <div id="div_loginbutton" class="div_buttons">
                <button id="loginbutton" type="submit">${hdr.bundleEntry("loginbutton")}</button>
            </div>
        </form>
    </shiro:guest>
    <shiro:user>
        <% CsrBeans.log(3, "Username is " + SecurityUtils.getSubject().getPrincipal().toString()); %>
        <div id="add-pkcs10-block">
            <div id="ra_form">
                    ${hdr.form}
            </div>

            <div id="div_buttons" class="div_buttons">
                <div id="div_create">
                    <button id="create" type="button" onclick="createCSR();">${hdr.bundleEntry("create")}</button>
                </div>

                <c:if test="${hdr.authenticationEnabled}">
                    <div id="div_logoutbutton" class="div_input">
                        <button id="logoutbutton" type="button"
                                onclick="window.location.replace('./login');">${hdr.bundleEntry("logoutbutton")}
                        </button>
                    </div>
                </c:if>
            </div>

            <div id="pkcs12_password_area">
                <div id="div_password">
                    <label for="password" id="lbl_password" class="left pwd_toggle_off"></label>
                    <span id="span_password"><div id="password" class="pwd_toggle_off"></div></span>
                </div>

                <div id="div_passwordButton" class="pwd_toggle_off">
                    <button id="showPass" type="button"
                            onclick="showPassword();">${hdr.bundleEntry("showPassword")}</button>
                </div>
            </div>

            <div id="div_certificate" style="display:none"></div>

            <div id="errormsg"></div>
        </div>
    </shiro:user>
</div>

<%@ include file="copyright.jsp" %>

<div id="spinner"></div>

<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="application/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/pkcs10.js"></script>
<script type="text/javascript" src="js/forge.min.js"></script>
<script type="text/javascript" src="js/csr.js"></script>

</body>
</html>