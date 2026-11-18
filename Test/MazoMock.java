import Interfaces.IMazo;
import Modelos.Carta;
import exceptions.CartaDuplicadaException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.Stack;

public class MazoMock implements IMazo {

    private Deque<Carta> mazo;

    public MazoMock(){
        mazo = new ArrayDeque<>();
    }

    @Override
    public Carta[] getMazo() {
        return mazo.toArray(Carta[]::new);
    }

    @Override
    public boolean esVacio() {
        return mazo.isEmpty();
    }

    @Override
    public Carta tomarCarta() {
        return mazo.pop();
    }

    @Override
    public void mezclarMazo() {}

    @Override
    public int getCantidad() {
        return mazo.size();
    }

    //TODO: Se uso para los Test. Revisar o eliminar
    public void agregarCarta(Carta carta)
    {
        if(carta == null) return;
        if(mazo.contains(carta)) throw new CartaDuplicadaException("La carta ya existe en el mazo");
        mazo.push(carta);

    }
}
