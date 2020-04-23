package p3.test;

import static org.junit.Assert.*;
import org.junit.Test;

import model.data_structures.ArrayListNotComparable;
import model.data_structures.ArrayListT;
import model.data_structures.DiGraph;
import model.data_structures.Queue;
import model.data_structures.Vertex;
import model.vo.InformacionArcoServicios;

public class DiGraphTest 
{

	private DiGraph<Integer, ArrayListT<String> ,InformacionArcoServicios > grafoDePrueba;

	public DiGraphTest() {
		grafoDePrueba = new DiGraph<Integer, ArrayListT<String> ,InformacionArcoServicios >();

	}
	// TODO Auto-generated constructor stub

	public void setUp1()
	{

		for (int i = 0; i < 10; i++) 
		{
			ArrayListT<String> lista = new ArrayListT<String>();
			for (int j = 0; j < 3; j++)
			{
				lista.add("A"+(j*5));
			}
			grafoDePrueba.addVertex(i, lista);
		}
		for (int i = 0; i < 20 ; i++) 
		{
			Integer vert1 = (int)(i*Math.random()*10)%10;
			Integer vert2 = (int)(i*Math.random()*11)%10;
			Double info = Math.random()*Math.random();
			grafoDePrueba.addEdge(vert1, vert2, new InformacionArcoServicios(info, 0, 0, 2));
		}

	}
	public void setUp2()
	{
		grafoDePrueba.addVertex(0, new ArrayListT<String>());
		grafoDePrueba.addVertex(1, new ArrayListT<String>());
		grafoDePrueba.addVertex(2, new ArrayListT<String>());
		grafoDePrueba.addVertex(3, new ArrayListT<String>());
		grafoDePrueba.addVertex(4, new ArrayListT<String>());
		grafoDePrueba.addVertex(5, new ArrayListT<String>());

		grafoDePrueba.addEdge(4, 0, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(0, 2, new InformacionArcoServicios(4.5, 0, 0, 2));
		grafoDePrueba.addEdge(0, 1, new InformacionArcoServicios(4.7, 0, 0, 2));
		grafoDePrueba.addEdge(0, 3, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(1, 3, new InformacionArcoServicios(4.1, 0, 0, 2));
		grafoDePrueba.addEdge(2, 5, new InformacionArcoServicios(4.9, 0, 0, 2));
		grafoDePrueba.addEdge(3, 4, new InformacionArcoServicios(3.0, 0, 0, 2));
		grafoDePrueba.addEdge(5, 1, new InformacionArcoServicios(5.0, 0, 0, 2));
		
		grafoDePrueba.inicializar();

	}
	public void setUp3()
	{
		grafoDePrueba.addVertex(0, new ArrayListT<String>());
		grafoDePrueba.addVertex(1, new ArrayListT<String>());
		grafoDePrueba.addVertex(2, new ArrayListT<String>());
		grafoDePrueba.addVertex(3, new ArrayListT<String>());
		grafoDePrueba.addVertex(4, new ArrayListT<String>());
		grafoDePrueba.addVertex(5, new ArrayListT<String>());
		
		grafoDePrueba.addEdge(5, 0, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(0, 2, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(2, 1, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(1, 3, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(3, 4, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(4, 2, new InformacionArcoServicios(4.2, 0, 0, 2));
		
		
		grafoDePrueba.inicializar();
	}
	
	public void setUp4()
	{
		grafoDePrueba.addVertex(0, new ArrayListT<String>());
		grafoDePrueba.addVertex(1, new ArrayListT<String>());
		grafoDePrueba.addVertex(2, new ArrayListT<String>());
		grafoDePrueba.addVertex(3, new ArrayListT<String>());
		grafoDePrueba.addVertex(4, new ArrayListT<String>());
		grafoDePrueba.addVertex(5, new ArrayListT<String>());
		
		grafoDePrueba.addEdge(0, 1, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(0, 2, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(0, 3, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(1, 4, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(2, 4, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(3, 4, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(1, 2, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(2, 3, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(0, 4, new InformacionArcoServicios(4.2, 0, 0, 2));
		
		
		grafoDePrueba.inicializar();
	}
	@Test
	public void Addtest()
	{
		setUp1();
		ArrayListT<String> l = new ArrayListT<String>();
		l.add("Hola");
		grafoDePrueba.addVertex(11, l);
		assertEquals("Hola", grafoDePrueba.getInfoVertex(11).get(0));
		grafoDePrueba.getInfoVertex(11).add("Chao");

		for (int i = 0; i < grafoDePrueba.getInfoVertex(11).size(); i++) 
		{
			System.out.println("---"+grafoDePrueba.getInfoVertex(11).get(i));
		}
		l.add("Arrivederchi");
		grafoDePrueba.addVertex(11, l);
		assertEquals(l, grafoDePrueba.getInfoVertex(11));

		assertNull(grafoDePrueba.getInfoEdge(11, 0));
		grafoDePrueba.addEdge(11, 0 , new InformacionArcoServicios(17.7, 0, 0, 2));
		assertEquals("17.7",grafoDePrueba.getInfoEdge(11, 0).getMiles()+"");

		grafoDePrueba.addEdge(11, 0, new InformacionArcoServicios(17.9, 0, 0, 2));
		assertEquals("17.9",grafoDePrueba.getInfoEdge(11, 0).getMiles()+"");
	}
	
	@Test
	public void bfsTest()
	{
		setUp2();
		int[][] matrix = grafoDePrueba.breadthFirstSearch(2);
		
		//Aqui probamos el vértice por el cual se llega
		assertEquals(4, matrix[1][0]);
		assertEquals(5, matrix[1][1]);
		assertEquals(-1, matrix[1][2]);
		assertEquals(1, matrix[1][3]);
		assertEquals(3, matrix[1][4]);
		assertEquals(2, matrix[1][5]);
		
		//Aqui probamos la distancia que hay desde el vértice 2 hasta los demás.
		assertEquals(5, matrix[0][0]);
		assertEquals(2, matrix[0][1]);
		assertEquals(0, matrix[0][2]);
		assertEquals(3, matrix[0][3]);
		assertEquals(4, matrix[0][4]);
		assertEquals(1, matrix[0][5]);
	}
	@Test
	public void dfsTest()
	{
		setUp3();
		int edgeTo[] = grafoDePrueba.depthFirstSearch(0);
		
		assertEquals(2, edgeTo[1]);
		assertEquals(0, edgeTo[2]);
		assertEquals(1, edgeTo[3]);
		assertEquals(3, edgeTo[4]);	
	}
	@Test
	public void caminosTest()
	{
		setUp4();
		
		ArrayListNotComparable<ArrayListNotComparable<Vertex<Integer, ArrayListT<String> ,InformacionArcoServicios>>> listaDeCaminos =  grafoDePrueba.encontrarCaminos(0, 4);
		
		for (int i = 0; i < listaDeCaminos.size(); i++) 
		{
			System.out.println();
			System.out.println("El camino "+i+" es :");
			for(int j= 0 ; j < listaDeCaminos.get(i).size() ; j++)
			{
				
				System.out.print("--->"+ listaDeCaminos.get(i).get(j).getId());
			}
		}
		
		
	}
}
