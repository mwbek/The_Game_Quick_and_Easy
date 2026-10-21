package models;

import java.awt.*;

public class CartaJugador {

    //Atributos

    //Son de tipo final ya que una vez creada la carta nunca mas cambian
    private final ColorCartaJugador color;
    private final int valor;

    public CartaJugador(ColorCartaJugador color, int valor)
    {
        this.color = color;
        this.valor = valor;
    }

    public ColorCartaJugador getColor(){ return color;}
    public int getValor() {return valor;}

    //Sobreescribo la clase Equals
    @Override
    public boolean equals(Object obj) {

        //1) Comparo el objeto con si mismo
        if(obj == this) return true;

        //2)Chequeo que el objeto pasado sea CartaJugador
        if(! (obj instanceof CartaJugador)) return false;

        //3)Casteo el tipo de dato y comparo con el atributo 'color'

        CartaJugador cj = (CartaJugador) obj;

        return this.color.equals(cj.color) && cj.valor == this.valor;

    }
}
