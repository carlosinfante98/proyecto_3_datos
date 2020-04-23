package model.data_structures;

public class Vertex<K extends Comparable<K>, V, E>
{
	private K id;
	
	private V value;
	
	private ArrayListNotComparable<Edge<K,E>> inEdges;
	
	private ArrayListNotComparable<Edge<K,E>> outEdges;
	
	private int idNum;
	
	private int idComponent;
	
	public Vertex(K id, V value, int pId)
	{
		this.id = id;
		this.value = value;
		inEdges = new ArrayListNotComparable<Edge<K,E>>();
		outEdges = new ArrayListNotComparable<Edge<K,E>>();
		idNum = pId;
		idComponent = -1;
	}
	
	
	public K getId()
	{
		return id;
	}
	
	public int getIdNum()
	{
		return idNum;
	}
	
	public V getValue()
	{
		return value;
	}
	
	public void setValue(V n)
	{
		value = n;
	}
	
	public ArrayListNotComparable<Edge<K,E>> getInEdges()
	{
		return inEdges;
	}
	
	public ArrayListNotComparable<Edge<K,E>> getOutEdges()
	{
		return outEdges;
	}
	
	public void addInEdge(Edge<K,E> e)
	{
		inEdges.add(e);
	}
	
	public void addOutEdge(Edge<K,E> e)
	{
		outEdges.add(e);
	}
	
	public Edge<K,E> getEdge(K idArrival)
	{
		Edge<K,E> found = null;
		for (int i = 0; i < outEdges.size() && found == null; i++) 
		{
			if(outEdges.get(i).getIdVertEnd().compareTo(idArrival) == 0)
			{
				found = outEdges.get(i);
			}
		}
		return found;
	}
	public void setIdComponent(int pComponent)
	{
		this.idComponent = pComponent;
	}
	public int getIdComponent()
	{
		return idComponent;
	}
}
