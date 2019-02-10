/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uaz.poo2.basedatos;



import edu.uaz.poo2.clientesrest.ClienteCargaEstudiante;
import edu.uaz.poo2.clientesrest.ClienteCarrera;
import edu.uaz.poo2.clientesrest.ClienteEstado;
import edu.uaz.poo2.clientesrest.ClienteEstudiante;
import edu.uaz.poo2.clientesrest.ClienteMateria;
import edu.uaz.poo2.clientesrest.ClienteMunicipio;
import edu.uaz.poo2.clientesrest.ClientePeriodoEscolar;
import edu.uaz.poo2.entidades.CargaEstudiante;
import edu.uaz.poo2.entidades.Estado;
import edu.uaz.poo2.entidades.Municipio;
import edu.uaz.poo2.entidades.Carrera;
import edu.uaz.poo2.entidades.Estudiante;
import edu.uaz.poo2.entidades.ListaCargaEstudiante;
import edu.uaz.poo2.entidades.Materia;
import edu.uaz.poo2.entidades.PeriodoEscolar;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.GenericType;

/**
 * Esta clase contiene los metodos necesarios para obtener/actualizar
 * informacion de la base de datos
 *
 * @author Jared Daniel Salinas Gonzalez
 */
public class ConectorBaseDeDatos {

    private Connection conn;
    private Statement stmt;
    private Statement stmtAux;
    private String urlConexion;
    //Objetos de clase Estado
    
    private ClienteEstado cEstado;
    private GenericType<List<Estado>>tipoListaEstado;
    //objetos de clase Muncipio
    private ClienteMunicipio cMunicipio;
    private GenericType<List<Municipio>>tipoListaMunicipio;
    //objetos de clase Carrera
    private ClienteCarrera cCarrera;
    private GenericType <List<Carrera>>tipoListaCarrera; 
    //objetos de clase Materia
    private ClienteMateria cMateria;
    private GenericType <List<Materia>>tipoListaMateria;
    //objetos de clase PeriodoEscolar
    private ClientePeriodoEscolar cPeriodo;
    private GenericType <List<PeriodoEscolar>>tipoListaPeriodo;
    //objetos de clase Estudiante
    private ClienteEstudiante cEstudiante;
    private GenericType<List<Estudiante>>tipoListaEstudiante;
    //objetos de clase CargaEstudiante
    private ClienteCargaEstudiante cLCEstudiante;
    private GenericType<List<ListaCargaEstudiante>>tipoListaCargaEstudiante;
    
    

    /**
     * Este constructor intentan hacer la conexion a la base de datos con los
     * parametros recibidos
     *
     * @param dirServidor Ubicacion del servidor MySQL
     * @param puerto Puerto al que escucha el servidor MySQL
     * @param usuario Cuenta de usuario a utilizar para conectarse a MySQL
     * @param clave Clave de usuario a utilizar para conectarse a MySQL
     * @throws ConexionException Excepcion generada en caso de no poder hacer la
     * conexion a MySQL
     */
    public ConectorBaseDeDatos(
            String dirServidor,
            Integer puerto,
            String usuario,
            String clave)
            throws ConexionException {
        this();
        urlConexion = "jdbc:mysql://" + dirServidor + ":"
                + puerto + "/ControlEscolar?user="
                + usuario + "&password=" + clave
                + "&characterEncoding=utf8";
        try {
            conn = DriverManager.getConnection(urlConexion);
            stmt = conn.createStatement();
            stmtAux = conn.createStatement();
            //stmt.executeQuery(""); 
        } catch (SQLException e) {
            throw new ConexionException(e.getMessage(), e.getCause());
        }
    }
    public ConectorBaseDeDatos(){
        cEstado=new ClienteEstado();
        cMunicipio=new ClienteMunicipio();
        cMateria=new ClienteMateria();
        cCarrera=new ClienteCarrera();
        cPeriodo=new ClientePeriodoEscolar();
        cEstudiante=new ClienteEstudiante();
        cLCEstudiante=new ClienteCargaEstudiante();
        tipoListaEstado=new GenericType<List<Estado>>(){}; 
        tipoListaMunicipio=new GenericType<List<Municipio>>(){};
        tipoListaCarrera=new GenericType <List<Carrera>>(){};
        tipoListaMateria=new GenericType <List<Materia>>(){};
        tipoListaPeriodo=new GenericType <List<PeriodoEscolar>>(){};
        tipoListaEstudiante=new GenericType<List<Estudiante>>(){};
        tipoListaCargaEstudiante=new GenericType<List<ListaCargaEstudiante>>(){};
        
    }

