package model.data_structures;

public class DiGraph<K extends Comparable<K>, V, A>
{
	/**
	 * Los vertices del grafo.
	 */
	private SeparateChainingHashST<K, Vertex<K, V, A>> vertices;	

	/**
	 * El numero de arcos que hay en el grafo.
	 */
	private int numEdges;

	/**
	 * Numero de vertices en el grafo.
	 */
	private int numVertices;

	/**
	 * Numero de referencia del vertice.
	 */
	private int referenceId;

	private int  edgeToDfs[];
	private boolean  markDfs[];

	private Queue<Vertex<K,V,A>> preOrder;
	private Queue<Vertex<K,V,A>> postOrder;

	private ArrayListT<Integer> idComponent;

	private Vertex<K,V,A> sourceVertex;

	private ArrayListNotComparable<Vertex<K,V,A>> listaVerticesConComponente;


	/**
	 * Lista de vertices que estan ubicados por su numero de referencia.
	 */
	private ArrayListNotComparable<Vertex<K, V, A>> listaDeVertices;

	/**
	 * El constructor del grafo
	 */
	public DiGraph()
	{
		vertices = new SeparateChainingHashST<K, Vertex<K, V, A>>();
		numEdges = 0;
		numVertices = 0;
		referenceId = 0;
		listaDeVertices = new ArrayListNotComparable<Vertex<K, V, A>>();
		listaVerticesConComponente = new ArrayListNotComparable<Vertex<K,V,A>>();
	}

	public void inicializar()
	{
		edgeToDfs = new int[vertices.size()];
		markDfs = new boolean[vertices.size()];
		preOrder = new Queue<Vertex<K,V,A>>();
		postOrder = new Queue<Vertex<K,V,A>>();
		idComponent = new ArrayListT<Integer>(vertices.size());	
	}

	public void inicializarLista()
	{
		listaVerticesConComponente = new ArrayListNotComparable<Vertex<K,V,A>>();
	}

	public Vertex<K, V, A> getSourceVertex()
	{
		return sourceVertex;
	}

	/**
	 * Metodo que da el numero de vertices
	 * @return El numero de vertices.
	 */
	public int V() 
	{
		return vertices.size();
	}

	public ArrayListNotComparable<Vertex<K, V, A>> getListOfVertices()
	{
		return listaDeVertices;
	}

	/**
	 * Metodo que da el numero de edges.
	 * @return El numero de edges.
	 */
	public int E() 
	{
		return numEdges;
	}

	/**
	 * Metodo que anade un vertice.
	 * @param idVertex El id del vertice
	 * @param infoVertex La informacion del vertice
	 */
	public void addVertex(K idVertex, V infoVertex)
	{
		Vertex<K,V,A> v = new Vertex<K,V,A>(idVertex, infoVertex, referenceId);
		vertices.put(idVertex, v);
		referenceId++;
		numVertices++;
		listaDeVertices.add(v);
		if(listaDeVertices.size()== 1)
		{
			sourceVertex = listaDeVertices.get(0);
		}
	}

	/**
	 * Metodo que anade un vertice con uno por parametro.
	 * @param idVertex El id del vertice
	 * @param infoVertex La informacion del vertice
	 */
	public void addVertex(Vertex<K,V,A> v)
	{
		vertices.put(v.getId(), v);
		numVertices++;
	}

	/**
	 * Metodo que anade un nuevo Arco en el grafo en sus respectivos vertices.
	 * @param idVertexIni: El id del vertice de inicio.
	 * @param idVertexFin :El id del vertice de llegada.
	 * @param infoEdge: La info del arco.
	 */
	public boolean addEdge(K idVertexIni, K idVertexFin, A infoArc )
	{
		Vertex<K,V,A> start = vertices.get(idVertexIni);
		Vertex<K,V,A> end = vertices.get(idVertexFin);
		boolean didIt = false;

		if(getInfoEdge(idVertexIni, idVertexFin) != null)
		{
			setInfoEdge(idVertexIni, idVertexFin, infoArc);
		}
		else
		{
			if(!idVertexIni.equals(idVertexFin))
			{
				if(start != null && end != null)
				{
					Edge<K,A> edge = new Edge<K,A>(idVertexIni, idVertexFin, infoArc);
					start.addOutEdge(edge);
					end.addInEdge(edge);
					numEdges++;
					didIt = true;
				}							
			}
		}
		return didIt;
	}

