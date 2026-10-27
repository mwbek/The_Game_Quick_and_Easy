import models.CartaJugador;
import models.ColorCartaJugador;
import models.Mazo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Stack;

public class MazoCartasJugadorTest {


    @Test
    @DisplayName("Testeo la creacion del mazo y todas sus cartas")
    public void crearMazoCompletoTest()
    {
        Mazo pilaMazo = new Mazo();
        Assertions.assertEquals(0, pilaMazo.getMazo().size());
        pilaMazo.llenarMazo();
        List<CartaJugador> mazo = pilaMazo.getMazo();

        Assertions.assertEquals(50, mazo.size());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.ROJO)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.AMARILLO)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.AZUL)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.GRIS)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.VERDE)).count());

    }

    @Test
    public void tomarCartaMazoTest()
    {
        Mazo pilaMazo = new Mazo();
        pilaMazo.llenarMazo();
        Assertions.assertNotNull(pilaMazo.tomarCarta());
    }

    @Test
    public void EvitarllenarDosVecesElMazoTest()
    {
        Mazo pilaMazo = new Mazo();
        Assertions.assertEquals(0, pilaMazo.getMazo().size());
        pilaMazo.llenarMazo();

        Assertions.assertEquals(50, pilaMazo.getMazo().size());

        pilaMazo.tomarCarta();
        pilaMazo.tomarCarta();
        Assertions.assertEquals(48, pilaMazo.getMazo().size());

        pilaMazo.llenarMazo();

        Assertions.assertEquals(48, pilaMazo.getMazo().size());



    }
}
