package Graph;

/**
author = "Francois Major"
version = "1.0"
date = "8 December 2020"

Java program for IFT2015/Graphes

Taken and modified from Goodrich, Tamassia, Goldwasser
  Data Structures and Algorithms in Java (c)2014
**/

public interface Vertex<V> {
    // Return the element that is associated with the vertex.
    V getElement();
}
