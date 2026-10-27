package Vista.Consola;

import models.CartaJugador;
import models.Jugador;
import models.Partida;
import models.PilaEnJuego;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VistaManoJugador extends Vista {

    public void mostrarHUDJugador(Partida partida) {
        Jugador jugadorActual = partida.getJugadorturnoActual();
        int ronda = partida.getRondaActual();
        List<CartaJugador> cartasEnMano = jugadorActual.getCartas();
        PilaEnJuego pilaAscendente = partida.getPilaAscendente();
        PilaEnJuego pilaDescendente = partida.getPilaDescendente();


        mostrarMensaje("================================================================");
        mostrarMensaje("RONDA " + ronda + " ·  Turno de " + jugadorActual.getNombre());
        mostrarMensaje("================================================================");

        // ▲ Ascendente (12): [7 R]      ▼ Descendente (9): [3 V]
        mostrarMensaje("▲ Ascendente:" + pilaAscendente.verUltimaCartaComoTexto()  + " ▼ Descendente: " + pilaDescendente.verUltimaCartaComoTexto());
        mostrarMensaje("----------------------------------------------------------------");
        mostrarMensaje("Tu mano:  1) " + cartasEnMano.getFirst().toString() + " | " + "2) " + cartasEnMano.getLast().toString());

    }

    public PilaEnJuego solicitarPila

    public int solicitarCartaAJugar() {

        int respuesta = 0;
        Scanner sc = new Scanner(System.in);

        mostrarMensaje("¿Qué carta jugas? (1, 2 o 3 (pasar): ");
        while (respuesta == 0) {
            try {
                System.out.print("> ");
                respuesta = sc.nextInt();

                if (respuesta > 0 && respuesta < 4) {
                    return respuesta;
                } else {
                    mostrarMensaje("La opcion seleccionada no es valida. Por favor ingrese alguna de las opciones mostradas");
                }
            } catch (InputMismatchException e) {
                sc.nextLine(); //Limpio el scanner
                respuesta = 0;
                mostrarMensaje("El valor ingresado no corresponde a un numero");
            }
        }

        return respuesta;
    }


}
