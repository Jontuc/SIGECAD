package sigecad.servicio;

import sigecad.modelo.ErrorValidacion;

import java.util.List;

public record ResultadoValidacion(
        int registrosValidos,
        List<ErrorValidacion> errores) {

    public boolean esValido() {
        return errores.isEmpty();
    }
}
