package API;

import model.data_structures.ArrayListNotComparable;
import model.data_structures.ArrayListT;
import model.data_structures.Componente;
import model.data_structures.DoubleLinkedList;
import model.data_structures.Vertex;
import model.vo.InformacionArcoServicios;
import model.vo.InformacionVertice;

/**
 * API para la clase de logica principal  
 */
public interface ITaxiTripsManager 
{

		/**
	 * Dada la direccion del json que se desea cargar, se generan vo's, estructuras y datos necesarias
	 * @param direccionJson, ubicacion del json a cargar
	 * @return true si se lo logro cargar, false de lo contrario
	 */
	//REQ 0
	public void cargarGrafo();
	
	//REQ 1
	public Vertex<String,InformacionVertice,InformacionArcoServicios>  darInformacionDelVerticeMasCongestionado();
	
	//REQ 2
	public ArrayListNotComparable<Componente> darComponentesFuertementeConectadas();
	
	//REQ 3
	public void graficar();
	
	//REQ 4
	public void darInformacionCaminoMenorDistancia();
	
	//REQ 5
	public void darCaminoMasCortoYMasLargo();
	
	//REQ 6
	public ArrayListNotComparable<ArrayListT<String>> darCaminosSinPeaje();

}