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
@Table(name = "Estudiante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT e FROM Estudiante e"),
    @NamedQuery(name = "Estudiante.findByMatricula", query = "SELECT e FROM Estudiante e WHERE e.matricula = :matricula"),
    @NamedQuery(name = "Estudiante.findByNombre", query = "SELECT e FROM Estudiante e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Estudiante.findByApPaterno", query = "SELECT e FROM Estudiante e WHERE e.apPaterno = :apPaterno"),
    @NamedQuery(name = "Estudiante.findByApMaterno", query = "SELECT e FROM Estudiante e WHERE e.apMaterno = :apMaterno"),
    @NamedQuery(name = "Estudiante.findByCalle", query = "SELECT e FROM Estudiante e WHERE e.calle = :calle"),
    @NamedQuery(name = "Estudiante.findByColonia", query = "SELECT e FROM Estudiante e WHERE e.colonia = :colonia"),
    @NamedQuery(name = "Estudiante.findByCodPostal", query = "SELECT e FROM Estudiante e WHERE e.codPostal = :codPostal"),
    @NamedQuery(name = "Estudiante.findByTelefono", query = "SELECT e FROM Estudiante e WHERE e.telefono = :telefono"),
    @NamedQuery(name = "Estudiante.findByEmail", query = "SELECT e FROM Estudiante e WHERE e.email = :email"),
    @NamedQuery(name = "Estudiante.findByIdEstado", query = "SELECT e FROM Estudiante e WHERE e.idEstado = :idEstado"),
    @NamedQuery(name = "Estudiante.findByIdMunicipio", query = "SELECT e FROM Estudiante e WHERE e.idMunicipio = :idMunicipio")})
/**
 * Clase Estudiante que representa a la tabla Estudiante de la base de datos de control escolar
 */
public class Estudiante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Matricula")
    private String matricula;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "ApPaterno")
    private String apPaterno;
    @Column(name = "ApMaterno")
    private String apMaterno;
    @Column(name = "Calle")
    private String calle;
    @Column(name = "Colonia")
    private String colonia;
    @Column(name = "CodPostal")
    private String codPostal;
    @Column(name = "Telefono")
    private String telefono;
    @Basic(optional = false)
    @Column(name = "Email")
    private String email;
    @Column(name = "id_estado")
    private Short idEstado;
    @Column(name = "id_municipio")
    private Integer idMunicipio;
    
    
    /**
     * Contructor vacio de la clase estudiante
     */
    public Estudiante() {
    }
    /**
     * Constructor que pide la llave primaria de la tabla estudiante
     * @param matricula 
     */

    public Estudiante(String matricula) {
        this.matricula = matricula;
    }
    /**
     * Constructor que te pide los campos obligatorios de la tabla estudiante
     * @param matricula Cadena que representa la matricula
     * @param nombre Cadena que representa el nombre
     * @param apPaterno Cadena que representa el apellido paterno
     * @param email  cadena que representa el correo electronico
     */

    public Estudiante(String matricula, String nombre, String apPaterno, String email) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.email = email;
    }
    /**
     * Constructor que incializa  todos los atributos de la clase estudiante
     * @param matricula
     * @param nombre
     * @param apPaterno
     * @param apMaterno
     * @param calle
     * @param colonia
     * @param codPostal
     * @param telefono
     * @param email
     * @param idEstado
     * @param idMunicipio 
     */
    
      public Estudiante(String matricula, String nombre, String apPaterno, String apMaterno
              ,String calle, String colonia,
                String codPostal, String telefono,String email, short idEstado, int idMunicipio) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno=apMaterno;
        this.calle=calle;
        this.colonia=colonia;
        this.codPostal=codPostal;
        this.telefono=telefono;
        this.email = email;
        this.idEstado=idEstado;
        this.idMunicipio=idMunicipio;
    }
    /**
     * Constructor que inicializa los atributos de estudiante, menos de las id municipio y de estado
     * @param matricula
     * @param nombre
     * @param apPaterno
     * @param apMaterno
     * @param calle
     * @param colonia
     * @param codPostal
     * @param telefono
     * @param email 
     */
    public Estudiante(String matricula, String nombre, String apPaterno, String apMaterno
              ,String calle, String colonia,
                String codPostal, String telefono,String email){
        this.matricula = matricula;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno=apMaterno;
        this.calle=calle;
        this.colonia=colonia;
        this.codPostal=codPostal;
        this.telefono=telefono;
        this.email = email;
       
        
    }
    /**
     * Metodo get que devuelve una cadena representada por la llave primaria
     * @return cadena representada por la llave primaria 
     */

    public String getMatricula() {
        return matricula;
    }
    /**
     * Metodo que asigna el valor de matricula dependiendo del parametro enviado
     * @param matricula 
     */

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    /**
     * Metodo get
     * @return 
     */
    

    public String getNombre() {
        return nombre;
    }
    /**
     * Metodo set
     * @param nombre 
     */

    public void setNombre(String nombre) {
      
        this.nombre = nombre;
    }
    /**
     * Metodo get
     * @return 
     */
   

    public String getApPaterno() {
        return apPaterno;
    }
    
    
    /**
     * Metodo set
     * @param apPaterno 
     */

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }
    /**
     * Metodo get
     * @return 
     */
   

    public String getApMaterno() {
        return apMaterno;
    }
    
    /**
     * Metodo set
     * @param apMaterno 
     */

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }
    /**
     * Metodo get
     * @return  calle
     */

    public String getCalle() {
        return calle;
    }
    /**
     * Metodo set
     * @param calle 
     */

    public void setCalle(String calle) {
        this.calle = calle;
    }
    /**
     * Metodo get
     * @return colonia
     */

    public String getColonia() {
        return colonia;
    }
    /**
     * Metodo set 
     * @param colonia 
     */

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }
    /**
     * Metodo get
     * @return  codPostal
     */

    public String getCodPostal() {
        return codPostal;
    }
    /**
     * Metodo set
     * @param codPostal 
     */

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }
    /**
     * Metodo get 
     * @return telefono
     */

    public String getTelefono() {
        return telefono;
    }
    /**
     * Metodo set
     * @param telefono 
     */

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    /**
     * Metodo get
     * @return email
     */

    public String getEmail() {
        return email;
    }
    /**
     * Metodo set
     * @param email 
     */

    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Metodo get
     * @return  idEstado
     */

    public Short getIdEstado() {
        return idEstado;
    }
    /**
     * Metodo set
     * @param idEstado 
     */

    public void setIdEstado(Short idEstado) {
        this.idEstado = idEstado;
    }
    /**
     * Metodo get
     * @return  idMunicipio
     */

    public Integer getIdMunicipio() {
        return idMunicipio;
    }
    /**
     * Metodo set
     * @param idMunicipio 
     */

    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }
    /**
     * Metodo hash de la clase estudiante
     * @return valor hash
     */

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matricula != null ? matricula.hashCode() : 0);
        return hash;
    }
    /**
     * Metodo equals de la clase estudiante
     * @param object 
     * @return regresa un valor booleano true si son iguales, falso si no son iguales
     */

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.matricula == null && other.matricula != null) || (this.matricula != null && !this.matricula.equals(other.matricula))) {
            return false;
        }
        return true;
    }
    /**
     * Metodo que muestra el nombre del estudiante del objeto Estudiante
     * @return el nombre del estudiante
     */

    @Override
    public String toString() {
        return nombre;
    }
    
}
