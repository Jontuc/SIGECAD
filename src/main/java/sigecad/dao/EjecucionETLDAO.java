package sigecad.dao;

import sigecad.modelo.EjecucionETL;
import sigecad.modelo.EstadoEjecucion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EjecucionETLDAO {
    private final Connection conexion;
    private final List<EjecucionETL> memoria = new ArrayList<>();

    public EjecucionETLDAO() {
        conexion = null;
    }

    public EjecucionETLDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public EjecucionETL guardar(EjecucionETL ejecucion) {
        if (conexion == null) {
            ejecucion.setIdEjecucion(memoria.size() + 1);
            memoria.add(ejecucion);
            return ejecucion;
        }
        String sql = """
                INSERT INTO ejecuciones_etl
                (id_proceso, id_archivo, fecha_inicio, fecha_fin,
                 estado, registros_procesados)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            completar(ps, ejecucion);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ejecucion.setIdEjecucion(rs.getInt(1));
                }
            }
            return ejecucion;
        } catch (SQLException e) {
            throw new DAOException("No se pudo guardar la ejecucion", e);
        }
    }

    public void actualizar(EjecucionETL ejecucion) {
        if (conexion == null) {
            return;
        }
        String sql = """
                UPDATE ejecuciones_etl
                SET fecha_fin = ?, estado = ?, registros_procesados = ?
                WHERE id_ejecucion = ?
                """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setTimestamp(1, java.sql.Timestamp.valueOf(ejecucion.getFechaFin()));
            ps.setString(2, ejecucion.getEstado().name());
            ps.setInt(3, ejecucion.getRegistrosProcesados());
            ps.setInt(4, ejecucion.getIdEjecucion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("No se pudo actualizar la ejecucion", e);
        }
    }

    public List<EjecucionETL> listar() {
        if (conexion == null) {
            return List.copyOf(memoria);
        }
        List<EjecucionETL> resultado = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT * FROM ejecuciones_etl ORDER BY fecha_inicio DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new EjecucionETL(
                        rs.getInt("id_ejecucion"),
                        rs.getInt("id_proceso"),
                        rs.getInt("id_archivo"),
                        rs.getTimestamp("fecha_inicio").toLocalDateTime(),
                        rs.getTimestamp("fecha_fin") == null ? null
                                : rs.getTimestamp("fecha_fin").toLocalDateTime(),
                        EstadoEjecucion.valueOf(rs.getString("estado")),
                        rs.getInt("registros_procesados")));
            }
            return resultado;
        } catch (SQLException e) {
            throw new DAOException("No se pudo consultar el historial", e);
        }
    }

    private void completar(PreparedStatement ps, EjecucionETL e) throws SQLException {
        ps.setInt(1, e.getIdProceso());
        ps.setInt(2, e.getIdArchivo());
        ps.setTimestamp(3, java.sql.Timestamp.valueOf(e.getFechaInicio()));
        ps.setTimestamp(4, e.getFechaFin() == null ? null
                : java.sql.Timestamp.valueOf(e.getFechaFin()));
        ps.setString(5, e.getEstado().name());
        ps.setInt(6, e.getRegistrosProcesados());
    }
}
