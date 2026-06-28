package sigecad.vista;

import sigecad.modelo.EstadoProceso;

import java.util.List;
import java.util.Scanner;

public class MenuConsolaView {
    private final Scanner scanner;

    public MenuConsolaView(Scanner scanner) {
        this.scanner = scanner;
    }

    public int leerOpcionMenu() {
        mostrarMenu();
        return leerEntero("Opcion: ");
    }

    public void mostrarMenu() {
        System.out.println("""

                ===== SIGECAD =====
                1. Registrar proceso ETL
                2. Listar procesos
                3. Buscar procesos por texto
                4. Consultar proceso por ID
                5. Modificar proceso completo
                6. Cambiar estado activo/inactivo
                7. Baja logica de proceso
                8. Validar y registrar ejecucion CSV
                9. Ver ejecuciones
                10. Ver errores de validacion
                11. Ver logs
                12. Ver alertas pendientes
                0. Salir
                """);
    }

    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                mostrarMensaje("Ingrese un numero entero.");
            }
        }
    }

    public String leerTextoObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            mostrarMensaje("El valor es obligatorio.");
        }
    }

    public EstadoProceso leerEstadoProceso() {
        int valor = leerEntero("Estado (1=ACTIVO, 2=INACTIVO): ");
        return switch (valor) {
            case 1 -> EstadoProceso.ACTIVO;
            case 2 -> EstadoProceso.INACTIVO;
            default -> throw new IllegalArgumentException("Estado invalido");
        };
    }

    public boolean confirmar(String mensaje) {
        System.out.print(mensaje + " (S/N): ");
        String valor = scanner.nextLine().trim();
        return valor.equalsIgnoreCase("S") || valor.equalsIgnoreCase("SI");
    }

    public void mostrarListado(List<?> elementos) {
        if (elementos.isEmpty()) {
            mostrarMensaje("No hay datos.");
        } else {
            elementos.forEach(System.out::println);
        }
    }

    public void mostrarResultado(Object resultado) {
        System.out.println(resultado);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String mensaje) {
        System.out.println("Error: " + mensaje);
    }
}
