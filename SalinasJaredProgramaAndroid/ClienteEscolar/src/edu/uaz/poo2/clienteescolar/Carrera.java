package edu.uaz.poo2.clienteescolar;

import java.io.Serializable;
/**
 * Clase que representa la tabla carrera de la base de datos
 * @author Jared Daniel Salinas Gonzalez
 *
 */
public class Carrera implements Serializable {
	private String claveCarrera;
	private String nombreCarrera;
	/**
	 * Contructor vacio
	 */
	public Carrera() {
    }
	/**
	 * Constructor que inicializa la carrera con la llave primaria
	 * @param claveCarrera cadena con la info de la clave de la carrera
	 */
    public Carrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }
    /**
     * Constructor que inicializa los campos de nombre y clave carrera
     * @param claveCarrera
     * @param nombreCarrera
     */
    public Carrera(String claveCarrera, String nombreCarrera) {
        this.claveCarrera = claveCarrera;
        this.nombreCarrera = nombreCarrera;
    }
    /**
     * 
     * @return Metodo que regresa el valor de la clave carrera actual
     */
    public String getClaveCarrera() {
        return claveCarrera;
    }
    /**
     * 
     * @param claveCarrera Metodo que pone un valor string al campo clave carrera
     */

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }
    /**
     * Metodo que obtiene el nombre de una carrera
     * @return la cadena con la carrera en especifico
     */

    public String getNombreCarrera() {
        return nombreCarrera;
    }
    /**
     * Metodo que le coloca un nombre a la carrera
     * @param nombreCarrera cadena con el nombre
     */
    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }
    /**
     * Metodo hash de la clase carrera
     */

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (claveCarrera != null ? claveCarrera.hashCode() : 0);
        return hash;
    }
    /**
     * Metodo equals de la clase carrera
     * @return si los valores de las carreras son iguales devuelve verdadero, falso caso contrario
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carrera)) {
            return false;
        }
        Carrera other = (Carrera) object;
        if ((this.claveCarrera == null && other.claveCarrera != null) || (this.claveCarrera != null && !this.claveCarrera.equals(other.claveCarrera))) {
            return false;
        }
        return true;
    }
    /**
     * Metodo to string de la clase carrera
     * @return el nombre de la carrera 
     */
    @Override
    public String toString() {
        //return "edu.uaz.poo2.entidades.Carrera[ claveCarrera=" + claveCarrera + " ]";
        return nombreCarrera;
    }

}
