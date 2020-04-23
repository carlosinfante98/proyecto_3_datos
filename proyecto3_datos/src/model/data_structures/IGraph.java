package model.data_structures;

public interface IGraph<K,V,A>
{
	public int V();
	
	public int E();
	
	public void addVertex( K idVertex, V infoVertex);
	
	public void addEdge(K idVertexIni, K idVertexFin, A infoArc );
	
	public V getInfoVertex(K idVertex);
	
	public void setInfoVertex(K idVertex, V infoVertex);
	
	public A getInfoArc(K idVertexIni, K idVertexFin);
	
	public void setInfoArc(K idVertexIni, K idVertexFin, A infoArc);
	
	public Iterable<K> adj(K idVertex);

}
