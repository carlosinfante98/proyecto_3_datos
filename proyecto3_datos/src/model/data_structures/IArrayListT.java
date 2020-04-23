package model.data_structures;

/**
 * Interfaz para poder manejar ArrayList gen�rico.
 * @param <T> Elemento gen�rico.
 */
public interface IArrayListT<T>
{
	//--------------------------------
	//METODOS
	//--------------------------------
	
	/**
	 * Agrega un elemento que se da por par�metro.
	 * @param element Elemento que se quiere agregar a la lista. element != null
	 * @return El elemento que agreg� a la lista.
	 */
	public T add(T element);
	
	/**
	 * Remueve un elemento de la lista.
	 * @param element Elemento que se quiere remover de la lista.
	 * @return True si pudo remover un elemento de la lista. False de lo contrario.
	 */
	public boolean remove( T element);
	
	/**
	 * Remueve un elemento de la lista buscando por su posici�n.
	 * @param position Posici�n de la lista sobre la cual se quiere remover un dato.
	 * @return Elemento que se removi� de la lista. Null si no encontr� el elemento.
	 */
	public T remove (int position);
	
	/**
	 * Inserta un elemento en la lista con la posici�n dada por par�metro.
	 * @param position Posici�n del arreglo sobre la cual se quiere insertar un elemento.
	 * @param element Elemento el cual quiere ser insertado.
	 */
	public void insert( int position, T element);
	
	/**
	 * Agrega en una posici�n dada por par�metro en la lista y corre los dem�s elementos.
	 * @param position Posici�n del arreglo sobre la cual se quiere insertar un elemento.
	 * @param element Elemento el cual quiere ser insertado.
	 * @return True si pudo agregar el elemento en la lista. False de lo contrario.
	 */
	public boolean addIn(int position, T element);
	
	/**
	 * Busca el �ndice de un elemento.
	 * @param element Elemento al cual se le quiere buscar el �ndice.
	 * @return Indice del elemento que si pidi� buscar.
	 */
	public int indexOf(T element);
	
	/**
	 * Consigue el elemento de la lista dada una posici�n.
	 * @param position Posici�n del elemento que se busca en la lista.
	 * @return Elemento que se busca dentro de la lista por posici�n.
	 */
	public T get(int position);
	
	/**
	 * Consigue el elemento de la lista.
	 * @param element Elemento que se busca en la lista.
	 * @return Elemento que se busca dentro de la lista.
	 */
	public T get(T element);
	
	/**
	 * Tama�o actual de la lista.
	 * @return N�mero del tama�o actual de la lista.
	 */
	public int size();
	
	/**
	 * Remueve todos los elementos que est�n en la lista.
	 */
	public void removeAll();
	
	/**
	 * Revisa si el arreglo est� vac�o.
	 * @return True si el arreglo est� vac�o. False de lo contrario.
	 */
	public boolean isEmpty();
}
