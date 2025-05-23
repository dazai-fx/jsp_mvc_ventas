package org.iesvdm.jsp_mvc_ventas.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Comercial {

    private Long id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private BigDecimal comision;

    public Comercial() {
    }

    public Comercial(Long id, String nombre, String apellido1, String apellido2, BigDecimal comision) {
        this.id = id;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.comision = comision;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Comercial comercial = (Comercial) o;
        return Objects.equals(id, comercial.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Comercial{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", comision=" + comision +
                '}';
    }

}
