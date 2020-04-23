package model.data_structures;

import java.util.Iterator;

import model.vo.InformacionArcoServicios;
import model.vo.InformacionVertice;

/**
 *  The {@code KosarajuSharirSCC} class represents a data type for 
 *  determining the strong components in a digraph.
 *  The <em>id</em> operation determines in which strong component
 *  a given vertex lies; the <em>areStronglyConnected</em> operation
 *  determines whether two vertices are in the same strong component;
 *  and the <em>count</em> operation determines the number of strong
 *  components.

 *  The <em>component identifier</em> of a component is one of the
 *  vertices in the strong component: two vertices have the same component
 *  identifier if and only if they are in the same strong component.

 *  <p>
 *  This implementation uses the Kosaraju-Sharir algorithm.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <em>id</em>, <em>count</em>, and <em>areStronglyConnected</em>
 *  operations take constant time.
 *  For alternate implementations of the same API, see
 *  {@link TarjanSCC} and {@link GabowSCC}.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class KosarajuSharir
{
	private boolean[] marked;     // marked[v] = has vertex v been visited?
	private ArrayListT<Integer>id;             // id[v] = id of strong component containing v
	private int indicatorComponent;            // number of strongly-connected components
	
	private ArrayListNotComparable<ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>>> laMadreDeLasListas ;
	
	

	/**
	 * Computes the strong components of the digraph {@code G}.
	 * @param G the digraph
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
 	public KosarajuSharir(DiGraph G) throws Exception 
	{
		laMadreDeLasListas = new ArrayListNotComparable<ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>>>();
		indicatorComponent = 0;
		// compute reverse postorder of reverse graph
		int tamanio = G.V();
		if(tamanio != 0)
		{
			DiGraph grafoReverso = G.reverse();
			grafoReverso.inicializar();

			// run DFS on reverse G, using reverse postorder to guide calculation
			marked = new boolean[tamanio];
			id = new ArrayListT<Integer>(tamanio);
			grafoReverso.depthFirstSearch(grafoReverso.getSourceVertex().getId());
			Iterator<Vertex> iter = grafoReverso.reversePost().iterator();
			grafoReverso.inicializar();
			grafoReverso.inicializarLista();
			
			while (iter.hasNext())
			{
				Vertex v = iter.next();
				if (!marked[v.getIdNum()]) 
				{
					id = G.depthFirstSearch(v.getId(),indicatorComponent);
					marked = G.getMarked();
					laMadreDeLasListas.add(G.getListOfVertex());
					G.inicializarLista();
					indicatorComponent++;
				}
			}
			int i = 0;
			while(i < G.getMarked().length)
			{
				Vertex v = (Vertex) G.getListOfVertices().get(i);
				if(!G.getMarked()[i])
				{
					G.depthFirstSearch(v.getId(), indicatorComponent);
					laMadreDeLasListas.add(G.getListOfVertex());
					G.inicializarLista();
					indicatorComponent++;
				}
				i++;
			}
		}
		else
		{
			throw new Exception("El grafo no tiene ningún vértice");
		}

		// check that id[] gives strong components
		//        assert check(G);
	}
	/**
	 * Returns the number of strong components.
	 * @return the number of strong components
	 */
	public int count()
	{
		return indicatorComponent;
	}

	/**
	 * Are vertices {@code v} and {@code w} in the same strong component?
	 * @param  v one vertex
	 * @param  w the other vertex
	 * @return {@code true} if vertices {@code v} and {@code w} are in the same
	 *         strong component, and {@code false} otherwise
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 * @throws IllegalArgumentException unless {@code 0 <= w < V}
	 */
	public boolean stronglyConnected(Vertex v, Vertex w)
	{
		validateVertex(v);
		validateVertex(w);
		return id.get(v.getIdNum()) == id.get(w.getIdNum());
	}

	/**
	 * Returns the component id of the strong component containing vertex {@code v}.
	 * @param  v the vertex
	 * @return the component id of the strong component containing vertex {@code v}
	 * @throws IllegalArgumentException unless {@code 0 <= s < V}
	 */
	public int id(Vertex v) 
	{
		validateVertex(v);
		return id.get(v.getIdNum());
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(Vertex v)
	{
		int V = marked.length;
		if (v.getIdNum() < 0 || v.getIdNum() >= V)
			throw new IllegalArgumentException("vertex " + v.getId() + " is not between 0 and " + (V-1));
	}
	
	public int numComponents()
	{
		return indicatorComponent + 1;
	}
	
	public ArrayListT<Integer> getComponents()
	{
		return id;
	}
	public ArrayListNotComparable<ArrayListNotComparable<Vertex<String, InformacionVertice, InformacionArcoServicios>>> getMotherOfLists()
	{
		return laMadreDeLasListas;
	}
}
