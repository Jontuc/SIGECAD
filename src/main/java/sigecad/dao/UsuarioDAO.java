package sigecad.dao;

import sigecad.modelo.Rol;
import sigecad.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO {
    private final Connection conexion;
    private final List<Usuario> memoria = new ArrayList<>();

    public UsuarioDAO() {
        conexion = null;
    }

    public UsuarioDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public void agregarDemo(Usuario usuario) {
        memoria.add(usuario);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        if (conexion == null) {
            return memoria.stream()
                    .filter(u -> u.getEmail().equalsIgnoreCase(email))
                    .findFirst();
        }
        String sql = """
                SELECT u.*, r.nombre_rol
                FROM usuarios u
                INNER JOIN roles r ON r.id_rol = u.id_rol
                WHERE u.email = ?
                """;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                Rol rol = new Rol(rs.getInt("id_rol"), rs.getString("nombre_rol"));
                return Optional.of(new Usuario(
                        rs.getInt("id_usuario"), rs.getString("nombre"),
                        rs.getString("email"), rs.getString("password"), rol));
            }
        } catch (SQLException e) {
            throw new DAOException("No se pudo buscar el usuario", e);
        }
    }
}
