package Vista;

import Controladores.ControladorConsola;
import Interfaces.IVista;
import Modelos.Carta;
import Modelos.Jugador;

import java.util.InputMismatchException;
import java.util.Scanner;

public class VistaConsola implements IVista {

    public final ControladorConsola controlador;

    private final Scanner sc;

    public VistaConsola(ControladorConsola controlador) {

        //Programacion defensiva
        if(controlador == null) throw new IllegalArgumentException("El controlador no puede ser nulo");

        this.controlador = controlador;
        this.controlador.setVista(this);

        sc = new Scanner(System.in);
    }


    /// @return retorna un 1 cuando el usuario quiere iniciar una nueva partida o un 0 cuando quiere salir
    public int mostrarMenuPrincipal()
    {
        int opcion = 0;

        System.out.println("==================================================");
        System.out.println("          THE GAME (Quick and Easy)");
        System.out.println("==================================================");
        System.out.println("\n1) Iniciar nueva partida");
        System.out.println("2) Salir\n");
        System.out.println("==================================================");

        while(opcion == 0){
            try{
                System.out.print("\nIngrese una opcion > ");
                opcion = sc.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("El valor ingresado no corresponde con las opciones disponibles");
                sc.reset(); //Limpio el scanner
            }
        }
        return opcion;
    }


    public void mostrarHUDJugador(Jugador jugador) {
        //int ronda = partida.getRondaActual();
        int ronda = 1;
        Carta[] cartasEnMano = jugador.getCartas();
//        PilaDeIndicacion pilaAscendente = partida.getPilaAscendente();
//        PilaDeIndicacion pilaDescendente = partida.getPilaDescendente();


        System.out.println("================================================================");
        System.out.println("RONDA " + ronda + " ·  Turno de " + jugador.getNombre());
        System.out.println("================================================================");

        // ▲ Ascendente (12): [7 R]      ▼ Descendente (9): [3 V]
        System.out.println("▲ Ascendente:" + "[]" + " ▼ Descendente: " + "[]");
        System.out.println("----------------------------------------------------------------");
        System.out.println("Tu mano:  1) " + cartasEnMano[0] + " | " + "2) " + cartasEnMano[1]);

    }






    //=======================
    //SOLICITUD DE DATOS
    //=======================

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
                System.out.println("El valor ingresado no corresponde a un valor numerico");
                sc.nextLine(); //Limpio el scanner
                cantidadJugadores = 0;
            }
            catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
                sc.nextLine(); //Limpio el scanner
                cantidadJugadores = 0;
            }
        }

        return cantidadJugadores;
    }


    private void solicitarJugadores()
    {

        int cantidadJugadores = solicitarNumeroDeJugadores();

        for(int i = 1; i < cantidadJugadores + 1; i++)
        {
            String nombre = "";
            while(nombre.isEmpty()){
                System.out.print("[JUGADOR " + i + "]" + "- Ingrese su nombre> ");
                nombre = sc.next();
            }

            //controlador.solicitarJugador(nombre);
            nombre = "";
        }
    }

    private int solicitarCartaAJugar() {

        int respuesta = 0;
        Scanner sc = new Scanner(System.in);

        System.out.println("¿Qué carta jugas? (1, 2 o 3 (pasar): ");
        while (respuesta == 0) {
            try {
                System.out.print("> ");
                respuesta = sc.nextInt();

                if (respuesta > 0 && respuesta < 4) {
                    return respuesta;
                } else {
                    System.out.println("La opcion seleccionada no es valida. Por favor ingrese alguna de las opciones mostradas");
                }
            } catch (InputMismatchException e) {
                sc.nextLine(); //Limpio el scanner
                respuesta = 0;
                System.out.println("El valor ingresado no corresponde a un numero");
            }
        }

        return respuesta;
    }

}
