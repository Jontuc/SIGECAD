package sigecad.controlador;

import sigecad.modelo.EstadoProceso;
import sigecad.modelo.ProcesoETL;
import sigecad.vista.MenuConsolaView;

public class SIGECADController {
    private final MenuConsolaView vista;
    private final ProcesoETLController procesoController;
    private final EjecucionETLController ejecucionController;

    public SIGECADController(MenuConsolaView vista,
                             ProcesoETLController procesoController,
                             EjecucionETLController ejecucionController) {
        this.vista = vista;
        this.procesoController = procesoController;
        this.ejecucionController = ejecucionController;
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.leerOpcionMenu();
            try {
                procesar(opcion);
            } catch (RuntimeException e) {
                vista.mostrarError(e.getMessage());
            }
        } while (opcion != 0);
    }

    private void procesar(int opcion) {
        switch (opcion) {
            case 1 -> registrarProceso();
            case 2 -> vista.mostrarListado(procesoController.listar());
            case 3 -> vista.mostrarListado(procesoController.buscarPorTexto(
                    vista.leerTextoObligatorio("Texto: ")));
            case 4 -> vista.mostrarResultado(procesoController.consultarPorId(
                    vista.leerEntero("ID del proceso: ")));
            case 5 -> modificarProceso();
            case 6 -> cambiarEstado();
            case 7 -> bajaLogica();
            case 8 -> ejecutarCsv();
            case 9 -> vista.mostrarListado(ejecucionController.listarEjecuciones());
            case 10 -> vista.mostrarListado(ejecucionController.listarErrores());
            case 11 -> vista.mostrarListado(ejecucionController.listarLogs());
            case 12 -> vista.mostrarListado(ejecucionController.listarAlertas());
            case 0 -> vista.mostrarMensaje("SIGECAD finalizado.");
            default -> vista.mostrarMensaje("Opcion inexistente.");
        }
    }

    private void registrarProceso() {
        ProcesoETL proceso = procesoController.registrar(
                vista.leerTextoObligatorio("Nombre: "),
                vista.leerTextoObligatorio("Descripcion: "),
                vista.leerTextoObligatorio("Fuente: "),
                vista.leerTextoObligatorio("Destino: "));
        vista.mostrarMensaje("Registrado: " + proceso);
    }

    private void modificarProceso() {
        int id = vista.leerEntero("ID del proceso: ");
        procesoController.modificar(
                id,
                vista.leerTextoObligatorio("Nuevo nombre: "),
                vista.leerTextoObligatorio("Nueva descripcion: "),
                vista.leerTextoObligatorio("Nueva fuente: "),
                vista.leerTextoObligatorio("Nuevo destino: "),
                vista.leerEstadoProceso());
        vista.mostrarMensaje("Proceso actualizado.");
    }

    private void cambiarEstado() {
        procesoController.cambiarEstado(
                vista.leerEntero("ID del proceso: "),
                vista.leerEstadoProceso());
        vista.mostrarMensaje("Estado actualizado.");
    }

    private void bajaLogica() {
        int id = vista.leerEntero("ID del proceso: ");
        if (vista.confirmar("Confirma la baja logica del proceso " + id + "?")) {
            procesoController.bajaLogica(id);
            vista.mostrarMensaje("Proceso dado de baja logicamente.");
        } else {
            vista.mostrarMensaje("Operacion cancelada.");
        }
    }

    private void ejecutarCsv() {
        vista.mostrarResultado(ejecucionController.ejecutarCsv(
                vista.leerEntero("ID del proceso: "),
                vista.leerTextoObligatorio("Ruta del CSV: "),
                vista.leerTextoObligatorio("Separador: ")));
    }
}
