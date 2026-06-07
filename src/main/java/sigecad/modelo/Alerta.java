package sigecad.modelo;

import java.time.LocalDateTime;

public class Alerta {
    private int idAlerta;
    private final int idEjecucion;
    private final String mensaje;
    private final LocalDateTime fechaAlerta;
    private String estado;

    public Alerta(int idAlerta, int idEjecucion, String mensaje,
                  LocalDateTime fechaAlerta, String estado) {
        this.idAlerta = idAlerta;
        this.idEjecucion = idEjecucion;
        this.mensaje = mensaje;
        this.fechaAlerta = fechaAlerta;
        this.estado = estado;
    }

    public int getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }

    public int getIdEjecucion() {
        return idEjecucion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaAlerta() {
        return fechaAlerta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return idAlerta + " | ejecucion=" + idEjecucion + " | "
                + estado + " | " + mensaje;
    }
}
