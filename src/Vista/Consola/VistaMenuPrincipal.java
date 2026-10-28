package Vista.Consola;

import Modelos.Jugador;

import java.util.*;

public class VistaMenuPrincipal extends Vista {

    public int mostrarMenuPrincipal()
    {
        int opcion = 0;

        mostrarMensaje("==================================================");
        mostrarMensaje("          THE GAME (Quick and Easy)");
        mostrarMensaje("==================================================");
        mostrarMensaje("\n1) Iniciar nueva partida");
        mostrarMensaje("2) Salir\n");
        mostrarMensaje("==================================================");

        Scanner sc = new Scanner(System.in);
        while(opcion == 0){
            try{
                System.out.print("\nIngrese una opcion > ");
                opcion = sc.nextInt();
            }
            catch (InputMismatchException e) {
                mostrarMensaje("El valor ingresado no corresponde con las opciones disponibles");
                sc.reset(); //Limpio el scanner
            }
        }
        return opcion;
    }

    private int solicitarNumeroDeJugadores()
    {
        int cantidadJugadores = 0;
        Scanner sc = new Scanner(System.in);

        while(cantidadJugadores == 0){
            try{
                System.out.print("Ingrese la cantidad de jugadores > ");
                cantidadJugadores = sc.nextInt();
                if(cantidadJugadores < 2 || cantidadJugadores > 5) throw new IllegalArgumentException("El numero de jugadores debe estar entre 2 y 5");
            }
            catch (InputMismatchException e) {
                mostrarMensaje("El valor ingresado no corresponde a un valor numerico");
                sc.nextLine(); //Limpio el scanner
                cantidadJugadores = 0;
            }
            catch (IllegalArgumentException e){
                mostrarMensaje(e.getMessage());
                sc.nextLine(); //Limpio el scanner
                cantidadJugadores = 0;
            }
        }

        return cantidadJugadores;
    }

    public Queue<Jugador> solicitarJugadores()
    {
        String nombre = "";
        Queue<Jugador> jugadores = new ArrayDeque<>();
        int cantidadJugadores = solicitarNumeroDeJugadores();

        Scanner sc = new Scanner(System.in);
        sc.reset();

        for(int i = 1; i < cantidadJugadores + 1; i++)
        {
            while(nombre.isEmpty()){
                System.out.print("[JUGADOR " + i + "]" + "- Ingrese su nombre> ");
                nombre = sc.next();
            }

            jugadores.add(new Jugador(nombre));
            nombre = "";
        }
        return jugadores;
    }
}
