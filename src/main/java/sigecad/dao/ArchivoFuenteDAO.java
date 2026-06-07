package sigecad.dao;

import sigecad.modelo.ArchivoFuente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ArchivoFuenteDAO {
    private final Connection conexion;
    private final List<ArchivoFuente> memoria = new ArrayList<>();

    public ArchivoFuenteDAO() {
        conexion = null;
    }

    public ArchivoFuenteDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public ArchivoFuente guardar(ArchivoFuente archivo) {
        if (conexion == null) {
            archivo.setIdArchivo(memoria.size() + 1);
            memoria.add(archivo);
            return archivo;
        }
        String sql = """
                INSERT INTO archivos_fuente
                (nombre_archivo, ruta_archivo, fecha_recepcion) VALUES (?, ?, ?)
                """;
        try (PreparedStatement ps = conexion.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, archivo.getNombreArchivo());
            ps.setString(2, archivo.getRutaArchivo());
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(archivo.getFechaRecepcion()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    archivo.setIdArchivo(rs.getInt(1));
                }
            }
            return archivo;
        } catch (SQLException e) {
            throw new DAOException("No se pudo guardar el archivo", e);
        }
    }
}
