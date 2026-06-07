package sigecad.modelo;

public class ProcesoETL {
    private int idProceso;
    private String nombre;
    private String descripcion;
    private String fuenteDatos;
    private String destinoDatos;
    private EstadoProceso estado;

    public ProcesoETL(int idProceso, String nombre, String descripcion,
                      String fuenteDatos, String destinoDatos,
                      EstadoProceso estado) {
        this.idProceso = idProceso;
        setNombre(nombre);
        this.descripcion = descripcion == null ? "" : descripcion.trim();
        setFuenteDatos(fuenteDatos);
        setDestinoDatos(destinoDatos);
        setEstado(estado);
    }

    public int getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(int idProceso) {
        this.idProceso = idProceso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = obligatorio(nombre, "nombre");
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getFuenteDatos() {
        return fuenteDatos;
    }

    public void setFuenteDatos(String fuenteDatos) {
        this.fuenteDatos = obligatorio(fuenteDatos, "fuente de datos");
    }

    public String getDestinoDatos() {
        return destinoDatos;
    }

    public void setDestinoDatos(String destinoDatos) {
        this.destinoDatos = obligatorio(destinoDatos, "destino de datos");
    }

    public EstadoProceso getEstado() {
        return estado;
    }

    public void setEstado(EstadoProceso estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        this.estado = estado;
    }

    private String obligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El " + campo + " es obligatorio");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return "%d | %-25s | %-8s | %s -> %s".formatted(
                idProceso, nombre, estado, fuenteDatos, destinoDatos);
    }
}
