package model.data_structures;

import model.vo.InformacionArcoServicios;
import model.vo.InformacionVertice;

public class Componente implements Comparable<Componente>
{
	private int numeroDeVertices;
	
	private int color;
	
	private ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>> listaDeVertices;
	
	public Componente(int pNum, int pColor, ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>> arrayListNotComparable)
	{
		setNumeroDeVertices(pNum);
		setColor(pColor);
		listaDeVertices = arrayListNotComparable;
	}

	public int getNumeroDeVertices()
	{
		return numeroDeVertices;
	}

	public void setNumeroDeVertices(int numeroDeVertices)
	{
		this.numeroDeVertices = numeroDeVertices;
	}
	//ya
	public int getColor() 
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}
	public ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>> getList()
	{
		return listaDeVertices;
	}

	@Override
	public int compareTo(Componente o) 
	{
		int compare = this.getColor() - o.getColor();
		if(compare > 0)
		{
			return 1;
		}
		else if(compare < 0)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
	

}
