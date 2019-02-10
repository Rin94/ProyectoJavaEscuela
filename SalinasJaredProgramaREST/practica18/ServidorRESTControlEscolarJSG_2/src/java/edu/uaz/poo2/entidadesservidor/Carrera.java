/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.entidadesservidor;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@Entity
@Table(name = "Carrera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carrera.findAll", query = "SELECT c FROM Carrera c"),
    @NamedQuery(name = "Carrera.findByClaveCarrera", query = "SELECT c FROM Carrera c WHERE c.claveCarrera = :claveCarrera"),
    @NamedQuery(name = "Carrera.findByNombreCarrera", query = "SELECT c FROM Carrera c WHERE c.nombreCarrera = :nombreCarrera")})
public class Carrera implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ClaveCarrera")
    private String claveCarrera;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NombreCarrera")
    private String nombreCarrera;

    public Carrera() {
    }

    public Carrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }

    public Carrera(String claveCarrera, String nombreCarrera) {
        this.claveCarrera = claveCarrera;
        this.nombreCarrera = nombreCarrera;
    }

    public String getClaveCarrera() {
        return claveCarrera;
    }

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }

    public String getNombreCarrera() {
        return nombreCarrera;
    }

    public void setNombreCarrera(String nombreCarrera) {
        this.nombreCarrera = nombreCarrera;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (claveCarrera != null ? claveCarrera.hashCode() : 0);
        return hash;
    }

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

    @Override
    public String toString() {
        return "edu.uaz.poo2.entidadesservidor.Carrera[ claveCarrera=" + claveCarrera + " ]";
    }
    
}
