package sigecad.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionBD {
    private ConexionBD() {
    }

    public static Connection abrir() throws SQLException {
        String url = entorno("SIGECAD_DB_URL", "jdbc:mysql://localhost:3306/sigecad");
        String usuario = entorno("SIGECAD_DB_USER", "root");
        String password = entorno("SIGECAD_DB_PASSWORD", "");
        return DriverManager.getConnection(url, usuario, password);
    }

    private static String entorno(String nombre, String predeterminado) {
        String valor = System.getenv(nombre);
        return valor == null || valor.isBlank() ? predeterminado : valor;
    }
}
