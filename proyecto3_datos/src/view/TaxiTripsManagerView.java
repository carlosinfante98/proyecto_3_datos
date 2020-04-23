package view;

import java.util.Scanner;
import controller.Controller;
import model.data_structures.ArrayListNotComparable;
import model.data_structures.Componente;

/**
 * view del programa
 */
public class TaxiTripsManagerView 
{

	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		boolean fin=false;
		while(!fin)
		{
			//imprime menu
			printMenu();

			//opcion req
			int option = sc.nextInt();

			switch(option)
			{
			// REQ 0
			case 1:

				//Memoria y tiempo
				long memoryBeforeCase1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				long startTime = System.nanoTime();
				
				Controller.cargarGrafo();

				//Tiempo en cargar
				long endTime = System.nanoTime();
				long duration = (endTime - startTime)/(1000000);

				//Memoria usada
				long memoryAfterCase1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				System.out.println("Tiempo en cargar: " + duration + " milisegundos \nMemoria utilizada:  "+ ((memoryAfterCase1 - memoryBeforeCase1)/1000000.0) + " MB");

				break;
			
			//REQ 1
			case 2:
				Controller.darInformacionDelVerticeMasCongestionado();
				break;
				
			//REQ 2
			case 3:
				ArrayListNotComparable<Componente> listinha1 = Controller.darComponentesFuertementeConectadas();
				for (int i = 0; i < listinha1.size(); i++)
				{
					System.out.println("A la componente " + i + " le corresponden los siguientes datos : ");
					System.out.println("Numero de vértices : "+listinha1.get(i).getNumeroDeVertices());
					System.out.println("Color/identificador :"+listinha1.get(i).getColor());
				}
				System.out.println();
				break;
				
			//REQ 3
			case 4:
				Controller.graficar();
				break;
				
			//REQ 4
			case 5:
				Controller.darInformacionCaminoMenorDistancia();
				break;
				
			//REQ 5
			case 6:
				 Controller.darCaminoMasCortoYMasLargo();
				break;
				
			//REQ 6	
			case 7:
				Controller.darCaminosSinPeaje();
				break;
				
			case 8: 
				fin=true;
				sc.close();
				break;

			}
		}
	}

	/**
	 * Menu 
	 */
	private static void printMenu() //
	{
		System.out.println("---------ISIS 1206 - Estructuras de datos----------");
		System.out.println("---------------------Proyecto 3----------------------");
		System.out.println("Iniciar la Fuente de Datos a Consultar :");
		System.out.println("1. Cargar grafo generado en el taller 8.");
		System.out.println("2. Obtener la información del vértice más congestionado de Chicago. ");
		System.out.println("3. Obtener las componentes conectadas del grafo y brindar su información.");
		System.out.println("4. Generar mapa de la red vial de Chicago.");
		System.out.println("5. Encontrar el camino de menor distancia entre dos localizaciones del mapa de Chicago.");
		System.out.println("6. Encontrar el camino de mayor y menor duración entre dos localizaciones del mapa de Chicago.");
		System.out.println("7. Encontrar(si existe) camino(s) entre dos localizaciones en los cuales no haya peaje.");
		System.out.println("8. Salir");
		System.out.println("Ingrese el numero de la opcion seleccionada y presione <Enter> para confirmar: (e.g., 1):");


	}

}
