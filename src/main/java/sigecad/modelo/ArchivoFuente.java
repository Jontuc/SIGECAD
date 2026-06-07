package sigecad.modelo;

import java.time.LocalDateTime;

public class ArchivoFuente {
    private int idArchivo;
    private final String nombreArchivo;
    private final String rutaArchivo;
    private final LocalDateTime fechaRecepcion;

    public ArchivoFuente(int idArchivo, String nombreArchivo,
                         String rutaArchivo, LocalDateTime fechaRecepcion) {
        this.idArchivo = idArchivo;
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
        this.fechaRecepcion = fechaRecepcion;
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    @Override
    public String toString() {
        return idArchivo + " | " + nombreArchivo + " | " + rutaArchivo;
    }
}
