package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.util.DBConnection;
import java.sql.*;
import java.util.Optional;

public abstract class AbstractDAOImpl {

    /**
     * Ejecuta un PreparedStatement de tipo insert.
     * @param ps de tipo insert
     * @return devuelve Optional de entero correspondiente al ID generado.
     * @throws SQLException si hay errores en la ejecución
     */

    protected Optional<Integer> executeInsert(PreparedStatement ps) throws SQLException {
        int rowNum = ps.executeUpdate();

        if (rowNum == 0) {
            System.out.println("Ninguna fila insertada.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return Optional.of(generatedKeys.getInt(1));
            } else {
                System.out.println("No se generó clave primaria.");
                return Optional.empty();
            }
        } catch (SQLFeatureNotSupportedException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Ejecuta un PreparedStatement de tipo update/delete.
     * @param ps de tipo update/delete
     * @throws SQLException si hay errores en la ejecución
     */
    protected void executeUpdate(PreparedStatement ps) throws SQLException {
        int rowNum = ps.executeUpdate();
        if (rowNum == 0) {
            System.out.println("Ninguna fila modificada.");
        }
    }

    protected PreparedStatement prepareStmtGeneratedKeys(Connection conn, String query) throws SQLException {
        return conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }

    protected PreparedStatement prepareStmtGeneratedKeys(Connection conn, String query, String[] indexNames) throws SQLException {
        return conn.prepareStatement(query, indexNames);
    }

    /**
     * Método para obtener una conexión a la base de datos usando DBConnection.
     * @return conexión activa
     * @throws SQLException si falla la conexión
     */
    protected Connection connectDB() throws SQLException {
        return DBConnection.getConnection();
    }

    /**
     * Cierra la conexión y recursos JDBC.
     */
    protected void closeDb(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
