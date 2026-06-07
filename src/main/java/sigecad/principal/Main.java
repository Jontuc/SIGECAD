package sigecad.principal;

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

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
            ejecutarMenu(scanner, contexto);
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
                new ValidadorDatosService());
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

    private static void ejecutarMenu(Scanner scanner, Contexto contexto) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero(scanner, "Opcion: ");
            try {
                switch (opcion) {
                    case 1 -> registrar(scanner, contexto.procesos());
                    case 2 -> imprimir(contexto.procesos().listarOrdenados());
                    case 3 -> imprimir(contexto.procesos().buscarPorTexto(
                            leerTexto(scanner, "Texto: ")));
                    case 4 -> ejecutar(scanner, contexto.ejecuciones());
                    case 5 -> imprimir(contexto.ejecuciones().listarEjecuciones());
                    case 6 -> imprimir(contexto.ejecuciones().listarErrores());
                    case 7 -> imprimir(contexto.ejecuciones().listarLogs());
                    case 8 -> imprimir(contexto.ejecuciones().listarAlertas());
                    case 9 -> cambiarEstado(scanner, contexto.procesos());
                    case 0 -> System.out.println("SIGECAD finalizado.");
                    default -> System.out.println("Opcion inexistente.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("""

                ===== SIGECAD =====
                1. Registrar proceso ETL
                2. Listar procesos
                3. Buscar procesos
                4. Validar y registrar ejecucion CSV
                5. Ver ejecuciones
                6. Ver errores de validacion
                7. Ver logs
                8. Ver alertas pendientes
                9. Cambiar estado de proceso
                0. Salir
                """);
    }

    private static void registrar(Scanner scanner, ProcesoETLService service) {
        ProcesoETL proceso = new ProcesoETL(
                0, leerTexto(scanner, "Nombre: "),
                leerTexto(scanner, "Descripcion: "),
                leerTexto(scanner, "Fuente: "),
                leerTexto(scanner, "Destino: "), EstadoProceso.ACTIVO);
        System.out.println("Registrado: " + service.registrar(proceso));
    }

    private static void ejecutar(Scanner scanner, EjecucionETLService service) {
        int id = leerEntero(scanner, "ID del proceso: ");
        Path ruta = Path.of(leerTexto(scanner, "Ruta del CSV: "));
        String separador = leerTexto(scanner, "Separador: ");
        System.out.println("Resultado: " + service.ejecutar(id, ruta, separador));
    }

    private static void cambiarEstado(Scanner scanner, ProcesoETLService service) {
        int id = leerEntero(scanner, "ID del proceso: ");
        int valor = leerEntero(scanner, "1=ACTIVO, 2=INACTIVO: ");
        if (valor != 1 && valor != 2) {
            throw new IllegalArgumentException("Estado invalido");
        }
        service.cambiarEstado(id, valor == 1
                ? EstadoProceso.ACTIVO : EstadoProceso.INACTIVO);
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero.");
            }
        }
    }

    private static String leerTexto(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        String valor = scanner.nextLine().trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException("El valor es obligatorio");
        }
        return valor;
    }

    private static void imprimir(List<?> elementos) {
        if (elementos.isEmpty()) {
            System.out.println("No hay datos.");
        } else {
            elementos.forEach(System.out::println);
        }
    }

    private record Contexto(
            ProcesoETLService procesos,
            EjecucionETLService ejecuciones) {
    }
}
