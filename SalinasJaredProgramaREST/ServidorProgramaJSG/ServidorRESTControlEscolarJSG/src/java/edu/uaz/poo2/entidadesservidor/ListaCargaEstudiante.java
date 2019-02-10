/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.entidadesservidor;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

/**
 * Clase que une la informacion de Materia, PeriodoEscolar, Carrera y CargaEstudiante 
 * de la base de datos de ControlEscolar para ls manipulacion de la lista del panel CargaEstudiante
 * 
 * @author Jerad
 */
public class ListaCargaEstudiante implements Serializable {
    
    private int idCargaEstudiante;
    private String nombreMateria;
    private short periodo;
    private int year;
    private int idPeriodo;
    private String claveCarrera;
    private String claveMateria;
    private String Matricula;
    
    /**
     * Contructor vacio
     */
    
    public ListaCargaEstudiante(){
        
    }
    /**
     * Contructor con los campos que van a ser utilizados en la lista de CargaEstudiante
     * @param idCargaEstudiante entero que tiene la informacion de idCargaEstudiante de la base de datos
     * @param nombreMateria cadena que tiene la informacion del nombre de la materia de la base de datos
     * @param periodo corto que tiene la informacion de periodo de la base de datos
     * @param year año entero que tiene la informacion de year de la base de datos
     */
    
    public ListaCargaEstudiante(int idCargaEstudiante, String nombreMateria, short periodo, int year){
        this.idCargaEstudiante=idCargaEstudiante;
        this.nombreMateria=nombreMateria;
        this.periodo=periodo;
        this.year=year;
        
    }
    /**
     * Constructor que almacena la informacion completa de la lista carga estudiante
     * @param idCargaEstudiante entero que tiene la informacion de idCargaEstudiante de la base de datos
     * @param nombreMateria cadena que tiene la informacion del nombre de la materia de la base de datos
     * @param periodo corto que tiene la informacion de periodo de la base de datos
     * @param year  año entero que tiene la informacion de year de la base de datos
     * @param idPeriodo entero con la informacion de id_periodo de la base de datos
     * @param claveMateria cadena con la informacion de la clave de la materia seleccionada
     * @param claveCarrera cadena con la informacion de la clave de la carrera de la materia seleccionada
     */
     public ListaCargaEstudiante(int idCargaEstudiante, String nombreMateria, short periodo,
             int year,int idPeriodo, String claveMateria, String claveCarrera
             ){
        this.idCargaEstudiante=idCargaEstudiante;
        this.nombreMateria=nombreMateria;
        this.periodo=periodo;
        this.year=year;
        this.claveCarrera=claveCarrera;
        this.claveMateria=claveMateria;
        this.idPeriodo=idPeriodo;
        
    }
     /**
      * Metodo get y set de la clase 
      * 
      */

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public String getClaveCarrera() {
        return claveCarrera;
    }

    public void setClaveCarrera(String claveCarrera) {
        this.claveCarrera = claveCarrera;
    }

    public String getClaveMateria() {
        return claveMateria;
    }

    public void setClaveMateria(String claveMateria) {
        this.claveMateria = claveMateria;
    }

    public int getIdCargaEstudiante() {
        return idCargaEstudiante;
    }

    public void setIdCargaEstudiante(int idCargaEstudiante) {
        this.idCargaEstudiante = idCargaEstudiante;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMatricula() {
        return Matricula;
    }

    public void setMatricula(String Matricula) {
        this.Matricula = Matricula;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.idCargaEstudiante;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ListaCargaEstudiante other = (ListaCargaEstudiante) obj;
        if (this.idCargaEstudiante != other.idCargaEstudiante) {
            return false;
        }
        return true;
    }
    public String obtenPeriodoString(short p){
        
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
     * Metodo que obtiene un corto y la compara con 1,2 y 3 para sacar la informacion
     * de periodo
     * @param p corto puede ser 1,2 y 3
     * @return cadena con el periodo seleccionado
     */
      public String obtenPeriodoStringCB(short p){
        
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
          
        
        
        return year+" "+perString;
        
    }
      /**
       * Metodo toString de la clase devuelve el valor de los atributos año, periodoString mas  nombreMateria
       * para sacar la lista de carga estudiante
       * @return una cadena con la informacion del objeto, que va ser usado para llena la lista del panel de la carga
       * del estudiante
       */

    @Override
    public String toString() {
        String periodoString=obtenPeriodoString(periodo);
        return year+"  "+periodoString+"  "+nombreMateria;
    }
}
