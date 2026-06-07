package sigecad.modelo;

public class ErrorValidacion extends EventoEjecucion {
    private final int fila;
    private final String campo;
    private final String descripcion;

    public ErrorValidacion(int idError, int idEjecucion, int fila,
                           String campo, String descripcion) {
        super(idError, idEjecucion);
        this.fila = fila;
        this.campo = campo;
        this.descripcion = descripcion;
    }

    public int getIdError() {
        return getId();
    }

    public void setIdError(int idError) {
        setId(idError);
    }

    public int getFila() {
        return fila;
    }

    public String getCampo() {
        return campo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String resumir() {
        return "ERROR | fila=" + fila + " | " + campo + " | " + descripcion;
    }

    @Override
    public String toString() {
        return resumir();
    }
}
