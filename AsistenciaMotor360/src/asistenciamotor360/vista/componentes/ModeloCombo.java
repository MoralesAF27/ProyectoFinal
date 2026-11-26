package asistenciamotor360.vista.componentes;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import asistenciamotor360.modelo.estructuras.Lista;

/**
 * Modelo de combo reutilizable que almacena sus elementos en una lista propia,
 * evitando el uso de las colecciones de Java.
 *
 * @param <T> tipo de elemento que muestra el combo.
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class ModeloCombo<T> implements ComboBoxModel<T> {

    private Lista<T> elementos;
    private final Lista<ListDataListener> escuchas;
    private Object seleccionado;

    /**
     * Crea un modelo de combo vacio.
     */
    public ModeloCombo() {
        this.elementos = new Lista<T>();
        this.escuchas = new Lista<ListDataListener>();
        this.seleccionado = null;
    }

    /**
     * Reemplaza los elementos del combo conservando la seleccion cuando el
     * elemento sigue presente.
     *
     * @param nuevos nuevos elementos del combo.
     */
    public void setElementos(Lista<T> nuevos) {
        Object anterior = seleccionado;
        this.elementos = nuevos;
        this.seleccionado = null;
        if (anterior != null) {
            for (int i = 0; i < elementos.tamano(); i++) {
                if (elementos.obtener(i) != null && elementos.obtener(i).equals(anterior)) {
                    seleccionado = anterior;
                    break;
                }
            }
        }
        if (seleccionado == null && !elementos.estaVacia()) {
            seleccionado = elementos.obtener(0);
        }
        notificarCambio();
    }

    /**
     * Retorna los elementos actuales del modelo.
     *
     * @return lista de elementos.
     */
    public Lista<T> getElementos() {
        return elementos;
    }

    /**
     * Retorna el elemento seleccionado convertido al tipo del combo.
     *
     * @return elemento seleccionado o nulo si no hay seleccion.
     */
    public T getSeleccionado() {
        @SuppressWarnings("unchecked")
        T valor = (T) seleccionado;
        return valor;
    }

    /**
     * Retorna el elemento de la posicion indicada.
     *
     * @param indice posicion consultada.
     * @return elemento de la posicion.
     */
    @Override
    public T getElementAt(int indice) {
        return elementos.obtener(indice);
    }

    /**
     * Retorna la cantidad de elementos del modelo.
     *
     * @return cantidad de elementos.
     */
    @Override
    public int getSize() {
        return elementos.tamano();
    }

    /**
     * Retorna el elemento seleccionado del modelo.
     *
     * @return elemento seleccionado.
     */
    @Override
    public Object getSelectedItem() {
        return seleccionado;
    }

    /**
     * Define el elemento seleccionado del modelo.
     *
     * @param elemento elemento que se desea seleccionar.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setSelectedItem(Object elemento) {
        if (elemento == null) {
            this.seleccionado = null;
        } else if (elementos.contiene((T) elemento)) {
            this.seleccionado = elemento;
        }
        notificarCambio();
    }

    /**
     * Registra un escucha de cambios del modelo.
     *
     * @param escucha escucha que se desea registrar.
     */
    @Override
    public void addListDataListener(ListDataListener escucha) {
        escuchas.agregar(escucha);
    }

    /**
     * Retira un escucha de cambios del modelo.
     *
     * @param escucha escucha que se desea retirar.
     */
    @Override
    public void removeListDataListener(ListDataListener escucha) {
        escuchas.eliminar(escucha);
    }

    private void notificarCambio() {
        ListDataEvent evento = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, -1, -1);
        for (int i = 0; i < escuchas.tamano(); i++) {
            ListDataListener escucha = escuchas.obtener(i);
            if (escucha != null) {
                escucha.contentsChanged(evento);
            }
        }
    }
}
