package Graph;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import Imports.LinkedPositionalList;
import Imports.Position;
import Imports.PositionalList;


/**
author = "Francois Major"
version = "1.0"
date = "8 December 2020"

Java program for IFT2015/Graphs

Taken and modified from Goodrich, Tamassia, Goldwasser
  Data Structures and Algorithms in Java (c)2014
**/

public class AdjacencyMapGraph<V,E> implements Graph<V,E> {
  private boolean isDirected;
  private PositionalList<Vertex<V>> vertices = new LinkedPositionalList<>();
  private PositionalList<Edge<E>> edges = new LinkedPositionalList<>();

    // Construct an empty graph.
    //   the parameter determines whether this is an undirected or directed graph
    public AdjacencyMapGraph( boolean directed ) { isDirected = directed; }

    //---------------- nested Vertex class ----------------
    // A vertex of an adjacency map graph representation
    private class InnerVertex<V> implements Vertex<V> {
	private V element;
	private Position<Vertex<V>> pos;
	private Map<Vertex<V>, Edge<E>> outgoing, incoming;

	// Construct a new InnerVertex instance storing the given element
	public InnerVertex( V elem, boolean graphIsDirected ) {
	    element = elem;
	    outgoing = new HashMap<>();
	    if( graphIsDirected ) incoming = new HashMap<>();
	    else incoming = outgoing; // if undirected, alias outgoing map
	}

	// Validate that this vertex instance belongs to the given graph
	public boolean validate( Graph<V,E> graph ) {
	    return( AdjacencyMapGraph.this == graph && pos != null );
	}
	// Return the element associated with the vertex
	public V getElement() { return element; }
	// Store the position of this vertex within the graph's vertex list
	public void setPosition( Position<Vertex<V>> p ) { pos = p; }
	// Return the position of this vertex within the graph's vertex list
	public Position<Vertex<V>> getPosition() { return pos; }
	// Return the reference to the underlying map of outgoing edges
	public Map<Vertex<V>, Edge<E>> getOutgoing() { return outgoing; }
	// Return the reference to the underlying map of incoming edges
	public Map<Vertex<V>, Edge<E>> getIncoming() { return incoming; }
    } //------------ end of InnerVertex class ------------
    
    //---------------- nested InnerEdge class ----------------
    // An edge between two vertices
    private class InnerEdge<E> implements Edge<E> {
	private E element;
	private Position<Edge<E>> pos;
	private Vertex<V>[] endpoints;

	// @SuppressWarnings({"unchecked"})
	// Construct InnerEdge instance from u to v, storing the given element
	public InnerEdge( Vertex<V> u, Vertex<V> v, E elem ) {
	    element = elem;
	    endpoints = (Vertex<V>[]) new Vertex[]{u,v}; // array of length 2
	}

	// Return the element associated with the edge
	public E getElement() { return element; }
	// Return a reference to the endpoint array
	public Vertex<V>[] getEndpoints() { return endpoints; }
	// Validate that this edge instance belongs to the given graph
	public boolean validate( Graph<V,E> graph ) {
	    return AdjacencyMapGraph.this == graph && pos != null;
	}
	// Store the position of this edge within the graph's vertex list
	public void setPosition( Position<Edge<E>> p ) { pos = p; }
	// Return the position of this edge within the graph's vertex list
	public Position<Edge<E>> getPosition() { return pos; }
    } //------------ end of InnerEdge class ------------

