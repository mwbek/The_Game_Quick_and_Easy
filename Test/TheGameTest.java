import Enumerados.CartaDeIndicacion;
import Enumerados.ColorCarta;
import Enumerados.Eventos;
import Interfaces.IJugador;
import Modelos.*;
import exceptions.MovimientoInvalidoException;
import exceptions.PartidaIniciadaException;
import org.junit.jupiter.api.*;

import java.rmi.RemoteException;

public class TheGameTest {

    TheGame juego;
    IJugador martin, ludmila;
    MazoMock mazo;

    @BeforeEach
    public void setup() throws RemoteException {
        juego = new TheGame();
        mazo = new MazoMock();
        martin = juego.agregarJugador("Martin");
        ludmila = juego.agregarJugador("Ludmila");
    }

    private void iniciarMazo() throws RemoteException {
        //Restante en el mazo
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 1));
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 2));

        //Cartas de ludmila
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 3));
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 4));


        //Cartas de martin
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 5));
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 6));

        martin.tomarCarta(mazo);
        martin.tomarCarta(mazo);

        ludmila.tomarCarta(mazo);
        ludmila.tomarCarta(mazo);
        juego.inicializarPartida(mazo);

    }

    @Test
    void iniciarPartidaRepartirYDejaElEstadoInicialTest() throws RemoteException {

        iniciarMazo();

        //Compruebo estado de la partida
        Assertions.assertEquals(Eventos.JUGANDO, juego.getEstadoPartida());
        //Compruebo que todos tengan la cantidad de cartas correspondientes
        Assertions.assertEquals(2, martin.getCantidadCartasEnMano());
        Assertions.assertEquals(2, ludmila.getCantidadCartasEnMano());
        Assertions.assertEquals(2, juego.getCantidadCartasMazo());

        //Compruebo ronda actual
        Assertions.assertEquals(1, juego.getRondaActual());
        //Compruebo jugador actual
        Assertions.assertSame(martin, juego.getJugadorActual());
    }

    @Test
    void jugarFueraDeTurnoNoCambiaNadaTest() throws RemoteException {

        iniciarMazo();

        Assertions.assertThrows(MovimientoInvalidoException.class, () -> juego.jugarCarta(ludmila, CartaDeIndicacion.ASCENDENTE, 0));

        Assertions.assertEquals(2, ludmila.getCantidadCartasEnMano());
        Assertions.assertNull(juego.getUltimaCartaAscendente());
    }

    @Test
    void pasarSinJugarNoCambiaElTurnoTest() throws RemoteException {
        iniciarMazo();

        Assertions.assertThrows(MovimientoInvalidoException.class, () -> juego.pasarDeRonda(martin));

        Assertions.assertSame(martin, juego.getJugadorActual());
        Assertions.assertEquals(1, juego.getRondaActual());
    }


    @Test
    void jugarAntesDeIniciarLanzaExcepcion() {
        Assertions.assertThrows(PartidaIniciadaException.class,
                () -> juego.jugarCarta(martin, CartaDeIndicacion.ASCENDENTE, 0));
    }

    @Test
    void turnoCompletoReponeCartasYPasaElTurno() throws RemoteException {
        iniciarMazo();

        juego.jugarCarta(martin, CartaDeIndicacion.ASCENDENTE, 0);   // juega el 6
        Assertions.assertEquals(1, martin.getCantidadCartasEnMano());
        Assertions.assertEquals(new Carta(ColorCarta.ROJO, 6), juego.getUltimaCartaAscendente());

        juego.pasarDeRonda(martin);                                  // roba el 2
        Assertions.assertEquals(new Carta(ColorCarta.ROJO, 2), martin.getCartas()[1]);

        Assertions.assertEquals(2, martin.getCantidadCartasEnMano());
        Assertions.assertEquals(1, juego.getCantidadCartasMazo());
        Assertions.assertSame(ludmila, juego.getJugadorActual());
        Assertions.assertEquals(2, juego.getRondaActual());
        Assertions.assertEquals(Eventos.JUGANDO, juego.getEstadoPartida());
    }

    @Test
    void partidaGanada() throws RemoteException {

        //Cartas de ludmila
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 6));
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 5));

        //Cartas de martin
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 8));
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 3));

        martin.tomarCarta(mazo);
        martin.tomarCarta(mazo);

        ludmila.tomarCarta(mazo);
        ludmila.tomarCarta(mazo);

        juego.inicializarPartida(mazo);

        juego.jugarCarta(martin, CartaDeIndicacion.ASCENDENTE, 0);    // 3
        juego.jugarCarta(martin, CartaDeIndicacion.ASCENDENTE, 0);    // 5
        juego.pasarDeRonda(martin);

        // Ana quedó sin cartas pero Beto todavía tiene: la partida sigue
        Assertions.assertEquals(Eventos.JUGANDO, juego.getEstadoPartida());
        Assertions.assertSame(ludmila, juego.getJugadorActual());

        juego.jugarCarta(ludmila, CartaDeIndicacion.DESCENDENTE, 0);  // 8
        juego.jugarCarta(ludmila, CartaDeIndicacion.DESCENDENTE, 0);  // 6
        juego.pasarDeRonda(ludmila);

        Assertions.assertEquals(Eventos.GANARON, juego.getEstadoPartida());
    }

    @Test
    void partidaPerdidaCuandoElSiguienteNoTieneJugadas() throws RemoteException {
        //Cartas de mazo
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 4));
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 7));

        //Cartas de ludmila
        mazo.agregarCarta(new Carta(ColorCarta.AZUL, 6));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 5));

        //Cartas de martin
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 2));
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 9));

        martin.tomarCarta(mazo);
        martin.tomarCarta(mazo);

        ludmila.tomarCarta(mazo);
        ludmila.tomarCarta(mazo);

        juego.inicializarPartida(mazo);


        juego.jugarCarta(martin, CartaDeIndicacion.ASCENDENTE, 0);    // 9
        juego.jugarCarta(martin, CartaDeIndicacion.DESCENDENTE, 0);   // 2
        juego.pasarDeRonda(martin);

        Assertions.assertEquals(Eventos.PERDIERON, juego.getEstadoPartida());
    }

    @Test
    void terminadaLaPartidaSeRechazaCualquierAccion() throws RemoteException {

        //Cartas de mazo
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 4));
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 7));

        //Cartas de ludmila
        mazo.agregarCarta(new Carta(ColorCarta.AZUL, 6));
        mazo.agregarCarta(new Carta(ColorCarta.VERDE, 5));

        //Cartas de martin
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 2));
        mazo.agregarCarta(new Carta(ColorCarta.ROJO, 9));

        martin.tomarCarta(mazo);
        martin.tomarCarta(mazo);

        ludmila.tomarCarta(mazo);
        ludmila.tomarCarta(mazo);

        juego.inicializarPartida(mazo);


        juego.jugarCarta(martin, CartaDeIndicacion.ASCENDENTE, 0);
        juego.jugarCarta(martin, CartaDeIndicacion.DESCENDENTE, 0);
        juego.pasarDeRonda(martin);                                   // PERDIERON

        Assertions.assertThrows(MovimientoInvalidoException.class,
                () -> juego.jugarCarta(ludmila, CartaDeIndicacion.ASCENDENTE, 0));
        Assertions.assertThrows(MovimientoInvalidoException.class,
                () -> juego.pasarDeRonda(ludmila));
    }
}
