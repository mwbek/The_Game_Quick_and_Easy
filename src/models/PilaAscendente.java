package models;

import exceptions.JugadaInvalidaException;

import java.util.Optional;
import java.util.Stack;

public class PilaAscendente implements PilaEnJuego {

    private final Stack<CartaJugador> pila;

    public PilaAscendente() {

        pila = new Stack<>();
    }

    public void agregarCarta(CartaJugador cartaNueva) {

        //Caso 1: Si es la primer carta siendo agregada a la pila. En este caso se puede agregar cualquier carta
        if(pila.empty()) {
            pila.push(cartaNueva);
        }

        //Caso 2: No es la primer carta y por lo tanto debo revisar dos condiciones
        //       Condicion 1: Son del mismo color, en este caso puede agregar cualquier valor
        //       Condicion 2: Son de distinto color, en este caso el valor debe ser necesariamente MAYOR

        CartaJugador cartaAnterior = pila.peek(); //Miro la ultima carta agrega, pero sin sacarla
        if(cartaAnterior.getColor().equals(cartaNueva.getColor())) {
            //La carta puede tener cualquier valor
            pila.push(cartaNueva);
        }
        else if(cartaNueva.getValor() > cartaAnterior.getValor()) {
            pila.push(cartaNueva);
        }
        else {
            throw new JugadaInvalidaException("La carta es menor a:  " + cartaAnterior.toString());
        }

    }

    public String tipoPila() {
        return "ascendente";
    }

    //Estos metodos se repiten en pilaAscendent y Desendente. Podrian ir en una clase heredada (abstracta?)
    public Optional<CartaJugador> getUltimaCarta() {
        return Optional.of( pila.peek());
    }

    public String verUltimaCartaComoTexto(){
        if(pila.isEmpty()) return "[]";
        return pila.peek().toString();
    }


}
