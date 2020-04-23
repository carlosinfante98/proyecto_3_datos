package controller;

import API.ITaxiTripsManager;
import model.data_structures.ArrayListNotComparable;
import model.data_structures.ArrayListT;
import model.data_structures.Componente;
import model.data_structures.DoubleLinkedList;
import model.data_structures.Vertex;
import model.logic.TaxiTripsManager;
import model.vo.InformacionArcoServicios;
import model.vo.InformacionVertice;

public class Controller 
{
	/**
	 * modela el manejador de la clase l�gica
	 */
	private static ITaxiTripsManager manager =new TaxiTripsManager();

	//REQ 0
	public static void cargarGrafo()
	{
		manager.cargarGrafo();
	}
	
	//REQ 1
	public 	static Vertex<String,InformacionVertice,InformacionArcoServicios> darInformacionDelVerticeMasCongestionado()
	{
		return manager.darInformacionDelVerticeMasCongestionado();
	}
	
	//REQ 2
	public static ArrayListNotComparable<Componente> darComponentesFuertementeConectadas()
	{
		return manager.darComponentesFuertementeConectadas();
	}

	//REQ 3
	public static void graficar()
	{
		manager.graficar();
	}
	
	//REQ 4
	public static void darInformacionCaminoMenorDistancia()
	{
		manager.darInformacionCaminoMenorDistancia();
	}
	
	//REQ 5
	public static void darCaminoMasCortoYMasLargo()
	{
		manager.darCaminoMasCortoYMasLargo();
	}
	
	//REQ 6
	public static ArrayListNotComparable<ArrayListT<String>> darCaminosSinPeaje()
	{
		return manager.darCaminosSinPeaje();
	}


}
