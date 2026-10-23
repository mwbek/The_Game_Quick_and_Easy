package models;

import java.util.Stack;

public class PilaAscendente implements PilaEnJuego {

    private final Stack<CartaJugador> pila;

    public PilaAscendente() {

        pila = new Stack<>();
    }

    public boolean agregarCarta(CartaJugador cartaNueva) {

        boolean cartaAgregada = true;

        /*
            A la hora de agregar una carta debo revisar algunos casos:
            Caso 1: Si es la primer carta siendo agregada a la pila. En este caso se puede agregar cualquier carta
            Caso 2: No es la primer carta y por lo tanto debo revisar dos condiciones
                    Condicion 1: Son del mismo color, en este caso puede agregar cualquier valor
                    Condicion 2: Son de distinto color, en este caso el valor debe ser necesariamente MAYOR
         */

        //Caso 1
        if(pila.empty()) {
            pila.push(cartaNueva);
            return cartaAgregada;
        }

        //Caso 2
        CartaJugador cartaAnterior = pila.peek(); //Miro la ultima carta agrega, pero sin sacarla

        if(cartaAnterior.getColor().equals(cartaNueva.getColor()))
        {
            //La carta puede tener cualquier valor
            pila.push(cartaNueva);
        }
        else if(cartaNueva.getValor() > cartaAnterior.getValor())
        {
            pila.push(cartaNueva);
        }
        else {
            cartaAgregada = false;
        }

        return cartaAgregada;
    }

    public String mostrarUltimaCarta(){
        if(pila.isEmpty()) return "[]";
        return pila.peek().toString();
    }

    public String tipoPila() {
        return "ascendente";
    }
}
