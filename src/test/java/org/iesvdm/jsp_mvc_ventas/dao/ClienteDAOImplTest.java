package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.model.Cliente;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDAOImplTest {

    @Test
    void create() {

        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();

        Cliente cliente = new Cliente();

        cliente.setNombre("Paco");
        cliente.setApellido1("Perez");
        cliente.setApellido2("Nuñez");
        cliente.setCiudad("Málaga");
        cliente.setCategoria(100);

        assertDoesNotThrow(() -> clienteDAO.create(cliente));


    }

    @Test
    void getAll() {

        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();

        assertDoesNotThrow(() -> {
            List<Cliente> clientes = clienteDAO.getAll();
            assertNotNull(clientes, "Lista de clientes no debería ser null");
        });

        List<Cliente> clientes = clienteDAO.getAll();
        assertFalse(clientes.isEmpty());

        //clientes.forEach(System.out::println);

    }

   @Test
    void findClienteExistente(){

        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();

        Long idExistente=1L;

        Optional<Cliente> clienteOpt = clienteDAO.find(idExistente);

        assertTrue(clienteOpt.isPresent(), "El cliente debería existir");

        Cliente cliente = clienteOpt.get();
        assertEquals(idExistente, cliente.getId());


   }

   @Test
    void findClienteInexistente(){

        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();

        Long idInexistente=999999999L;

        Optional<Cliente> clienteOpt = clienteDAO.find(idInexistente);

        assertTrue(clienteOpt.isEmpty(), "El cliente no debería existir");

   }

    @Test
    void update() {

        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();

        Cliente cliente = new Cliente(1L, "María", "López","Garcia", "Granada", 100);

        clienteDAO.update(cliente);

        assertDoesNotThrow(() -> clienteDAO.update(cliente));

    }

    @Test
    void deleteComercialExistente() {
        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();

        Long idExistente=11L;

        assertDoesNotThrow(()-> clienteDAO.delete(idExistente));
        Optional<Cliente> clienteEliminado = clienteDAO.find(idExistente);
        assertTrue(clienteEliminado.isEmpty());

    }

    @Test
    void deleteComercialInexistente() {

        ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
        Long idInexistente=999999999L;

        assertDoesNotThrow(()-> clienteDAO.delete(idInexistente));


    }

}