	/**
	 * Metodo para tener una lista de todos los infovertices.
	 * @return Lista de todos los infovertices.
	 */
	public ArrayListNotComparable<V> vertices() 
	{
		ArrayListNotComparable<V> lista = new ArrayListNotComparable<V>();

		for (K id : vertices.keys()) 
		{
			lista.add(vertices.get(id).getValue());
		}
		return lista;
	}

	/**
	 * Metodo que retorna un iterable sobre los identificadores de los
	 * vertices adyacentes al del parametro.
	 * @param idVertex: El id del vertice a dar sus adjacentes.
	 * @return El iterable sobre los adjacentes al vertice.
	 */
	public Iterable<K> adj(K idVertex)
	{
		DoubleLinkedList<K> iter = new DoubleLinkedList<K>();
		Vertex<K,V,A> v = vertices.get(idVertex);
		if(v != null)
		{
			for(int i = 0; i < v.getOutEdges().size(); i++)
			{
				iter.add(v.getOutEdges().get(i).getIdVertEnd());
			}
		}
		return iter;
	}

	/**
	 * Metodo para tener una lista de todos los edges.
	 * @return Lista de todos los edges.
	 */
	public ArrayListNotComparable<Edge<K,A>> edges() 
	{
		ArrayListNotComparable<Edge<K,A>> lista = new ArrayListNotComparable<Edge<K,A>>();

		for (K id : vertices.keys()) 
		{
			ArrayListNotComparable<Edge<K,A>> ed = vertices.get(id).getOutEdges();
			for (int i = 0; i < ed.size(); i++) 
			{				
				lista.add(ed.get(i));
			}
		}
		return lista;
	}

	/**
	 * Metodo para tener una lista de todos los edges que son adyacentes a un vertice
	 * @return Lista de todos los edges adyacentes a un vertice.
	 */
	public ArrayListNotComparable<Edge<K,A>> adjEdges(K key) 
	{
		ArrayListNotComparable<Edge<K,A>> lista = new ArrayListNotComparable<Edge<K,A>>();

		ArrayListNotComparable<Edge<K,A>> ed = vertices.get(key).getOutEdges();
		for (int i = 0; i < ed.size(); i++) 
		{				
			lista.add(ed.get(i));
		}
		return lista;
	}

	/**
	 * Metodo que da la informacion de un vertice.
	 * @param idVertex: El id del vertice
	 * @return La info del vertice.
	 */
	public V getInfoVertex(K idVertex)
	{
		return vertices.get(idVertex).getValue();
	}

	/**
	 * Metodo que da la informacion de un Edge.
	 * @param idVertexIni: Id del vertice inicial.
	 * @param idVertexFin: Id del vertice final.
	 * @return La info del Edge.
	 */
	public A getInfoEdge(K idVertexIni, K idVertexFin)
	{
		Vertex<K,V,A> v = vertices.get(idVertexIni);
		A e = null;
		if(v != null)
		{			
			Edge<K,A> arc = v.getEdge(idVertexFin);
			if(arc != null)
			{
				e = arc.getElement();
			}
		}
		return e;
	}

	/**
	 * Metodo que da la informacion de un Edge.
	 * @param idVertexIni: Id del vertice inicial.
	 * @param idVertexFin: Id del vertice final.
	 * @return La info del Edge.
	 */
	public Edge<K,A> getEdge(K idVertexIni, K idVertexFin)
	{
		Vertex<K,V,A> v = vertices.get(idVertexIni);
		Edge<K,A> e = null;
		if(v != null)
		{			
			Edge<K,A> arc = v.getEdge(idVertexFin);
			if(arc != null)
			{
				e = arc;
			}
		}
		return e;
	}


	/**
	 * Edita la informacion de un vertice.
	 * @param idVertex: El Id del vertice a editar.
	 * @param infoVert: La nueva info.
	 * @return True si se logro editar, false de lo contrario.
	 */
	public boolean setInfoVertex(K idVertex, V infoVert)
	{
		Vertex<K,V,A> v = vertices.get(idVertex);
		boolean didIt = false;
		if(v != null)
		{
			v.setValue(infoVert);
			didIt = true;
		}
		return didIt;
	}

	/**
	 * Edita la informacion de un Edge.
	 * @param idVertexIni: Id del vertice de salida del edge.
	 * @param idVertexFin: Id del vertice de llegada del edge.
	 * @param infoArc: La info nueva del edge.
	 * @return True si se logro editar, false de lo contrario.
	 */
	public boolean setInfoEdge(K idVertexIni, K idVertexFin, A infoArc)
	{
		Vertex<K,V,A> v = vertices.get(idVertexIni);
		boolean didIt = false;
		if(v != null)
		{			
			Edge<K,A> arc = v.getEdge(idVertexFin);
			if(arc != null)
			{
				arc.setElement(infoArc);
				didIt = true;
			}
		}
		return didIt;
	}

