package model.data_structures;

/**
 *  The {@code DepthFirstOrder} class represents a data type for 
 *  determining depth-first search ordering of the vertices in a digraph
 *  or edge-weighted digraph, including preorder, postorder, and reverse postorder.
 *  <p>
 *  This implementation uses depth-first search.
 *  The constructor takes time proportional to <em>V</em> + <em>E</em>
 *  (in the worst case),
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the <em>preorder</em>, <em>postorder</em>, and <em>reverse postorder</em>
 *  operation takes take time proportional to <em>V</em>.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/42digraph">Section 4.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class DepthFirstOrder
{
	private boolean[] marked;          // marked[v] = has v been marked in dfs?
	private int[] pre;                 // pre[v]    = preorder  number of v
	private int[] post;                // post[v]   = postorder number of v
	private Queue<Integer> preorder;   // vertices in preorder
	private Queue<Integer> postorder;  // vertices in postorder
	private int preCounter;            // counter or preorder numbering
	private int postCounter;           // counter for postorder numbering

	/**
	 * Determines a depth-first order for the digraph {@code G}.
	 * @param G the digraph
	 */
	@SuppressWarnings("rawtypes")
	public DepthFirstOrder(DiGraph G) 
	{
		pre    = new int[G.V()];
		post   = new int[G.V()];
		postorder = new Queue<Integer>();
		preorder  = new Queue<Integer>();
		marked    = new boolean[G.V()];
		for (int v = 0; v < G.V(); v++)
			if (!marked[v]) dfs(G, v);

		assert check();
	}

	// run DFS in digraph G from vertex v and compute preorder/postorder
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void dfs(DiGraph G, int v) 
	{
		marked[v] = true;
		pre[v] = preCounter++;
		preorder.enqueue(v);
        IteratorList ite = (IteratorList) G.adj(v).iterator();
        while(ite.hasNext())
        {
        	int w = (int) ite.next();
			if (!marked[w]) {
				dfs(G, w);
			}
		}
		postorder.enqueue(v);
		post[v] = postCounter++;
	}

	/**
	 * Returns the preorder number of vertex {@code v}.
	 * @param  v the vertex
	 * @return the preorder number of vertex {@code v}
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public int pre(int v) 
	{
		validateVertex(v);
		return pre[v];
	}

	/**
	 * Returns the postorder number of vertex {@code v}.
	 * @param  v the vertex
	 * @return the postorder number of vertex {@code v}
	 * @throws IllegalArgumentException unless {@code 0 <= v < V}
	 */
	public int post(int v)
	{
		validateVertex(v);
		return post[v];
	}

	/**
	 * Returns the vertices in postorder.
	 * @return the vertices in postorder, as an iterable of vertices
	 */
	public Iterable<Integer> post() 
	{
		return postorder;
	}

	/**
	 * Returns the vertices in preorder.
	 * @return the vertices in preorder, as an iterable of vertices
	 */
	public Iterable<Integer> pre() 
	{
		return preorder;
	}

	/**
	 * Returns the vertices in reverse postorder.
	 * @return the vertices in reverse postorder, as an iterable of vertices
	 */
	public Iterable<Integer> reversePost() 
	{
		Stack<Integer> reverse = new Stack<Integer>();
		for (int v : postorder)
			reverse.push(v);
		return reverse;
	}


	// check that pre() and post() are consistent with pre(v) and post(v)
	private boolean check() 
	{

		// check that post(v) is consistent with post()
		int r = 0;
		for (int v : post())
		{
			if (post(v) != r)
			{
				System.out.println("post(v) and post() inconsistent");
				return false;
			}
			r++;
		}

		// check that pre(v) is consistent with pre()
		r = 0;
		for (int v : pre()) 
		{
			if (pre(v) != r) 
			{
				System.out.println("pre(v) and pre() inconsistent");
				return false;
			}
			r++;
		}

		return true;
	}

	// throw an IllegalArgumentException unless {@code 0 <= v < V}
	private void validateVertex(int v) 
	{
		int V = marked.length;
		if (v < 0 || v >= V)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
	}
}
