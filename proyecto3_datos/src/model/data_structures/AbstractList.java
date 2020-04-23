package model.data_structures;

/**
 * Lista abstracta de lista.
 * @param <T> Elemento gen�rico de la lista abstracta.
 */
public abstract class AbstractList<T extends Comparable<T>> implements LinkedList<T> 
{	
	/**
	 * Atributo que indica el tamaa�o de la lista.
	 */
	protected int size; 
	
	/**
	 * Primer nodo de la lista.
	 */
	protected SimpleNode<T> firstNode;
	
	/**
	 * Indica el tama�o de la lista.
	 * @return La cantidad de nodos de la lista.
	 */
	public int size() 
	{
		return size;
	}
	
	/**
	 * Pone un elemento en la posici�n del arreglo la cual se pidi� por par�metro.
	 * @param index Indice del arreglo sobre el cual se quiere poner el nuevo elemento.
	 * @param element Elemento que se quiere insertar en la lista.
	 * @return Elemento que se retira de la lista por haber sido sobreescrito.
	 * @throws IndexOutOfBoundsException Si se elige un indice que est� fuera de la lista.
	 */
	public T set(int index, T element) throws IndexOutOfBoundsException 
	{
		if(index < 0 || index >= size())
		{
			throw new IndexOutOfBoundsException();
		}
		
		SimpleNode<T> n = getNode(index);
		T retirado = n.getElement();
		n.setElement(element);
		return retirado;
		
	}
	
	/**
	 * Revisa si la lista est� vac�a.
	 * @return True si la lista est� vac�a. False de lo contrario.
	 */
	public boolean isEmpty() 
	{	
		return firstNode == null;
	}
	
	/**
     * Indica la posici�n en la lista del objeto que llega por par�metro
     * @param objeto El objeto que se desea buscar en la lista. objeto != null
     * @return La posici�n del objeto o -1 en caso que no se encuentre en la lista
     */
	@SuppressWarnings("unchecked")
	public int indexOf(Object o) 
	{
		int pos = 0;
		boolean encontro = false;
		int retorno = -1;
		
		SimpleNode<T> n = firstNode;
		
		while(n != null && !encontro )
		{
			if(n.getElement().compareTo((T) o) == 0)
			{
				encontro = true;
				retorno = pos;
			}
			else
			{
				n = n.getNextNode();
				pos++;
			}	
		}
		return retorno;
		
	}
	
	/**
     * Devuelve el elemento de la posici�n dada.
     * @param pos La posici�n  buscada.
     * @return El elemento en la posici�n dada.
     * @throws IndexOutOfBoundsException si index < 0 o index >= size()
     */
	public T get(int index) throws IndexOutOfBoundsException
	{
		if(index < 0 || index >= size())
		{
			throw new IndexOutOfBoundsException();
		}
		
		SimpleNode<T> n = getNode(index);
		
		return n.getElement();		
	}
	
	/**
     * Devuelve el nodo de la posici�n dada
     * @param pos la posici�n  buscada
     * @return el nodo en la posici�n dada 
     * @throws IndexOutOfBoundsException si index < 0 o index >= size()
     */
	public SimpleNode<T> getNode(int index)
	{
		if(index < 0 || index >= size)
		{
			throw new IndexOutOfBoundsException("Se est� pidiendo el indice: " + index + " y el tama�o de la lista es de " + size);
		}
		
		SimpleNode<T> actual = firstNode;
		int posActual = 0;
		while(actual != null && posActual < index)
		{
			actual = actual.getNextNode();
			posActual++;
		}
		
		return actual;
	}
	
	/**
     * Indica si la lista contiene el objeto indicado
     * @param objeto el objeto que se desea buscar en la lista. objeto != null
     * @return true en caso que el objeto est� en la lista o false en caso contrario
     */
	public boolean contains(Object o) 
	{
		boolean contiene = false;
		if(indexOf(o) != -1)
		{
			contiene = true;
		}
		return contiene;	
	}
	
	/**
     * Borra todos los elementos de la lista. Actualiza la cantidad de elementos en 0
     * <b>post:</b> No hay elementos en la lista
     */
	public void clear() 
	{
		firstNode = null;
		size = 0;	
	}

}
