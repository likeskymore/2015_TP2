package Graph;

/**
author = "Francois Major"
version = "1.0"
date = "8 December 2020"

Java program for IFT2015/Graphes

Taken and modified from Goodrich, Tamassia, Goldwasser
  Data Structures and Algorithms in Java (c)2014
**/

public interface Graph<V,E> {

    // Return the number of vertices of the graph
    int numVertices();

    // Return the number of edges of the graph
    int numEdges();

    // Return the vertices of the graph as an iterable collection
    Iterable<Vertex<V>> vertices();

    // Return the edges of the graph as an iterable collection
    Iterable<Edge<E>> edges();

    // Return the number of edges leaving vertex v.
    //   For an undirected graph, this is the same result
    //   returned by inDegree
    // @throws IllegalArgumentException if v is not a valid vertex
    int outDegree( Vertex<V> v ) throws IllegalArgumentException;

    // Return the number of edges for which vertex v is the destination.
    //   For an undirected graph, this is the same result
    //   returned by outDegree
    // @throws IllegalArgumentException if v is not a valid vertex
    int inDegree( Vertex<V> v ) throws IllegalArgumentException;

    // Return an iterable collection of edges for which vertex v is the origin.
    //   For an undirected graph, this is the same result
    //   returned by incomingEdges.
    // @throws IllegalArgumentException if v is not a valid vertex
    Iterable<Edge<E>> outgoingEdges( Vertex<V> v ) throws IllegalArgumentException;

    // Return an iterable collection of edges for which vertex v is the destination.
    //   For an undirected graph, this is the same result
    //   returned by outgoingEdges.
    // @throws IllegalArgumentException if v is not a valid vertex
    Iterable<Edge<E>> incomingEdges( Vertex<V> v ) throws IllegalArgumentException;

    // Return the edge from u to v, or null if they are not adjacent.
    Edge<E> getEdge( Vertex<V> u, Vertex<V> v ) throws IllegalArgumentException;

    // Return the vertices of edge e as an array of length two.
    //   For a directed graph, the first vertex is the origin,
    //   and the second is the destination.
    //   If the graph is undirected, the order is arbitrary.
    Vertex<V>[] endVertices(Edge<E> e) throws IllegalArgumentException;

    // Return the vertex that is opposite vertex v on edge e.
    Vertex<V> opposite(Vertex<V> v, Edge<E> e) throws IllegalArgumentException;

    // Insert and returns a new vertex with the given element.
    Vertex<V> insertVertex( V element );

    // Insert and return a new edge between vertices u and v, storing given element.
    // @throws IllegalArgumentException if u or v are invalid vertices, or if an edge already exists between u and v.
    Edge<E> insertEdge( Vertex<V> u, Vertex<V> v, E element ) throws IllegalArgumentException;

    // Remove a vertex and all its incident edges from the graph.
    void removeVertex( Vertex<V> v ) throws IllegalArgumentException;

    // Remove an edge from the graph.
    void removeEdge( Edge<E> e ) throws IllegalArgumentException;
}
