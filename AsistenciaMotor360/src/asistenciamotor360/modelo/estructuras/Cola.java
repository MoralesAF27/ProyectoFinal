
package asistenciamotor360.modelo.estructuras;

public class Cola<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] elementos;
    private int frente;
    private int fin;
    private int tamano;

    public Cola() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.frente = 0;
        this.fin = 0;
        this.tamano = 0;
    }

    public void encolar(T elemento) {
        if (tamano == elementos.length) {
            expandir();
        }

        elementos[fin] = elemento;
        fin = (fin + 1) % elementos.length;
        tamano++;
    }

    @SuppressWarnings("unchecked")
    public T desencolar() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La cola no tiene elementos.");
        }

        T elemento = (T) elementos[frente];
        elementos[frente] = null;
        frente = (frente + 1) % elementos.length;
        tamano--;

        return elemento;
    }

    @SuppressWarnings("unchecked")
    public T frente() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La cola no tiene elementos.");
        }

        return (T) elementos[frente];
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    public boolean contiene(T elemento) {
        for (int i = 0; i < tamano; i++) {
            Object actual = elementos[(frente + i) % elementos.length];

            if (actual != null && actual.equals(elemento)) {
                return true;
            }
        }

        return false;
    }

    public boolean eliminar(T elemento) {
        Lista<T> restantes = new Lista<T>();
        boolean eliminado = false;

        for (int i = 0; i < tamano; i++) {
            @SuppressWarnings("unchecked")
            T actual = (T) elementos[(frente + i) % elementos.length];

            if (!eliminado && actual != null && actual.equals(elemento)) {
                eliminado = true;
            } else {
                restantes.agregar(actual);
            }
        }

        if (eliminado) {
            cargar(restantes);
        }

        return eliminado;
    }

    @SuppressWarnings("unchecked")
    public Lista<T> listar() {
        Lista<T> lista = new Lista<T>();

        for (int i = 0; i < tamano; i++) {
            lista.agregar((T) elementos[(frente + i) % elementos.length]);
        }

        return lista;
    }

    public void cargar(Lista<T> elementosnuevos) {
        int capacidadNecesaria = Math.max(CAPACIDAD_INICIAL, elementosnuevos.tamano() + 1);

        this.elementos = new Object[capacidadNecesaria];
        this.frente = 0;
        this.fin = 0;
        this.tamano = 0;

        for (int i = 0; i < elementosnuevos.tamano(); i++) {
            encolar(elementosnuevos.obtener(i));
        }
    }

    public void limpiar() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.frente = 0;
        this.fin = 0;
        this.tamano = 0;
    }

    private void expandir() {
        Lista<T> actuales = listar();
        Object[] nuevoArreglo = new Object[elementos.length * 2];

        for (int i = 0; i < actuales.tamano(); i++) {
            nuevoArreglo[i] = actuales.obtener(i);
        }

        this.elementos = nuevoArreglo;
        this.frente = 0;
        this.fin = tamano;
    }
}