    // Metodos para el manejo de la tabla Estados
    /**
     * Este metodo regresa los estados encontrados en la base de datos en la
     * tabla Estado
     *
     * @return Lista de estados
     */
    public List<Estado> obtenEstados() {
        List<Estado> resultado
                = new ArrayList<Estado>();
        try {
            resultado=cEstado.findAll_XML(tipoListaEstado);
            }
         catch (ClientErrorException e) {
            System.err.println("Error al obtener estados:"
                    + e.getMessage());
        } catch (NullPointerException npe) {

        }
        return resultado;
    }

    // Metodos para el manejo de la tabla Municipios
    /**
     * Este metodo regresa los municipios encontrados en la base de datos en la
     * tabla Municipio de un estado en particular
     *
     * @param idEstado ID del estado del que se desean los municipios
     * @return Lista de municipios del estado indicado
     */
     public List<Municipio> obtenMunicipios(Short idEstado) {
        List<Municipio> resultado =
                new ArrayList<Municipio>();
        try {
          resultado=cMunicipio.encuentraMunicipios_XML(tipoListaMunicipio, idEstado.toString());
        } catch (ClientErrorException e) {
            System.err.println("Error al obtener municipios:"
                    + e.getMessage());
        }
        return resultado;
    }

  
    /**
     * Este metodo regresa la lista de carreras que
     * se encuentra en la tabla Carrera
     * @return Lista de objetos de clase Carrera
     */
    public List<Carrera> obtenCarreras() {
        List<Carrera> datos = 
                new ArrayList<Carrera>();
        try {
            datos=cCarrera.findAll_XML(tipoListaCarrera);
            
            }
        
        catch (ClientErrorException e) {
            System.err.println("Error al obtener carreras:"+e.getMessage());
        }
        catch(NullPointerException e){
            System.err.println("Carreras vacias? "+e.getMessage());
        }
        return datos;
    }  

    /**
     * Muestra las materias del tab de Materias
     *
     * @return lista de materias
     */
    public List<Materia> obtenMaterias() {
        List<Materia> datos = new ArrayList<Materia>();
        try {
            datos=cMateria.findAll_XML(tipoListaMateria);
            }
         catch (ClientErrorException e) {
            System.err.println("Error al obtener las materias:"
                    + e.getMessage());
        }
        return datos;
    }

    /**
     * Metodo que obtiene las carreras que van a ser usadas en el cambo box de
     * materia
     *
     * @param ClaveMat la clave de la materia
     * @return regresa las carreras del combo box del panel de materia,
     */
    public String obtenCarreraMateria(String claveMat) {
        StringBuilder datos = new StringBuilder();
        String clave = "";
        //System.out.println(claveMat);
        try {
            clave=cMateria.obtenNombreCarrera(claveMat);

            

        } catch (ClientErrorException e) {
            System.err.println("Error al obtener la carrera de la materia:"
                    + e.getMessage());
        } catch (NullPointerException npe) {

        }

        return clave;
    }
    /*
     * @param ClaveMat la clave de la materia
     * @return regresa las carreras del combo box del panel de materia,
     */

