package models;


import java.util.ArrayDeque;
import java.util.Queue;

public class ManoJugada {

    private CartaJugador cartaJugada;
    private PilaEnJuego pilaElegida;

    public ManoJugada(CartaJugador cartaJugada, PilaEnJuego pilaElegida){
        this.cartaJugada = cartaJugada;
        this.pilaElegida = pilaElegida;
    }

    public String getManoJugada() {
        return "Jugaste un " + cartaJugada.getValor() + " " + cartaJugada.getColor() + "en la pila " + pilaElegida.tipoPila();
    }
}
