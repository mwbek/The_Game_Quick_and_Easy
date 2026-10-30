import Interfaces.IMazo;
import Modelos.*;
import org.junit.jupiter.api.*;

public class JugadorTest {

    private Jugador jugador;
    IMazo iMazo;
    @Test
    @DisplayName("Testeo la cracion de un jugador")
    @BeforeEach
    public void crearJugadorTest()
    {
        jugador = new Jugador("Martin");
        iMazo = new Mazo();
        iMazo.mezclarMazo();

        Assertions.assertNotNull(jugador);
        Assertions.assertEquals("Martin", jugador.getNombre());
    }

    @Test
    public void tomarCartaTest()
    {
        jugador.tomarCarta(iMazo);
        Assertions.assertEquals(49, iMazo.getMazo().length);
    }


}
