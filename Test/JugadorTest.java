import Enumerados.CartaDeIndicacion;
import Enumerados.ColorCarta;
import Interfaces.IMazo;
import Modelos.*;
import exceptions.CartaNoExisteException;
import exceptions.MovimientoInvalidoException;
import org.junit.jupiter.api.*;

public class JugadorTest {

    private Jugador jugador;
    private MazoMock mazo;
    private PilaDeIndicacion pilaAscendente;

    @BeforeEach
    public void setup(){
        jugador = new Jugador("Martin");
        mazo = new MazoMock();
        pilaAscendente = new PilaDeIndicacion(CartaDeIndicacion.ASCENDENTE);
    }

    @Test
    public void crearJugadorNombreCorrectoTest()
    {
        jugador = new Jugador("Martin");
        Assertions.assertNotNull(jugador);
        Assertions.assertEquals("Martin", jugador.getNombre());
    }

    @Test
    public void crearJugadorNombreIncorrectoTest(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Jugador(" "));
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Jugador(null));
    }

    @Test
    public void cantidadCartasEnManoTest(){

        jugador = new Jugador("Martin");
        Assertions.assertEquals(0, jugador.getCantidadCartasEnMano());

        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 2));
        jugador.tomarCarta(mazo);
        Assertions.assertEquals(1, jugador.getCantidadCartasEnMano());

        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 4));
        jugador.tomarCarta(mazo);
        Assertions.assertEquals(2, jugador.getCantidadCartasEnMano());
    }

    @Test
    public void tomarCartaTest()
    {
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 2));
        Assertions.assertEquals(1, mazo.getMazo().length);

        jugador.tomarCarta(mazo);
        Assertions.assertEquals(0, mazo.getMazo().length);
        Assertions.assertEquals(new Carta(ColorCarta.ROJO,2), jugador.getCartas()[0]);
    }

    @Test
    public void jugarCartaValidaYRevisarQueSeElimineDeLaManoTest(){

        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 5));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 7));

        jugador.tomarCarta(mazo);
        jugador.tomarCarta(mazo);

        Assertions.assertEquals(2, jugador.getCantidadCartasEnMano());

        //Jugada valida
        Assertions.assertDoesNotThrow(() -> jugador.jugarCarta(new PilaDeIndicacion(CartaDeIndicacion.ASCENDENTE), new Carta(ColorCarta.ROJO, 5)));

        //Reviso que se haya eliminado de la mano
        Assertions.assertEquals(1, jugador.getCantidadCartasEnMano());
        Assertions.assertEquals(new Carta(ColorCarta.VERDE, 7), jugador.getCartas()[0]);

        Assertions.assertDoesNotThrow(() -> jugador.jugarCarta(new PilaDeIndicacion(CartaDeIndicacion.DESCENDENTE), new Carta(ColorCarta.VERDE, 7)));

        //Reviso que se haya eliminado de la mano
        Assertions.assertEquals(0, jugador.getCantidadCartasEnMano());
        Assertions.assertEquals(0,jugador.getCartas().length);

    }

    @Test
    public void jugarCartaInvalidaTest(){
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 5));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 7));

        jugador.tomarCarta(mazo);
        jugador.tomarCarta(mazo);

        //Jugada valida
        Assertions.assertDoesNotThrow(() -> jugador.jugarCarta(pilaAscendente,new Carta(ColorCarta.VERDE, 7)));
        //Jugada no valida
        Assertions.assertThrows(MovimientoInvalidoException.class,() -> jugador.jugarCarta(pilaAscendente, new Carta(ColorCarta.ROJO, 5)));
    }

    @Test
    public void jugarCartaInvalidaYCartaQuedaEnLaManoTest(){
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 5));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 7));

        jugador.tomarCarta(mazo);
        jugador.tomarCarta(mazo);

        Assertions.assertEquals(2, jugador.getCantidadCartasEnMano());

        //Jugada valida
        Assertions.assertDoesNotThrow(() -> jugador.jugarCarta(pilaAscendente,new Carta(ColorCarta.VERDE, 7)));
        Assertions.assertEquals(1, jugador.getCantidadCartasEnMano());
        Assertions.assertEquals(new Carta(ColorCarta.ROJO, 5), jugador.getCartas()[0]);

        //Jugada no valida
        Assertions.assertThrows(MovimientoInvalidoException.class,() -> jugador.jugarCarta(pilaAscendente, new Carta(ColorCarta.ROJO, 5)));
        Assertions.assertEquals(1, jugador.getCantidadCartasEnMano());
        Assertions.assertEquals(new Carta(ColorCarta.ROJO, 5), jugador.getCartas()[0]);


    }

    @Test
    public void jugarCartaValidaYRevisarQueEsteEnLaPilaTest(){

        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 5));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 7));

        jugador.tomarCarta(mazo);
        jugador.tomarCarta(mazo);

        //Jugada valida
        Assertions.assertDoesNotThrow(() -> jugador.jugarCarta(pilaAscendente, new Carta(ColorCarta.ROJO, 5)));

        Assertions.assertEquals(new Carta(ColorCarta.ROJO, 5), pilaAscendente.getUltimaCarta());

    }

    @Test
    public void manoLlenaTest(){
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 5));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 7));

        jugador.tomarCarta(mazo);
        jugador.tomarCarta(mazo);

        Assertions.assertEquals(2, jugador.getCantidadCartasEnMano());
    }

    @Test
    public void tomarCartasConMazoVacioNoCambiaLaManoNiLanzaExcepcion(){
        //Mazo Vacio
        jugador.tomarCarta(mazo);
        jugador.tomarCarta(mazo);

        Assertions.assertEquals(0, jugador.getCantidadCartasEnMano());
    }



    @Test
    public void cartaQueNoEstaEnLaManoTest(){
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 5));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 7));

        jugador.tomarCarta(mazo);
        jugador.tomarCarta(mazo);

        //Jugada no valida
        Assertions.assertThrows(CartaNoExisteException.class,() -> jugador.jugarCarta(pilaAscendente, new Carta(ColorCarta.AMARILLO, 5)));
    }




}