     /**
     * Este metodo elimina un registro de la tabla Carrera
     * @param clave Clave de la carrera a eliminar
     * @return true si se pudo eliminar o false en caso contrario
     */
    public boolean eliminaCarrera(
            String clave) {
        boolean resultado = false;
        
        try {
            resultado=cCarrera.remove(clave);
        }
        catch (ClientErrorException ex) {
            System.err.println("Error al eliminar carrera:"+
                      ex.getMessage());
        }
        catch(NullPointerException e){
            
        }
        return resultado;
    } 
     /**
     * Este metodo agrega un nuevo registro a la tabla Carrera
     * @param c Objeto de clase Carrera con la informacion a agregar
     * @return true si se pudo agregar o false en caso contrario
     */
    public boolean agregaCarrera(
            Carrera c) {
        boolean resultado = false;
        
        try {
            resultado=cCarrera.create_XML(c);
        }
        catch (ClientErrorException ex) {
            System.err.println("Error al agregar carrera:"+
                      ex.getMessage());
        }
        return resultado;
    }

    /**
     * Metodo que nos ayuda a modificar una carrera de la tabla de la base de
     * datos ControlEsolar
     *
    /**
     * Este metodo modifica un registro de la tabla Carrera
     * @param c Objeto de clase Carrera con la informacion a modificar
     * @return true si se pudo modificar o false en caso contrario
     */
    public boolean modificaCarrera(
            Carrera c) {
        boolean resultado = false;
        
        try {
            resultado=cCarrera.edit_XML(c,c.getClaveCarrera());
        }
        catch (ClientErrorException ex) {
            System.err.println("Error al modificar carrera:"+
                      ex.getMessage());
        }
        return resultado;
    } 

      /**
     * Este metodo agrega un nuevo registro a la tabla Carrera
     * @param c Objeto de clase Carrera con la informacion a agregar
     * @return true si se pudo agregar o false en caso contrario
     */
    public boolean agregarCarrera(
            Carrera c) {
        boolean resultado = false;
        
        try {
            resultado=cCarrera.create_XML(c);
        }
        catch (ClientErrorException ex) {
            System.err.println("Error al agregar carrera:"+
                      ex.getMessage());
        }
        return resultado;
    } 
       public boolean agregaMateria(Materia m) {

        boolean resultado = false;

        try {
           resultado=cMateria.create_XML(m);
        } catch (ClientErrorException ex) {
            System.err.println("Error al "
                    + "agregar Materias:"
                    + ex.getMessage());
            resultado = false;
        } catch (NullPointerException npe) {

        }

        return resultado;

    }

