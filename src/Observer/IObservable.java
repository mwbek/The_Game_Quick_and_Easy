package Observer;

import Enumerados.Eventos;

public interface IObservable {
    void agregarObservador(IObservador observador);
    void removerObservador(IObservador observador);
    void notificarObservadores(Eventos e);
}
