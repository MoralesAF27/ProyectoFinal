package asistenciamotor360.vista.componentes;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import asistenciamotor360.modelo.estructuras.Lista;

public class ModeloCombo<T> implements ComboBoxModel<T> {

    private Lista<T> elementos;
    private final Lista<ListDataListener> escuchas;
    private Object seleccionado;

    public ModeloCombo() {
        this.elementos = new Lista<T>();
        this.escuchas = new Lista<ListDataListener>();
        this.seleccionado = null;
    }

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

    public Lista<T> getElementos() {
        return elementos;
    }

    public T getSeleccionado() {
        @SuppressWarnings("unchecked")
        T valor = (T) seleccionado;
        return valor;
    }

    @Override
    public T getElementAt(int indice) {
        return elementos.obtener(indice);
    }

    @Override
    public int getSize() {
        return elementos.tamano();
    }

    @Override
    public Object getSelectedItem() {
        return seleccionado;
    }

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

    @Override
    public void addListDataListener(ListDataListener escucha) {
        escuchas.agregar(escucha);
    }

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