package sigecad.dao;

import sigecad.modelo.ErrorValidacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ErrorValidacionDAO {
    private final Connection conexion;
    private final List<ErrorValidacion> memoria = new ArrayList<>();

    public ErrorValidacionDAO() {
        conexion = null;
    }

    public ErrorValidacionDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public ErrorValidacion guardar(ErrorValidacion error) {
        if (conexion == null) {
            error.setIdError(memoria.size() + 1);
            memoria.add(error);
            return error;
        }
        String sql = """
                INSERT INTO errores_validacion
                (id_ejecucion, fila, campo, descripcion) VALUES (?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, error.getIdEjecucion());
            ps.setInt(2, error.getFila());
            ps.setString(3, error.getCampo());
            ps.setString(4, error.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    error.setIdError(rs.getInt(1));
                }
            }
            return error;
        } catch (SQLException e) {
            throw new DAOException("No se pudo guardar el error", e);
        }
    }

    public List<ErrorValidacion> listar() {
        if (conexion == null) {
            return List.copyOf(memoria);
        }
        List<ErrorValidacion> resultado = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(
                "SELECT * FROM errores_validacion ORDER BY id_error");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new ErrorValidacion(
                        rs.getInt("id_error"),
                        rs.getInt("id_ejecucion"),
                        rs.getInt("fila"),
                        rs.getString("campo"),
                        rs.getString("descripcion")));
            }
            return resultado;
        } catch (SQLException e) {
            throw new DAOException("No se pudieron consultar los errores", e);
        }
    }
}
