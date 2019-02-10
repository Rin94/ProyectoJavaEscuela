/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uaz.poo2.basedatos;

/**
 *
 * @author Roberto Solis Robles
 */
public class ConexionException extends RuntimeException {
    
    /**
     * Metodo que nos muestra si hubo un error en la base de datos
     * @param message
     * @param cause 
     */

    public ConexionException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
