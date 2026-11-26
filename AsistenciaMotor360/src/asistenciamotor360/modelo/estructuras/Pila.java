package asistenciamotor360.modelo.estructuras;

public class Pila<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] elementos;
    private int tamano;

    public Pila() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    public void empujar(T elemento) {
        if (tamano == elementos.length) {
            Object[] nuevoArreglo = new Object[elementos.length * 2];

            for (int i = 0; i < tamano; i++) {
                nuevoArreglo[i] = elementos[i];
            }

            elementos = nuevoArreglo;
        }

        elementos[tamano] = elemento;
        tamano++;
    }

    @SuppressWarnings("unchecked")
    public T desempujar() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La pila no tiene elementos.");
        }

        T elemento = (T) elementos[tamano - 1];
        elementos[tamano - 1] = null;
        tamano--;

        return elemento;
    }

    @SuppressWarnings("unchecked")
    public T cima() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La pila no tiene elementos.");
        }

        return (T) elementos[tamano - 1];
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    public boolean contiene(T elemento) {
        for (int i = 0; i < tamano; i++) {
            if (elementos[i] != null && elementos[i].equals(elemento)) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public Lista<T> listar() {
        Lista<T> lista = new Lista<T>();

        for (int i = tamano - 1; i >= 0; i--) {
            lista.agregar((T) elementos[i]);
        }

        return lista;
    }

    public void cargar(Lista<T> elementosnuevos) {
        limpiar();

        for (int i = 0; i < elementosnuevos.tamano(); i++) {
            empujar(elementosnuevos.obtener(i));
        }
    }

    public void limpiar() {
        for (int i = 0; i < tamano; i++) {
            elementos[i] = null;
        }

        tamano = 0;
    }

}