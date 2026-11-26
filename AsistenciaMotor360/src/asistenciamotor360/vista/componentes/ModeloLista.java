package asistenciamotor360.vista.componentes;

import javax.swing.AbstractListModel;

import asistenciamotor360.modelo.estructuras.Lista;

/**
 * Modelo de lista que almacena sus elementos en una estructura propia y los
 * expone a un JList de la interfaz grafica.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class ModeloLista extends AbstractListModel<String> {

    private static final long serialVersionUID = 1L;

    private Lista<String> elementos;

    /**
     * Crea un modelo de lista vacio.
     */
    public ModeloLista() {
        this.elementos = new Lista<String>();
    }

    /**
     * Reemplaza los elementos del modelo.
     *
     * @param nuevos nuevos elementos de la lista.
     */
    public void setElementos(Lista<String> nuevos) {
        this.elementos = nuevos;
        fireContentsChanged(this, 0, Math.max(0, elementos.tamano() - 1));
    }

    /**
     * Retorna el elemento de la posicion indicada.
     *
     * @param indice posicion consultada.
     * @return elemento de la posicion.
     */
    @Override
    public String getElementAt(int indice) {
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
}
