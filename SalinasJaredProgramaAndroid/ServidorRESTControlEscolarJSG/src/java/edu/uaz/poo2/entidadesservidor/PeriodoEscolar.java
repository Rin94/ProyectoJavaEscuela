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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@Entity
@Table(name = "PeriodoEscolar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeriodoEscolar.findAll", query = "SELECT p FROM PeriodoEscolar p"),
    @NamedQuery(name = "PeriodoEscolar.findByIdPeriodo", query = "SELECT p FROM PeriodoEscolar p WHERE p.idPeriodo = :idPeriodo"),
    @NamedQuery(name = "PeriodoEscolar.findByYear", query = "SELECT p FROM PeriodoEscolar p WHERE p.year = :year"),
    @NamedQuery(name = "PeriodoEscolar.findByPeriodo", query = "SELECT p FROM PeriodoEscolar p WHERE p.periodo = :periodo")})
public class PeriodoEscolar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_periodo")
    private Integer idPeriodo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Year")
    private short year;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Periodo")
    private short periodo;

    public PeriodoEscolar() {
    }

    public PeriodoEscolar(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public PeriodoEscolar(Integer idPeriodo, short year, short periodo) {
        this.idPeriodo = idPeriodo;
        this.year = year;
        this.periodo = periodo;
    }

    public Integer getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPeriodo != null ? idPeriodo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeriodoEscolar)) {
            return false;
        }
        PeriodoEscolar other = (PeriodoEscolar) object;
        if ((this.idPeriodo == null && other.idPeriodo != null) || (this.idPeriodo != null && !this.idPeriodo.equals(other.idPeriodo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uaz.poo2.entidadesservidor.PeriodoEscolar[ idPeriodo=" + idPeriodo + " ]";
    }
    
}
