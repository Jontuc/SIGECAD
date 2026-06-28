package sigecad.servicio;

import sigecad.dao.ProcesoETLDAO;
import sigecad.modelo.EstadoProceso;
import sigecad.modelo.ProcesoETL;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProcesoETLService {
    private final ProcesoETLDAO procesoDAO;

    public ProcesoETLService(ProcesoETLDAO procesoDAO) {
        this.procesoDAO = procesoDAO;
    }

    public ProcesoETL registrar(ProcesoETL proceso) {
        validar(proceso);
        return procesoDAO.guardar(proceso);
    }

    public List<ProcesoETL> listarOrdenados() {
        List<ProcesoETL> procesos = new ArrayList<>(procesoDAO.listar());
        procesos.sort(Comparator.comparing(
                ProcesoETL::getNombre, String.CASE_INSENSITIVE_ORDER));
        return procesos;
    }

    public ProcesoETL buscarPorId(int id) {
        return procesoDAO.buscarPorId(id).orElseThrow(
                () -> new IllegalArgumentException("No existe el proceso " + id));
    }

    public List<ProcesoETL> buscarPorTexto(String texto) {
        String criterio = texto == null ? "" : texto.trim().toLowerCase();
        if (criterio.isEmpty()) {
            throw new IllegalArgumentException("El texto es obligatorio");
        }
        return listarOrdenados().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(criterio)
                        || p.getDescripcion().toLowerCase().contains(criterio))
                .toList();
    }

    public void cambiarEstado(int id, EstadoProceso estado) {
        procesoDAO.actualizarEstado(id, estado);
    }

    public void modificar(ProcesoETL proceso) {
        validar(proceso);
        if (proceso.getIdProceso() <= 0) {
            throw new IllegalArgumentException("El identificador del proceso es obligatorio");
        }
        procesoDAO.actualizar(proceso);
    }

    public void bajaLogica(int id) {
        procesoDAO.bajaLogica(id);
    }

    private void validar(ProcesoETL proceso) {
        if (proceso == null) {
            throw new IllegalArgumentException("El proceso es obligatorio");
        }
        if (proceso.getEstado() == null) {
            throw new IllegalArgumentException("El estado del proceso es obligatorio");
        }
    }
}
