package sigecad.modelo;

import java.time.LocalDateTime;

public class LogEjecucion extends EventoEjecucion {
    private final LocalDateTime fechaHora;
    private final String evento;
    private final String detalle;

    public LogEjecucion(int idLog, int idEjecucion, LocalDateTime fechaHora,
                        String evento, String detalle) {
        super(idLog, idEjecucion);
        this.fechaHora = fechaHora;
        this.evento = evento;
        this.detalle = detalle;
    }

    public int getIdLog() {
        return getId();
    }

    public void setIdLog(int idLog) {
        setId(idLog);
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getEvento() {
        return evento;
    }

    public String getDetalle() {
        return detalle;
    }

    @Override
    public String resumir() {
        return "LOG | " + fechaHora + " | " + evento + " | " + detalle;
    }

    @Override
    public String toString() {
        return resumir();
    }
}
