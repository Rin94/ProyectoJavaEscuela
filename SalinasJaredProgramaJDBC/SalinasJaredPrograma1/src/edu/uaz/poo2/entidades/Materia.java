/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
@Table(name = "Materia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materia.findAll", query = "SELECT m FROM Materia m"),
    @NamedQuery(name = "Materia.findByClaveMateria", query = "SELECT m FROM Materia m WHERE m.claveMateria = :claveMateria"),
    @NamedQuery(name = "Materia.findByNombreMateria", query = "SELECT m FROM Materia m WHERE m.nombreMateria = :nombreMateria"),
    @NamedQuery(name = "Materia.findBySemestre", query = "SELECT m FROM Materia m WHERE m.semestre = :semestre"),
    @NamedQuery(name = "Materia.findByClaveCarrera", query = "SELECT m FROM Materia m WHERE m.claveCarrera = :claveCarrera")})
/**
 * Clase que mapea la tabla Materia de la base de datos ControlEscolar que implementa el metodo Serializable
 */
public class Materia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ClaveMateria")
    private String claveMateria;
    @Basic(optional = false)
    @Column(name = "NombreMateria")
    private String nombreMateria;
    @Basic(optional = false)
    @Column(name = "Semestre")
    private short semestre;
    @Basic(optional = false)
    @Column(name = "ClaveCarrera")
    private String claveCarrera;
    
    /**
     * Contructor vacio de la clase Materia
     */
    public Materia() {
    }
    /**
     * Contructor que pide un string para inicializar el atributo de clave Materia
     * @param claveMateria 
     */

    public Materia(String claveMateria) {
        this.claveMateria = claveMateria;
    }
    /**
     * Constructor que pide los campos obligatorios de la clase materia
     * @param claveMateria
     * @param nombreMateria
     * @param semestre
     * @param claveCarrera 
     */

    public Materia(String claveMateria, String nombreMateria, short semestre, String claveCarrera) {
        this.claveMateria = claveMateria;
        this.nombreMateria = nombreMateria;
        this.semestre = semestre;
        this.claveCarrera = claveCarrera;
    }
    public Materia(String claveMateria, String nombreMateria){
        this.claveMateria = claveMateria;
        this.nombreMateria = nombreMateria;
        
    }
    /**
     * Metodo get de la clase materia que regresa un string con el valor de clave Materia
     * @return 
     */

    public String getClaveMateria() {
        return claveMateria;
    }
    /**
     * Metodo set de la clase materia que le asigna un string al atributo clave materia
     * @param claveMateria 
     */

    public void setClaveMateria(String claveMateria) {
        this.claveMateria = claveMateria;
    }
    /**
     * Metodo que obtiene el valor del atributo de nombre materia
     * @return  un string con el valor del nombreMateria
     */

    public String getNombreMateria() {
        return nombreMateria;
    }
    /**
     * Metodo que coloca un valor al atributo nombre materia
     * @param nombreMateria cadena que se le va asignar al atributo nombreMateria
     */

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }
    /**
     * Metodo que regresa el semestre del objeto materia
     * @return un short que tiene el valor del semestre 
     */

    public short getSemestre() {
        return semestre;
    }
    /**
     * Metodo que  asigna el  atributo semestre el parametro que le llega
     * @param semestre  short con un valor de semestre
     */

    public void setSemestre(short semestre) {
        this.semestre = semestre;
    }
    /**
     * Metodo que devuelve la clave de la carrera  de la materia seleccionada
     * @return un string con el valor del objeto
     */

    public String getClaveCarrera() {
        return claveCarrera;
    }
    /**
     * Metodo que coloca al atributo clave carrera un valor que es pasado como argumento
     * @param claveCarrera 
     */

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }
    /**
     * Metodo hash de la clase Carrera
     * @return un valor hash 
     */

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (claveMateria != null ? claveMateria.hashCode() : 0);
        return hash;
    }
    /**
     * Metodo equals de la clase 
     * @param object
     * @return falso si el objeto es diferente true si estas son iguales
     */

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Materia)) {
            return false;
        }
        Materia other = (Materia) object;
        if ((this.claveMateria == null && other.claveMateria != null) || (this.claveMateria != null && !this.claveMateria.equals(other.claveMateria))) {
            return false;
        }
        return true;
    }
    /**
     * Metodo toString de la clase Materia 
     * @return regresa una cadena con el atributo o campo de nombre materia del objeto Materia
     */

    @Override
    public String toString() {
        return nombreMateria;
    }
    
}
