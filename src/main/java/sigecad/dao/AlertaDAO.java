package sigecad.dao;

import sigecad.modelo.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AlertaDAO {
    private final Connection conexion;
    private final Queue<Alerta> memoria = new ArrayDeque<>();

    public AlertaDAO() {
        conexion = null;
    }

    public AlertaDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public Alerta guardar(Alerta alerta) {
        if (conexion == null) {
            alerta.setIdAlerta(memoria.size() + 1);
            memoria.offer(alerta);
            return alerta;
        }
        String sql = """
                INSERT INTO alertas
                (id_ejecucion, mensaje, fecha_alerta, estado) VALUES (?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, alerta.getIdEjecucion());
            ps.setString(2, alerta.getMensaje());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(alerta.getFechaAlerta()));
            ps.setString(4, alerta.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    alerta.setIdAlerta(rs.getInt(1));
                }
            }
            return alerta;
        } catch (SQLException e) {
            throw new DAOException("No se pudo guardar la alerta", e);
        }
    }

    public List<Alerta> listarPendientes() {
        if (conexion == null) {
            return new ArrayList<>(memoria);
        }
        List<Alerta> resultado = new ArrayList<>();
        String sql = "SELECT * FROM alertas WHERE estado = 'PENDIENTE'";
        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Alerta(
                        rs.getInt("id_alerta"),
                        rs.getInt("id_ejecucion"),
                        rs.getString("mensaje"),
                        rs.getTimestamp("fecha_alerta").toLocalDateTime(),
                        rs.getString("estado")));
            }
            return resultado;
        } catch (SQLException e) {
            throw new DAOException("No se pudieron consultar las alertas", e);
        }
    }
}
