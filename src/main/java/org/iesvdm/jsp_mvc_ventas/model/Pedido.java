package org.iesvdm.jsp_mvc_ventas.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Pedido {

    private Long id;
    private BigDecimal total;
    private Date fecha;
    private Cliente cliente;
    private Comercial comercial;

    public Pedido() {
    }

    public Pedido(Long id, Comercial comercial, Cliente cliente, Date fecha, BigDecimal total) {
        this.id = id;
        this.comercial = comercial;
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Comercial getComercial() {
        return comercial;
    }

    public void setComercial(Comercial comercial) {
        this.comercial = comercial;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", total=" + total +
                ", fecha=" + fecha +
                ", cliente=" + cliente +
                ", comercial=" + comercial +
                '}';
    }

}
