<%@ page import="org.iesvdm.jsp_mvc_ventas.model.Pedido" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.iesvdm.jsp_mvc_ventas.model.ResumenClientesPorComercial" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Lista de Pedidos</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            border: 1px solid #aaa;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #ddd;
        }
        form {
            display: inline;
            margin: 0;
            padding: 0;
        }
        button {
            cursor: pointer;
        }
    </style>
</head>
<body>

<h1>Pedidos</h1>

<%
    List<ResumenClientesPorComercial> resumenPedidos = (List<ResumenClientesPorComercial>) request.getAttribute("resumenComerciales");
%>

<%
    for (ResumenClientesPorComercial resumen : resumenPedidos) {
%>
<p><%= resumen.toString() %></p>
<%
    }
%>

<h2>Buscador por horquilla de cantidad</h2>
<form method="get" action="${pageContext.request.contextPath}/pedidos">
    <label for="min">Mínimo:</label>
    <input type="number" name="min" step="0.01" />

    <label for="max">Máximo:</label>
    <input type="number" name="max" step="0.01" />

    <button type="submit">Enviar</button>
</form>


<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Fecha</th>
        <th>Cliente</th>
        <th>Comercial</th>
        <th>Total</th>
        <th>Acciones</th>
    </tr>
    </thead>
    <tbody>
    <%!
        // Variable de clase JSP, accesible en todo el JSP
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    %>
    <%
        List<Pedido> listaPedidos = (List<Pedido>) request.getAttribute("listaPedidos");
        for (Pedido pedido : listaPedidos) {
            String clienteNombre = "Sin cliente";
            if (pedido.getCliente() != null) {
                clienteNombre = pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido1() + " " + pedido
                        .getCliente().getApellido2();
            }
            String comercialNombre = "Sin comercial";
            if (pedido.getComercial() != null) {
                comercialNombre = pedido.getComercial().getNombre() + " " + pedido.getComercial().getApellido1() + " " + pedido.getComercial().getApellido2();
            }
    %>
    <tr>
        <td><%= pedido.getId() %></td>
        <td><%= pedido.getFecha() != null ? sdf.format(pedido.getFecha()) : "" %></td>
        <td><%= clienteNombre %></td>
        <td><%= comercialNombre %></td>
        <td><%= pedido.getTotal() != null ? pedido.getTotal() : "" %></td>
        <td>
            <a href="<%= request.getContextPath() %>/pedidos/<%= pedido.getId() %>">Ver</a> |
            <a href="<%= request.getContextPath() %>/pedidos/editar/<%= pedido.getId() %>">Editar</a> |
            <form action="<%= request.getContextPath() %>/pedidos" method="post" onsubmit="return confirm('¿Eliminar pedido ID <%= pedido.getId() %>?');">
                <input type="hidden" name="__method__" value="delete" />
                <input type="hidden" name="codigo" value="<%= pedido.getId() %>" />
                <button type="submit">Borrar</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

<p><a href="<%= request.getContextPath() %>/pedidos/crear">Crear nuevo pedido</a></p>

</body>
</html>
