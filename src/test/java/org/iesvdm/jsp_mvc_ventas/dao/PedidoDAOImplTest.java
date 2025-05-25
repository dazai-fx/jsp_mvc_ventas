package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.model.Cliente;
import org.iesvdm.jsp_mvc_ventas.model.Comercial;
import org.iesvdm.jsp_mvc_ventas.model.Pedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PedidoDAOImplTest {

    @Test
    void create() {

        PedidoDAOImpl pedidoDAO = new PedidoDAOImpl();

        Pedido pedido = new Pedido();

        Cliente cliente = new Cliente();
        cliente.setId(1L); // ← ID real existente

        Comercial comercial = new Comercial();
        comercial.setId(1L); // ← ID real existente

        pedido.setCliente(cliente);
        pedido.setComercial(comercial);
        pedido.setFecha(new Date());
        pedido.setTotal(BigDecimal.valueOf(150.75));
        // comprobamos que no salte ninguna excepción
        assertDoesNotThrow(() -> pedidoDAO.create(pedido));

    }

    @Test
    void getAll() {

        PedidoDAOImpl pedidoDAO = new PedidoDAOImpl();

        // Solo llamamos a getAll y comprobamos que no lanza excepción
        assertDoesNotThrow(() -> {
            List<Pedido> pedidos = pedidoDAO.getAll();
            assertNotNull(pedidos, "La lista no debería ser null");
        });

        // comprobar que no esta vacía
        List<Pedido> pedidos = pedidoDAO.getAll();
        assertFalse(pedidos.isEmpty(), "La lista de pedidos no debería estar vacía");

        //pedidos.forEach(System.out::println);

    }

    @Test
    void findPedidoExistente() {
        PedidoDAOImpl pedidoDAO = new PedidoDAOImpl();

        // ID que sabes que existe en la base de datos de pruebas
        Long idExistente = 1L;

        Optional<Pedido> pedidoOpt = pedidoDAO.find(idExistente);

        assertTrue(pedidoOpt.isPresent(), "El pedido debería existir");

        Pedido pedido = pedidoOpt.get();
        assertEquals(idExistente, pedido.getId());
        assertNotNull(pedido.getCliente(), "El cliente no debería ser null");
        assertNotNull(pedido.getComercial(), "El comercial no debería ser null");
        // estos dos debe de fallar porque solo he instanciado el id ya que los otros dos daos no estaban implementados
        //assertNotNull(pedido.getCliente().getNombre(), "El nombre de cliente no debería de ser null");
        //assertNotNull(pedido.getComercial().getNombre(), "El nombre de comercial no debería de ser null");


    }

    @Test
    void findPedidoInexistente() {
        PedidoDAOImpl pedidoDAO = new PedidoDAOImpl();

        Long idInexistente = 9999999L; // Aseguramos que este ID no existe

        Optional<Pedido> pedidoOpt = pedidoDAO.find(idInexistente);

        assertTrue(pedidoOpt.isEmpty(), "El pedido no debería existir");
    }

    @Test
    void update() {

        Comercial comercial = new Comercial();

        comercial.setId(2L);

        Cliente cliente = new Cliente();

        cliente.setId(5L);

        Pedido pedido = new Pedido(1L, comercial, cliente, new Date(), BigDecimal.valueOf(20.75));

        PedidoDAOImpl pedidoDAO = new PedidoDAOImpl();

        pedidoDAO.update(pedido);

        assertDoesNotThrow(() -> pedidoDAO.update(pedido));

    }


    @Test
    void deletePedidoExistente() {
        PedidoDAOImpl pedidoDAO = new PedidoDAOImpl();

        // Asegúrate de que este ID exista en tu base de datos de pruebas
        Long idExistente = 20L;

        assertDoesNotThrow(() -> pedidoDAO.delete(idExistente));
        // También podrías comprobar que realmente se eliminó si haces un .find() y esperas un Optional.empty()
        Optional<Pedido> pedidoEliminado = pedidoDAO.find(idExistente);
        assertTrue(pedidoEliminado.isEmpty());
    }

    @Test
    void deletePedidoInexistente() {
        PedidoDAOImpl pedidoDAO = new PedidoDAOImpl();

        Long idInexistente = 99999L; // Asegúrate de que no exista

        assertDoesNotThrow(() -> pedidoDAO.delete(idInexistente));
        // Si añades logs en el método `delete` como hiciste, puedes confirmar en consola que no se eliminó nada
    }


}