package org.iesvdm.jsp_mvc_ventas.servlet;

import jakarta.servlet.http.HttpServlet;

public class PedidoServlet extends HttpServlet {

    private String contextPath;

    @Override
    public void init() {
        this.contextPath = getServletContext().getContextPath();
        System.out.println("Context Path = " + this.contextPath);
    }




}
