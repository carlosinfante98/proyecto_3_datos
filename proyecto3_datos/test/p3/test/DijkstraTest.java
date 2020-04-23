package p3.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import model.data_structures.ArrayListT;
import model.data_structures.DiGraph;
import model.data_structures.Dijkstra;
import model.data_structures.Vertex;
import model.vo.InformacionArcoServicios;

public class DijkstraTest 
{
	private DiGraph<Integer, ArrayListT<String> ,InformacionArcoServicios > grafoDePrueba;

	@Before
	public void setUpEscenario1()
	{
		grafoDePrueba = new DiGraph<Integer, ArrayListT<String>,InformacionArcoServicios>();
		
		grafoDePrueba.addVertex(0, new ArrayListT<String>());
		grafoDePrueba.addVertex(1, new ArrayListT<String>());
		grafoDePrueba.addVertex(2, new ArrayListT<String>());
		grafoDePrueba.addVertex(3, new ArrayListT<String>());
		grafoDePrueba.addVertex(4, new ArrayListT<String>());
		grafoDePrueba.addVertex(5 , new ArrayListT<String>());
		grafoDePrueba.addVertex(6 , new ArrayListT<String>());
		grafoDePrueba.addVertex(7 , new ArrayListT<String>());
		
		grafoDePrueba.addEdge(0, 1, new InformacionArcoServicios(5.0, 0, 0, 2));
		grafoDePrueba.addEdge(0, 4, new InformacionArcoServicios(9.0, 0, 0, 2));
		grafoDePrueba.addEdge(0, 7, new InformacionArcoServicios(8.0, 0, 0, 2));
		grafoDePrueba.addEdge(1, 2, new InformacionArcoServicios(12.0, 0, 0, 2));
		grafoDePrueba.addEdge(1, 3, new InformacionArcoServicios(15, 0, 0, 2));
		grafoDePrueba.addEdge(1,7, new InformacionArcoServicios(4.0, 0, 0, 2));
		grafoDePrueba.addEdge(2,3, new InformacionArcoServicios(3.0, 0, 0, 2));
		grafoDePrueba.addEdge(2,6, new InformacionArcoServicios(11.0, 0, 0, 2));
		grafoDePrueba.addEdge(3,6, new InformacionArcoServicios(9.0, 0, 0, 2));
		grafoDePrueba.addEdge(4,5, new InformacionArcoServicios(4.0, 0, 0, 2));
		grafoDePrueba.addEdge(4,6, new InformacionArcoServicios(20.0, 0, 0, 2));
		grafoDePrueba.addEdge(4,7, new InformacionArcoServicios(5.0, 0, 0, 2));
		grafoDePrueba.addEdge(5,2, new InformacionArcoServicios(1.0, 0, 0, 2));
		grafoDePrueba.addEdge(5,6, new InformacionArcoServicios(13.0, 0, 0, 2));
		grafoDePrueba.addEdge(7,5, new InformacionArcoServicios(6.0, 0, 0, 2));
		grafoDePrueba.addEdge(7,2, new InformacionArcoServicios(7.0, 0, 0, 2));

	}

	@Test
	public void generalTest()
	{
		Dijkstra<Integer> d = new Dijkstra<Integer>(grafoDePrueba, 0,true, 0);
		assertEquals("25.0", ""+d.distTo(new Vertex<Integer, ArrayListT<String>, Double>(6, new ArrayListT<String>(), 6)));
		assertEquals("9.0", ""+d.distTo(new Vertex<Integer, ArrayListT<String>, Double>(4, new ArrayListT<String>(), 4)));
		assertTrue(d.hasPathTo(new Vertex<Integer, ArrayListT<String>, Double>(7, new ArrayListT<String>(), 7)));
		Dijkstra<Integer> d1 = new Dijkstra<Integer>(grafoDePrueba, 0, false, 0);
		assertEquals("14.0", ""+d1.distTo(new Vertex<Integer, ArrayListT<String>, Double>(7, new ArrayListT<String>(), 7)));
		assertEquals("21.0", ""+d1.distTo(new Vertex<Integer, ArrayListT<String>, Double>(2, new ArrayListT<String>(), 2)));
		
	}

}
