package PQueue;

import java.lang.IllegalArgumentException;

import Imports.Entry;


/**
* PriorityQueue is an interface for the ADT Priority Queue
* 
* Based on Goodrich, Tamassia, Goldwasser
*
* @author      Francois Major
* @version     1.0
* @since       1.0
*/

public interface PriorityQueue<K,V> {
    int size();
    boolean isEmpty();
    Entry<K,V> insert( K key, V value ) throws IllegalArgumentException;
    Entry<K,V> min();
    Entry<K,V> removeMin();
}
    
