package Modelos;

import Enumerados.CartaDeIndicacion;

import java.util.Optional;
import java.util.Stack;

/// Representa las cartas de indicacion (Ascendente y Descendente)
public class PilaDeIndicacion {

    private final CartaDeIndicacion indicacion;
    private Stack<Carta> cartas;
    public PilaDeIndicacion(CartaDeIndicacion indicacion) {
        if(indicacion == null) throw new IllegalArgumentException("La carta de indicacion no puede ser nula");

        this.indicacion = indicacion;
        cartas = new Stack<>();
    }

    public boolean agregarCarta(Carta carta){
        if(carta == null) throw new IllegalArgumentException("La carta no puede ser nula");

        boolean seAgregoCarta = true;
        //Caso 1: Si es la primer carta siendo agregada a la pila. En este caso se puede agregar cualquier carta
        if(cartas.empty()) {
            cartas.push(carta);
            return seAgregoCarta;
        }

        Carta cartaTope = cartas.peek();

        //Caso 2: No es la primer carta y por lo tanto debo revisar dos condiciones
        //Condicion 1: Son del mismo color, en este caso puede agregar cualquier valor
        if(carta.getColor().equals(cartaTope.getColor())){
            cartas.push(carta);
        }
        else{
            //Condicion 2: Son de distinto color, en este reviso a que pila se quiere agregar la carta
            boolean esMovimientoValido = validarMovimiento(cartaTope, carta);
            if(esMovimientoValido) cartas.push(carta);
            else seAgregoCarta = false;
        }

        return seAgregoCarta;

    }

    /// @Return Retorna el tipo de pila de indicacion
    public CartaDeIndicacion getTipo(){
        return indicacion;
    }



    private boolean validarMovimiento(Carta cartaTope, Carta cartaNueva){
        boolean esMovimientoValido = false;
        if(indicacion == CartaDeIndicacion.ASCENDENTE){
            if(cartaNueva.getNumero() > cartaTope.getNumero()){
                esMovimientoValido = true;
            }
        }
        else{
            if(cartaNueva.getNumero() < cartaTope.getNumero()){
                esMovimientoValido = true;
            }
        }
        return esMovimientoValido;
    }

    //Estos metodos se repiten en pilaAscendent y Desendente. Podrian ir en una clase heredada (abstracta?)
    public Carta getUltimaCarta() {
        if(cartas.isEmpty()) return null;
        return cartas.peek();
    }


}
