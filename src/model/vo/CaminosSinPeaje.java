package model.vo;

import model.data_structures.Edge;
import model.data_structures.Queue;

public class CaminosSinPeaje implements Comparable<CaminosSinPeaje>
{

	private String identificador;
	
	private double sumaCamino;
	
	private Queue<Edge<String,InformacionArcoServicios>> camino;
	
	public CaminosSinPeaje(String pId, double pSuma, Queue<Edge<String,InformacionArcoServicios>> pCola)
	{
		identificador = pId;
		sumaCamino = pSuma;
		camino = pCola;
	}
	
	public String darIdentificador()
	{
		return identificador;
	}
	
	public double darSumaCamino()
	{
		return sumaCamino;
	}
	
	public Queue<Edge<String,InformacionArcoServicios>> darCamino()
	{
		return camino;
	}

	@Override
	public int compareTo(CaminosSinPeaje o) 
	{
		int comparar = (int) (this.darSumaCamino() - o.darSumaCamino());
		
		if(comparar > 0)
		{
			return 1;
		}
		else if(comparar < 0)
		{
			return -1;
		}
		else
		{
			return 0;
		}
		
		// TODO Auto-generated method stub
	}
	
}
