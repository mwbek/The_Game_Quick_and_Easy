package models;


import java.util.*;

public class Mazo{

    private final int cantidad = 52;

    private Stack<CartaJugador> pilaMazo;

    public Mazo()
    {
        pilaMazo = new Stack<>();
        llenarMazo();
    }

    //Este metodo permite generar juegos de 10 cartas aleatorias del mismo color
    private void generarCartasPorColor(ColorCartaJugador color, List<CartaJugador> cartas)
    {
        for(int i = 1; i < 11; i++){
            cartas.add(new CartaJugador(color, i));
        }
        //Mezcla las cartas
        Collections.shuffle(cartas);
    }

    private void llenarMazo()
    {
        /* Tengo que generar 50 cartas de forma aleatoria.
            Para hacer esto puedo solicitar aleatoriamente los colores y los numeros*/
        List<CartaJugador> cartas = new ArrayList<>();

        ColorCartaJugador[] colores = ColorCartaJugador.values();
        for(int i = 0; i < 5; i++){
            generarCartasPorColor(colores[i], cartas);
        }

        pilaMazo.addAll(cartas);

    }

    public Stack<CartaJugador> getMazo() {
        return pilaMazo;
    }

}
