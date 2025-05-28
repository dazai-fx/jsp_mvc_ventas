package org.iesvdm.jsp_mvc_ventas.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesvdm.jsp_mvc_ventas.dao.*;
import org.iesvdm.jsp_mvc_ventas.model.Cliente;
import org.iesvdm.jsp_mvc_ventas.model.Comercial;
import org.iesvdm.jsp_mvc_ventas.model.Pedido;

import java.io.IOException;
import java.text.SimpleDateFormat; // importante para tratar fechas
import java.util.Date;
import java.util.Optional;

@WebServlet("/pedidos/*")
public class PedidoServlet extends HttpServlet {

    private String contextPath;



    @Override
    public void init() {
        this.contextPath = getServletContext().getContextPath();
        System.out.println("Context Path = " + this.contextPath);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher;
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {
            PedidoDAO pedidoDAO = new PedidoDAOImpl();
            request.setAttribute("listaPedidos", pedidoDAO.getAll());
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/pedidos.jsp");

        } else {
            pathInfo = pathInfo.replaceAll("/$", "");
            String[] pathParts = pathInfo.split("/");

            if (pathParts.length == 2 && "crear".equals(pathParts[1])) {
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/crear-pedido.jsp");

            } else if (pathParts.length == 2) {
                PedidoDAO pedidoDAO = new PedidoDAOImpl();
                try {
                    request.setAttribute("pedido", pedidoDAO.find((long) Integer.parseInt(pathParts[1])));
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/detalle-pedido.jsp");
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/pedidos.jsp");
                }

            } else if (pathParts.length == 3 && "editar".equals(pathParts[1])) {
                PedidoDAO pedidoDAO = new PedidoDAOImpl();
                try {
                    request.setAttribute("pedido", pedidoDAO.find((long) Integer.parseInt(pathParts[2])));
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/editar-pedido.jsp");
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/pedidos.jsp");
                }

            } else {
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/pedidos.jsp");
            }
        }

        dispatcher.forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String __method__ = request.getParameter("__method__");

        if (__method__ == null) {

            PedidoDAO pedidoDAO = new PedidoDAOImpl();
            Pedido nuevoPedido = new Pedido();

            String fechaStr = request.getParameter("fecha");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // asegúrate de que el formato coincida con el input
            Date fecha = null;

            try {
                fecha = formatter.parse(fechaStr);
            } catch (Exception e) {
                e.printStackTrace(); // podrías mostrar un mensaje de error al usuario si quieres
            }

            String clienteIdStr = request.getParameter("cliente");

            Cliente cliente = obtenerCliente(clienteIdStr);

            nuevoPedido.setFecha(fecha); // ajustar si usas Date
            pedidoDAO.create(nuevoPedido);

        } else if ("put".equalsIgnoreCase(__method__)) {
            doPut(request, response);

        } else if ("delete".equalsIgnoreCase(__method__)) {
            doDelete(request, response);
        }

        response.sendRedirect(this.contextPath + "/pedidos");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PedidoDAO pedidoDAO = new PedidoDAOImpl();
        Pedido pedido = new Pedido();

        // convertimos la fecha de String a Date
        String fechaStr = request.getParameter("fecha");

        Date fecha = parseFecha(fechaStr);

        String clienteIdStr = request.getParameter("cliente");

        // convertimos el string del request a cliente
        Cliente cliente = obtenerCliente(clienteIdStr);

        pedido.setCliente(cliente);

        try {

            Long id = Long.parseLong(request.getParameter("codigo"));
            pedido.setId(id);
            pedido.setFecha(fecha); // importante hacer lo de arriba para pasar de string a date
            pedidoDAO.update(pedido);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PedidoDAO pedidoDAO = new PedidoDAOImpl();

        try {
            Long id = Long.parseLong(request.getParameter("codigo"));
            pedidoDAO.delete(id);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

    }

    // métodos para convertir fecha de String a util.Date y de clienteString a Cliente

    private Date parseFecha(String fechaStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Cliente obtenerCliente(String clienteIdStr) {
        if (clienteIdStr != null) {
            try {
                long clienteId = Long.parseLong(clienteIdStr);
                ClienteDAO clienteDAO = new ClienteDAOImpl();
                Optional<Cliente> clienteOpt = clienteDAO.find(clienteId);
                if (clienteOpt.isPresent()) {
                    return clienteOpt.get();
                } else {
                    throw new IllegalArgumentException("Cliente con ID " + clienteId + " no encontrado");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Comercial obtenerComercial(String comercialIdStr) {
        if (comercialIdStr != null) {
            try {
                long comercialID= Long.parseLong(comercialIdStr);
                ComercialDAO comercialDAO = new ComercialDAOImpl();
                Optional<Comercial> comercialOpt = comercialDAO.find(comercialID);
                if (comercialOpt.isPresent()) {
                    return comercialOpt.get();
                } else {
                    throw new IllegalArgumentException("Cliente con ID " + comercialID + " no encontrado");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
