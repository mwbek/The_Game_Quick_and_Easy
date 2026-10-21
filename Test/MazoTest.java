import models.CartaJugador;
import models.ColorCartaJugador;
import models.Mazo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

public class MazoTest {

    @Test
    @DisplayName("Testeo la creacoin del mazo y todas sus cartas")
    public void crearMazoCompleto_Test()
    {
        Mazo pilaMazo = new Mazo();
        Stack<CartaJugador> mazo = pilaMazo.getMazo();

        Assertions.assertEquals(50, mazo.size());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.ROJO)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.AMARILLO)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.AZUL)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.GRIS)).count());
        Assertions.assertEquals(10, mazo.stream().filter(carta -> carta.getColor().equals(ColorCartaJugador.VERDE)).count());


    }
}
