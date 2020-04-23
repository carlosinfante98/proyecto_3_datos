package model.data_structures;

import model.vo.InformacionArcoServicios;

public class Dijkstra<K extends Comparable<K>> 
{
	private double[] distTo;          // distTo[v] = distance  of shortest s->v path
	private Edge<K, InformacionArcoServicios>[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
	private IndexMinPQ<Double> pq;    // priority queue of vertices
	private IndexMaxPQ<Double> pqMax;

	/**
	 * Computes a shortest-paths tree from the source vertex {@code s} to every other
	 * vertex in the edge-weighted digraph {@code G}.
	 *
	 * @param  G the edge-weighted digraph
	 * @param  s the source vertex
	 * @throws IllegalArgumentException if an edge weight is negative
	 * @throws IllegalArgumentException unless {@code 0 <= s < V}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Dijkstra(DiGraph G, K s, boolean orientacion, int tipo) 
	{
//		for (int i = 0; i < G.edges().size(); i++) 
//		{
//			Edge<K, InformacionArcoServicios> e = (Edge<K,  InformacionArcoServicios>) G.edges().get(i);
//			if (e.getEspecifiedElement(tipo) < 0)
//				throw new IllegalArgumentException("edge " + e + " has negative weight");
//		}

		distTo = new double[G.V()];
		edgeTo = new Edge[G.V()];

		Vertex v = G.getVertex(s);

		validateVertex(v);
		if(orientacion)
		{
			for (int i = 0; i < G.V(); i++)
				distTo[i] = Double.POSITIVE_INFINITY;
			distTo[v.getIdNum()] = 0.0;

			// relax vertices in order of distance from s
			pq = new IndexMinPQ<Double>(G.V());
			pq.insert(v.getIdNum(), distTo[v.getIdNum()]);
			while (!pq.isEmpty())
			{
				int indice = pq.delMin();
				v = G.getVertex(indice);
				ArrayListNotComparable<Vertex> lista = G.getAdj(v.getId());
				for (int i = 0; i< lista.size(); i++)
				{
					Vertex ve = v;
					Vertex vw = lista.get(i);
					
					InformacionArcoServicios weight = (InformacionArcoServicios) G.getInfoEdge(ve.getId(), vw.getId());
					Edge e = G.getEdge(ve.getId(), vw.getId());
					double peso = weight.seleccionarCriterio(tipo);

					if(distTo[vw.getIdNum()] > distTo[ve.getIdNum()] + peso)
					{
						distTo[vw.getIdNum()] = distTo[ve.getIdNum()] + peso;
						edgeTo[vw.getIdNum()] = e;
						if(pq.contains(vw.getIdNum()))
							pq.decreaseKey(vw.getIdNum(), distTo[vw.getIdNum()]);
						else
							pq.insert(vw.getIdNum(), distTo[vw.getIdNum()]);
					}
				}
			}
		}
		else
		{
			for (int i = 0; i < G.V(); i++)
				distTo[i] = 0;
			distTo[v.getIdNum()] = 0;

			// relax vertices in order of distance from s
			pqMax = new IndexMaxPQ<Double>(G.V());
			pqMax.insert(v.getIdNum(), distTo[v.getIdNum()]);
			while (!pqMax.isEmpty())
			{
				int indice = pqMax.delMax();
				v = G.getVertex(indice);
				ArrayListNotComparable<Vertex> lista = G.getAdj(v.getId());
				for (int i = 0; i< lista.size(); i++)
				{
					Vertex ve = v;
					Vertex vw = lista.get(i);
					
					InformacionArcoServicios weight = (InformacionArcoServicios) G.getInfoEdge(ve.getId(), vw.getId());
					Edge e = G.getEdge(ve.getId(), vw.getId());
					double peso = weight.seleccionarCriterio(tipo);

					if(distTo[vw.getIdNum()] < distTo[ve.getIdNum()] + peso)
					{
						distTo[vw.getIdNum()] = distTo[ve.getIdNum()] + peso;
						edgeTo[vw.getIdNum()] = e;
						if(pqMax.contains(vw.getIdNum()))
							pqMax.increaseKey(vw.getIdNum(), distTo[vw.getIdNum()]);
						else
							pqMax.insert(vw.getIdNum(), distTo[vw.getIdNum()]);
					}
				}
			}
		}

	}

	/**
	 * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
	 * @param  v the destination vertex
	 * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
	 *         {@code Double.POSITIVE_INFINITY} if no such path
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	@SuppressWarnings("rawtypes")
	public double distTo(Vertex v) 
	{
		validateVertex(v);
		return distTo[v.getIdNum()];
	}

	/**
	 * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
	 *
	 * @param  v the destination vertex
	 * @return {@code true} if there is a path from the source vertex
	 *         {@code s} to vertex {@code v}; {@code false} otherwise
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	@SuppressWarnings("rawtypes")
	public boolean hasPathTo(Vertex v) 
	{
		validateVertex(v);
		return distTo[v.getIdNum()] < Double.POSITIVE_INFINITY;
	}

	/**
	 * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
	 *
	 * @param  v the destination vertex
	 * @return a shortest path from the source vertex {@code s} to vertex {@code v}
	 *         as an iterable of edges, and {@code null} if no such path
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Iterable<Edge<K,InformacionArcoServicios>> pathTo(DiGraph G, Vertex v)
	{
		validateVertex(v);
		if (!hasPathTo(v)) return null;
		Stack path = new Stack();
		for (Edge<K,InformacionArcoServicios> e = edgeTo[v.getIdNum()]; e != null; e = edgeTo[G.getVertex(e.getIdVertStart()).getIdNum()]) 
		{
			path.push(e);
		}
		return path;
	}
	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	@SuppressWarnings("rawtypes")
	private void validateVertex(Vertex v) 
	{
		int V = distTo.length;
		if (v.getIdNum() < 0 || v.getIdNum() >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}

}
