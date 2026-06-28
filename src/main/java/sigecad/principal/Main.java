package sigecad.principal;

import sigecad.controlador.EjecucionETLController;
import sigecad.controlador.ProcesoETLController;
import sigecad.controlador.SIGECADController;
import sigecad.dao.AlertaDAO;
import sigecad.dao.ArchivoFuenteDAO;
import sigecad.dao.ConexionBD;
import sigecad.dao.EjecucionETLDAO;
import sigecad.dao.ErrorValidacionDAO;
import sigecad.dao.LogEjecucionDAO;
import sigecad.dao.ProcesoETLDAO;
import sigecad.servicio.EjecucionETLService;
import sigecad.servicio.ProcesoETLService;
import sigecad.servicio.ValidadorDatosService;
import sigecad.vista.MenuConsolaView;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        try (Connection conexion = ConexionBD.abrir();
             Scanner scanner = new Scanner(System.in)) {
            Contexto contexto = crearContexto(conexion);
            MenuConsolaView vista = new MenuConsolaView(scanner);
            SIGECADController controlador = new SIGECADController(
                    vista,
                    new ProcesoETLController(contexto.procesos()),
                    new EjecucionETLController(contexto.ejecuciones()));
            controlador.iniciar();
        } catch (SQLException e) {
            System.err.println("No fue posible conectar con MySQL: " + e.getMessage());
        }
    }

    private static Contexto crearContexto(Connection conexion) {
        ProcesoETLDAO procesoDAO = new ProcesoETLDAO(conexion);
        ProcesoETLService procesos = new ProcesoETLService(procesoDAO);
        EjecucionETLService ejecuciones = new EjecucionETLService(
                procesos,
                new ArchivoFuenteDAO(conexion),
                new EjecucionETLDAO(conexion),
                new ErrorValidacionDAO(conexion),
                new LogEjecucionDAO(conexion),
                new AlertaDAO(conexion),
                new ValidadorDatosService(),
                conexion);
        return new Contexto(procesos, ejecuciones);
    }

    private record Contexto(
            ProcesoETLService procesos,
            EjecucionETLService ejecuciones) {
    }
}
