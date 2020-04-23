package model.data_structures;

import model.vo.InformacionArcoServicios;

public class Edge<K extends Comparable<K>,A>
{
	private InformacionArcoServicios element;
	
	private K idVertStart;
	
	private K idVertEnd;
	
	public Edge(K start, K end, A e)
	{
		element = (InformacionArcoServicios) e;
		idVertStart = start;
		idVertEnd = end;
	}
	public Edge(K[] startEnd, A e)
	{
		element = (InformacionArcoServicios) e;
		idVertStart = startEnd[0] ;
		idVertEnd = startEnd[1];
	}
	
	
	public K getIdVertStart()
	{
		return idVertStart;
	}
	
	public K getIdVertEnd()
	{
		return idVertEnd;
	}
	
	public double getEspecifiedElement(int tipo)
	{
		return element.seleccionarCriterio(tipo);
	}
	
	@SuppressWarnings("unchecked")
	public A getElement()
	{
		return (A) element;
	}
	
	public void setElement(A nnew)
	{
		element = (InformacionArcoServicios) nnew;
	}
	public void getValue()
	{
	}
}
