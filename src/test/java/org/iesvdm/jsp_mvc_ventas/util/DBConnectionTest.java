package org.iesvdm.jsp_mvc_ventas.util;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @Test
    void testConnection(){
        try (Connection conn = DBConnection.getConnection()) {
            assertNotNull(conn, "La conexión no debe ser null");
            assertFalse(conn.isClosed(), "La conexión no debe estar cerrada");
        } catch (Exception e) {
            fail("Ocurrió una excepción al conectar a la base de datos: " + e.getMessage());
        }
    }

}