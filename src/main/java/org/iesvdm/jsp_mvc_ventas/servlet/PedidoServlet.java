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
import org.iesvdm.jsp_mvc_ventas.model.ResumenClientesPorComercial;

import javax.swing.event.MenuEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat; // importante para tratar fechas
import java.util.Date;
import java.util.List;
import java.util.Optional;

@WebServlet("/pedidos/*")
public class PedidoServlet extends HttpServlet {

    private String contextPath;
    PedidoDAO pedidoDAO = new PedidoDAOImpl();
    ComercialDAO comercialDAO = new ComercialDAOImpl();
    ClienteDAO clienteDAO = new ClienteDAOImpl();

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
            request.setAttribute("listaPedidos", pedidoDAO.getAll());
            List<ResumenClientesPorComercial> resumen = pedidoDAO.getComercialXClientes();
            request.setAttribute("resumenComerciales", resumen);
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/pedidos.jsp");

            String minStr = request.getParameter("min");
            String maxStr = request.getParameter("max");

            List<Pedido> listaPedidos;

            if (minStr != null && maxStr != null && !minStr.isBlank() && !maxStr.isBlank()) {
                try {
                    BigDecimal min = new BigDecimal(minStr);
                    BigDecimal max = new BigDecimal(maxStr);
                    //listaPedidos = pedidoDAO.findByTotalBetween(min, max); // este método lo creas abajo
                    request.setAttribute("filtroMin", min);
                    request.setAttribute("filtroMax", max);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    listaPedidos = pedidoDAO.getAll(); // por si el usuario pone algo no numérico
                }
            } else {
                listaPedidos = pedidoDAO.getAll();
            }

            //request.setAttribute("listaPedidos", listaPedidos);

        } else {
            pathInfo = pathInfo.replaceAll("/$", "");
            String[] pathParts = pathInfo.split("/");

            if (pathParts.length == 2 && "crear".equals(pathParts[1])) {
                request.setAttribute("listaClientes", clienteDAO.getAll());
                request.setAttribute("listaComerciales", comercialDAO.getAll());
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/crear-pedido.jsp");

            } else if (pathParts.length == 2) {
                try {
                    Optional<Pedido> pedidoOpt = pedidoDAO.find(Long.parseLong(pathParts[1]));
                    if (pedidoOpt.isPresent()) {
                        request.setAttribute("pedido", pedidoOpt.get());
                    } else {
                        request.setAttribute("error", "Pedido no encontrado");
                    }
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/detalle-pedido.jsp");
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/pedidos.jsp");
                }

            } else if (pathParts.length == 3 && "editar".equals(pathParts[1])) {
                try {
                    Optional<Pedido>optPedido=pedidoDAO.find(Long.parseLong(pathParts[2]));
                    if (optPedido.isPresent()) {
                        request.setAttribute("pedido", optPedido.get());
                    }else {
                        request.setAttribute("error", "Pedido no encontrado");
                    }
                    request.setAttribute("listaClientes", clienteDAO.getAll());
                    request.setAttribute("listaComerciales", comercialDAO.getAll());
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
        RequestDispatcher dispatcher;

        if (__method__ == null) {

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
            String comercialIdStr = request.getParameter("comercial");

            Cliente cliente = obtenerCliente(clienteIdStr);
            Comercial comercial = obtenerComercial(comercialIdStr);

            nuevoPedido.setCliente(cliente);
            nuevoPedido.setComercial(comercial);

            if (fecha == null || cliente == null || comercial == null) {
                request.setAttribute("error", "Datos inválidos");
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/crear-pedido.jsp");
                dispatcher.forward(request, response);
                return;
            }

            BigDecimal total = new BigDecimal(request.getParameter("total"));

            nuevoPedido.setFecha(fecha); // ajustar si usas Date
            nuevoPedido.setTotal(total);
            nuevoPedido.setCliente(cliente);
            nuevoPedido.setComercial(comercial);
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

        Pedido pedido = new Pedido();

        // convertimos la fecha de String a Date
        String fechaStr = request.getParameter("fecha");

        Date fecha = parseFecha(fechaStr);

        String clienteIdStr = request.getParameter("cliente");
        String comercialIdStr = request.getParameter("comercial");

        // convertimos el string del request a cliente
        Cliente cliente = obtenerCliente(clienteIdStr);
        Comercial comercial = obtenerComercial(comercialIdStr);

        BigDecimal total = new BigDecimal(request.getParameter("total"));
        pedido.setFecha(fecha);
        pedido.setTotal(total);

        pedido.setCliente(cliente);
        pedido.setComercial(comercial);


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
                Optional<Comercial> comercialOpt = comercialDAO.find(comercialID);
                if (comercialOpt.isPresent()) {
                    return comercialOpt.get();
                } else {
                    throw new IllegalArgumentException("Comercial con ID " + comercialID + " no encontrado");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