    // Return the number of vertices of the graph
    public int numVertices() { return vertices.size(); }
    // Return the vertices of the graph as an iterable collection
    public Iterable<Vertex<V>> vertices() { return vertices; }
    // Return the number of edges of the graph
    public int numEdges() { return edges.size(); }
    // Return the edges of the graph as an iterable collection
    public Iterable<Edge<E>> edges() { return edges; }
    // Return the number of edges for which vertex v is the origin
    //   for an undirected graph, this is the same result
    //   returned by inDegree(v)
    // @throws IllegalArgumentException if v is not a valid vertex
    public int outDegree( Vertex<V> v ) throws IllegalArgumentException {
	InnerVertex<V> vert = validate( v );
	return vert.getOutgoing().size();
    }
    // Return an iterable collection of edges for which vertex v is the origin.
    //   for an undirected graph, this is the same result
    //   returned by incomingEdges(v)
    // @throws IllegalArgumentException if v is not a valid vertex
    public Iterable<Edge<E>> outgoingEdges( Vertex<V> v ) throws IllegalArgumentException {
	InnerVertex<V> vert = validate( v );
	return vert.getOutgoing().values(); // edges are the values in the adjacency map
    }
    // Return the number of edges for which vertex v is the destination.
    //   for an undirected graph, this is the same result
    //   returned by outDegree(v).
    // @throws IllegalArgumentException if v is not a valid vertex
    public int inDegree( Vertex<V> v ) throws IllegalArgumentException {
	InnerVertex<V> vert = validate( v );
	return vert.getIncoming().size();
    }
    // Return an iterable collection of edges for which vertex v is the destination.
    //   for an undirected graph, this is the same result
    //   returned by outgoingEdges(v).
    // @throws IllegalArgumentException if v is not a valid vertex
    public Iterable<Edge<E>> incomingEdges( Vertex<V> v ) throws IllegalArgumentException {
	InnerVertex<V> vert = validate(v);
	return vert.getIncoming().values(); // edges are the values in the adjacency map
    }
    // Return the edge from u to v, or null if they are not adjacent
    public Edge<E> getEdge( Vertex<V> u, Vertex<V> v ) throws IllegalArgumentException {
	InnerVertex<V> origin = validate( u );
	return origin.getOutgoing().get( v ); // will be null if no edge from u to v
    }
    // Return the vertices of edge e as an array of length two.
    //   if the graph is directed, the first vertex is the origin,
    //   and the second is the destination. If the graph is undirected,
    //   the order is arbitrary.
    public Vertex<V>[] endVertices( Edge<E> e ) throws IllegalArgumentException {
	InnerEdge<E> edge = validate( e );
	return edge.getEndpoints();
    }
    // Return the vertex that is opposite vertex v on edge e.
    public Vertex<V> opposite( Vertex<V> v, Edge<E> e )
	throws IllegalArgumentException {
	InnerEdge<E> edge = validate( e );
	Vertex<V>[] endpoints = edge.getEndpoints();
	if( endpoints[0] == v ) return endpoints[1];
	else if( endpoints[1] == v ) return endpoints[0];
	else throw new IllegalArgumentException( "v is not incident to this edge" );
    }
    // Insert and return a new vertex with the given element
    public Vertex<V> insertVertex( V element ) {
	InnerVertex<V> v = new InnerVertex<>(element, isDirected);
	v.setPosition( vertices.addLast(v) );
	return v;
    }
    // Insert and return a new edge between vertices u and v, storing given element
    // @throws IllegalArgumentException if u or v are invalid vertices, or if an edge already exists between u and v.
    public Edge<E> insertEdge( Vertex<V> u, Vertex<V> v, E element )
	throws IllegalArgumentException {
	if( getEdge( u, v ) == null ) {
	    InnerEdge<E> e = new InnerEdge<>( u, v, element );
	    e.setPosition( edges.addLast( e ) );
	    InnerVertex<V> origin = validate( u );
	    InnerVertex<V> dest = validate( v );
	    origin.getOutgoing().put( v, e );
	    dest.getIncoming().put( u, e );
	    return e;
	} else throw new IllegalArgumentException( "Edge from u to v exists" );
    }
    // Remove a vertex and all its incident edges from the graph
    public void removeVertex( Vertex<V> v ) throws IllegalArgumentException {
	InnerVertex<V> vert = validate( v );
	// remove all incident edges from the graph
	for( Edge<E> e : vert.getOutgoing().values() )
	    removeEdge( e );
	for( Edge<E> e : vert.getIncoming().values() )
	    removeEdge( e );
	// remove this vertex from the list of vertices
	vertices.remove( vert.getPosition() );
	vert.setPosition( null ); // invalidates the vertex
    }
    // @SuppressWarnings({"unchecked"})
    // Removes an edge from the graph
    public void removeEdge( Edge<E> e ) throws IllegalArgumentException {
	InnerEdge<E> edge = validate( e );
	// remove this edge from vertices' adjacencies
	InnerVertex<V>[] verts = (InnerVertex<V>[]) edge.getEndpoints();
	verts[0].getOutgoing().remove( verts[1] );
	verts[1].getIncoming().remove( verts[0] );
	// remove this edge from the list of edges
	edges.remove( edge.getPosition() );
	edge.setPosition( null ); // invalidates the edge
    }
    // utilities
    // @SuppressWarnings({"unchecked"})
    private InnerVertex<V> validate( Vertex<V> v ) {
	if( !( v instanceof InnerVertex) ) throw new IllegalArgumentException( "Invalid vertex" );
	InnerVertex<V> vert = (InnerVertex<V>) v; // safe cast
	if(! vert.validate(this) ) throw new IllegalArgumentException( "Invalid vertex" );
	return vert;
    }
    // @SuppressWarnings({"unchecked"})
    private InnerEdge<E> validate( Edge<E> e ) {
	if( !( e instanceof InnerEdge ) ) throw new IllegalArgumentException( "Invalid edge" );
	InnerEdge<E> edge = (InnerEdge<E>) e; // safe cast
	if( !edge.validate( this ) ) throw new IllegalArgumentException( "Invalid edge" );
	return edge;
    }
    // traversal methods
    public static <V,E> void DFS( Graph<V,E> g, Vertex<V> u,
				  Set<Vertex<V>> known,
				  Map<Vertex<V>,Edge<E>> forest ) {
	known.add( u );
	for( Edge<E> e : g.outgoingEdges( u ) ) {
	    Vertex<V> v = g.opposite( u, e );
	    if( !known.contains( v ) ) {
		forest.put( v, e );
		DFS( g, v, known, forest );
	    }
	}
    }
    public static <V,E> void BFS( Graph<V,E> g,
				  Vertex<V> s,
				  Set<Vertex<V>> known,
				  Map<Vertex<V>,Edge<E>> forest ) {
	PositionalList<Vertex<V>> level = new LinkedPositionalList<>();
	known.add( s );
	level.addLast( s );
	while( !level.isEmpty() ) {
	    PositionalList<Vertex<V>> nextLevel = new LinkedPositionalList<>();
	    for( Vertex<V> u : level )
		for( Edge<E> e : g.outgoingEdges( u ) ) {
		    Vertex<V> v = g.opposite( u, e );
		    if( !known.contains( v ) ) {
			known.add( v );
			forest.put( v, e );
			nextLevel.addLast( v );
		    }
		}
	    level = nextLevel;
	}
    }
    public static <V,E> PositionalList<Edge<E>> constructPath( Graph<V,E> g,
							       Vertex<V> u,
							       Vertex<V> v,
							       Map<Vertex<V>,Edge<E>> forest ) {
	PositionalList<Edge<E>> path = new LinkedPositionalList<>();
	if( forest.get( v ) != null ) {
	    Vertex<V> walk = v;
	    while( walk != u ) {
		Edge<E> edge = forest.get( walk );
		path.addFirst( edge );
		walk = g.opposite( walk, edge );
	    }
	}
	return path;
    }
    // Return a string representation of the graph
    //   used for debugging
    public String toString() {
	StringBuilder sb = new StringBuilder();
	if( !vertices.isEmpty() )
	    for( Vertex<V> v : vertices ) {
		sb.append( "Vertex " + v.getElement() + "\n" );
		if( isDirected )
		    sb.append( " [outgoing]" );
		sb.append( " " + outDegree(v) + " adjacencies:" );
		for( Edge<E> e: outgoingEdges(v) )
		    sb.append( String.format( " (%s, %s)", opposite( v, e ).getElement(), e.getElement() ) );
		sb.append( "\n" );
		if( isDirected ) {
		    sb.append( " [incoming]" );
		    sb.append( " " + inDegree(v) + " adjacencies:" );
		    for( Edge<E> e: incomingEdges( v ) )
			sb.append( String.format(" (%s, %s)", opposite( v, e ).getElement(), e.getElement() ) );
		    sb.append( "\n" );
		}
	    }
	else sb.append( "{}" );
	return sb.toString();
    }
}
