/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uaz.poo2.entidades;

/**
 * Esta clase de entidad representa la informacion de la tabla Municipio
 * @author Roberto Solis Robles
 */
public class Municipio {
    private Integer idMunicipio;
    private String nombreMunicipio;
    /**
     * Contructor vacio de la clase municipio
     */

    public Municipio() {
    }
    /**
     * Contructor de la clase municipio que recibe los parametros para inicializar los campos que son obligatorios
     * en la tabla municipio
     * @param idMunicipio
     * @param nombreMunicipio 
     */

    public Municipio(Integer idMunicipio, String nombreMunicipio) {
        this.idMunicipio = idMunicipio;
        this.nombreMunicipio = nombreMunicipio;
    }
    /**
     * Contructor de la clase municipio que pide como parametro la llave primaria de la tabla de municipios
     * @param idMunicipio 
     */
     public Municipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
        
    }
    /**
     * Metodo que regresa un entero con el valor de la llave primaria
     * @return un entero con el valor de idMunicipio
     */

    public Integer getIdMunicipio() {
        return idMunicipio;
    }
    /**
     * Metodo que coloca la llave primaria que es pasada como parametro al objeto municipio
     * @param idMunicipio 
     */

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }
    /**
     * Metodo que devuelve una cadena de caracteres con el valor del atributo de nombreMunicipio
     * @return  cadena con el valor de nombreMunicipio
     */

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }
    /**
     * Metodo que coloca un valor al atributo nombre municipio
     * @param nombreMunicipio  cadena con algun valor
     */

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }
    /**
     * Metodo hash de la clase municipio
     * @return 
     */

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.idMunicipio != null ? this.idMunicipio.hashCode() : 0);
        return hash;
    }
    /**
     * Metodo equals de la clase Municipio
     * @param obj
     * @return devuelve verdadero si los objetos son iguales, falsos si son caso contrareo
     */

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Municipio other = (Municipio) obj;
        if (this.idMunicipio != other.idMunicipio && (this.idMunicipio == null || !this.idMunicipio.equals(other.idMunicipio))) {
            return false;
        }
        return true;
    }

    /** 
     * Texto que se quiere sea visible al 
     * imprimir o colocar un objeto de clase
     * Municipio en un control de GUI
     * @return Texto a mostrarse para el municipio
     */
    @Override
    public String toString() {
        return nombreMunicipio;
    }
    
    
}
