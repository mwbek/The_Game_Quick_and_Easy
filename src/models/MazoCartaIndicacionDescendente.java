package models;

public class MazoCartaIndicacionDescendente implements ICartasIndicacion {
    @Override
    public boolean compararUltimaCarta(CartaJugador carta) {
        return false;
    }

    @Override
    public void agregarCarta(CartaJugador carta) {

    }
}
