package Observer;
import Enumerados.Eventos;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable implements IObservable {

    private final List<IObservador> observadores = new ArrayList<>();

    @Override
    public void agregarObservador(IObservador observador) {
        observadores.add(observador);
    }

    @Override
    public void removerObservador(IObservador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores(Eventos e) {
        for(IObservador o: observadores){
            o.actualizar(this, e);
        }
    }


}
