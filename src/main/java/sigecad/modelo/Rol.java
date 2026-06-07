package sigecad.modelo;

public class Rol {
    private int idRol;
    private String nombreRol;

    public Rol(int idRol, String nombreRol) {
        this.idRol = idRol;
        setNombreRol(nombreRol);
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        if (nombreRol == null || nombreRol.isBlank()) {
            throw new IllegalArgumentException("El nombre del rol es obligatorio");
        }
        this.nombreRol = nombreRol.trim();
    }

    @Override
    public String toString() {
        return idRol + " | " + nombreRol;
    }
}
