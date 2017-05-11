<%@ page import="java.util.Locale" %>
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
<%
    Locale locale = request.getLocale();
    session.setAttribute("locale", locale);
%>
<c:set var="language" value="${pageContext.request.locale.language}" scope="session"/>
<c:set var="userLanguage" value="${pageContext.request.getHeader(\"Accept-Language\")}" scope="session"/>
<jsp:setProperty name="hdr" property="lang" value="${userLanguage}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>${hdr.title}</title>
    <link rel="stylesheet" type="text/css" href="css/csr.css"/>
    <link rel="stylesheet" href="css/jquery-ui.min.css">
</head>
<body>
<div class="wrapper">
    <div id="title" class="div_title">${hdr.title}</div>

    <div class="div_loginfailed"><%= hdr.bundleEntry("login.failed") %></div>

    <div id="div_create">
        <button id="create" type="button" onclick="window.location = '/';"><%= hdr.bundleEntry("login.failed.button")%></button>
    </div>

</div>
</body>
</html>
