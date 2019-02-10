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
@Table(name = "Profesor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profesor.findAll", query = "SELECT p FROM Profesor p"),
    @NamedQuery(name = "Profesor.findByRfc", query = "SELECT p FROM Profesor p WHERE p.rfc = :rfc"),
    @NamedQuery(name = "Profesor.findByNombre", query = "SELECT p FROM Profesor p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Profesor.findByApPaterno", query = "SELECT p FROM Profesor p WHERE p.apPaterno = :apPaterno"),
    @NamedQuery(name = "Profesor.findByApMaterno", query = "SELECT p FROM Profesor p WHERE p.apMaterno = :apMaterno"),
    @NamedQuery(name = "Profesor.findByCalle", query = "SELECT p FROM Profesor p WHERE p.calle = :calle"),
    @NamedQuery(name = "Profesor.findByColonia", query = "SELECT p FROM Profesor p WHERE p.colonia = :colonia"),
    @NamedQuery(name = "Profesor.findByCodPostal", query = "SELECT p FROM Profesor p WHERE p.codPostal = :codPostal"),
    @NamedQuery(name = "Profesor.findByTelefono", query = "SELECT p FROM Profesor p WHERE p.telefono = :telefono"),
    @NamedQuery(name = "Profesor.findByEmail", query = "SELECT p FROM Profesor p WHERE p.email = :email"),
    @NamedQuery(name = "Profesor.findByIdEstado", query = "SELECT p FROM Profesor p WHERE p.idEstado = :idEstado"),
    @NamedQuery(name = "Profesor.findByIdMunicipio", query = "SELECT p FROM Profesor p WHERE p.idMunicipio = :idMunicipio")})
public class Profesor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 13)
    @Column(name = "RFC")
    private String rfc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ApPaterno")
    private String apPaterno;
    @Size(max = 20)
    @Column(name = "ApMaterno")
    private String apMaterno;
    @Size(max = 30)
    @Column(name = "Calle")
    private String calle;
    @Size(max = 30)
    @Column(name = "Colonia")
    private String colonia;
    @Size(max = 5)
    @Column(name = "CodPostal")
    private String codPostal;
    @Size(max = 10)
    @Column(name = "Telefono")
    private String telefono;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "Email")
    private String email;
    @Column(name = "id_estado")
    private Short idEstado;
    @Column(name = "id_municipio")
    private Integer idMunicipio;

    public Profesor() {
    }

    public Profesor(String rfc) {
        this.rfc = rfc;
    }

    public Profesor(String rfc, String nombre, String apPaterno, String email) {
        this.rfc = rfc;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.email = email;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Short getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Short idEstado) {
        this.idEstado = idEstado;
    }

    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rfc != null ? rfc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profesor)) {
            return false;
        }
        Profesor other = (Profesor) object;
        if ((this.rfc == null && other.rfc != null) || (this.rfc != null && !this.rfc.equals(other.rfc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.uaz.poo2.entidadesservidor.Profesor[ rfc=" + rfc + " ]";
    }
    
}
