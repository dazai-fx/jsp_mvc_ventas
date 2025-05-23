<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>JSP - Hello World</title>
  <script>
    // mandamos al usuario al jsp donde mostramos los pedidos desde js
    window.location = "https://google.com";
  </script>
</head>
<body>
<p>Redirigiendo. Por favor, espere...</p>
<%
  // otra opcion es hacerlo desde Java de esta forma
  //response.sendRedirect("fabricantes");
%>
</body>
</html>