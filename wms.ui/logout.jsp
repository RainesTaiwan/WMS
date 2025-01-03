<%-- (c) Copyright 2013 SAP AG. All rights reserved.  --%>
<%-- "$Revision$" --%>
<%@ page import="java.net.URL"%>
<%@ page import="com.sap.security.api.UMFactory"%>
        <%

                String redirectedUrl = "";

                // If a session exists, force a logoff and redirect the URL
                if(session!=null) {
                        redirectedUrl = request.getContextPath();
                        session.invalidate();
                        UMFactory.getAuthenticator().logout(request, response);
                }

        %>
<html>
<head>
</head>
<body>
<script language="JavaScript">
        var queryString = window.location.search;
        if(queryString.indexOf('?') === -1) {
        window.location.href = '<%=redirectedUrl%>';
        } else {
        var resultString = queryString.substr(1) || window.location.hash.split("?")[1];
        window.location.href = '<%=redirectedUrl%>' + '?' + resultString;
        }

</script>
</body>
</html>
