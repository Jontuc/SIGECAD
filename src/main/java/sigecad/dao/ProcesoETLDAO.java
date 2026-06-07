package sigecad.dao;

import sigecad.modelo.EstadoProceso;
import sigecad.modelo.ProcesoETL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProcesoETLDAO {
    private final Connection conexion;
    private final Map<Integer, ProcesoETL> memoria = new LinkedHashMap<>();
    private int siguienteId = 1;

    public ProcesoETLDAO() {
        this.conexion = null;
    }

    public ProcesoETLDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public ProcesoETL guardar(ProcesoETL proceso) {
        if (conexion == null) {
            proceso.setIdProceso(siguienteId++);
            memoria.put(proceso.getIdProceso(), proceso);
            return proceso;
        }
        String sql = """
                INSERT INTO procesos_etl
                (nombre, descripcion, fuente_datos, destino_datos, estado)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, proceso.getNombre());
            ps.setString(2, proceso.getDescripcion());
            ps.setString(3, proceso.getFuenteDatos());
            ps.setString(4, proceso.getDestinoDatos());
            ps.setString(5, proceso.getEstado().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    proceso.setIdProceso(rs.getInt(1));
                }
            }
            return proceso;
        } catch (SQLException e) {
            throw new DAOException("No se pudo guardar el proceso", e);
        }
    }

    public List<ProcesoETL> listar() {
        if (conexion == null) {
            return new ArrayList<>(memoria.values());
        }
        List<ProcesoETL> resultado = new ArrayList<>();
        String sql = "SELECT * FROM procesos_etl";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
            return resultado;
        } catch (SQLException e) {
            throw new DAOException("No se pudieron listar los procesos", e);
        }
    }

    public Optional<ProcesoETL> buscarPorId(int id) {
        if (conexion == null) {
            return Optional.ofNullable(memoria.get(id));
        }
        String sql = "SELECT * FROM procesos_etl WHERE id_proceso = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("No se pudo buscar el proceso", e);
        }
    }

    public void actualizarEstado(int id, EstadoProceso estado) {
        if (conexion == null) {
            ProcesoETL proceso = memoria.get(id);
            if (proceso == null) {
                throw new IllegalArgumentException("No existe el proceso " + id);
            }
            proceso.setEstado(estado);
            return;
        }
        try (PreparedStatement ps = conexion.prepareStatement(
                "UPDATE procesos_etl SET estado = ? WHERE id_proceso = ?")) {
            ps.setString(1, estado.name());
            ps.setInt(2, id);
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe el proceso " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("No se pudo actualizar el proceso", e);
        }
    }

    private ProcesoETL mapear(ResultSet rs) throws SQLException {
        return new ProcesoETL(
                rs.getInt("id_proceso"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getString("fuente_datos"),
                rs.getString("destino_datos"),
                EstadoProceso.valueOf(rs.getString("estado")));
    }
}
