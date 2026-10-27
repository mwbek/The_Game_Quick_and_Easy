package Controladores;

import exceptions.JugadorPasaTurnoException;
import exceptions.JugadoresGanaronException;
import exceptions.JugadoresPerdieronException;
import models.*;

import java.io.IOException;
import java.util.*;

public class Juego_Eliminar {

    //Atributos usados para la interfaz
    private final int anchoMax = 64;

    private final Scanner scanner;

    private List<Jugador> jugadores;
    private Mazo mazo;
    private int movimientosJugador;

    //Con las pilas aplico polimorfismo usando la intefaz 'PilaEnJuego'
    private final PilaEnJuego pilaDescendente;
    private final PilaEnJuego pilaAscendente;

    private static int ronda;
    public Juego_Eliminar(List<Jugador> jugadores)
    {

        if(jugadores == null) throw new IllegalArgumentException("La lista de jugadores no puede ser nula");
        if(jugadores.size() > 5) throw new IllegalArgumentException("No puede haber mas de 5 jugadores en la partida");
        if(jugadores.size() < 2) throw new IllegalArgumentException("No puede haber menos de 2 jugadores en la partida");

        this.jugadores = jugadores;

        scanner = new Scanner(System.in);
        //Inicializo en ronda 1
        ronda = 1;

        //Inicializo mazo
        mazo = new Mazo();

        pilaAscendente = new PilaAscendente();
        pilaDescendente = new PilaDescendente();
    }

    public void comenzarPartida()
    {
        Jugador jugadorActual = null;
        Jugador jugadorSiguiente = null;

        //Le doy cartas a todos los jugadores
        jugadores.forEach(j -> j.tomarCartas(mazo));


        Iterator<Jugador> iteradorJugadores = jugadores.iterator();
        while(true)
        {
            if(iteradorJugadores.hasNext())
            {
                jugadorActual = iteradorJugadores.next();
                if(iteradorJugadores.hasNext()){
                    jugadorSiguiente = iteradorJugadores.next();
                }
                else {
                    //Reinicio el iterador
                    iteradorJugadores = jugadores.iterator();
                    jugadorSiguiente = iteradorJugadores.next();
                }

                try {

                    mostrarHUDPorJugador(jugadorActual);

                    if(mazo.esMazoVacio()) throw new JugadoresGanaronException("Los jugadores ganan la partida al haber terminado el mazo");
                    else pasarTurno(jugadorSiguiente);

                }
                catch (JugadoresPerdieronException | JugadoresGanaronException e){
                    System.out.println(e.getMessage());
                }
            }

        }
    }


    private void mostrarHUDPorJugador(Jugador jugador) {
        CartaJugador carta1 = jugador.getCarta1();
        CartaJugador carta2 = jugador.getCarta2();
        int cartasJugadas = 0;

        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("RONDA " + ronda + " ·  Turno de " + jugador.getNombre());
        System.out.println("════════════════════════════════════════════════════════════════");

        // ▲ Ascendente (12): [7 R]      ▼ Descendente (9): [3 V]
        System.out.println("▲ Ascendente:" + pilaAscendente.verUltimaCartaComoTexto() + "▼ Descendente: " + pilaDescendente.verUltimaCartaComoTexto());

        System.out.println("────────────────────────────────────────────────────────────────");
        System.out.println("Tu mano:  1) " + carta1.toString() + " | " + "2) " + carta2.toString());

        try {
            //No controlo 'JugadoresPerdieronException' ya que me interesa que la controle la clase superior
            //El jugador juega su mano
            cartasJugadas = jugarMano(jugador);

            //Luego roba las cartas que le correspondan
            System.out.println("Robas " + cartasJugadas + " cartas");
            jugador.tomarCartas(mazo);

        }
        catch (JugadorPasaTurnoException e) {
            System.out.println(e.getMessage());
        }

    }

    private void pasarTurno(Jugador siguienteJugador)
    {
        System.out.println("════════════════════════════════════════════════════════════════");

        System.out.println("Turno terminado.\n\n");
        System.out.println("Pasale la compu a " + siguienteJugador.getNombre() + "\n\n");
        System.out.println("Presioná Enter cuando estés listo...\n\n");

        System.out.println("════════════════════════════════════════════════════════════════");

        int pasarTurno = 0;
        {
            while (pasarTurno == 0)
                try {
                    pasarTurno = System.in.read();
                    //Limpio la consola
                    if (pasarTurno == '\n') {
                        limpiarConsola();
                    }
                } catch (IOException e) {
                    System.out.println("Presione ENTER para pasar el turno");
                }
        }


    }

