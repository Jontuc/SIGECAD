package sigecad.servicio;

import sigecad.dao.AlertaDAO;
import sigecad.dao.ArchivoFuenteDAO;
import sigecad.dao.EjecucionETLDAO;
import sigecad.dao.ErrorValidacionDAO;
import sigecad.dao.LogEjecucionDAO;
import sigecad.modelo.Alerta;
import sigecad.modelo.ArchivoFuente;
import sigecad.modelo.EjecucionETL;
import sigecad.modelo.ErrorValidacion;
import sigecad.modelo.EstadoEjecucion;
import sigecad.modelo.EstadoProceso;
import sigecad.modelo.EventoEjecucion;
import sigecad.modelo.LogEjecucion;
import sigecad.modelo.ProcesoETL;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EjecucionETLService {
    private final ProcesoETLService procesoService;
    private final ArchivoFuenteDAO archivoDAO;
    private final EjecucionETLDAO ejecucionDAO;
    private final ErrorValidacionDAO errorDAO;
    private final LogEjecucionDAO logDAO;
    private final AlertaDAO alertaDAO;
    private final ValidadorDatosService validador;

    public EjecucionETLService(
            ProcesoETLService procesoService,
            ArchivoFuenteDAO archivoDAO,
            EjecucionETLDAO ejecucionDAO,
            ErrorValidacionDAO errorDAO,
            LogEjecucionDAO logDAO,
            AlertaDAO alertaDAO,
            ValidadorDatosService validador) {
        this.procesoService = procesoService;
        this.archivoDAO = archivoDAO;
        this.ejecucionDAO = ejecucionDAO;
        this.errorDAO = errorDAO;
        this.logDAO = logDAO;
        this.alertaDAO = alertaDAO;
        this.validador = validador;
    }

    public EjecucionETL ejecutar(int idProceso, Path rutaCsv, String separador) {
        ProcesoETL proceso = procesoService.buscarPorId(idProceso);
        if (proceso.getEstado() != EstadoProceso.ACTIVO) {
            throw new IllegalStateException("El proceso esta inactivo");
        }
        ArchivoFuente archivo = archivoDAO.guardar(new ArchivoFuente(
                0, rutaCsv.getFileName().toString(),
                rutaCsv.toAbsolutePath().toString(), LocalDateTime.now()));
        EjecucionETL ejecucion = ejecucionDAO.guardar(new EjecucionETL(
                0, idProceso, archivo.getIdArchivo(), LocalDateTime.now(),
                null, EstadoEjecucion.FALLIDO, 0));
        logDAO.guardar(new LogEjecucion(
                0, ejecucion.getIdEjecucion(), LocalDateTime.now(),
                "INICIO", "Inicio de validacion del archivo " + archivo.getNombreArchivo()));

        ResultadoValidacion resultado = validador.validarCsv(
                ejecucion.getIdEjecucion(), rutaCsv, separador);
        for (ErrorValidacion error : resultado.errores()) {
            errorDAO.guardar(error);
        }

        EstadoEjecucion estado = resultado.esValido()
                ? EstadoEjecucion.EXITOSO : EstadoEjecucion.FALLIDO;
        ejecucion.finalizar(estado, resultado.registrosValidos());
        ejecucionDAO.actualizar(ejecucion);

        if (resultado.esValido()) {
            logDAO.guardar(new LogEjecucion(
                    0, ejecucion.getIdEjecucion(), LocalDateTime.now(),
                    "FIN", "CSV validado correctamente"));
        } else {
            logDAO.guardar(new LogEjecucion(
                    0, ejecucion.getIdEjecucion(), LocalDateTime.now(),
                    "ERROR", "Se detectaron errores de validacion"));
            alertaDAO.guardar(new Alerta(
                    0, ejecucion.getIdEjecucion(),
                    "El proceso " + proceso.getNombre()
                            + " finalizo con errores de validacion",
                    LocalDateTime.now(), "PENDIENTE"));
        }
        return ejecucion;
    }

    public List<EjecucionETL> listarEjecuciones() {
        return ejecucionDAO.listar();
    }

    public List<ErrorValidacion> listarErrores() {
        return errorDAO.listar();
    }

    public List<LogEjecucion> listarLogs() {
        return logDAO.listar();
    }

    public List<Alerta> listarAlertas() {
        return alertaDAO.listarPendientes();
    }

    public List<String> resumirEventos() {
        List<EventoEjecucion> eventos = new ArrayList<>();
        eventos.addAll(listarErrores());
        eventos.addAll(listarLogs());
        return eventos.stream().map(EventoEjecucion::resumir).toList();
    }
}
