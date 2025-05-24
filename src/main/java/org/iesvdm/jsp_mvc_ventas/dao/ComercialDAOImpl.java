package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.model.Cliente;
import org.iesvdm.jsp_mvc_ventas.model.Comercial;
import org.iesvdm.jsp_mvc_ventas.model.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComercialDAOImpl extends AbstractDAOImpl implements ComercialDAO {

    @Override
    public void create(Comercial comercial) {

        String sql = """
        -- language=SQL
        INSERT INTO comercial (nombre, apellido1, apellido2, comision)
        VALUES (?, ?, ?, ?)
        """;
        try(Connection conn = connectDB();
            PreparedStatement ps = prepareStmtGeneratedKeys(conn, sql)){

            ps.setString(1, comercial.getNombre());
            ps.setString(2, comercial.getApellido1());
            ps.setString(3, comercial.getApellido2());
            ps.setBigDecimal(4, comercial.getComision());

            Optional<Integer> generatedId = executeInsert(ps);
            generatedId.ifPresent(id -> comercial.setId(id.longValue()));

        }catch (Exception e) {
            System.err.println("Error al insertar pedido: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public List<Comercial> getAll() {

        List<Comercial> comerciantes = new ArrayList<>();

        String sql = """
                -- language=SQL
                SELECT id, nombre, apellido1, apellido2, comision
                FROM comercial;
                """;

        try(Connection conn = connectDB();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Comercial comercial = new Comercial();

                comercial.setId(rs.getLong("id"));
                comercial.setNombre(rs.getString("nombre"));
                comercial.setApellido1(rs.getString("apellido1"));
                comercial.setApellido2(rs.getString("apellido2"));
                comercial.setComision(rs.getBigDecimal("comision"));

                comerciantes.add(comercial);

            }

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return comerciantes;

    }

    @Override
    public Optional<Comercial> find(Long id) {

        String sql = """
        -- language=SQL
        SELECT id, nombre, apellido1, apellido2, comision
        FROM comercial 
        WHERE id = ?
        """;

        try(
                Connection conn = connectDB();
                PreparedStatement ps = conn.prepareStatement(sql);
                ) {

                ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    Comercial comercial = new Comercial();
                    comercial.setId(rs.getLong("id"));
                    comercial.setNombre(rs.getString("nombre"));
                    comercial.setApellido1(rs.getString("apellido1"));
                    comercial.setApellido2(rs.getString("apellido2"));
                    comercial.setComision(rs.getBigDecimal("comision"));

                    return Optional.of(comercial);

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
    public void update(Comercial comercial) {

        String sql = """
            -- language=SQL
            UPDATE comercial 
            SET nombre = ?, 
                apellido1 = ?, 
                apellido2 = ?, 
                comision = ?
            WHERE id = ?
            """;

        try(Connection conn = connectDB();
            PreparedStatement ps = prepareStmtGeneratedKeys(conn, sql)){

            ps.setString(1, comercial.getNombre());
            ps.setString(2, comercial.getApellido1());
            ps.setString(3, comercial.getApellido2());
            ps.setBigDecimal(4, comercial.getComision());
            ps.setLong(5, comercial.getId());

            // Usa el método de AbstractDAOImpl para ejecutar update con validación
            executeUpdate(ps);

        } catch (Exception e){
            System.err.println("Error al actualizar comercial: " + e.getMessage());
            e.printStackTrace();
        }


    }

    @Override
    public void delete(Long id) {

        String sql = """
            -- language=SQL
            DELETE FROM comercial
            WHERE id = ?;
            """;

        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                System.err.println("No se eliminó ningún comercial con id: " + id);
            }

        } catch (Exception e) {
            System.err.println("Error al eliminar comercial: " + e.getMessage());
            e.printStackTrace();
        }


    }

}
