package sigecad.servicio;

import sigecad.modelo.ErrorValidacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ValidadorDatosService {
    public ResultadoValidacion validarCsv(
            int idEjecucion, Path ruta, String separador) {
        List<ErrorValidacion> errores = new ArrayList<>();
        if (!ruta.getFileName().toString().toLowerCase().endsWith(".csv")) {
            errores.add(new ErrorValidacion(
                    0, idEjecucion, 0, "archivo",
                    "El archivo debe tener extension CSV"));
            return new ResultadoValidacion(0, errores);
        }
        if (!Files.isRegularFile(ruta)) {
            errores.add(new ErrorValidacion(
                    0, idEjecucion, 0, "ruta_archivo",
                    "El archivo no existe o no puede leerse"));
            return new ResultadoValidacion(0, errores);
        }

        String delimitador = separador == null || separador.isEmpty() ? "," : separador;
        int validos = 0;
        try (BufferedReader lector = Files.newBufferedReader(
                ruta, StandardCharsets.UTF_8)) {
            String cabecera = lector.readLine();
            if (cabecera == null || cabecera.isBlank()) {
                errores.add(new ErrorValidacion(
                        0, idEjecucion, 1, "cabecera", "El archivo esta vacio"));
                return new ResultadoValidacion(0, errores);
            }
            int columnasEsperadas = cabecera.split(
                    Pattern.quote(delimitador), -1).length;
            String linea;
            int fila = 1;
            while ((linea = lector.readLine()) != null) {
                fila++;
                String[] columnas = linea.split(Pattern.quote(delimitador), -1);
                if (columnas.length != columnasEsperadas) {
                    errores.add(new ErrorValidacion(
                            0, idEjecucion, fila, "columnas",
                            "Se esperaban " + columnasEsperadas
                                    + " columnas y se encontraron " + columnas.length));
                } else if (linea.isBlank()) {
                    errores.add(new ErrorValidacion(
                            0, idEjecucion, fila, "fila", "La fila esta vacia"));
                } else {
                    validos++;
                }
            }
        } catch (IOException e) {
            errores.add(new ErrorValidacion(
                    0, idEjecucion, 0, "archivo",
                    "No se pudo leer el CSV: " + e.getMessage()));
        }
        return new ResultadoValidacion(validos, errores);
    }
}
