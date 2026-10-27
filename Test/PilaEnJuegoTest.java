import exceptions.JugadaInvalidaException;
import models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class PilaEnJuegoTest {

    @Test
    @DisplayName("Testeo la creacion de una pila ascendente")
    public void crearPilaAscendenteYDescedenteTest()
    {
        PilaEnJuego pilaAscendente = new PilaAscendente();
        PilaEnJuego pilaDescendente = new PilaDescendente();

        Assertions.assertNotEquals(null, pilaAscendente);
        Assertions.assertNotEquals(null, pilaDescendente);

        Assertions.assertEquals("ascendente", pilaAscendente.tipoPila());
        Assertions.assertEquals("descendente", pilaDescendente.tipoPila());

    }


    @Test
    public void agregarCartasPilaAscendenteTest()
    {
        PilaEnJuego pilaAscendente = new PilaAscendente();

        //Pongo primera carta (Puede ser cualquiera)
        pilaAscendente.agregarCarta(new CartaJugador(ColorCartaJugador.AZUL, 4));
        Assertions.assertEquals(new CartaJugador(ColorCartaJugador.AZUL, 4), pilaAscendente.getUltimaCarta());


        //Pongo segunda carta, mayor a 4. Deberia agregarse sin problema
        pilaAscendente.agregarCarta(new CartaJugador(ColorCartaJugador.ROJO, 6));
        Assertions.assertEquals(new CartaJugador(ColorCartaJugador.ROJO, 6), pilaAscendente.getUltimaCarta());

        //Pongo una tercer carta, menor a 6. NO deberia agregarse lanzando la excepcion 'JugadaInvalidaException'
        Assertions.assertThrows(JugadaInvalidaException.class, ()->pilaAscendente.agregarCarta(new CartaJugador(ColorCartaJugador.VERDE, 2)));


        //Pongo una carta menor pero del mismo color. Deberia agregarse sin problema
        pilaAscendente.agregarCarta(new CartaJugador(ColorCartaJugador.ROJO, 2));
        Assertions.assertEquals(new CartaJugador(ColorCartaJugador.ROJO, 2), pilaAscendente.getUltimaCarta());

    }

    @Test
    public void agregarCartasPilaDescendenteTest()
    {
        PilaEnJuego pilaDescendente = new PilaDescendente();

        //Pongo primera carta (Puede ser cualquiera)
        pilaDescendente.agregarCarta(new CartaJugador(ColorCartaJugador.AZUL, 6));
        Assertions.assertEquals(new CartaJugador(ColorCartaJugador.AZUL, 6), pilaDescendente.getUltimaCarta());


        //Pongo segunda carta, menor a 6. Deberia agregarse sin problema
        pilaDescendente.agregarCarta(new CartaJugador(ColorCartaJugador.ROJO, 4));
        Assertions.assertEquals(new CartaJugador(ColorCartaJugador.ROJO, 4), pilaDescendente.getUltimaCarta());

        //Pongo una tercer carta, mayor a 4. NO deberia agregarse lanzando la excepcion 'JugadaInvalidaException'
        Assertions.assertThrows(JugadaInvalidaException.class, ()->pilaDescendente.agregarCarta(new CartaJugador(ColorCartaJugador.VERDE, 9)));


        //Pongo una carta mayor pero del mismo color. Deberia agregarse sin problema
        pilaDescendente.agregarCarta(new CartaJugador(ColorCartaJugador.ROJO, 7));
        Assertions.assertEquals(new CartaJugador(ColorCartaJugador.ROJO, 7), pilaDescendente.getUltimaCarta());

    }
}
