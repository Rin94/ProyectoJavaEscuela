/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uaz.poo2.entidades;

/**
 * Esta clase de entidad representa la informacion de la tabla Estado
 * @author Jared Daniel Salinas Gonzalez
 */
public class Estado {
    private Short idEstado;
    private String nombreEstado;
    
    /**
     * Constructor vacio de la clase estado
     */

    public Estado() {
    }
    /**
     * Constructor de la clase estado con los campos obligatorios
     * @param idEstado id de la tabla Estado
     * @param nombreEstado nombre del estado de la tabla de Estado
     */
    public Estado(Short idEstado, String nombreEstado) {
        this.idEstado = idEstado;
        this.nombreEstado = nombreEstado;
    }
    /**
     * Constructor de la clase estado que te pide la llave primaria de la TABLA estado
     * @param idEstado  id
     */
    public Estado(Short idEstado) {
        this.idEstado = idEstado;
        
    }
    /**
     * Obtiene el id del estado
     * @return  regresa un short que representa el id del estado
     */
    public Short getIdEstado() {
        return idEstado;
    }
    /**
     * Metodo que coloca un id de un estado
     * @param idEstado asigna el parametro al atributo idEstado 
     */
    public void setIdEstado(Short idEstado) {
        this.idEstado = idEstado;
    }
    /**
     * Obtiene el nombre del estado del objeto Estado
     * @return una cadena con el nombre del estado de la base de datos
     */
    public String getNombreEstado() {
        return nombreEstado;
    }
    /**
     * Metodo que coloca una cadena como un nombre de estado al atrubuto nombreEstado
     * @param nombreEstado cadena que representa el nombre del estado que se va asignar
     */

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }
    /**
     * Metodo hashCode de la clase Estado
     * @return un hash
     */

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (this.idEstado != null ? this.idEstado.hashCode() : 0);
        return hash;
    }
    /**
     * Metodo equals de la clase estado
     * @param obj objeto
     * @return un booleano que puede ser falso o verdadero, si los objetos son iguales entonces devuelve verdadero
     * caso contrario falso
     */

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Estado other = (Estado) obj;
        if (this.idEstado != other.idEstado && (this.idEstado == null || !this.idEstado.equals(other.idEstado))) {
            return false;
        }
        return true;
    }

     /** 
     * Texto que se quiere sea visible al 
     * imprimir o colocar un objeto de clase
     * Estado en un control de GUI
     * @return Texto a mostrarse para el estado
     */

    @Override
    public String toString() {
        return nombreEstado;
    }
    
}
