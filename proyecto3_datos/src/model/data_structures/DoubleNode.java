package model.data_structures;

/**
 * Clase con el nodoDoble que contiene elemento gen�rico.
 * @param <T> Elemento gen�rico.
 */
public class DoubleNode<T extends Comparable<T>> extends SimpleNode<T> 
{
	
	//--------------------------------
	//ATRIBUTOS
	//--------------------------------
	
	/**
	 * El nodo anterior.
	 */
	private DoubleNode<T> previousNode;
	
	
	//--------------------------------
	//CONSTRUCTOR
	//--------------------------------
	
	/**
	 * M�todo constructor del nodo doble.
	 * @param elemento El elemento dentro del nodo.
	 */
	public DoubleNode(T elemento) 
	{
		super(elemento);
		previousNode = null;
	}
	
	//--------------------------------
	//METODOS
	//--------------------------------
	
	/**
	 * M�todo que retorna el nodo anterior al actual.
	 * @return El nodo anterior al actual.
	 */
	public DoubleNode<T> getPrevious()
	{
		return previousNode;
	}
	
	/**
	 * M�todo que modifica el nodo anterior al actual.
	 * @param previous El nuevo nodo anterior al actual.
	 */
	public void changePrevious(DoubleNode<T> previous)
	{
		this.previousNode = previous;
	}
}
