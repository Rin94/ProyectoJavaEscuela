/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uaz.poo2.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jerad
 */
@Entity
@Table(name = "Carrera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carrera.findAll", query = "SELECT c FROM Carrera c"),
    @NamedQuery(name = "Carrera.findByClaveCarrera", query = "SELECT c FROM Carrera c WHERE c.claveCarrera = :claveCarrera"),
    @NamedQuery(name = "Carrera.findByNombreCarrera", query = "SELECT c FROM Carrera c WHERE c.nombreCarrera = :nombreCarrera")})
/**
 * Clase que es mapea una tabla de control escolar 
 */
public class Carrera implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ClaveCarrera")
    private String claveCarrera;
    @Basic(optional = false)
    @Column(name = "NombreCarrera")
    private String nombreCarrera;
    
    /***
     * 
     *Constructor vacio de carrera
     */

    public Carrera() {
    }
    /***
     * Contructor de Carrera que pide la lleve primaria de la tabla carrera
     * @param claveCarrera 
     */
    public Carrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }
    
    //Contructor de carrera que pide la clave y el nombre de la carrera

    public Carrera(String claveCarrera, String nombreCarrera) {
        this.claveCarrera = claveCarrera;
        this.nombreCarrera = nombreCarrera;
    }
    /**
     * Medodo que devuelve la clave carrera de un objeto carrera
     * @return  claveCarrera regresa la clave carrera
     */

    public String getClaveCarrera() {
        return claveCarrera;
    }
    /**
     * Pone una clave carrera al atributo clave carrera
     * @param claveCarrera String que es la clave carrera que se le va a dara al
     * atributo clave carrera
     */

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }
    /**
     * METODO que obtenemos el nombre de la carrera de la base de datos
     * @return 
     */
    public String getNombreCarrera() {
        return nombreCarrera;
    }
    /**
     * Metodo donde colocamos el parametro al atributo nombre carrera
     * @param nombreCarrera  String que coloca un nombre de carrera al atributo 
     * nombreCarrera
     */
    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }
    /**
     * Metodo hash de carrera
     * @return 
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (claveCarrera != null ? claveCarrera.hashCode() : 0);
        return hash;
    }
    /**
     * Metodo equals de la clase de carrera
     * @param object 
     * @return FALSO si no son iguales TRUE si son iguales
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
     * Metodo toString de la clase carrera
     * @return el nombre de la carrera del objeto Carrera seleccionada
     */
    @Override
    public String toString() {
        return nombreCarrera;
    }
    
}