	/**
	 * Returns the reverse of the digraph.
	 *
	 * @return the reverse of the digraph
	 */
	public DiGraph<K,V,A> reverse()
	{
		DiGraph<K,V,A> reverse = new DiGraph<K,V,A>();
		ArrayListNotComparable<Vertex<K,V,A>> vertices = getListOfVertices();
		for (int i = 0; i < listaDeVertices.size(); i++)
		{
			reverse.addVertex(vertices.get(i).getId(), vertices.get(i).getValue());
		}

		for (int i = 0; i < V(); i++)
		{
			Vertex<K,V,A> actual = getListOfVertices().get(i);
			for (int j = 0; j < actual.getOutEdges().size(); j++)
			{
				Edge<K,A> edge = actual.getOutEdges().get(j);
				reverse.addEdge(edge.getIdVertEnd(), edge.getIdVertStart(), edge.getElement());
			}
		}

		return reverse;
	}

	public ArrayListT<Integer> depthFirstSearch(K key, int count)
	{
		//vectores iniciales
		Vertex<K,V,A> v = vertices.get(key);
		v.setIdComponent(count);
		listaDeVertices.add(v);
		listaVerticesConComponente.add(v);
		markDfs[v.getIdNum()] = true;
		idComponent.insert(v.getIdNum(), count);
		ArrayListNotComparable<Vertex<K,V,A>> lista = getAdj(key);
		preOrder.enqueue(v);
		for (int i=0 ; i < lista.size(); i++)
		{
			Vertex<K,V,A> w = lista.get(i);
			w.setIdComponent(count);
			if (! markDfs[w.getIdNum()])
			{
				edgeToDfs[w.getIdNum()] = v.getIdNum();
				depthFirstSearch(w.getId(),count);
			}
		}
		postOrder.enqueue(v);			

		return idComponent;
	}

	public int[] depthFirstSearch(K key)
	{
		//vectores iniciales
		Vertex<K,V,A> v = vertices.get(key);
		markDfs[v.getIdNum()] = true;
		ArrayListNotComparable<Vertex<K,V,A>> lista =  getAdj(key);
		preOrder.enqueue(v);
		for (int i=0 ; i < lista.size(); i++)
		{
			Vertex<K,V,A> w = lista.get(i);
			if (! markDfs[w.getIdNum()])
			{
				edgeToDfs[w.getIdNum()] = v.getIdNum();
				depthFirstSearch(w.getId());
			}
		}
		postOrder.enqueue(v);

		return edgeToDfs;
	}

	/**
	 * Returns the vertices in reverse postorder.
	 * @return the vertices in reverse postorder, as an iterable of vertices
	 */
	public Iterable<Vertex<K,V,A>> reversePost() 
	{
		Stack<Vertex<K,V,A>> reverse = new Stack<Vertex<K,V,A>>();
		for (Vertex<K,V,A> v : postOrder)
			reverse.push(v);
		return reverse;
	}

	private K findKeyNumber(int numero )
	{
		int centro,inf=0,sup=listaDeVertices.size()-1;
		while(inf<=sup)
		{
			centro=(sup+inf)/2;
			Vertex<K,V,A> v = listaDeVertices.get(centro);
			if(v.getIdNum()==numero) return v.getId();
			else if(numero < v.getIdNum() )
			{
				sup=centro-1;
			}
			else 
			{
				inf=centro+1;
			}
		}
		return null;
	}


	public int[][] breadthFirstSearch(K key)
	{
		int retorno[][]= new int[2][V()];
		int distTo[] = new int[V()];
		int edgeTo[] = new int[V()];
		boolean mark[] = new boolean[V()];
		//Obtenemos el vertice correspondiente al key dado.
		Vertex<K,V,A> v = vertices.get(key);
		//Cola con los identificadores numericos de los vertices
		Queue<Integer> q = new Queue<Integer>();

		for (int i = 0; i <V(); i++)
			distTo[i] = Integer.MAX_VALUE;

		distTo[v.getIdNum()] = 0;
		edgeTo[v.getIdNum()]= -1;
		mark[v.getIdNum()] = true;

		q.enqueue(v.getIdNum());

		while (!q.isEmpty()) 
		{
			int n = q.dequeue();
			ArrayListNotComparable<Vertex<K,V,A>> lista =  getAdj(findKeyNumber(n));
			for (int i=0; i < lista.size(); i++)
			{
				if (!mark[lista.get(i).getIdNum()])
				{
					int numb = lista.get(i).getIdNum();
					edgeTo[numb] = n;
					distTo[numb] = distTo[n] + 1;
					mark[lista.get(i).getIdNum()] =  true;
					q.enqueue(numb);
				}
			}
		}
		retorno[0]= distTo;
		retorno[1]= edgeTo;

		return retorno;
	}