    private int jugarMano(Jugador jugador)
    {
        int intentosFallidos = 0;
        int cartasJugadas = 0;
        boolean jugadaValida = false;
        CartaJugador cartaEnJuego = null;
        PilaEnJuego pilaEnJuego = null;

        Queue<ManoJugada> manosJugadas = new ArrayDeque<>();
        //Dado que un jugador puede jugar 1 o 2 de sus cartas, se le pregunta 2 veces para hacer un movimiento
        while (cartasJugadas < 2)
        {
            try {
                //Si un jugador no puede poner ninguna de sus dos cartas, sale la excepcion de que perdieron
                //No la controlo para que pase al metodo superior
                if(intentosFallidos == 2) throw new JugadoresPerdieronException("Los jugadores perdieron la partida debido a que el ultimo jugador no pudo realizar ningun movimiento");

                cartaEnJuego = elegirCarta(jugador.getCartas());
                pilaEnJuego = elegirPila();

                jugador.jugarCarta(pilaEnJuego, cartaEnJuego);
                //Si la mano no es valida, sumo uno a los intentosFallidos, que indican si el jugador puede o no puede jugar su mano
                if(!jugadaValida) intentosFallidos++;
                else {
                    //Si la mano es valida, sumo uno a las cartas jugadas y guardo la mano para imprimirla despues
                    manosJugadas.add(new ManoJugada(cartaEnJuego, pilaEnJuego));
                    cartasJugadas++;
                }
            }
            catch (JugadorPasaTurnoException e) {
                throw new JugadorPasaTurnoException("%s pasa de turno".formatted(jugador.getNombre()));
            }

        }

        //Salgo del bucle, muestro la jugada
        for(ManoJugada mj : manosJugadas){
            System.out.println(mj.getManoJugada());
        }

        return cartasJugadas;
    }

    private CartaJugador elegirCarta(List<CartaJugador> cartas) {
        int respuesta = 0;
        CartaJugador cartaEnJuego = null;
        System.out.println("¿Qué carta jugas? (1, 2 o 3 (pasar): ");
        while(respuesta == 0){
            try {
                System.out.print("> ");
                respuesta = scanner.nextInt();

                if(respuesta == 1 || respuesta == 2) {
                    cartaEnJuego = cartas.get(respuesta-1);
                    System.out.println("Elegiste la carta [" + respuesta + "]: " + cartaEnJuego.toString());
                }
                //Si el jugador puso al menos una carta, puede pasar. Caso contrario debe elegir una si o si
                else if (respuesta == 3) {

                    if(cartas.size() == 2) throw new IllegalArgumentException("Para pasar de turno se debe jugar al menos una carta");
                    else throw new JugadorPasaTurnoException(); //No la controlo para que pase sola al metodo que la llame

                } else throw new IllegalArgumentException("[ERROR] - El valor ingresado no es correcto");
            }
            catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                respuesta = 0;
            }
            catch (InputMismatchException e) {
                System.out.println("[ERROR] - El valor ingresado debe ser un numero entero");
                respuesta = 0;
            }

        }
        return cartaEnJuego;
    }

    private PilaEnJuego elegirPila() {
        int respuesta = 0;
        PilaEnJuego pilaElegida = null;

        System.out.println("¿En qué pila la jugas?  [1] Ascendente   [2] Descendente");
        while(respuesta == 0){
            System.out.print("> ");
            try {
                respuesta = scanner.nextInt();
                if(respuesta == 1) pilaElegida = pilaAscendente;
                else if(respuesta == 2) pilaElegida = pilaDescendente;
                else throw new IllegalArgumentException("[ERROR] - El valor ingresado no es correcto");
            }
            catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                respuesta = 0;
            }
            catch (InputMismatchException e) {
                System.out.println("[ERROR] - El valor ingresado debe ser un numero entero");
                respuesta = 0;
            }
        }

        return pilaElegida;
    }

    public final static void limpiarConsola()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                String[] comando = {"cls"};
                Runtime.getRuntime().exec(comando);
            }
            else
            {
                String[] comando = {"clear"};
                Runtime.getRuntime().exec(comando);
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

}
