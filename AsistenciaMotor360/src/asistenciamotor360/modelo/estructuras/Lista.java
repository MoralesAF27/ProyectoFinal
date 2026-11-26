package asistenciamotor360.modelo.estructuras;

public class Lista<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] elementos;
    private int tamano;

    public Lista() {
        this.elementos = new Object[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    public void agregar(T elemento) {
        asegurarCapacidad(tamano + 1);
        elementos[tamano] = elemento;
        tamano++;
    }

    public void agregar(int indice, T elemento) {
        if (indice < 0 || indice > tamano) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }

        asegurarCapacidad(tamano + 1);

        for (int i = tamano; i > indice; i--) {
            elementos[i] = elementos[i - 1];
        }

        elementos[indice] = elemento;
        tamano++;
    }

    @SuppressWarnings("unchecked")
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }

        return (T) elementos[indice];
    }

    public void reemplazar(int indice, T elemento) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }

        elementos[indice] = elemento;
    }

    @SuppressWarnings("unchecked")
    public T eliminar(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice fuera de rango: " + indice);
        }

        T eliminado = (T) elementos[indice];

        for (int i = indice; i < tamano - 1; i++) {
            elementos[i] = elementos[i + 1];
        }

        elementos[tamano - 1] = null;
        tamano--;

        return eliminado;
    }

    public boolean eliminar(T elemento) {
        int indice = indiceDe(elemento);

        if (indice >= 0) {
            eliminar(indice);
            return true;
        }

        return false;
    }

    public int indiceDe(T elemento) {
        for (int i = 0; i < tamano; i++) {
            if (sonIguales(elementos[i], elemento)) {
                return i;
            }
        }

        return -1;
    }

    public boolean contiene(T elemento) {
        return indiceDe(elemento) >= 0;
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    public void limpiar() {
        for (int i = 0; i < tamano; i++) {
            elementos[i] = null;
        }

        tamano = 0;
    }

    public void cargar(Lista<T> elementosnuevos) {
        limpiar();
        asegurarCapacidad(elementosnuevos.tamano());

        for (int i = 0; i < elementosnuevos.tamano(); i++) {
            agregar(elementosnuevos.obtener(i));
        }
    }

    public Lista<T> copia() {
        Lista<T> copia = new Lista<T>();

        for (int i = 0; i < tamano; i++) {
            copia.agregar(obtener(i));
        }

        return copia;
    }

    public void ordenar(Comparador<T> comparador) {
        for (int i = 1; i < tamano; i++) {
            T actual = obtener(i);
            int j = i - 1;

            while (j >= 0 && comparador.comparar(obtener(j), actual) > 0) {
                reemplazar(j + 1, obtener(j));
                j--;
            }

            reemplazar(j + 1, actual);
        }
    }

    private void asegurarCapacidad(int capacidadRequerida) {
        if (capacidadRequerida > elementos.length) {
            int nuevaCapacidad = elementos.length * 2;

            if (nuevaCapacidad < capacidadRequerida) {
                nuevaCapacidad = capacidadRequerida;
            }

            Object[] nuevoArreglo = new Object[nuevaCapacidad];

            for (int i = 0; i < tamano; i++) {
                nuevoArreglo[i] = elementos[i];
            }

            elementos = nuevoArreglo;
        }
    }

    private boolean sonIguales(Object primero, Object segundo) {
        if (primero == null) {
            return segundo == null;
        }

        return primero.equals(segundo);
    }

}