<%@page isErrorPage="true" %>
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
    <link rel="stylesheet" href="css/error.css" type="text/css" media="all"/>
</head>
<body>
<span class="neg">ERROR ${pageContext.response.status}</span>
<p>
    Something really bad happened. You can wait and<br/>
    see if it becomes available again, or you can restart your computer.
</p>
<p>
    * Send us an e-mail to notify this and try it later.<br/>

    * Press CTRL+ALT+DEL to restart your computer. You will<br/>
    &nbsp; lose unsaved information in any programs that are running.
</p>
<p>Press any link to continue
    <blink>_</blink>
</p>
<p class="menu">
    <a href="./">Home</a>
</p>

</body>
</html>
