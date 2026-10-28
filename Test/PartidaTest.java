import Enumerados.ColorCarta;
import Enumerados.EstadoDePartida;
import Interfaces.PilaEnJuego;
import Modelos.*;
import org.junit.jupiter.api.*;

import java.util.ArrayDeque;
import java.util.Queue;

public class PartidaTest {


    Jugador j1,j2;
    Partida nuevaPartida;
    Mazo mazo;
    Queue<Jugador> jugadores = new ArrayDeque<>();

    @BeforeEach
    public void setup()
    {
        j1 = new Jugador("Martin");
        j2 = new Jugador("Ludmila");
        mazo = new Mazo();
        mazo.llenarMazo();

        jugadores.add(j1);
        jugadores.add(j2);
        nuevaPartida = new Partida(jugadores,mazo);

        nuevaPartida.repartirCartas();
        nuevaPartida.pasarRonda();

    }

    @Test
    public void crearPartidaTest()
    {
        Assertions.assertNotNull(nuevaPartida);
        Assertions.assertEquals(EstadoDePartida.JUGANDO, nuevaPartida.getEstadoPartida());
        Assertions.assertEquals(2, nuevaPartida.getJugadores().size());
        Assertions.assertEquals(1, nuevaPartida.getRondaActual());
    }

    @Test
    @DisplayName("Testeo el pasaje de turnos entre jugadores")
    public void JugarManoYPasarTurno(){

        PilaAscendente pilaAsc = new PilaAscendente();

        //Juega Jugador 1
        Assertions.assertEquals(j1, nuevaPartida.getJugadorturnoActual());
        nuevaPartida.jugarMano(j1, new Carta(ColorCarta.ROJO, 3), pilaAsc);
        Assertions.assertEquals(j2, nuevaPartida.pasarRonda());


        //Juega Jugador 2
        Assertions.assertEquals(j2, nuevaPartida.getJugadorturnoActual());
        nuevaPartida.jugarMano(j2, new Carta(ColorCarta.VERDE, 5), pilaAsc);
        Assertions.assertEquals(j1, nuevaPartida.pasarRonda());


        //Vuelve a jugar Jugador 1
        //Juega Jugador 1
        nuevaPartida.jugarMano(j1, new Carta(ColorCarta.ROJO, 7), pilaAsc);
        Assertions.assertEquals(j2, nuevaPartida.pasarRonda());
    }

    @Test
    @DisplayName("Los jugadores pierden cuando alguno no puede jugar ninguna de sus dos cartas")

    public void jugadoresPierdenTest()
    {
        PilaEnJuego pilaAsc = new PilaAscendente();

        //Jugador 1 juega su primer mano y pasa de ronda
        nuevaPartida.jugarMano(j1, new Carta(ColorCarta.ROJO, 10), pilaAsc);
        nuevaPartida.pasarRonda();
        Assertions.assertFalse(nuevaPartida.esFinDeJuego());

        //Jugador 2 intenta jugar un 9 azul. No puede
        nuevaPartida.jugarMano(j2, new Carta(ColorCarta.AZUL, 9), pilaAsc);
        Assertions.assertFalse(nuevaPartida.esFinDeJuego());

        //Jugador 2 intenta jugar su segunda carta, un 8 verde. Tampoco puede
        nuevaPartida.jugarMano(j2, new Carta(ColorCarta.VERDE, 8), pilaAsc);

        //El contador de intentos de jugador vale 2, se chequea el fin de juego y es true
        Assertions.assertTrue(nuevaPartida.esFinDeJuego());
    }

    @Test
    public void jugadoresGananTest()
    {
        Mazo mazo = new Mazo();

        //Mazo en 0
        Partida nuevaPartida2 = new Partida(jugadores, mazo);

        Assertions.assertTrue(nuevaPartida2.esFinDeJuego());
    }

    @Test
    public void jugadorActualTest()
    {

        Queue<Jugador> colaJugadores = new ArrayDeque<>();
        colaJugadores.add(j1);
        colaJugadores.add(j2);

        Partida nuevaPartida3 = new Partida(colaJugadores,mazo);

        Assertions.assertEquals(j1, nuevaPartida3.getJugadorturnoActual());

        nuevaPartida.pasarRonda();

        Assertions.assertEquals(j2, nuevaPartida.getJugadorturnoActual());

        nuevaPartida.pasarRonda();
        Assertions.assertEquals(j1, nuevaPartida.getJugadorturnoActual());


    }


}
