package sigecad.modelo;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String email;
    private String password;
    private Rol rol;

    public Usuario(int idUsuario, String nombre, String email,
                   String password, Rol rol) {
        this.idUsuario = idUsuario;
        setNombre(nombre);
        setEmail(email);
        setPassword(password);
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = obligatorio(nombre, "nombre");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String valor = obligatorio(email, "email");
        if (!valor.contains("@")) {
            throw new IllegalArgumentException("El email no es valido");
        }
        this.email = valor;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = obligatorio(password, "password");
    }

    public Rol getRol() {
        return rol;
    }

    private String obligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El " + campo + " es obligatorio");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return idUsuario + " | " + nombre + " | " + email + " | " + rol.getNombreRol();
    }
}
