package Imports;

/**
* Entry is an interface for the ADT PriorityQueue
*    in which a key of type K and
*             a value of type V are joined.
* 
* Based on Goodrich, Tamassia, Goldwasser
*
* @author      Francois Major
* @version     1.0
* @since       1.0
*/

public interface Entry<K,V> {
    K getKey(); // return the key stored in the Entry
    V getValue(); // return the value stored in the Entry
}
