package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.model.Comercial;
import org.iesvdm.jsp_mvc_ventas.model.Pedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ComercialDAOImplTest {

    @Test
    void create() {

        ComercialDAOImpl comercialDAO = new ComercialDAOImpl();

        Comercial comercial = new Comercial();

        comercial.setNombre("Manolo");
        comercial.setApellido1("Carascosa");
        comercial.setApellido2("Jimenez");
        comercial.setComision(BigDecimal.valueOf(10.50));

        assertDoesNotThrow(() -> comercialDAO.create(comercial));


    }

    @Test
    void getAll() {

        ComercialDAOImpl comercialDAO = new ComercialDAOImpl();

        // Solo llamamos a getAll y comprobamos que no lanza excepción
        assertDoesNotThrow(() -> {
            List<Comercial> comerciaantes = comercialDAO.getAll();
            assertNotNull(comerciaantes, "La lista no debería ser null");
        });

        // comprobar que no esta vacía
        List<Comercial> comerciantes = comercialDAO.getAll();
        assertFalse(comerciantes.isEmpty(), "La lista de comerciantes no debería estar vacía");

        //comerciantes.forEach(System.out::println);

    }


    @Test
    void update() {

        ComercialDAOImpl comercialDAO = new ComercialDAOImpl();
        Comercial comercial = new Comercial(1L, "Javier", "Sanchez", "Molina", BigDecimal.valueOf(0.75));

        comercialDAO.update(comercial);

        assertDoesNotThrow(() -> comercialDAO.update(comercial));

    }

    @Test
    void deleteComercialExistente(){

        ComercialDAOImpl comercialDAO = new ComercialDAOImpl();

        // Asegúrate de que este ID exista en tu base de datos de pruebas
        Long idExistente = 9L;

        assertDoesNotThrow(() -> comercialDAO.delete(idExistente));
        // También podrías comprobar que realmente se eliminó si haces un .find() y esperas un Optional.empty()
        Optional<Comercial> comercialEliminado = comercialDAO.find(idExistente);
        assertTrue(comercialEliminado.isEmpty());

    }

    @Test
    void deleteComercialNoExistente(){
        ComercialDAOImpl comercialDAO = new ComercialDAOImpl();

        Long idInexistente = 999999L;

        assertDoesNotThrow(() -> comercialDAO.delete(idInexistente));
        // Si añades logs en el método `delete` como hiciste, puedes confirmar en consola que no se eliminó nada

    }



}