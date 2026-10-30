import Enumerados.CartaDeIndicacion;
import Enumerados.ColorCarta;
import Modelos.*;
import org.junit.jupiter.api.*;

public class TheGameTest {

    TheGame nuevaPartida;
    Mazo mazo;

    @BeforeEach
    public void setup()
    {
        nuevaPartida = new TheGame();
        nuevaPartida.agregarJugador("Martin");
        nuevaPartida.agregarJugador("Ludmila");
        nuevaPartida.inicializarPartida();
    }

    @Test
    public void crearPartidaTest()
    {
        Assertions.assertEquals(50, nuevaPartida.getCantidadCartasMazo());
        nuevaPartida.repartirCartas();
        nuevaPartida.pasarDeRonda();
        Assertions.assertNotNull(nuevaPartida);
        Assertions.assertFalse(nuevaPartida.esFinDeJuego());
        Assertions.assertEquals(2, nuevaPartida.getJugadores().length);
        Assertions.assertEquals(1, nuevaPartida.getRondaActual());

    }

    @Test
    @DisplayName("Testeo el pasaje de turnos entre jugadores")
    public void JugarManoYPasarTurno(){

        PilaDeIndicacion pilaAsc = new PilaDeIndicacion(CartaDeIndicacion.ASCENDENTE);
        mazo = new Mazo();

        //Juega Jugador 1
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 1));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 5));

        Jugador jugadorActual = nuevaPartida.getJugadorActual();
        jugadorActual.tomarCarta(mazo);
        jugadorActual.tomarCarta(mazo);

        Assertions.assertEquals("Martin", jugadorActual.getNombre());
        Assertions.assertFalse(nuevaPartida.pasarDeRonda());

        jugadorActual.jugarCarta(pilaAsc, new Carta(ColorCarta.ROJO, 1));
        Assertions.assertEquals(1, jugadorActual.getCantidadCartasEnMano());
        Assertions.assertTrue(nuevaPartida.pasarDeRonda());
        Assertions.assertEquals(2, jugadorActual.getCantidadCartasEnMano());

        //Juega Jugador 2
        mazo.agregarCarta(new Carta(ColorCarta.AZUL, 2));
        mazo.agregarCarta(new Carta(ColorCarta.AMARILLO, 9));
        jugadorActual = nuevaPartida.getJugadorActual();
        jugadorActual.tomarCarta(mazo);
        jugadorActual.tomarCarta(mazo);

        Assertions.assertEquals("Ludmila", jugadorActual.getNombre());
        Assertions.assertFalse(nuevaPartida.pasarDeRonda());

        jugadorActual.jugarCarta(pilaAsc, new Carta(ColorCarta.AZUL, 2));
        Assertions.assertEquals(1, jugadorActual.getCantidadCartasEnMano());
        Assertions.assertTrue(nuevaPartida.pasarDeRonda());
        Assertions.assertEquals(2, jugadorActual.getCantidadCartasEnMano());

    }

    @Test
    @DisplayName("Los jugadores pierden cuando alguno no puede jugar ninguna de sus dos cartas")

    public void jugadoresPierdenTest()
    {
        mazo = new Mazo();
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 10));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 1));

        Jugador jugadorActual = nuevaPartida.getJugadorActual();
        jugadorActual.tomarCarta(mazo);
        jugadorActual.tomarCarta(mazo);

        //Jugador 1 juega un 10 ROJO en ASC y un 1 VERDE en DESC
        Assertions.assertEquals("Martin", nuevaPartida.getJugadorActual().getNombre());
        Assertions.assertTrue(nuevaPartida.jugarMano(new Carta(ColorCarta.ROJO, 10), CartaDeIndicacion.ASCENDENTE)); //Juego la primer carta y la elimino de la mano
        Assertions.assertTrue(nuevaPartida.jugarMano(new Carta(ColorCarta.VERDE, 1), CartaDeIndicacion.DESCENDENTE)); //Por lo tanto aca sigue siendo el indice 0
        Assertions.assertTrue(nuevaPartida.pasarDeRonda());

        //Jugador 2 toma sus cartas
        jugadorActual = nuevaPartida.getJugadorActual();
        mazo.agregarCarta(new Carta(ColorCarta.AZUL, 3));
        mazo.agregarCarta(new Carta(ColorCarta.AZUL, 5));
        jugadorActual.tomarCarta(mazo);
        jugadorActual.tomarCarta(mazo);

        Assertions.assertEquals("Ludmila", nuevaPartida.getJugadorActual().getNombre());
        Assertions.assertTrue(nuevaPartida.esFinDeJuego());

    }

    @Test
    public void jugadoresGananTest()
    {
        TheGame nuevaPartida2 = new TheGame();

        nuevaPartida2.agregarJugador("Martin");
        nuevaPartida2.agregarJugador("Ludmila");

        Mazo mazo = new Mazo();
        //Los jugadores inicializados tienen cartas y el mazo es 0
        Assertions.assertTrue(nuevaPartida2.esFinDeJuego());

        nuevaPartida2.inicializarPartida();

        //Los jugadores no tienen cartas pero el mazo tiene al menos 1 carta
        Assertions.assertFalse(nuevaPartida2.esFinDeJuego());

        Jugador jugadorActual = nuevaPartida2.getJugadorActual();

        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 3));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 3));
        jugadorActual.tomarCarta(mazo);

        //Al menos un jugador tiene cartas y el mazo tiene al menos 1 carta
        Assertions.assertFalse(nuevaPartida2.esFinDeJuego());
    }



}
