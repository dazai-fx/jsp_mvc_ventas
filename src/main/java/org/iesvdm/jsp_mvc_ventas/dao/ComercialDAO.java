package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.model.Comercial;

import java.util.List;
import java.util.Optional;

public interface ComercialDAO {

    void create(Comercial comercial);

    List<Comercial> getAll();

    Optional<Comercial> find(Long id);

    void update(Comercial comercial);

    void delete(Long id);

}
