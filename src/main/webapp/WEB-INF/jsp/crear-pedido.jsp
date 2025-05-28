<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="java.util.List" %>
<%@ page import="org.iesvdm.jsp_mvc_ventas.model.Cliente" %>
<%@ page import="org.iesvdm.jsp_mvc_ventas.model.Comercial" %>

<html>
<head>
    <title>Crear Pedido</title>
</head>
<body>
<style>
    form {
        width: 400px;
        margin: 2rem auto;
        padding: 1.5rem;
        border: 1px solid #ccc;
        border-radius: 8px;
        background-color: #f9f9f9;
        box-shadow: 2px 2px 10px rgba(0,0,0,0.05);
        font-family: sans-serif;
    }

    label {
        display: block;
        margin-top: 1rem;
        font-weight: bold;
    }

    input[type="date"],
    input[type="number"],
    select {
        width: 100%;
        padding: 0.5rem;
        margin-top: 0.3rem;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    button {
        margin-top: 1.5rem;
        padding: 0.6rem 1.2rem;
        background-color: #007BFF;
        color: white;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-size: 1rem;
    }

    button:hover {
        background-color: #0056b3;
    }

    a {
        display: inline-block;
        margin-top: 1rem;
        color: #007BFF;
        text-decoration: none;
    }

    a:hover {
        text-decoration: underline;
    }
</style>
<h1>Crear nuevo Pedido</h1>
<form method="post" action="${pageContext.request.contextPath}/pedidos">

    <label>Total:</label>
    <input type="number" name="total" step="0.01" required/><br/>

    <label>Fecha:</label>
    <input type="date" name="fecha" required/><br/>

    <label>Cliente:</label>
    <select name="cliente" required>
        <option value="">-- Selecciona un cliente --</option>
        <%
            List<Cliente> clientes = (List<Cliente>) request.getAttribute("listaClientes");
            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);
        %>
        <option value="<%= cliente.getId() %>">
            <%= cliente.getNombre() %> <%= cliente.getApellido1() + " " + cliente.getApellido2() %>
        </option>
        <%
            }
        %>
    </select><br/>

    <label>Comercial:</label>
    <select name="comercial" required>
        <option value="">-- Selecciona un comercial --</option>
        <%
            List<Comercial> comerciales = (List<Comercial>) request.getAttribute("listaComerciales");
            for (int i = 0; i < comerciales.size(); i++) {
                Comercial comercial = comerciales.get(i);
        %>
        <option value="<%= comercial.getId() %>">
            <%= comercial.getNombre() %> <%= comercial.getApellido1()+ " " + comercial.getApellido2() %>
        </option>
        <%
            }
        %>
    </select><br/>

    <button type="submit">Guardar</button>
</form>

<a href="${pageContext.request.contextPath}/pedidos">Volver al listado</a>
</body>
</html>
