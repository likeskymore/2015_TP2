package Imports;

import java.lang.IllegalStateException;

/**
* Position is an interface for the Position ADT
* 
* Based on Goodrich, Tamassia, Goldwasser
*
* @author      Francois Major
* @version     1.0
* @since       1.0
*/

public interface Position<E> {
    // return the corresponding element (stored at this Position)
    E getElement() throws IllegalStateException;
}
