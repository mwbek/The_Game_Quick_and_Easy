package Modelos;

import Enumerados.CartaDeIndicacion;
import Interfaces.IPilaDeIndicacion;

import java.io.Serializable;
import java.util.Stack;

/// Representa las cartas de indicacion (Ascendente y Descendente)
public class PilaDeIndicacion implements IPilaDeIndicacion, Serializable {

    private final CartaDeIndicacion indicacion;
    private final Stack<Carta> cartas;
    public PilaDeIndicacion(CartaDeIndicacion indicacion) {
        if(indicacion == null) throw new IllegalArgumentException("La carta de indicacion no puede ser nula");

        this.indicacion = indicacion;
        cartas = new Stack<>();
    }

    @Override
    public boolean agregarCarta(Carta carta) {
        if (carta == null) throw new IllegalArgumentException("La carta no puede ser nula");
        if(puedeAgregarCarta(carta)){
            cartas.push(carta);
            return true;
        }
        else return false;
    }
    @Override
    public boolean puedeAgregarCarta(Carta carta) {

        if(cartas.contains(carta)) return false;

        boolean sePuedeAgregar;
        //Caso 1: Si es la primer carta siendo agregada a la pila. En este caso se puede agregar cualquier carta
        if(cartas.empty()) {
            return true;
        }

        Carta cartaTope = cartas.peek();

        //Caso 2: No es la primer carta y por lo tanto debo revisar dos condiciones
        //Condicion 1: Son del mismo color, en este caso puede agregar cualquier valor
        if(carta.getColor().equals(cartaTope.getColor())){
            sePuedeAgregar = true;
        }
        else{
            //Condicion 2: Son de distinto color, en este reviso a que pila se quiere agregar la carta
            sePuedeAgregar = validarMovimientoPorPila(cartaTope, carta);
        }

        return sePuedeAgregar;
    }



    /// @return  Retorna el tipo de pila de indicacion
    @Override
    public CartaDeIndicacion getTipo(){
        return indicacion;
    }



    private boolean validarMovimientoPorPila(Carta cartaTope, Carta cartaNueva){
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
    @Override
    public Carta getUltimaCarta() {
        if(cartas.isEmpty()) return null;
        return cartas.peek();
    }


}
