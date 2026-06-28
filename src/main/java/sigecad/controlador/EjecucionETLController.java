package sigecad.controlador;

import sigecad.modelo.Alerta;
import sigecad.modelo.EjecucionETL;
import sigecad.modelo.ErrorValidacion;
import sigecad.modelo.LogEjecucion;
import sigecad.servicio.EjecucionETLService;

import java.nio.file.Path;
import java.util.List;

public class EjecucionETLController {
    private final EjecucionETLService ejecucionService;

    public EjecucionETLController(EjecucionETLService ejecucionService) {
        this.ejecucionService = ejecucionService;
    }

    public EjecucionETL ejecutarCsv(int idProceso, String rutaCsv, String separador) {
        return ejecucionService.ejecutar(idProceso, Path.of(rutaCsv), separador);
    }

    public List<EjecucionETL> listarEjecuciones() {
        return ejecucionService.listarEjecuciones();
    }

    public List<ErrorValidacion> listarErrores() {
        return ejecucionService.listarErrores();
    }

    public List<LogEjecucion> listarLogs() {
        return ejecucionService.listarLogs();
    }

    public List<Alerta> listarAlertas() {
        return ejecucionService.listarAlertas();
    }
}
