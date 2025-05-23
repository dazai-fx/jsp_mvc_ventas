package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoDAO {

    // no es necesario poner public delante de los métodos
    void create(Pedido pedido);

    List<Pedido> getAll();

    Optional<Pedido> find(Long id);

    void update(Pedido pedido);

    void delete(Long id);

}
