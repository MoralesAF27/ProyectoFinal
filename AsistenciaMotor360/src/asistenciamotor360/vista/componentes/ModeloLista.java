package asistenciamotor360.vista.componentes;

import javax.swing.AbstractListModel;

import asistenciamotor360.modelo.estructuras.Lista;

public class ModeloLista extends AbstractListModel<String> {

    private static final long serialVersionUID = 1L;

    private Lista<String> elementos;

    public ModeloLista() {
        this.elementos = new Lista<String>();
    }

    public void setElementos(Lista<String> nuevos) {
        this.elementos = nuevos;
        fireContentsChanged(this, 0, Math.max(0, elementos.tamano() - 1));
    }

    @Override
    public String getElementAt(int indice) {
        return elementos.obtener(indice);
    }

    @Override
    public int getSize() {
        return elementos.tamano();
    }
}