package Modelos;

import Enumerados.ColorCarta;
import Interfaces.ICarta;

import java.io.Serializable;
import java.util.Objects;

public class Carta implements ICarta,Serializable {

    //Atributos

    //Son de tipo final ya que una vez creada la carta nunca mas cambian
    private final ColorCarta color;
    private final int valor;

    public Carta(ColorCarta color, int valor) {
        this.color = color;
        this.valor = valor;
    }

    public ColorCarta getColor(){ return color;}
    public int getNumero() {return valor;}

    //Sobreescribo la clase hashSet
    @Override
    public int hashCode(){
        return Objects.hash(color, valor);
    }


    //Sobreescribo la clase Equals
    @Override
    public boolean equals(Object obj) {
        //1) Comparo el objeto con si mismo
        if(obj == this) return true;

        //2)Chequeo que el objeto pasado sea CartaJugador
        if(! (obj instanceof Carta)) return false;

        //3)Casteo el tipo de dato y comparo con el atributo 'color'

        Carta cj = (Carta) obj;

        return this.color.equals(cj.color) && cj.valor == this.valor;
    }

    @Override
    public String toString() {
        return "[" + this.valor + " " + this.color.toString() + "]";
    }
}
