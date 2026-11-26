package asistenciamotor360.modelo.estructuras;

/**
 * Define el criterio de ordenamiento utilizado por las estructuras propias del
 * sistema. Reemplaza el uso de interfaces del framework de colecciones de Java.
 *
 * @param <T> tipo de elemento que se compara.
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public interface Comparador<T> {

    /**
     * Compara dos elementos del mismo tipo.
     *
     * @param primero primer elemento de la comparacion.
     * @param segundo segundo elemento de la comparacion.
     * @return un valor negativo si el primero es menor, cero si son iguales y un
     *         valor positivo si el primero es mayor.
     */
    int comparar(T primero, T segundo);
}
