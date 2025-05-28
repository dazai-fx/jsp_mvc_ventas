package org.iesvdm.jsp_mvc_ventas.model;

public class ResumenClientesPorComercial {
    private int id;
    private String nombre;
    private int numClientes;

    public ResumenClientesPorComercial() {
    }

    public ResumenClientesPorComercial(int id, String nombre, int numClientes) {
        this.id = id;
        this.nombre = nombre;
        this.numClientes = numClientes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumClientes() {
        return numClientes;
    }

    public void setNumClientes(int numClientes) {
        this.numClientes = numClientes;
    }

    @Override
    public String toString() {
        return "ResumenClientesPorComercial{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", numClientes=" + numClientes +
                '}';
    }
}
