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
import sigecad.modelo.EstadoProceso;
import sigecad.modelo.ProcesoETL;
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
        boolean mysql = args.length > 0 && "--mysql".equalsIgnoreCase(args[0]);
        try (Connection conexion = mysql ? ConexionBD.abrir() : null;
             Scanner scanner = new Scanner(System.in)) {
            Contexto contexto = crearContexto(conexion);
            if (!mysql) {
                cargarDemo(contexto.procesos());
            }
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
        ProcesoETLDAO procesoDAO = conexion == null
                ? new ProcesoETLDAO() : new ProcesoETLDAO(conexion);
        ProcesoETLService procesos = new ProcesoETLService(procesoDAO);
        EjecucionETLService ejecuciones = new EjecucionETLService(
                procesos,
                conexion == null ? new ArchivoFuenteDAO() : new ArchivoFuenteDAO(conexion),
                conexion == null ? new EjecucionETLDAO() : new EjecucionETLDAO(conexion),
                conexion == null ? new ErrorValidacionDAO() : new ErrorValidacionDAO(conexion),
                conexion == null ? new LogEjecucionDAO() : new LogEjecucionDAO(conexion),
                conexion == null ? new AlertaDAO() : new AlertaDAO(conexion),
                new ValidadorDatosService(),
                conexion);
        return new Contexto(procesos, ejecuciones);
    }

    private static void cargarDemo(ProcesoETLService procesos) {
        procesos.registrar(new ProcesoETL(
                0, "Carga de ventas CSV", "Carga informacion de ventas",
                "ventas.csv", "tabla_ventas", EstadoProceso.ACTIVO));
        procesos.registrar(new ProcesoETL(
                0, "Carga de clientes CSV", "Carga informacion de clientes",
                "clientes.csv", "tabla_clientes", EstadoProceso.ACTIVO));
    }

    private record Contexto(
            ProcesoETLService procesos,
            EjecucionETLService ejecuciones) {
    }
}
