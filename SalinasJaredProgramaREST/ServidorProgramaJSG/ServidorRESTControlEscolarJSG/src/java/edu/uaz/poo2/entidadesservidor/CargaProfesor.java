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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "CargaProfesor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CargaProfesor.findAll", query = "SELECT c FROM CargaProfesor c"),
    @NamedQuery(name = "CargaProfesor.findByIdCargaProfesor", query = "SELECT c FROM CargaProfesor c WHERE c.idCargaProfesor = :idCargaProfesor"),
    @NamedQuery(name = "CargaProfesor.findByRfc", query = "SELECT c FROM CargaProfesor c WHERE c.rfc = :rfc"),
    @NamedQuery(name = "CargaProfesor.findByClaveMateria", query = "SELECT c FROM CargaProfesor c WHERE c.claveMateria = :claveMateria"),
    @NamedQuery(name = "CargaProfesor.findByIdPeriodo", query = "SELECT c FROM CargaProfesor c WHERE c.idPeriodo = :idPeriodo")})
public class CargaProfesor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_carga_profesor")
    private Integer idCargaProfesor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "RFC")
    private String rfc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "ClaveMateria")
    private String claveMateria;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_periodo")
    private int idPeriodo;

    public CargaProfesor() {
    }

    public CargaProfesor(Integer idCargaProfesor) {
        this.idCargaProfesor = idCargaProfesor;
    }

    public CargaProfesor(Integer idCargaProfesor, String rfc, String claveMateria, int idPeriodo) {
        this.idCargaProfesor = idCargaProfesor;
        this.rfc = rfc;
        this.claveMateria = claveMateria;
        this.idPeriodo = idPeriodo;
    }

    public Integer getIdCargaProfesor() {
        return idCargaProfesor;
    }

    public void setIdCargaProfesor(Integer idCargaProfesor) {
        this.idCargaProfesor = idCargaProfesor;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
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
        hash += (idCargaProfesor != null ? idCargaProfesor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CargaProfesor)) {
            return false;
        }
        CargaProfesor other = (CargaProfesor) object;
        if ((this.idCargaProfesor == null && other.idCargaProfesor != null) || (this.idCargaProfesor != null && !this.idCargaProfesor.equals(other.idCargaProfesor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uaz.poo2.entidadesservidor.CargaProfesor[ idCargaProfesor=" + idCargaProfesor + " ]";
    }
    
}
