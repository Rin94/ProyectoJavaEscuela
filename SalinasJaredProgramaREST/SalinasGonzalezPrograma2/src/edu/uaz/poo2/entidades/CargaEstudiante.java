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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase que tiene la informacion de la tabla de carga estudiante de la base de datos
 * @author Jerad
 */
@Entity
@Table(name = "CargaEstudiante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CargaEstudiante.findAll", query = "SELECT c FROM CargaEstudiante c"),
    @NamedQuery(name = "CargaEstudiante.findByIdCargaEstudiante", query = "SELECT c FROM CargaEstudiante c WHERE c.idCargaEstudiante = :idCargaEstudiante"),
    @NamedQuery(name = "CargaEstudiante.findByMatricula", query = "SELECT c FROM CargaEstudiante c WHERE c.matricula = :matricula"),
    @NamedQuery(name = "CargaEstudiante.findByClaveMateria", query = "SELECT c FROM CargaEstudiante c WHERE c.claveMateria = :claveMateria"),
    @NamedQuery(name = "CargaEstudiante.findByIdPeriodo", query = "SELECT c FROM CargaEstudiante c WHERE c.idPeriodo = :idPeriodo")})
public class CargaEstudiante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_carga_estudiante")
    private Integer idCargaEstudiante;
    @Basic(optional = false)
    @Column(name = "Matricula")
    private String matricula;
    @Basic(optional = false)
    @Column(name = "ClaveMateria")
    private String claveMateria;
    @Basic(optional = false)
    @Column(name = "id_periodo")
    private int idPeriodo;
    /**
     * Contructor vacio de la clase carga estudiante
     */

    public CargaEstudiante() {
    }
    /**
     * Contructor de carga estudiante que pide como arg un entero que tiene
     * la informacion que se va a pasar a la llave primaria de carga estudiante
     * @param idCargaEstudiante valor con la llave primario
     */

    public CargaEstudiante(Integer idCargaEstudiante) {
        this.idCargaEstudiante = idCargaEstudiante;
    }
    /**
     * Contructor con todos los atributos de la clase Carga estudiante
     * @param idCargaEstudiante llave primaria
     * @param matricula cadena con la matricula del estudiante
     * @param claveMateria cadena con el valor de una clave materia
     * @param idPeriodo entero con el valor del id de periodo escolar
     */

    public CargaEstudiante(Integer idCargaEstudiante, String matricula, String claveMateria, int idPeriodo) {
        this.idCargaEstudiante = idCargaEstudiante;
        this.matricula = matricula;
        this.claveMateria = claveMateria;
        this.idPeriodo = idPeriodo;
    }
    /**
     * Metodo que regresa un entero con la info del atributo de id carga estudiante
     * @return regresa el valor del idCargaEstudiante
     */

    public Integer getIdCargaEstudiante() {
        return idCargaEstudiante;
    }
    /**
     * Metodo que coloca un valor entero al atributo de id carga estudiante
     * @param idCargaEstudiante entero con un valor de idCargaEstudiante 
     */

    public void setIdCargaEstudiante(Integer idCargaEstudiante) {
        this.idCargaEstudiante = idCargaEstudiante;
    }
    /**
     * Metodo que regresa el valor del atributo matricula 
     * @return regresa una cadena con el valor de matricula
     */

    public String getMatricula() {
        return matricula;
    }
    /**
     * Metodo que coloca una cadena en el atributo de  matricula
     * @param matricula cadena con un valor de matricula
     */

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    /**
     * Metodo que regresa una cadena con el valor del atributo clave materia
     * @return  una cadena con el valor de claveMateria
     */

    public String getClaveMateria() {
        return claveMateria;
    }
    /**
     * Metodo que coloca una cadena en el atributo claveMateria
     * @param claveMateria 
     */

    public void setClaveMateria(String claveMateria) {
        this.claveMateria = claveMateria;
    }
    /**
     * Metodo que regresa un entero con el valor de idPeriodo
     * @return 
     */

    public int getIdPeriodo() {
        return idPeriodo;
    }
    /**
     * Metodo que coloca un entero al atributo ide periodo
     * @param idPeriodo  entero con el valor que se va a colocar
     */

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }
    /**
     * Metodo hash de la clase CargaEstudiante
     * @return regresa un hash 
     */

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCargaEstudiante != null ? idCargaEstudiante.hashCode() : 0);
        return hash;
    }
    /***
     * Metodo equals de la clase CargaEstudiante 
     * @param object objeto a comparar
     * @return regresa un booleano verdadero si lod id de las cargas son iguales, falso si no son
     */

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargaEstudiante)) {
            return false;
        }
        CargaEstudiante other = (CargaEstudiante) object;
        if ((this.idCargaEstudiante == null && other.idCargaEstudiante != null) || (this.idCargaEstudiante != null && !this.idCargaEstudiante.equals(other.idCargaEstudiante))) {
            return false;
        }
        return true;
    }
    /**
     * Metodo toString de la clase CargaEstudiante
     * @return una cadena con la info del idPeriodo y la clave de la materia 
     */

    @Override
    public String toString() {
        
        
        return "Periodo: "+idPeriodo+" Materia: "+claveMateria;
    }
    
}
