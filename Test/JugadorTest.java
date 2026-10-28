import Enumerados.CartaDeIndicacion;
import Modelos.*;
import org.junit.jupiter.api.*;

public class JugadorTest {

    private Jugador jugador;
    Mazo mazo;
    @Test
    @DisplayName("Testeo la cracion de un jugador")
    @BeforeEach
    public void crearJugadorTest()
    {
        jugador = new Jugador("Martin");
        mazo = new Mazo();
        mazo.mezclarMazo();

        Assertions.assertNotNull(jugador);
        Assertions.assertEquals("Martin", jugador.getNombre());
    }

    @Test
    public void tomarCartaTest()
    {
        jugador.tomarCarta(mazo);
        Assertions.assertEquals(49,mazo.getMazo().length);
    }

    @Test
    @DisplayName("Intento pasar de ronda sin jugar, juego una carta y paso de ronda")
    public void jugarCartaYPasarRondaTest()
    {
        //Tomo dos cartas
        jugador.tomarCarta(mazo);
        jugador.tomarCarta(mazo);


        Assertions.assertFalse(jugador.puedePasarRonda());
        Assertions.assertTrue(jugador.jugarCarta(new PilaDeIndicacion(CartaDeIndicacion.DESCENDENTE), jugador.getCartas()[0]));
        Assertions.assertTrue(jugador.puedePasarRonda());


    }

}
