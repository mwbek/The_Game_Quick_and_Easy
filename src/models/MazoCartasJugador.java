package models;


import java.util.*;

public class MazoCartasJugador {

    private final Stack<CartaJugador> pilaMazo;

    public MazoCartasJugador()
    {
        pilaMazo = llenarMazo();

    }

    private void generarCartasPorColor(ColorCartaJugador color, List<CartaJugador> cartas) {
        for(int i = 1; i < 11; i++){
            cartas.add(new CartaJugador(color, i));
        }
        //Mezcla las cartas
        Collections.shuffle(cartas);
    }

    //Genera 50 cartas de forma aleatoria y las agrega a una PILA
    private Stack<CartaJugador> llenarMazo() {

        Stack<CartaJugador> pilaMazo = new Stack<>();
        List<CartaJugador> cartas = new ArrayList<>();

        ColorCartaJugador[] colores = ColorCartaJugador.values();
        for(int i = 0; i < 5; i++){
            generarCartasPorColor(colores[i], cartas);
        }
        boolean mazoCreado = pilaMazo.addAll(cartas);
        if(mazoCreado) return pilaMazo;

        else throw new EmptyStackException();

    }

    public List<CartaJugador> getMazo() {
        return pilaMazo.stream().toList();
    }

    public CartaJugador tomarCarta(){
        return pilaMazo.pop();
    }
}
