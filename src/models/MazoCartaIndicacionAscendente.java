package models;

import exceptions.JugadoresPerdieronException;

import java.io.InvalidObjectException;
import java.util.EmptyStackException;
import java.util.Stack;

public class MazoCartaIndicacionAscendente implements ICartasIndicacion {

    private Stack<CartaJugador> mazo;

    public MazoCartaIndicacionAscendente()
    {
        mazo = new Stack<>();
    }



    private boolean validarMovimiento(CartaJugador cartaNueva)
    {
        /*
            Aca hay 3 caminos:
            1) La carta es del mismo color -> Puede poner la carta cualquier valor
            2) La carta es de distinto color pero un valor superior -> puede poner la carta
            3) No coincide ningun caso, no se pone la carta y por lo tanto se pierde el juego
         */
        CartaJugador ultimaCarta = mazo.peek(); //Miro el tope sin sacarlo
        if(ultimaCarta.getColor().equals(cartaNueva.getColor())){
            //Camino 1: Colores iguales
            return true;
        } else if (ultimaCarta.getValor() < cartaNueva.getValor()) {
            //Camino 2: Colores distintos, valor mayor en nuevaCarta
            return true;
        }
        else{
            //Camino 3: Los jugadores pierden
            throw new JugadoresPerdieronException();
        }
    }


    @Override
    public void agregarCarta(CartaJugador cartaNueva) {

       boolean movimientoValido = validarMovimiento(cartaNueva);

       if(movimientoValido) mazo.push(cartaNueva);
       
    }

}
