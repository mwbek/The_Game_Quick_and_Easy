import Controladores.Juego;
import models.Jugador;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args)
    {
        Jugador j1 = new Jugador("Martin");
        Jugador j2 = new Jugador("Ludmila");
        Jugador j3 = new Jugador("Agustin");
        Jugador j4 = new Jugador("Ignacio");

        List<Jugador> jugadores = new ArrayList<>();

        jugadores.add(j1);
        jugadores.add(j2);
        jugadores.add(j3);
        jugadores.add(j4);

        Juego juego = new Juego(jugadores);

        juego.comenzarPartida();
    }
}
