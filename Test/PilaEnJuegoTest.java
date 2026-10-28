import Enumerados.CartaDeIndicacion;
import Enumerados.ColorCarta;
import Interfaces.PilaEnJuego;
import exceptions.JugadaInvalidaException;
import Modelos.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PilaEnJuegoTest {

    @Test
    @DisplayName("Testeo la creacion de una pila ascendente")
    public void crearPilaAscendenteYDescedenteTest()
    {

        PilaDeIndicacion pilaAscendente = new PilaDeIndicacion(CartaDeIndicacion.ASCENDENTE);
        PilaDeIndicacion pilaDescendente = new PilaDeIndicacion(CartaDeIndicacion.DESCENDENTE);

        Assertions.assertNotEquals(null, pilaAscendente);
        Assertions.assertNotEquals(null, pilaDescendente);

        Assertions.assertEquals(CartaDeIndicacion.ASCENDENTE, pilaAscendente.getTipo());
        Assertions.assertEquals(CartaDeIndicacion.DESCENDENTE, pilaDescendente.getTipo());

    }


    @Test
    public void agregarCartasValidasPilaAscendenteTest()
    {
        PilaDeIndicacion pilaAscendente = new PilaDeIndicacion(CartaDeIndicacion.ASCENDENTE);

        //Pongo primera carta (Puede ser cualquiera)
        pilaAscendente.agregarCarta(new Carta(ColorCarta.AZUL, 4));
        Assertions.assertEquals(new Carta(ColorCarta.AZUL, 4), pilaAscendente.getUltimaCarta());

        //Pongo segunda carta, mayor a 4. Deberia agregarse sin problema
        pilaAscendente.agregarCarta(new Carta(ColorCarta.ROJO, 6));
        Assertions.assertEquals(new Carta(ColorCarta.ROJO, 6), pilaAscendente.getUltimaCarta());


        //Pongo una carta menor pero del mismo color. Deberia agregarse sin problema
        pilaAscendente.agregarCarta(new Carta(ColorCarta.ROJO, 2));
        Assertions.assertEquals(new Carta(ColorCarta.ROJO, 2), pilaAscendente.getUltimaCarta());

    }

    @Test
    public void agregarCartasValidasPilaDescendenteTest()
    {
        PilaDeIndicacion pilaDescendente = new PilaDeIndicacion(CartaDeIndicacion.DESCENDENTE);

        //Pongo primera carta (Puede ser cualquiera)
        pilaDescendente.agregarCarta(new Carta(ColorCarta.AZUL, 6));
        Assertions.assertEquals(new Carta(ColorCarta.AZUL, 6), pilaDescendente.getUltimaCarta());


        //Pongo segunda carta, menor a 6. Deberia agregarse sin problema
        pilaDescendente.agregarCarta(new Carta(ColorCarta.ROJO, 4));
        Assertions.assertEquals(new Carta(ColorCarta.ROJO, 4), pilaDescendente.getUltimaCarta());


        //Pongo una carta mayor pero del mismo color. Deberia agregarse sin problema
        pilaDescendente.agregarCarta(new Carta(ColorCarta.ROJO, 7));
        Assertions.assertEquals(new Carta(ColorCarta.ROJO, 7), pilaDescendente.getUltimaCarta());

    }

    @Test
    public void agregarCartasInvalidasPilaAscendenteTest()
    {
        PilaDeIndicacion pilaAscendente = new PilaDeIndicacion(CartaDeIndicacion.ASCENDENTE);

        //Pongo primera carta (Puede ser cualquiera)
        Assertions.assertTrue(pilaAscendente.agregarCarta(new Carta(ColorCarta.AZUL, 6)));
        Assertions.assertEquals(new Carta(ColorCarta.AZUL, 6), pilaAscendente.getUltimaCarta());

        //Pongo una tercer carta, menor a 6. NO deberia agregarse lanzando la excepcion 'JugadaInvalidaException'
        Assertions.assertFalse(pilaAscendente.agregarCarta(new Carta(ColorCarta.VERDE, 2)));
        Assertions.assertEquals(new Carta(ColorCarta.AZUL, 6), pilaAscendente.getUltimaCarta());

        //Pongo una carta menor pero del mismo color. Deberia agregarse sin problema
        Assertions.assertTrue(pilaAscendente.agregarCarta(new Carta(ColorCarta.AZUL, 2)));
        Assertions.assertEquals(new Carta(ColorCarta.AZUL, 2), pilaAscendente.getUltimaCarta());

    }

    @Test
    public void agregarCartasInvalidasPilaDescendenteTest()
    {
        PilaDeIndicacion pilaDescendente = new PilaDeIndicacion(CartaDeIndicacion.DESCENDENTE);

        //Pongo primera carta (Puede ser cualquiera)
        Assertions.assertTrue(pilaDescendente.agregarCarta(new Carta(ColorCarta.AZUL, 6)));
        Assertions.assertEquals(new Carta(ColorCarta.AZUL, 6), pilaDescendente.getUltimaCarta());


        //Pongo segunda carta, mayor a 6. NO Deberia agregarse sin problema
        Assertions.assertFalse(pilaDescendente.agregarCarta(new Carta(ColorCarta.ROJO, 7)));
        Assertions.assertEquals(new Carta(ColorCarta.AZUL, 6), pilaDescendente.getUltimaCarta());

        //Pongo una carta mayor pero del mismo color. Deberia agregarse sin problema
        Assertions.assertTrue(pilaDescendente.agregarCarta(new Carta(ColorCarta.AZUL, 8)));
        Assertions.assertEquals(new Carta(ColorCarta.AZUL, 8), pilaDescendente.getUltimaCarta());

    }


}
