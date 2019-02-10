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
 *
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

    public CargaEstudiante() {
    }

    public CargaEstudiante(Integer idCargaEstudiante) {
        this.idCargaEstudiante = idCargaEstudiante;
    }

    public CargaEstudiante(Integer idCargaEstudiante, String matricula, String claveMateria, int idPeriodo) {
        this.idCargaEstudiante = idCargaEstudiante;
        this.matricula = matricula;
        this.claveMateria = claveMateria;
        this.idPeriodo = idPeriodo;
    }

    public Integer getIdCargaEstudiante() {
        return idCargaEstudiante;
    }

    public void setIdCargaEstudiante(Integer idCargaEstudiante) {
        this.idCargaEstudiante = idCargaEstudiante;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getClaveMateria() {
        return claveMateria;
    }

    public void setClaveMateria(String claveMateria) {
        this.claveMateria = claveMateria;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCargaEstudiante != null ? idCargaEstudiante.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        
        
        return "Periodo: "+idPeriodo+" Materia: "+claveMateria;
    }
    
}
