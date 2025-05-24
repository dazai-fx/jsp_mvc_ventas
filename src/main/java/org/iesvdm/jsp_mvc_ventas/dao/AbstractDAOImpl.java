package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.util.DBConnection;
import java.sql.*;
import java.util.Optional;

public abstract class AbstractDAOImpl {

    /**
     * Ejecuta un PreparedStatement de tipo insert.
     * @param ps de tipo insert
     * @return devuelve Optional de entero correspondiente al ID generado.
     * @throws SQLException si hay errores en la ejecución o si no se insertó ninguna fila o no se generó clave.
     */
    protected Optional<Integer> executeInsert(PreparedStatement ps) throws SQLException {
        int rowNum = ps.executeUpdate();

        if (rowNum == 0) {
            throw new SQLException("Ninguna fila fue insertada.");
        }

        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return Optional.of(generatedKeys.getInt(1));
            } else {
                throw new SQLException("No se generó clave primaria.");
            }
        } catch (SQLFeatureNotSupportedException e) {
            // Si el driver no soporta claves generadas, retornamos vacío pero sin imprimir stack
            return Optional.empty();
        }
    }

    /**
     * Ejecuta un PreparedStatement de tipo update/delete.
     * @param ps de tipo update/delete
     * @throws SQLException si hay errores en la ejecución o si no se modificó ninguna fila.
     */
    protected void executeUpdate(PreparedStatement ps) throws SQLException {
        int rowNum = ps.executeUpdate();

        if (rowNum == 0) {
            throw new SQLException("Ninguna fila fue modificada.");
        }
    }

    // Usa Statement.RETURN_GENERATED_KEYS y recupera todas las claves generadas automáticamente.
    protected PreparedStatement prepareStmtGeneratedKeys(Connection conn, String query) throws SQLException {
        return conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }

    // Especifica qué nombre(s) de columna(s) generada(s) quieres recuperar. Más controlado.
    /*
    * en la mayoría de los casos no es necesario usar la sobrecarga con indexNames, porque Statement.RETURN_GENERATED_KEYS
    * ya funciona correctamente si solo tienes una clave generada (id autoincremental típico). Pero si quieres ser más explícito
    *  o si estás usando una base de datos que lo requiera (como Oracle o PostgreSQL en ciertas configuraciones), entonces puedes usarla.*/
    protected PreparedStatement prepareStmtGeneratedKeys(Connection conn, String query, String[] indexNames) throws SQLException {
        return conn.prepareStatement(query, indexNames);
    }

    /**
     * Método para obtener una conexión a la base de datos usando DBConnection.
     * @return conexión activa
     * @throws SQLException si falla la conexión
     */
    protected Connection connectDB() throws SQLException {
        Connection conn = DBConnection.getConnection();
        if (conn == null) {
            throw new SQLException("No se pudo obtener conexión a la base de datos.");
        }
        return conn;
    }

    /**
     * Cierra la conexión y recursos JDBC (no lanza excepciones).
     */
    protected void closeDb(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException ignored) { }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException ignored) { }
        try {
            if (conn != null) conn.close();
        } catch (SQLException ignored) { }
    }

}
