package sigecad.modelo;

public abstract class EventoEjecucion {
    private int id;
    private final int idEjecucion;

    protected EventoEjecucion(int id, int idEjecucion) {
        this.id = id;
        this.idEjecucion = idEjecucion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEjecucion() {
        return idEjecucion;
    }

    public abstract String resumir();
}
