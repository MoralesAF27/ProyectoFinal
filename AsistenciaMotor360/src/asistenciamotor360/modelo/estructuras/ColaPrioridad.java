
package asistenciamotor360.modelo.estructuras;

public class ColaPrioridad<T> {

    private static final int CAPACIDAD_INICIAL = 10;

    private Object[] monticulo;
    private int tamano;
    private Comparador<T> comparador;

    public ColaPrioridad(Comparador<T> comparador) {
        if (comparador == null) {
            throw new IllegalArgumentException("El comparador es obligatorio.");
        }

        this.comparador = comparador;
        this.monticulo = new Object[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    public void encolar(T elemento) {
        if (tamano == monticulo.length) {
            Object[] nuevoArreglo = new Object[monticulo.length * 2];

            for (int i = 0; i < tamano; i++) {
                nuevoArreglo[i] = monticulo[i];
            }

            monticulo = nuevoArreglo;
        }

        monticulo[tamano] = elemento;
        tamano++;
        flotar(tamano - 1);
    }

    public T desencolar() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La cola de prioridad no tiene elementos.");
        }

        T mayor = obtener(0);
        monticulo[0] = monticulo[tamano - 1];
        monticulo[tamano - 1] = null;
        tamano--;

        if (tamano > 0) {
            hundir(0);
        }

        return mayor;
    }

    public T mirar() {
        if (estaVacia()) {
            throw new EstructuraVaciaExcepcion("La cola de prioridad no tiene elementos.");
        }

        return obtener(0);
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    public boolean contiene(T elemento) {
        for (int i = 0; i < tamano; i++) {
            if (monticulo[i] != null && monticulo[i].equals(elemento)) {
                return true;
            }
        }

        return false;
    }

    public boolean eliminar(T elemento) {
        int indice = -1;

        for (int i = 0; i < tamano; i++) {
            if (monticulo[i] != null && monticulo[i].equals(elemento)) {
                indice = i;
                break;
            }
        }

        if (indice < 0) {
            return false;
        }

        monticulo[indice] = monticulo[tamano - 1];
        monticulo[tamano - 1] = null;
        tamano--;

        if (indice < tamano) {
            T reubicado = obtener(indice);
            int padre = (indice - 1) / 2;

            if (indice > 0 && comparador.comparar(reubicado, obtener(padre)) > 0) {
                flotar(indice);
            } else {
                hundir(indice);
            }
        }

        return true;
    }

    public Lista<T> listar() {
        ColaPrioridad<T> copia = new ColaPrioridad<T>(comparador);

        for (int i = 0; i < tamano; i++) {
            copia.encolar(obtener(i));
        }

        Lista<T> lista = new Lista<T>();

        while (!copia.estaVacia()) {
            lista.agregar(copia.desencolar());
        }

        return lista;
    }

    public void cargar(Lista<T> elementosnuevos) {
        this.monticulo = new Object[Math.max(CAPACIDAD_INICIAL, elementosnuevos.tamano() + 1)];
        this.tamano = 0;

        for (int i = 0; i < elementosnuevos.tamano(); i++) {
            encolar(elementosnuevos.obtener(i));
        }
    }

    public void limpiar() {
        this.monticulo = new Object[CAPACIDAD_INICIAL];
        this.tamano = 0;
    }

    @SuppressWarnings("unchecked")
    private T obtener(int indice) {
        return (T) monticulo[indice];
    }

    private void flotar(int indice) {
        int actual = indice;

        while (actual > 0) {
            int padre = (actual - 1) / 2;

            if (comparador.comparar(obtener(actual), obtener(padre)) > 0) {
                intercambiar(actual, padre);
                actual = padre;
            } else {
                break;
            }
        }
    }

    private void hundir(int indice) {
        int actual = indice;

        while (true) {
            int izquierdo = 2 * actual + 1;
            int derecho = 2 * actual + 2;
            int mayor = actual;

            if (izquierdo < tamano && comparador.comparar(obtener(izquierdo), obtener(mayor)) > 0) {
                mayor = izquierdo;
            }

            if (derecho < tamano && comparador.comparar(obtener(derecho), obtener(mayor)) > 0) {
                mayor = derecho;
            }

            if (mayor == actual) {
                break;
            }

            intercambiar(actual, mayor);
            actual = mayor;
        }
    }

    private void intercambiar(int primero, int segundo) {
        Object temporal = monticulo[primero];
        monticulo[primero] = monticulo[segundo];
        monticulo[segundo] = temporal;
    }
}