    /**
     * Borra una Materia de la base de datos a partir de una cadena que
     * representa la clave de la materia en la base de datos de la tabla materia
     *
     * @param clave Es la clave de la materia, es una cadena que trae consigo
     * dicho valor
     * @return falso si no se pudo, verdadero si se pudo
     */
    public boolean eliminaMateria(String clave) {

        boolean resultado = false;
        try {
            resultado=cMateria.remove(clave);
        } catch (ClientErrorException ex) {
            System.err.println("Error al eliminar carreras:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }

        

        return resultado;

    }

    /**
     * Metodo que modifica una materia de la base de datos
     *
     * @param c Materia
     * @return falso si no se pudo, true si se pudo
     */
    public boolean modificaMateria(Materia m) {

        boolean resultado = false;

        try {
            resultado=cMateria.edit_XML(m,m.getClaveCarrera());
            
        } catch (ClientErrorException ex) {
            System.err.println("Error al "
                    + "modificar materias:"
                    + ex.getMessage());
            resultado = false;
        }
        System.out.println(resultado);

        return resultado;

    }
    //Metodos de PeridoEscolar
    /**
     * Obtiene una lista de los periodos escolares de la base de datos de
     * PeriodoEscolar
     *
     * @return obtiene la lista de objetos periodos escolares para llenar la
     * lista de Perido Escolar
     */
    public List<PeriodoEscolar> obtenPeriodos() {
        List<PeriodoEscolar> datos = new ArrayList<PeriodoEscolar>();
        try {
            datos=cPeriodo.findAll_XML(tipoListaPeriodo);
            
        } catch (ClientErrorException e) {
            System.err.println("Error al obtener los periodos: "
                    + e.getMessage());
        }
        return datos;
    }

    /**
     * Obetemos el siguiente id del periodo, para que el id del periodo no lo
     * pueda modificar el usuario
     *
     * @return idConvertido un entero con el valor del iD +1
     */
    public Short obtenSigPeriodo() {
        
        String id = "";
        
       
        
        List<PeriodoEscolar> p=new ArrayList();
        Short idConvertido=0;
        
        try {
            //p=cPeriodo.obtenSigPeriodo(tipoListaPeriodo);
            /*
            for (PeriodoEscolar periodoEscolar : p) {
                idConvertido=periodoEscolar.getPeriodo();
                
            }
            */
            idConvertido=cPeriodo.obtenSigPeriodo();
            
            
           
            
            
            
            
            
        } catch (ClientErrorException ex) {
            System.err.println("Error al "
                    + "agregar el siguiente id de los Periodos:"
                    + ex.getMessage());
        }
        return idConvertido;

    }

    /**
     * Metodo que agrega un PeriodoEscolar a la base de datos
     *
     * @param p Objeto de PeriodoEscolar
     * @return falso si se puede, true si no se puede
     */
    public boolean agregaPeriodo(PeriodoEscolar p) {

        boolean resultado = false;

        try {
            if (p.getYear() < 2000 || p.getYear() > 2014) {

                resultado = false;
                System.err.print("Año invalido");

            } else {
                try {
                   resultado=cPeriodo.create_XML(p);
                   
                } catch (ClientErrorException ex) {
                    System.err.println("Error al "
                            + "agregar Periodos:"
                            + ex.getMessage());
                } catch (NullPointerException npe) {

                }

            }

        } catch (NullPointerException npe) {

        }

        return resultado;

    }

    /**
     * Metodo que elimina un periodo escolar de la base de datos
     *
     * @param id Id del periodo escolar que se va eliminar
     * @return true si se pudo eliminar, false si no se pudo eliminar
     */
    public boolean eliminaPeriodo(Integer id) {

        boolean resultado = false;
        

        try {
            resultado=cPeriodo.remove(id.toString());
            
        } catch (ClientErrorException ex) {
            System.err.println("Error al eliminar Periodo:"
                    + ex.getMessage());
            resultado = false;
        } catch (NullPointerException npe) {

        }

        return resultado;
    }

    /**
     * Metodo que modifica un periodo escolar de la base de datos
     *
     * @param p PeriodoEscolar
     * @return falso si no se pudo modificar, verdadero si no se pudo modificar
     */
    public boolean modificaPeriodo(PeriodoEscolar p) {

        boolean resultado = false;
        try {
            if (p.getYear() < 2000 || p.getYear() > 2014) {

                resultado = false;
                System.err.print("Año invalido");

            } else {
                try {
                    resultado=cPeriodo.edit_XML(p, p.getIdPeriodo().toString());
                    
                } catch (ClientErrorException ex) {
                    System.err.println("Error al "
                            + "modificar periodos:"
                            + ex.getMessage());
                    resultado = false;
                } catch (NullPointerException npe) {
                    System.out.println("Error");

                }

            }

        } catch (NullPointerException npe) {

        }

        return resultado;

    }

    /**
     * Metodo que obtiene un string dependiendo del parametro periodo que se le
     * pase
     *
     * @param p Periodo
     * @return Regresa un string que representa el periodo seleccionado en la
     * gui
     */
    public String obtenPeriodoString(short p) {

        String perString = null;
        if (p == 1) {
            perString = "Agosto-Diciembre";

        } else if (p == 2) {
            perString = "Enero-Junio";
        } else if (p == 3) {
            perString = "Verano";
        }

        return perString;

    }

    /**
     * Metodo que llena el combo box de PeriodoEscolar del tab PeriodoEscolar
     *
     * @return Obtiene una lista de periodos para el combo box de periodo
     * escolar
     */
    public List<Short> periodoBox() {
        List<Short> datos = new ArrayList<Short>();
        
        
        short[] cortos=new short[3];
        
        try {
//            datos=cPeriodo.periodoBox();
            
            for (Short i = 1; i <=3; i++) {
                datos.add(i);
                
            }
        
            
              
            

            
        } catch (ClientErrorException e) {
            System.err.println("Error al obtener los periodos: "
                    + e.getMessage());
        } catch (NullPointerException npe) {

        }

        return datos;
    }

    /**
     * Metodo que obtiene una lista de estudiantes de la tabla Estudiante de la
     * base de datos de control escolar
     *
     * @return regresa una lista de estudiantes extraidas de la base de datos
     */
    public List<Estudiante> obtenEstudiantes() {

        List<Estudiante> datos = new ArrayList<Estudiante>();
        try {
            datos=cEstudiante.findAll_XML(tipoListaEstudiante);
           

        } catch (ClientErrorException e) {
            System.err.println("Error al obtener a los estudiantes :"
                    + e.getMessage());
        } catch (NullPointerException npe) {

        }
        return datos;
    }

    /**
     * Metodo agrega un estudiante a la base de datos
     *
     * @param e Estudiante
     * @return True si se pudo agregar al estudiante, false si no se pudo
     * agregar el estudiante
     */
    public boolean agregaEstudiante(Estudiante e) {

        boolean resultado = false;

        try {
            resultado=cEstudiante.create_XML(e);
        } catch (ClientErrorException ex) {
            System.err.println("Error al "
                    + "agregar al estudiante:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {
            System.err.println(" " + npe);
        }

        return resultado;

    }

    /**
     * Borra un Estudiante mediante la matricula
     *
     * @param clave Matricula del estudiante
     * @return falso si no se pudo, verdadero si se pudo
     */
    public boolean eliminaEstudiante(String clave) {

        boolean resultado = false;
       

        try {
            resultado=cEstudiante.remove(clave);
        } catch (ClientErrorException ex) {
            System.err.println("Error al eliminar al estudiante:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {
            System.err.println(" " + npe);
        }

        return resultado;

    }

    /**
     * Modifica un estudiante de la base de datos
     *
     * @param e Estudiante
     * @return True si se pudo agregar al estudiante, false si no se pudo
     * agregar el estudiante
     */
    public boolean modificaEstudiante(Estudiante e) {

        boolean resultado = false;

        try {
            resultado=cEstudiante.edit_XML(e, e.getMatricula());
        } catch (ClientErrorException ex) {
            System.err.println("Error al "
                    + "modificar al estudiante:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }

        return resultado;

    }

    /**
     * Metodo que nos regresa el nombre de un municipio de la base de datos
     *
     * @param claveMat id del municipio
     * @return el nombre del municipio del id seleccionado
     */
    
    public String obtenNombreMunicipio(Integer claveMat) {
        Municipio m=new Municipio();
        m.setIdMunicipio(claveMat);
        String clave = "";
        System.out.println(claveMat);
        
        
        
        try {
            
            
           m=cMunicipio.find_XML(m, claveMat.toString());
           clave=m.getNombreMunicipio();
            System.out.println("clave: "+clave);
  

        } catch (ClientErrorException e) {
            System.err.println("Error al obtener el nombre del municipio:"
                    + e.getMessage());
        } catch (NullPointerException npe) {
            System.err.println(" " + npe);
        }
        

        return clave;
        

        
    }
    

    /**
     * Metodo que nos regresa el nombre de un estado de la base de datos
     *
     * @param claveMat id del estado
     * @return regresa el nombre del estado seleccionado
     */
    public String obtenNombreEstado(short claveMat) {
       
        String clave = "";
        //System.out.println(claveMat);
        try {
            clave=cEstado.obtenNombreEstado(claveMat);

        } catch (ClientErrorException e) {
            System.err.println("Error al obtener el nombre del estado:"
                    + e.getMessage());
        } catch (NullPointerException npe) {
            System.err.println(" " + npe);
        }

        return clave;
    }

    /**
     * Metodo que elimina una carga de estudiante de la base de datos
     *
     * @param ce Carga Estudiante
     * @return falso si no se pudo eliminar la carga, verdadero si se pudo
     * eliminar la carga
     */
    public boolean eliminaCargaEstudiante(CargaEstudiante ce) {

        boolean resultado = false;
        Integer clave = null;

        
        try {
            resultado=cLCEstudiante.remove(ce.getIdCargaEstudiante().toString());
        } catch (ClientErrorException ex) {
            System.err.println("Error al eliminar la carga del estudiante:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }

        return resultado;

    }

    /**
     * Metodo que agrega una carga de estudiante a la base de datos de control
     * escolar
     *
     * @param cEstu Carga estudiante
     * @return falso si no se pudo agregar, verdadero si se pudo agregar
     */
    public boolean agregaCargaEstudiante(CargaEstudiante cEstu) {

        boolean resultado = false;

        try {
            resultado=cLCEstudiante.create_XML(cEstu);
            
        } catch (ClientErrorException ex) {
            System.err.println("Error al "
                    + "agregar la carga de estudiante:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {
            System.err.println(" " + npe);
        }

        return resultado;

    }

    /**
     * Metodo que obtiene el nombre de la materia de la CargaEstudiante para el
     * combo box del panel CargaEstudiante
     *
     * @param claveCarrera la clave de la carrera se va a comparar para tener el
     * nombre de la materia
     * @return una lista con el nombre de las materias
     */
    public List<Materia> obtenerCargaMateria(String claveCarrera) {
        List<Materia> datos = new ArrayList<Materia>();

        try {
           datos=cMateria.obtenNombreMateria_XML(tipoListaMateria, claveCarrera);

            

        } catch (ClientErrorException ex) {
            System.err.println("Error al obtener las materias:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }

        return datos;
    }

    /**
     * Metodo que obtiene el id_carga_estudiante e incrementarla para agregar
     * una nueva CargaEstudiante
     *
     * @return un entero con el id incrementado de la carga estudiante
     */
    public Integer obtenSigIdCargaEstudiante() {
        String incrementador = "SELECT MAX(id_carga_estudiante)+1 FROM CargaEstudiante";
        //String id = "";
        Integer idConvertido = null;
        try {
            ResultSet rs;
            rs = stmt.executeQuery(incrementador);
            while (rs.next()) {
                idConvertido = rs.getInt(1);
            }

        } catch (SQLException ex) {
            System.err.println("Error al "
                    + "agregar el siguiente id de los Periodos:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }

        return idConvertido;

    }

    /**
     * Metodo que obtiene la Carga Esutudiante de un estudiante a partir de su
     * Matricula
     *
     * @param cargaEstu
     * @return Regresa una lista con los datos para la lista de carga estudiante
     */
    public List<ListaCargaEstudiante> obtenCargaEstudiante(CargaEstudiante cargaEstu) {
        List<ListaCargaEstudiante> datos = new ArrayList<ListaCargaEstudiante>();
       
        String matricula = null;
      
        matricula = cargaEstu.getMatricula();
        System.out.println("matricula: "+matricula);
        try {
            datos=cLCEstudiante.encuentraListaCarga_XML(tipoListaCargaEstudiante
                    , matricula);


        } catch (ClientErrorException e) {
            System.err.println("Error al obtener la carga de estudiante: "
                    + e.getMessage());
        } catch (NullPointerException npe) {

        }
        return datos;
    }

    /**
     * Metodo que obtiene el nombre de una carrera a partir de un string que
     * simboliza un nombre de luna materia de la base de datos
     *
     * @param nombreMateria una cadena que simboliza el nombre de la materia de
     * la base de datos
     * @return una cadena con el nombre de la materia
     */
    public String obtenerNombreCarrera(String nombreMateria) {
        String clave = null;
        String nombre = null;

        try {
           nombre=cMateria.obtenNombreCarreraCB(nombreMateria);

        } catch (ClientErrorException e) {
            System.err.println("Error al obtener la carga de estudiante: "
                    + e.getMessage());
        } catch (NullPointerException npe) {

        }
        return nombre;
    }

}
