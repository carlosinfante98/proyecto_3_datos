package p3.test;

import static org.junit.Assert.*;

import model.data_structures.ArrayListT;
import model.data_structures.DiGraph;
import model.data_structures.KosarajuSharir;
import model.data_structures.Vertex;
import model.vo.InformacionArcoServicios;

import org.junit.Test;
public class KosarajuSharirTest 
{

	private DiGraph<Integer, ArrayListT<String>, InformacionArcoServicios > grafoDePrueba;
	
	public void setUp2()
	{
		grafoDePrueba.addVertex(0, new ArrayListT<String>());
		grafoDePrueba.addVertex(1, new ArrayListT<String>());
		grafoDePrueba.addVertex(2, new ArrayListT<String>());
		grafoDePrueba.addVertex(3, new ArrayListT<String>());
		grafoDePrueba.addVertex(4, new ArrayListT<String>());
		grafoDePrueba.addVertex(5 , new ArrayListT<String>());
		grafoDePrueba.addVertex(6 , new ArrayListT<String>());
		grafoDePrueba.addVertex(7 , new ArrayListT<String>());
		grafoDePrueba.addVertex(8 , new ArrayListT<String>());
		grafoDePrueba.addVertex(9 , new ArrayListT<String>());
		grafoDePrueba.addVertex(10 , new ArrayListT<String>());
		grafoDePrueba.addVertex(11 , new ArrayListT<String>());
		grafoDePrueba.addVertex(12 , new ArrayListT<String>());

		grafoDePrueba.addEdge(4, 3, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(0, 1, new InformacionArcoServicios(4.7, 0, 0, 2));
		grafoDePrueba.addEdge(0, 5, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(2, 0, new InformacionArcoServicios(4.1, 0, 0, 2));
		grafoDePrueba.addEdge(2, 3, new InformacionArcoServicios(4.9, 0, 0, 2));
		grafoDePrueba.addEdge(3, 2, new InformacionArcoServicios(3.0, 0, 0, 2));
		grafoDePrueba.addEdge(3,5, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(3,5, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(4,2, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(5,4, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(6,0, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(6,4, new InformacionArcoServicios(4.2, 0, 0, 2));
		grafoDePrueba.addEdge(6,8, new InformacionArcoServicios(5.3, 0, 0, 2));
		grafoDePrueba.addEdge(6,9, new InformacionArcoServicios(5.3, 0, 0, 2));
		grafoDePrueba.addEdge(7,6, new InformacionArcoServicios(5.3, 0, 0, 2));
		grafoDePrueba.addEdge(7,9, new InformacionArcoServicios(5.3, 0, 0, 2));
		grafoDePrueba.addEdge(8,6, new InformacionArcoServicios(5.3, 0, 0, 2));
		grafoDePrueba.addEdge(9,11, new InformacionArcoServicios(5.3, 0, 0, 2));
		grafoDePrueba.addEdge(9,10, new InformacionArcoServicios(5.3, 0, 0, 2));
		grafoDePrueba.addEdge(10,12, new InformacionArcoServicios(4.0, 0, 0, 2));
		grafoDePrueba.addEdge(11,4, new InformacionArcoServicios(5.3, 0, 0, 2));
		grafoDePrueba.addEdge(11,12, new InformacionArcoServicios(5.3, 0, 0, 2));
		grafoDePrueba.addEdge(12,9, new InformacionArcoServicios(5.3, 0, 0, 2));

		grafoDePrueba.inicializar();

	}

	@Test
	public void generalTest()
	{

		try
		{
			grafoDePrueba = new DiGraph<Integer, ArrayListT<String>, InformacionArcoServicios>();
			KosarajuSharir k = new KosarajuSharir(grafoDePrueba);
		}
		catch(Exception e)
		{
			assertEquals("El grafo no tiene ningún vértice", e.getMessage());
		}
		try
		{
			setUp2();
			KosarajuSharir k1 = new KosarajuSharir(grafoDePrueba);
			assertEquals(k1.count(), 4);
			assertFalse(k1.stronglyConnected(new Vertex<>(2, new ArrayListT<String>(), 2), new Vertex<>(11, new ArrayListT<String>(), 11)));

		}
		catch(Exception e)
		{
			fail("Fallo fatal.");
		}

	}

}
