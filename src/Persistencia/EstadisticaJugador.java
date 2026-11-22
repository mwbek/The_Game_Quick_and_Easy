package Persistencia;

public class EstadisticaJugador {

    private final String nombre;
    private int ganadas;
    private int perdidas;
    private int abandonos;

    public EstadisticaJugador(String nombre) {
        this.nombre = nombre;
    }

    public void registrarVictoria()  { ganadas++; }
    public void registrarDerrota()   { perdidas++; }
    public void registrarAbandono()  { abandonos++; }

    public String getNombre()   { return nombre; }
    public int getGanadas()     { return ganadas; }
    public int getPerdidas()    { return perdidas; }
    public int getAbandonos()   { return abandonos; }

    @Override
    public String toString() {
        return "[" + nombre + "] - Ganado: " + ganadas + " / Perdido: " + perdidas + " / Abandonos: " + abandonos;
    }

}
