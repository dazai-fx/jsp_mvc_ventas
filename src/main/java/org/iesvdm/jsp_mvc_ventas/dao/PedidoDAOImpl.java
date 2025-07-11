package org.iesvdm.jsp_mvc_ventas.dao;

import org.iesvdm.jsp_mvc_ventas.model.Cliente;
import org.iesvdm.jsp_mvc_ventas.model.Comercial;
import org.iesvdm.jsp_mvc_ventas.model.Pedido;
import org.iesvdm.jsp_mvc_ventas.model.ResumenClientesPorComercial;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAOImpl extends AbstractDAOImpl implements PedidoDAO {

    @Override
    public void create(Pedido pedido) {

        String sql = """
        -- language=SQL
        INSERT INTO pedido (total, fecha, id_cliente, id_comercial)
        VALUES (?, ?, ?, ?)
        """;

        // Esto automáticamente cierra rs, ps y conn al salir del bloque try, incluso si ocurre una excepción.
        try (
                Connection conn = connectDB();
                PreparedStatement ps = prepareStmtGeneratedKeys(conn, sql)
        ) {
            ps.setBigDecimal(1, pedido.getTotal());
            // importante con las fechas
            if (pedido.getFecha() != null) {
                ps.setDate(2, new java.sql.Date(pedido.getFecha().getTime()));
            } else {
                ps.setNull(2, java.sql.Types.DATE);
            }
            ps.setLong(3, pedido.getCliente().getId());
            ps.setLong(4, pedido.getComercial().getId());

            Optional<Integer> generatedId = executeInsert(ps);
            generatedId.ifPresent(id -> pedido.setId(id.longValue()));

        } catch (SQLException e) {
            System.out.println("Error al insertar pedido "+e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e) {
            System.err.println("Error al insertar pedido: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public List<Pedido> getAll() {

        List<Pedido> pedidos = new ArrayList<>();

        String sql = """
        -- language=SQL
        SELECT id, total, fecha, id_cliente, id_comercial 
        FROM pedido;
        """;

        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getLong("id"));
                pedido.setTotal(rs.getBigDecimal("total"));
                // Importante para las fechas lo que te devuelve es un sql.date por eso lo convertimos a util.date
                java.sql.Date sqlDate = rs.getDate("fecha");
                pedido.setFecha(new java.util.Date(sqlDate.getTime()));


                Long idCliente = rs.getLong("id_cliente");
                ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
                //importante si cliente existe lo setea en el pedio en caso contrario saltamos excepción
                clienteDAO.find(idCliente).ifPresentOrElse(
                        cliente -> pedido.setCliente(cliente),
                        () -> { throw new RuntimeException("Pedidos: el Cliente con ID " + idCliente + " no encontrado."); }
                );

                Long idComercial = rs.getLong("id_comercial");
                ComercialDAOImpl comercialDAO = new ComercialDAOImpl();
                comercialDAO.find(idComercial).ifPresentOrElse(
                        comercial -> pedido.setComercial(comercial),
                        () -> { throw new RuntimeException("Pedidos: el comercial con ID " + idComercial + " no encontrado."); }
                );

                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Podrías lanzar excepción o devolver lista vacía, según diseño
        }catch (Exception e) {
            System.err.println("Error inesperado al obtener lista de pedidos con id "+ e.getMessage());
            e.printStackTrace();
        }

        return pedidos;
    }

    @Override
    public Optional<Pedido> find(Long id) {

        /*Puedes evitar varias llamadas a la base de datos si usas una consulta con JOIN, aunque eso depende de si prefieres delegar la carga de objetos a los DAOs individuales.
        SELECT p.id, p.total, p.fecha,
        c.id AS cliente_id, c.nombre AS cliente_nombre,
        com.id AS comercial_id, com.nombre AS comercial_nombre
        FROM pedido p
        JOIN cliente c ON p.id_cliente = c.id
        JOIN comercial com ON p.id_comercial = com.id
        WHERE p.id = ?
        * */

        String sql = """
        SELECT id, total, fecha, id_cliente, id_comercial 
        FROM pedido 
        WHERE id = ?
        """;

        try (
                Connection conn = connectDB();
                PreparedStatement ps = conn.prepareStatement(sql);
        ) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pedido pedido = new Pedido();
                    pedido.setId(rs.getLong("id"));
                    pedido.setTotal(rs.getBigDecimal("total"));

                    // pasamos de sql.date a util.date para evitar errores
                    java.sql.Date sqlDate = rs.getDate("fecha");
                    if (sqlDate != null) {
                        pedido.setFecha(new java.util.Date(sqlDate.getTime()));
                    }


                    Long idCliente = rs.getLong("id_cliente");

                    // Utilizamos los DAOS de cliente y Comercial para rellenar los objetos
                    ClienteDAOImpl clienteDAO = new ClienteDAOImpl();
                    clienteDAO.find(idCliente).ifPresentOrElse(
                            cliente -> pedido.setCliente(cliente),
                            () -> { throw new RuntimeException("Pedidos: el Cliente con ID " + idCliente + " no encontrado."); }
                    );


                    Long idComercial = rs.getLong("id_comercial");

                    ComercialDAOImpl comercialDAO = new ComercialDAOImpl();
                    comercialDAO.find(idComercial).ifPresentOrElse(
                            comercial -> pedido.setComercial(comercial),
                            () -> { throw new RuntimeException("Pedidos: el comercial con ID " + idComercial + " no encontrado."); }
                    );

                    return Optional.of(pedido);

                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }catch (Exception e) {
            System.err.println("Error inesperado al buscar pedido con id " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public void update(Pedido pedido) {
        String sql = """
            -- language=SQL
            UPDATE pedido 
            SET total = ?, 
                fecha = ?, 
                id_cliente = ?, 
                id_comercial = ?
            WHERE id = ?
            """;
                try(Connection conn = connectDB();
                    PreparedStatement ps = prepareStmtGeneratedKeys(conn, sql)){

                    ps.setBigDecimal(1, pedido.getTotal());
                    if (pedido.getFecha() != null) {
                        ps.setDate(2, new java.sql.Date(pedido.getFecha().getTime()));
                    } else {
                        ps.setNull(2, java.sql.Types.DATE);
                    }
                    ps.setLong(3, pedido.getCliente().getId());
                    ps.setLong(4, pedido.getComercial().getId());
                    ps.setLong(5, pedido.getId());

                    // Usa el método de AbstractDAOImpl para ejecutar update con validación
                    executeUpdate(ps);

                } catch (SQLException e) {
                    System.err.println("Error SQL al actualizar pedido: " + e.getMessage());
                    e.printStackTrace();
                }catch (Exception e){
                    System.err.println("Error al actualizar pedido: " + e.getMessage());
                    e.printStackTrace();
                }

    }

    @Override
    public void delete(Long id) {

        String sql = """
            -- language=SQL
            DELETE FROM pedido
            WHERE id = ?;
            """;

        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                System.err.println("No se eliminó ningún pedido con id: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar pedido: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public void getAll2(){
        List<Pedido> pedidos = new ArrayList<>();

        String sql = """
        -- language=SQL
        SELECT id, total, fecha, id_cliente, id_comercial 
        FROM pedido;
        """;
    }

    public List<ResumenClientesPorComercial> getComercialXClientes(){
        List<ResumenClientesPorComercial> resumen = new ArrayList<>();
        String sql = """
            -- language=SQL
            SELECT 
                c.id,
                CONCAT(c.nombre, ' ', c.apellido1) AS nombre_comercial,
                COUNT(DISTINCT p.id_cliente) AS num_clientes
            FROM 
                comercial c
            JOIN 
                pedido p ON c.id = p.id_comercial
            GROUP BY 
                c.id
            ORDER BY 
                nombre_comercial
    """;

        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ResumenClientesPorComercial r = new ResumenClientesPorComercial();
                r.setId(rs.getInt("id"));
                r.setNombre(rs.getString("nombre_comercial"));
                r.setNumClientes(rs.getInt("num_clientes"));
                resumen.add(r);
            }
        }catch (SQLException e) {
            System.err.println("Error SQL ResumenClientesPorComercial:  " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error al ResumenClientesPorComercial: " + e.getMessage());
            e.printStackTrace();
        }

        return resumen;
    }

    /*public List<Pedido> findByTotalBetween(BigDecimal min, BigDecimal max) {
        List<Pedido> listaPedidos = new ArrayList<>();

        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM pedido WHERE total BETWEEN ? AND ?")) {

            ps.setBigDecimal(1, min);
            ps.setBigDecimal(2, max);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getLong("id"));
                pedido.setFecha(rs.getDate("fecha"));
                pedido.setTotal(rs.getBigDecimal("total"));

                // Completar con cliente y comercial si lo necesitas
                // Puedes usar joins o cargarlos después

                listaPedidos.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaPedidos;
    }*/

}
