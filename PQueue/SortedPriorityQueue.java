package PQueue;

import java.util.Comparator;
import java.lang.IllegalArgumentException;
import Imports.PositionalList;
import Imports.Position;
import Imports.Entry;
import Imports.LinkedPositionalList;

/**
* SortedPriorityQueue is an implementation of the ADT Priority Queue
*    using a LinkedPositionalList that we keep sorted
* 
* Based on Goodrich, Tamassia, Goldwasser
*
* @author      Francois Major
* @version     1.0
* @since       1.0
*/

public class SortedPriorityQueue<K,V> extends AbstractPriorityQueue<K,V> {
    
    // attributes, physical data structure
    private PositionalList<Entry<K,V>> list = new LinkedPositionalList<>();
    
    // construct an empty PQ based on natural ordering
    public SortedPriorityQueue() { super(); }
    // construct an empty PQ based on a given comparator to order keys
    public SortedPriorityQueue( Comparator<K> comp ) { super( comp ); }

    // utility
    // insert a (key, value) entry and return the entry created in O(n)
    public Entry<K,V> insert( K k, V v ) throws IllegalArgumentException {
	checkKey( k ); // key checking method (could throw exception)
	Entry<K,V> newest = new PQEntry<>( k, v );
	Position<Entry<K,V>> walk = this.list.last();
	// walk backward, looking for smaller key
	while( walk != null && this.compare( newest, walk.getElement() ) < 0 )
	    walk = list.before( walk );
	if( walk == null ) list.addFirst( newest ); // new key is smallest
	else this.list.addAfter( walk, newest ); // newest goes after walk
	return newest;
    }

    // return (but do not remove) an entry of minimal key in O(1)
    @Override
    public Entry<K,V> min() {
	if( this.list.isEmpty() ) return null;
	return this.list.first().getElement();
    }

    // remove and return an entry of minimal key in O(1)
    @Override
    public Entry<K,V> removeMin() {
	if( this.list.isEmpty() ) return null;
	return this.list.remove( this.list.first() );
    }

    // return the number of entries in the PQ
    @Override
    public int size() { return this.list.size(); }

    // convert PQ to String
    @Override
    public String toString() { return this.list.toString(); }
}
