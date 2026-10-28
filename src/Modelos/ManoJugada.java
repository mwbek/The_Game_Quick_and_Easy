package Modelos;


import Interfaces.PilaEnJuego;

public class ManoJugada {

    private Carta cartaJugada;
    private PilaEnJuego pilaElegida;

    public ManoJugada(Carta cartaJugada, PilaEnJuego pilaElegida){
        this.cartaJugada = cartaJugada;
        this.pilaElegida = pilaElegida;
    }

    public String getManoJugada() {
        return "Jugaste un " + cartaJugada.getNumero() + " " + cartaJugada.getColor() + "en la pila " + pilaElegida.tipoPila();
    }
}
