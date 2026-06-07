package sigecad.modelo;

import java.time.LocalDateTime;

public class EjecucionETL {
    private int idEjecucion;
    private final int idProceso;
    private final int idArchivo;
    private final LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private EstadoEjecucion estado;
    private int registrosProcesados;

    public EjecucionETL(int idEjecucion, int idProceso, int idArchivo,
                        LocalDateTime fechaInicio, LocalDateTime fechaFin,
                        EstadoEjecucion estado, int registrosProcesados) {
        this.idEjecucion = idEjecucion;
        this.idProceso = idProceso;
        this.idArchivo = idArchivo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.registrosProcesados = registrosProcesados;
    }

    public int getIdEjecucion() {
        return idEjecucion;
    }

    public void setIdEjecucion(int idEjecucion) {
        this.idEjecucion = idEjecucion;
    }

    public int getIdProceso() {
        return idProceso;
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public EstadoEjecucion getEstado() {
        return estado;
    }

    public int getRegistrosProcesados() {
        return registrosProcesados;
    }

    public void finalizar(EstadoEjecucion estado, int registrosProcesados) {
        this.estado = estado;
        this.registrosProcesados = registrosProcesados;
        this.fechaFin = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "%d | proceso=%d | archivo=%d | %s | registros=%d".formatted(
                idEjecucion, idProceso, idArchivo, estado, registrosProcesados);
    }
}
