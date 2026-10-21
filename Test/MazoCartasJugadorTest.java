import models.CartaJugador;
import models.ColorCartaJugador;
import models.MazoCartasJugador;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MazoCartasJugadorTest {

    @Test
    @DisplayName("Testeo la creacoin del mazo y todas sus cartas")
    public void crearMazoCompleto_Test()
    {
        MazoCartasJugador pilaMazo = new MazoCartasJugador();
        List<CartaJugador> mazo = pilaMazo.getMazo();

        Assertions.assertEquals(50, mazo.size());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.ROJO)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.AMARILLO)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.AZUL)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.GRIS)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.VERDE)).count());


    }
}
