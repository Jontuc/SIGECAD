package sigecad;

import sigecad.dao.AlertaDAO;
import sigecad.dao.ArchivoFuenteDAO;
import sigecad.dao.EjecucionETLDAO;
import sigecad.dao.ErrorValidacionDAO;
import sigecad.dao.LogEjecucionDAO;
import sigecad.dao.ProcesoETLDAO;
import sigecad.modelo.EstadoEjecucion;
import sigecad.modelo.EstadoProceso;
import sigecad.modelo.ProcesoETL;
import sigecad.servicio.EjecucionETLService;
import sigecad.servicio.ProcesoETLService;
import sigecad.servicio.ValidadorDatosService;

import java.nio.file.Files;
import java.nio.file.Path;

public final class PruebaAplicacion {
    public static void main(String[] args) throws Exception {
        ProcesoETLService procesos = new ProcesoETLService(new ProcesoETLDAO());
        EjecucionETLService ejecuciones = new EjecucionETLService(
                procesos, new ArchivoFuenteDAO(), new EjecucionETLDAO(),
                new ErrorValidacionDAO(), new LogEjecucionDAO(),
                new AlertaDAO(), new ValidadorDatosService());
        procesos.registrar(new ProcesoETL(
                0, "Ventas", "Carga CSV", "ventas.csv", "tabla_ventas",
                EstadoProceso.ACTIVO));

        Path valido = Files.createTempFile("sigecad-valido", ".csv");
        Files.writeString(valido, "id;producto;cantidad\n1;Teclado;2\n2;Mouse;3\n");
        verificar(ejecuciones.ejecutar(1, valido, ";").getEstado()
                == EstadoEjecucion.EXITOSO, "CSV valido");

        Path invalido = Files.createTempFile("sigecad-invalido", ".csv");
        Files.writeString(invalido, "id;producto;cantidad\n1;Teclado\n");
        verificar(ejecuciones.ejecutar(1, invalido, ";").getEstado()
                == EstadoEjecucion.FALLIDO, "CSV invalido");
        verificar(ejecuciones.listarEjecuciones().size() == 2, "historial");
        verificar(ejecuciones.listarErrores().size() == 1, "errores");
        verificar(ejecuciones.listarAlertas().size() == 1, "alertas");
        verificar(ejecuciones.resumirEventos().size() == 5, "polimorfismo");
        verificar(procesos.buscarPorTexto("ventas").size() == 1, "busqueda");
        System.out.println("Todas las pruebas finalizaron correctamente.");
    }

    private static void verificar(boolean condicion, String caso) {
        if (!condicion) {
            throw new AssertionError("Fallo: " + caso);
        }
        System.out.println("OK - " + caso);
    }
}
