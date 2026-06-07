package sigecad.servicio;

import sigecad.dao.UsuarioDAO;
import sigecad.modelo.Usuario;

public class AutenticacionService {
    private final UsuarioDAO usuarioDAO;

    public AutenticacionService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario autenticar(String email, String password) {
        Usuario usuario = usuarioDAO.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario inexistente"));
        if (!usuario.getPassword().equals(password)) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }
        return usuario;
    }
}
