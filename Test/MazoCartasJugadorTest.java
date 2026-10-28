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
        Mazo pilaMazo = new Mazo();
        Assertions.assertEquals(0, pilaMazo.getMazo().length);
        pilaMazo.mezclarMazo();
        Carta[] mazo = pilaMazo.getMazo();

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
        Mazo pilaMazo = new Mazo();
        pilaMazo.mezclarMazo();
        Assertions.assertNotNull(pilaMazo.tomarCarta());
    }

    @Test
    public void EvitarllenarDosVecesElMazoTest()
    {
        Mazo pilaMazo = new Mazo();
        Assertions.assertEquals(0, pilaMazo.getMazo().length);
        pilaMazo.mezclarMazo();

        Assertions.assertEquals(50, pilaMazo.getMazo().length);

        pilaMazo.tomarCarta();
        pilaMazo.tomarCarta();
        Assertions.assertEquals(48, pilaMazo.getMazo().length);

        pilaMazo.mezclarMazo();

        Assertions.assertEquals(48, pilaMazo.getMazo().length);



    }
}
