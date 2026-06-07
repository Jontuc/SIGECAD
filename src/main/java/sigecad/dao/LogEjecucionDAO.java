package sigecad.dao;

import sigecad.modelo.LogEjecucion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LogEjecucionDAO {
    private final Connection conexion;
    private final List<LogEjecucion> memoria = new ArrayList<>();

    public LogEjecucionDAO() {
        conexion = null;
    }

    public LogEjecucionDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public LogEjecucion guardar(LogEjecucion log) {
        if (conexion == null) {
            log.setIdLog(memoria.size() + 1);
            memoria.add(log);
            return log;
        }
        String sql = """
                INSERT INTO logs_ejecucion
                (id_ejecucion, fecha_hora, evento, detalle) VALUES (?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, log.getIdEjecucion());
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(log.getFechaHora()));
            ps.setString(3, log.getEvento());
            ps.setString(4, log.getDetalle());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    log.setIdLog(rs.getInt(1));
                }
            }
            return log;
        } catch (SQLException e) {
            throw new DAOException("No se pudo guardar el log", e);
        }
    }

    public List<LogEjecucion> listar() {
        if (conexion == null) {
            return List.copyOf(memoria);
        }
        List<LogEjecucion> resultado = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT * FROM logs_ejecucion ORDER BY fecha_hora");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new LogEjecucion(
                        rs.getInt("id_log"),
                        rs.getInt("id_ejecucion"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getString("evento"),
                        rs.getString("detalle")));
            }
            return resultado;
        } catch (SQLException e) {
            throw new DAOException("No se pudieron consultar los logs", e);
        }
    }
}
