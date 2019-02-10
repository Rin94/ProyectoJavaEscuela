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
 * Clase que representa a la tabla PeriodoEscolar de la base de datos de control escolar
 * @author Jerad
 */
@Entity
@Table(name = "PeriodoEscolar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PeriodoEscolar.findAll", query = "SELECT p FROM PeriodoEscolar p"),
    @NamedQuery(name = "PeriodoEscolar.findByIdPeriodo", query = "SELECT p FROM PeriodoEscolar p WHERE p.idPeriodo = :idPeriodo"),
    @NamedQuery(name = "PeriodoEscolar.findByYear", query = "SELECT p FROM PeriodoEscolar p WHERE p.year = :year"),
    @NamedQuery(name = "PeriodoEscolar.findByPeriodo", query = "SELECT p FROM PeriodoEscolar p WHERE p.periodo = :periodo")})
/**
 * Clase que representa a la tabla PeriodoEscolar de la base de datos de control escolar
 */
public class PeriodoEscolar implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_periodo")
    private Integer idPeriodo;
    @Basic(optional = false)
    @Column(name = "Year")
    private short year;
    @Basic(optional = false)
    @Column(name = "Periodo")
    private short periodo;
    /**
     * Contructor vacio de la clase periodo escolar
     */

    public PeriodoEscolar() {
    }
    /**
     * Contructor que inicializa la llave primaria de la talba de Periodo escolar
     * @param idPeriodo  entero con el valor del id del periodo
     */

    public PeriodoEscolar(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }
    /**
     * Contructor que tiene inicializa todos los atributos que son obligatorios de la tabla periodo escolar
     * @param idPeriodo
     * @param year
     * @param periodo 
     */

    public PeriodoEscolar(Integer idPeriodo, short year, short periodo) {
        this.idPeriodo = idPeriodo;
        this.year = year;
        this.periodo = periodo;
    }
    /**
     * Metodo get que regresa un entero con el valor del atributo idPeriodo
     * @return  entero con el valor de idPeriodo
     */

    public Integer getIdPeriodo() {
        return idPeriodo;
    }
    /**
     * Metodo que coloca un valor al atributo idPeriodo
     * @param idPeriodo parametro que tiene el valor que se le asignara al atributo periodo
     */

    public void setIdPeriodo(Integer idPeriodo) {
        this.idPeriodo = idPeriodo;
    }
    /**
     * Metodo que devuelve el valor del campo year 
     * @return un short con el valor del campo year 
     */

    public short getYear() {
        return year;
    }
    /**
     * Coloca en le atributo year el parametro que se le pasa como argumento
     * @param year short con un valor valido
     */

    public void setYear(short year) {
        this.year = year;
    }
    /**
     *  Metodo que devuelve el valor del campo de periodo
     * @return  un corto con el valor de periodo
     */

    public short getPeriodo() {
        return periodo;
    }
    /**
     * Metodo que coloca un short al atributo periodo
     * @param periodo  short que entra como arg
     */

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }
    /**
     * Metodo hash del la clase PeriodoEscolar
     * @return 
     */

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPeriodo != null ? idPeriodo.hashCode() : 0);
        return hash;
    }
    /**
     * Metodo equals de la clase Periodo Escolar
     * @param object
     * @return  devuelve verdadero si los objetos son iguales falso si son distintos
     */

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
    /**
     * Metodo que le asigna un valor a la variable local perString dependiento del 
     * valor de su periodo
     * @param p periodo
     * @return un string con el valor del campo PerString periodo String
     */
    private String obtenPeriodoString(short p){
        
        String perString=null;
          if(p==1){
                perString="Agosto-Diciembre";
                
                
            }
            else if(p==2){
                perString="Enero-Junio";
            }
            else if(p==3){
                perString="Verano";
            }
        
        
        return perString;
        
    }
    /**
     * Metodo to string 
     * @return  regresa una cadena con el valor del campo year un espacio y el valor
     * de periodoString que se saca del metodo privado obtenPeriodoString
     */

    @Override
    public String toString() {
       String periodoString= obtenPeriodoString(periodo);
        
        return year+" "+periodoString;
    }
    /**
     * Metodo to String que solo muestra el valor de la variable local perString que lo obtiene del 
     * metodo privado obtenPeriodoString
     * @return 
     */
    
    public String toStringPerString() {
       String perString= obtenPeriodoString(periodo);
        
        return perString;
    }
    
    
}
