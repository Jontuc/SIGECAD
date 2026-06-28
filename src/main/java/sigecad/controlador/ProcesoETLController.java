package sigecad.controlador;

import sigecad.modelo.EstadoProceso;
import sigecad.modelo.ProcesoETL;
import sigecad.servicio.ProcesoETLService;

import java.util.List;

public class ProcesoETLController {
    private final ProcesoETLService procesoService;

    public ProcesoETLController(ProcesoETLService procesoService) {
        this.procesoService = procesoService;
    }

    public ProcesoETL registrar(String nombre, String descripcion,
                                String fuenteDatos, String destinoDatos) {
        return procesoService.registrar(new ProcesoETL(
                0, nombre, descripcion, fuenteDatos, destinoDatos,
                EstadoProceso.ACTIVO));
    }

    public List<ProcesoETL> listar() {
        return procesoService.listarOrdenados();
    }

    public List<ProcesoETL> buscarPorTexto(String texto) {
        return procesoService.buscarPorTexto(texto);
    }

    public ProcesoETL consultarPorId(int id) {
        return procesoService.buscarPorId(id);
    }

    public void modificar(int id, String nombre, String descripcion,
                          String fuenteDatos, String destinoDatos,
                          EstadoProceso estado) {
        procesoService.modificar(new ProcesoETL(
                id, nombre, descripcion, fuenteDatos, destinoDatos, estado));
    }

    public void cambiarEstado(int id, EstadoProceso estado) {
        procesoService.cambiarEstado(id, estado);
    }

    public void bajaLogica(int id) {
        procesoService.bajaLogica(id);
    }
}
