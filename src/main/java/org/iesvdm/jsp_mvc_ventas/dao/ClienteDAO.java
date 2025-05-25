package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteDAO {

    void create(Cliente cliente);

    List<Cliente> getAll();

    Optional<Cliente> find(Long id);

    void update(Cliente cliente);

    void delete(Long id);

}
