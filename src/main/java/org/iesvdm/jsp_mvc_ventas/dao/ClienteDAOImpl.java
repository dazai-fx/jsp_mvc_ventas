package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAOImpl extends AbstractDAOImpl implements ClienteDAO {

    @Override
    public void create(Cliente cliente) {

        String sql = """
        -- language=SQL
        INSERT INTO cliente (nombre, apellido1, apellido2, ciudad, categoria)
        VALUES (?, ?, ?, ?, ?)
        """;

        try(Connection conn = connectDB();
            PreparedStatement ps = prepareStmtGeneratedKeys(conn, sql)){

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido1());
            ps.setString(3, cliente.getApellido2());
            ps.setString(4, cliente.getCiudad());
            ps.setInt(5, cliente.getCategoria());

            Optional<Integer> generatedId = executeInsert(ps);
            generatedId.ifPresent(id -> cliente.setId(id.longValue()));

        }catch (Exception e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public List<Cliente> getAll() {

        List<Cliente> clientes = new ArrayList<>();

        String sql = """
                -- language=SQL
                SELECT id, nombre, apellido1, apellido2, ciudad, categoria
                FROM cliente;
                """;

        try(Connection conn = connectDB();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){

                Cliente cliente = new Cliente();

                cliente.setId(rs.getLong("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido1(rs.getString("apellido1"));
                cliente.setApellido2(rs.getString("apellido2"));
                cliente.setCiudad(rs.getString("ciudad"));
                cliente.setCategoria(rs.getInt("categoria"));

                clientes.add(cliente);

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    @Override
    public Optional<Cliente> find(Long id) {

        String sql = """
        -- language=SQL
        SELECT id, nombre, apellido1, apellido2, ciudad, categoria
        FROM cliente 
        WHERE id = ?
        """;

        try(Connection conn = connectDB();
            PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setLong(1, id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){

                    Cliente cliente = new Cliente();

                    cliente.setId(rs.getLong("id"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido1(rs.getString("apellido1"));
                    cliente.setApellido2(rs.getString("apellido2"));
                    cliente.setCiudad(rs.getString("ciudad"));
                    cliente.setCategoria(rs.getInt("categoria"));

                    return Optional.of(cliente);

                } else {
                    return Optional.empty();
                }
            }

        }catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public void update(Cliente cliente) {

        String sql = """
            -- language=SQL
            UPDATE cliente 
            SET nombre = ?, 
                apellido1 = ?, 
                apellido2 = ?, 
                ciudad = ?,
                categoria = ?
            WHERE id = ?
            """;

        try(Connection conn = connectDB();

            PreparedStatement ps = prepareStmtGeneratedKeys(conn, sql)){

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido1());
            ps.setString(3, cliente.getApellido2());
            ps.setString(4, cliente.getCiudad());
            ps.setInt(5, cliente.getCategoria());
            ps.setLong(6, cliente.getId());

            executeUpdate(ps);

        } catch (Exception e){
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void delete(Long id) {

        String sql = """
            -- language=SQL
            DELETE FROM cliente
            WHERE id = ?;
            """;

        try(Connection conn = connectDB();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setLong(1, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                System.err.println("No se eliminó ningún cliente con id: " + id);
            }


        }catch (Exception e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
