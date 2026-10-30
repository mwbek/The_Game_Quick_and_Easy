import Interfaces.IMazo;
import Modelos.Carta;
import Enumerados.ColorCarta;
import Modelos.Mazo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class MazoCartasJugadorTest {


    @Test
    @DisplayName("Testeo la creacion del mazo y todas sus cartas")
    public void crearMazoCompletoTest()
    {
        IMazo pilaIMazo = new Mazo();
        Assertions.assertEquals(0, pilaIMazo.getMazo().length);
        pilaIMazo.mezclarMazo();
        Carta[] mazo = pilaIMazo.getMazo();

        Assertions.assertEquals(50, mazo.length);
        Assertions.assertEquals(10, Arrays.stream(mazo).filter(carta -> carta.getColor().equals(ColorCarta.ROJO)).count());
        Assertions.assertEquals(10, Arrays.stream(mazo).filter(carta -> carta.getColor().equals(ColorCarta.AMARILLO)).count());
        Assertions.assertEquals(10, Arrays.stream(mazo).filter(carta -> carta.getColor().equals(ColorCarta.AZUL)).count());
        Assertions.assertEquals(10, Arrays.stream(mazo).filter(carta -> carta.getColor().equals(ColorCarta.GRIS)).count());
        Assertions.assertEquals(10, Arrays.stream(mazo).filter(carta -> carta.getColor().equals(ColorCarta.VERDE)).count());

    }

    @Test
    public void tomarCartaMazoTest()
    {
        IMazo pilaIMazo = new Mazo();
        pilaIMazo.mezclarMazo();
        Assertions.assertNotNull(pilaIMazo.tomarCarta());
    }

    @Test
    public void EvitarllenarDosVecesElMazoTest()
    {
        IMazo pilaIMazo = new Mazo();
        Assertions.assertEquals(0, pilaIMazo.getMazo().length);
        pilaIMazo.mezclarMazo();

        Assertions.assertEquals(50, pilaIMazo.getMazo().length);

        pilaIMazo.tomarCarta();
        pilaIMazo.tomarCarta();
        Assertions.assertEquals(48, pilaIMazo.getMazo().length);

        pilaIMazo.mezclarMazo();

        Assertions.assertEquals(48, pilaIMazo.getMazo().length);



    }
}
