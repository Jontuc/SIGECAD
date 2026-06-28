package sigecad.principal;

import sigecad.dao.ConexionBD;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class InicializadorBD {
    private InicializadorBD() {
    }

    public static void main(String[] args) {
        Path script = Path.of("sql", "sigecad.sql");
        try (Connection conexion = ConexionBD.abrirServidor();
             Statement statement = conexion.createStatement()) {
            for (String sentencia : leerSentencias(script)) {
                statement.execute(sentencia);
            }
            System.out.println("Base de datos SIGECAD inicializada correctamente.");
        } catch (SQLException e) {
            System.err.println("No se pudo inicializar MySQL: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("No se pudo leer " + script + ": " + e.getMessage());
        }
    }

    private static List<String> leerSentencias(Path script) throws IOException {
        String contenido = Files.readString(script, StandardCharsets.UTF_8);
        contenido = contenido.replaceAll("(?m)^\\s*--.*$", "");
        List<String> sentencias = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean enTexto = false;
        for (int i = 0; i < contenido.length(); i++) {
            char c = contenido.charAt(i);
            if (c == '\'') {
                enTexto = !enTexto;
            }
            if (c == ';' && !enTexto) {
                agregarSiNoVacia(sentencias, actual);
            } else {
                actual.append(c);
            }
        }
        agregarSiNoVacia(sentencias, actual);
        return sentencias;
    }

    private static void agregarSiNoVacia(List<String> sentencias, StringBuilder actual) {
        String sentencia = actual.toString().trim();
        if (!sentencia.isEmpty()) {
            sentencias.add(sentencia);
        }
        actual.setLength(0);
    }
}
