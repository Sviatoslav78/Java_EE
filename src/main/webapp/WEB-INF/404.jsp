<%@ include file="layout/metadata-standart.jsp" %>
<!doctype html>
<html lang="${param.lang}">
<head>
    <%@ include file="layout/bootstrap.jsp" %>
    <title ><fmt:message key="homepage.title"/></title>
</head>
<body>
<%@ include file="layout/header.jsp" %>
<img src='<c:url value="/img/404.jpg"></c:url>' />
<a href="https://ru.wikipedia.org/wiki/%D0%9E%D1%88%D0%B8%D0%B1%D0%BA%D0%B0_404">404</a>
<a href="static/teamHtml.html">team</a>
</body>
