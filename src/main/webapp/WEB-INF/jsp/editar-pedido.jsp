<%@ page import="org.iesvdm.jsp_mvc_ventas.model.Pedido" %>
<%@ page import="org.iesvdm.jsp_mvc_ventas.model.Cliente" %>
<%@ page import="org.iesvdm.jsp_mvc_ventas.model.Comercial" %>
<%@ page import="java.util.List" %>


<%
    Pedido pedido = (Pedido) request.getAttribute("pedido");
    List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("listaClientes");
    List<Comercial> listaComerciales = (List<Comercial>) request.getAttribute("listaComerciales");
%>
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
<form method="post" action="${pageContext.request.contextPath}/pedidos">
    <input type="hidden" name="__method__" value="put"/>
    <input type="hidden" name="codigo" value="<%=pedido.getId()%>"/>

    <!-- Fecha -->
    <label>Fecha:</label>
    <input type="date" name="fecha" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(pedido.getFecha()) %>" required/><br/>

    <!-- Cliente -->
    <label>Cliente:</label>
    <select name="cliente" required>
        <% for (Cliente cliente : listaClientes) { %>
        <option value="<%=cliente.getId()%>" <%= cliente.getId() == pedido.getCliente().getId() ? "selected" : "" %>>
            <%= cliente.getNombre() %> <%= cliente.getApellido1() %> <%= cliente.getApellido2() %>
        </option>
        <% } %>
    </select><br/>
    <!-- Comercial -->
    <label>Comercial:</label>
    <select name="comercial" required>
        <% for (Comercial comercial : listaComerciales) { %>
        <option value="<%=comercial.getId()%>" <%= comercial.getId() == pedido.getComercial().getId() ? "selected" : "" %>>
            <%= comercial.getNombre() %> <%= comercial.getApellido1() %> <%= comercial.getApellido2() %>
        </option>
        <% } %>
    </select><br/>

    <!-- Total -->
    <label>Total:</label>
    <input type="number" step="0.01" name="total" value="<%=pedido.getTotal()%>" required/><br/>
    <button type="submit">Guardar Cambios</button>
    <p><a href="<%= request.getContextPath() %>/pedidos/">Volver a pedidos</a></p>
</form>