	public ArrayListNotComparable<Vertex<K,V,A>> getAdj(K idVertex)
	{
		ArrayListNotComparable<Vertex<K,V,A>> iter = new ArrayListNotComparable<Vertex<K,V,A>>();
		Vertex<K,V,A> v = vertices.get(idVertex);
		if(v != null)
		{
			for(int i = 0; i < v.getOutEdges().size(); i++)
			{
				iter.add(vertices.get(v.getOutEdges().get(i).getIdVertEnd()));
			}
		}
		return iter;
	}
	
	public boolean[] getMarked()
	{
		return markDfs;
	}
	
	public Vertex<K,V,A> getVertex(K k)
	{
		return vertices.get(k);
	}
	
	public Vertex<K,V,A> getVertex(int id)
	{
		Vertex<K,V,A> v = null;
		for (int i = 0; i < listaDeVertices.size(); i++) 
		{
			if(listaDeVertices.get(i).getIdNum() == id)
			{
				v = listaDeVertices.get(i);
			}
		}
		return v;
	}
	
	public Queue<Vertex<K,V,A>> getPreOrder()
	{
		return preOrder;
	}
	
	public int[] getEdgeTo()
	{
		return edgeToDfs;
	}

	public ArrayListNotComparable<Vertex<K,V,A>> getListOfVertex()
	{
		return listaVerticesConComponente;
	}

	public void generarCaminos(Vertex<K, V,A> in, Vertex<K, V,A> out, ArrayListNotComparable<Vertex<K, V,A>> t, ArrayListNotComparable<ArrayListNotComparable<Vertex<K, V,A>>> caminos)
	{
		ArrayListNotComparable<Vertex<K, V,A>> adyacentes = getAdj(t.get(t.size()-1).getId());
		boolean esta = false;
		for (int i = 0 ; i < adyacentes.size(); i++ ) 
		{
			Vertex<K,V,A> vertex = adyacentes.get(i);
			for (int j=0 ; j < t.size() && !esta; j++ )
			{ 
				if(t.get(j).getId().compareTo(vertex.getId()) == 0)
				{ 
					esta = true;
				} 
			}

			if(!esta)
			{
				if(vertex.getId().compareTo(out.getId())== 0)
				{
					t.add(vertex);
					mostrarCamino(t, caminos);
					t.remove(t.size()-1);
					break;
				}
			}
		}
		for (int i = 0 ; i < adyacentes.size(); i++ ) 
		{
			boolean entra = false;
			if(esta || adyacentes.get(i).getId().compareTo(out.getId())== 0)
			{
				entra = true;
			}
			if(!entra)
			{
				t.add(adyacentes.get(i));
				generarCaminos(in, out, t, caminos);
				t.remove(t.size()-1);				
			}
		}
	}

	public ArrayListNotComparable<ArrayListNotComparable<Vertex<K, V,A>>>  encontrarCaminos(K in, K out)
	{
		ArrayListNotComparable<Vertex<K,V,A>> vertices = new ArrayListNotComparable<Vertex<K,V,A>>();
		ArrayListNotComparable<ArrayListNotComparable<Vertex<K, V,A>>> caminos = new ArrayListNotComparable<ArrayListNotComparable<Vertex<K, V,A>>>();
		Vertex<K,V,A> verticeInicial = getVertex(in);
		Vertex<K,V,A> verticeFinal = getVertex(out);
		vertices.add(verticeInicial);
		generarCaminos(verticeInicial, verticeFinal, vertices, caminos);
		return caminos;
	}

	public void mostrarCamino(ArrayListNotComparable<Vertex<K, V,A>> visitado, ArrayListNotComparable<ArrayListNotComparable<Vertex<K, V,A>>> caminos)
	{
		ArrayListNotComparable<Vertex<K, V,A>> arreglo = new ArrayListNotComparable<Vertex<K,V,A>>();
		for (int j= 0; j < visitado.size() ; j++) 
		{
			arreglo.add(visitado.get(j));
		}
		caminos.add(arreglo);
	}


}
