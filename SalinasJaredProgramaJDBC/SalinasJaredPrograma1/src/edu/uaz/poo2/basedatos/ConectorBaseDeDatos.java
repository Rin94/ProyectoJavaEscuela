/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uaz.poo2.basedatos;

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

/**
 * Esta clase contiene los metodos necesarios para obtener/actualizar
 * informacion de la base de datos
 *
 * @author Roberto Solis Robles
 */
public class ConectorBaseDeDatos {

    private Connection conn;
    private Statement stmt;
    private Statement stmtAux;
    private String urlConexion;

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
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM Estado");
            while (rs.next()) {
                Short id = rs.getShort(1);
                String nombre = rs.getString(2);
                Estado es = new Estado(
                        id, nombre);
                resultado.add(es);
            }
        } catch (SQLException e) {
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
        List<Municipio> resultado
                = new ArrayList<Municipio>();
        try {
            ResultSet rs = stmt.executeQuery(
                    "SELECT * FROM Municipio WHERE "
                    + "id_municipio DIV 1000="
                    + idEstado);
            while (rs.next()) {
                Integer id = rs.getInt(1);
                String nombre = rs.getString(2);
                Municipio es = new Municipio(
                        id, nombre);
                resultado.add(es);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener municipios:"
                    + e.getMessage());
        } catch (NullPointerException npe) {

        }
        return resultado;
    }

    //LISTA DE CARRERAS
    /**
     * Metodo que obtiene las carreras de la base de datos
     *
     * @return regresa una lista de Carreras
     */
    public List<Carrera> obtenCarrera() {

        List<Carrera> datos = new ArrayList<Carrera>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Carrera");
            while (rs.next()) {
                String clave = rs.getString(1);
                String nombre = rs.getString(2);
                datos.add(new Carrera(clave, nombre));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener Carreras :"
                    + e.getMessage());
        } catch (NullPointerException npe) {

        }
        return datos;
    }

    /**
     * Muestra las materias del tab de Materias
     *
     * @return lista de materias
     */
    public List<Materia> obtenMateria() {
        List<Materia> datos = new ArrayList<Materia>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Materia");
            while (rs.next()) {
                String clave = rs.getString(1);
                String nombre = rs.getString(2);
                short semestre = rs.getShort(3);
                String claveCarrera = rs.getString(4);
                datos.add(new Materia(clave, nombre, semestre, claveCarrera));
            }
        } catch (SQLException e) {
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
            ResultSet rs = stmt.executeQuery("SELECT NombreCarrera FROM Carrera  WHERE ClaveCarrera='" + claveMat + "'");
            // String clave;
            while (rs.next()) {
                clave = rs.getString(1);
                //System.out.println(clave);

            }

        } catch (SQLException e) {
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
     * Este metodo elimina un registro de la tabla carrera
     *
     * @param clave Clave de la carrera a Eliminar
     * @return falso si no se pudo eliminar true si si se pudo
     */
    public boolean eliminaCarrera(String clave) {

        boolean resultado = false;
         try {
            String sentenciaSQLMateria="SELECT ClaveMateria FROM Materia WHERE ClaveCarrera='"
                    +clave+"'";
            ResultSet rsMateria=stmt.executeQuery(sentenciaSQLMateria);
            String sentenciaSQL="";
            while(rsMateria.next()){
                String materia=rsMateria.getString(1);
                sentenciaSQL = "DELETE FROM CargaEstudiante WHERE"
                    + " ClaveMateria='" + materia + "'";
                
            }
             
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al eliminar carreras:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }
        try {
            String sentenciaSQL = "DELETE FROM Materia WHERE"
                    + " ClaveCarrera='" + clave + "'";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al eliminar carreras:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }
       

        try {
            String sentenciaSQL = "DELETE FROM Carrera WHERE"
                    + " ClaveCarrera='" + clave + "'";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al eliminar carreras:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }

        return resultado;

    }

    /**
     * Metodo que nos ayuda a agregar una carrera a la base de datos
     *
     * @param carrera c objeto de la clase carrera id y nombre
     * @return regresa true si puede agregar la carrera, devuelve false si no
     * puede agregar carrera
     */
    public boolean agregaCarrera(Carrera c) {

        boolean resultado = false;

        try {
            String sentenciaSQL = "INSERT INTO Carrera VALUES('"
                    + c.getClaveCarrera() + "','" + c.getNombreCarrera() + "')";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al "
                    + "agregar carreras:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }

        return resultado;

    }

    /**
     * Metodo que nos ayuda a modificar una carrera de la base de datos
     *
     * @param carrera c objeto de la clase carrera id y nombre
     * @return regresa true si puede modificar la carrera, devuelve false si no
     * puede agregar carrera
     */
    public boolean modificarCarrera(Carrera c) {

        boolean resultado = false;

        try {
            String sentenciaSQL = "UPDATE Carrera SET "
                    + " NombreCarrera='"
                    + c.getNombreCarrera() + "'"
                    + "WHERE ClaveCarrera='" + c.getClaveCarrera() + "'";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al "
                    + "modificar carreras:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }

        return resultado;

    }

    /**
     * Metodo para agregar una materia a la base de datos
     *
     * @param m Materia que trae consigo
     * @return
     */
    public boolean agregaMateria(Materia m) {

        boolean resultado = false;

        try {
            String sentenciaSQL = "INSERT INTO Materia VALUES('"
                    + m.getClaveMateria() + "','" + m.getNombreMateria() + "',"
                    + m.getSemestre() + ",'" + m.getClaveCarrera() + "')";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al "
                    + "agregar Materias:"
                    + ex.getMessage());
             resultado=false;
        } catch (NullPointerException npe) {

        }

        return resultado;

    }

    /**
     * Borra una Materia apartir de la clave de la materia
     *
     * @param clave
     * @return falso si no se pudo, verdadero si se pudo
     */
    public boolean eliminaMateria(String clave) {

        boolean resultado = false;
        try {
            String sentenciaSQL = "DELETE FROM CargaEstudiante WHERE"
                    + " ClaveMateria='" + clave + "'";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al eliminar carreras:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }

        try {
            String sentenciaSQL = "DELETE FROM Materia WHERE"
                    + " ClaveMateria='" + clave + "'";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al eliminar Materia:"
                    + ex.getMessage());
        }

        return resultado;

    }

    /**
     * Metodo que modifica una materia de la base de datos
     * @param c Materia
     * @return falso si no se pudo, true si se pudo
     */
    public boolean modificarMateria(Materia m) {

        boolean resultado = false;

        try {
            String sentenciaSQL = "UPDATE Materia SET "
                    + "NombreMateria='"
                    + m.getNombreMateria() + "', Semestre=" + m.getSemestre() + ", ClaveCarrera='"
                    + m.getClaveCarrera() + "' "
                    + "WHERE ClaveMateria='" + m.getClaveMateria() + "'";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al "
                    + "modificar materias:"
                    + ex.getMessage());
             resultado=false;
        }
        System.out.println(resultado);

        return resultado;

    }
    //Metodos de PeridoEscolar
    /**
     * Obtiene una lista de los periodos escolares de la base de datos de
     * PeriodoEscolar
     *
     * @return obtiene la lista de periodos escolares
     */
    public List<PeriodoEscolar> obtenPeriodo() {
        List<PeriodoEscolar> datos = new ArrayList<PeriodoEscolar>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM PeriodoEscolar");
            while (rs.next()) {
                int idPeriodo = rs.getInt(1);
                short year = rs.getShort(2);
                short periodo = rs.getShort(3);
                //String claveCarrera=rs.getString(4);
                datos.add(new PeriodoEscolar(idPeriodo, year, periodo));
            }
        } catch (SQLException e) {
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
    public short obtenSigPeriodo() {
        String incrementador = "SELECT MAX(id_periodo)+1 FROM PeriodoEscolar";
        String id = "";
        short idConvertido;
        try {
            ResultSet rs;
            rs = stmt.executeQuery(incrementador);
            while (rs.next()) {
                id = rs.getString(1);
            }

        } catch (SQLException ex) {
            System.err.println("Error al "
                    + "agregar el siguiente id de los Periodos:"
                    + ex.getMessage());
        }
        return idConvertido = Short.parseShort(id);

    }

    /**
     * Metodo que agrega un PeriodoEscolar a la base de datos
     *
     * @param pe Objeto de PeriodoEscolar
     * @return falso si se puede, true si no se puede
     */
    public boolean agregaPeriodo(PeriodoEscolar pe) {

        boolean resultado = false;

        try {
            String sentenciaSQL = "INSERT INTO PeriodoEscolar VALUES("
                    + pe.getIdPeriodo() + "," + pe.getYear() + "," + pe.getPeriodo() + ")";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al "
                    + "agregar Periodos:"
                    + ex.getMessage());
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
            String sentenciaSQL = "DELETE FROM CargaEstudiante WHERE"
                    + " id_periodo=" + id + "";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al eliminar el periodo:"
                    + ex.getMessage());
            resultado=false;
        } catch (NullPointerException npe) {

        }
        

        try {
            String sentenciaSQL = "DELETE FROM PeriodoEscolar WHERE"
                    + " id_periodo=" + id + "";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al eliminar Periodo:"
                    + ex.getMessage());
             resultado=false;
        } catch (NullPointerException npe) {

        }

        return resultado;
    }

    /**
     * Metodo que modifica un periodo escolar de la base de datos
     *
     * @param pe PeriodoEscolar
     * @return falso si no se pudo modificar, verdadero si no se pudo modificar
     */
    public boolean modificarPeriodo(PeriodoEscolar pe) {

        boolean resultado = false;

        try {
            String sentenciaSQL = "UPDATE PeriodoEscolar SET "
                    + "Year=" + pe.getYear() + ", Periodo="
                    + pe.getPeriodo() + " "
                    + "WHERE id_periodo=" + pe.getIdPeriodo() + "";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al "
                    + "modificar periodos:"
                    + ex.getMessage());
             resultado=false;
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
        try {
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT Periodo from PeriodoEscolar");
            while (rs.next()) {

                short periodo = rs.getShort(1);
                String per = obtenPeriodoString(periodo);
                //String claveCarrera=rs.getString(4);
                datos.add(periodo);

            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los periodos: "
                    + e.getMessage());
        } catch (NullPointerException npe) {

        }

        return datos;
    }

    /**
     * Estudiante
     */
    /**
     * Metodo que obtiene una lista de estudiantes de la tabla Estudiante
     *
     * @return regresa una lista de estudiantes extraidas de la base de datos
     */
    public List<Estudiante> obtenEstudiante() {

        List<Estudiante> datos = new ArrayList<Estudiante>();
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Estudiante");
            while (rs.next()) {
                String matricula = rs.getString(1);
                String nombre = rs.getString(2);
                String apPaterno = rs.getString(3);
                String apMaterno = rs.getString(4);
                String calle = rs.getString(5);
                String colonia = rs.getString(6);
                String codPostal = rs.getString(7);
                String telefono = rs.getString(8);
                String email = rs.getString(9);
                short idEstado = rs.getShort(10);
                int idMunicipio = rs.getInt(11);
                datos.add(new Estudiante(matricula, nombre, apPaterno, apMaterno, calle, colonia,
                        codPostal, telefono, email, idEstado, idMunicipio));
            }

        } catch (SQLException e) {
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
            String sentenciaSQL = "INSERT INTO Estudiante ( Matricula, Nombre, ApPaterno"
                    + ", ApMaterno, Calle, Colonia, CodPostal, Telefono, Email,"
                    + " id_estado, id_municipio) VALUES('" + e.getMatricula() + "', '"
                    + e.getNombre() + "', '" + e.getApPaterno() + "', '" + e.getApMaterno() + "', '"
                    + e.getCalle() + "', '" + e.getColonia() + "', '" + e.getCodPostal() + "', '"
                    + e.getTelefono() + "', '" + e.getEmail() + "', " + e.getIdEstado() + ",  "
                    + e.getIdMunicipio() + " )";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
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
            String sentenciaSQL = "DELETE FROM CargaEstudiante WHERE"
                    + " Matricula='" + clave + "'";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al eliminar al estudiante:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {
            System.err.println(" " + npe);
        }

        try {
            String sentenciaSQL = "DELETE FROM Estudiante WHERE"
                    + " Matricula='" + clave + "'";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
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
    public boolean modificarEstudiante(Estudiante e) {

        boolean resultado = false;

        try {
            String sentenciaSQL = "UPDATE Estudiante SET  Nombre=" + "'" + e.getNombre() + "', ApPaterno='"
                    + e.getApPaterno() + "', ApMaterno='" + e.getApMaterno() + "', Calle='" + e.getCalle() + "', Colonia='"
                    + e.getColonia() + "', CodPostal='" + e.getCodPostal() + "', Telefono='" + e.getTelefono() + "', Email='"
                    + e.getEmail() + "', id_estado=" + e.getIdEstado() + ", id_municipio=" + e.getIdMunicipio() + " WHERE Matricula='"
                    + e.getMatricula() + "' ";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
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
    public String obtenNombreMunicipio(int claveMat) {
        StringBuilder datos = new StringBuilder();
        String clave = "";
        //System.out.println(claveMat);
        try {
            ResultSet rs = stmt.executeQuery("SELECT nombre_municipio FROM Municipio"
                    + "  WHERE id_municipio=" + claveMat + "");
            // String clave;
            while (rs.next()) {
                clave = rs.getString(1);
                //System.out.println(clave);

            }

        } catch (SQLException e) {
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
        StringBuilder datos = new StringBuilder();
        String clave = "";
        //System.out.println(claveMat);
        try {
            ResultSet rs = stmt.executeQuery("SELECT nombre_estado FROM Estado"
                    + "  WHERE id_estado=" + claveMat + "");
            // String clave;
            while (rs.next()) {
                clave = rs.getString(1);
                //System.out.println(clave);

            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el nombre del estado:"
                    + e.getMessage());
        } catch (NullPointerException npe) {
            System.err.println(" " + npe);
        }

        return clave;
    }

    
       

    /**
     *Metodo que elimina una carga de estudiante de la base de datos
     * @param ce
     * @return falso si no se pudo eliminar la carga, verdadero si se pudo eliminar la carga
     */

    public boolean eliminaCargaEstudiante(CargaEstudiante ce) {

        boolean resultado = false;
        Integer clave = null;
       
        String sentenciaSQL = "DELETE FROM CargaEstudiante WHERE"
                + " id_carga_estudiante=" + ce.getIdCargaEstudiante() + "";
        try {
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al eliminar la carga del estudiante:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }

        return resultado;

    }
    /**
     * Metodo que agrega una carga de estudiante a la base de datos de control escolar
     * @param cEstu Carga estudiante
     * @return falso si no se pudo agregar, verdadero si se pudo agregar
     */
    public boolean agregaCargaEstudiante(CargaEstudiante cEstu){
        
        boolean resultado = false;

        try {
            String sentenciaSQL = "INSERT INTO CargaEstudiante (id_carga_estudiante,Matricula,ClaveMateria, id_periodo"
                    +") VALUES('"+cEstu.getIdCargaEstudiante()+"', '"+cEstu.getMatricula()+"', "
                    + "'"+cEstu.getClaveMateria()+"', "+cEstu.getIdPeriodo()+" )";
            int renglonesAfectados
                    = stmt.executeUpdate(sentenciaSQL);
            if (renglonesAfectados == 1) {
                resultado = true;
            }
        } catch (SQLException ex) {
            System.err.println("Error al "
                    + "agregar la carga de estudiante:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {
            System.err.println(" " + npe);
        }

        return resultado;
        
    }
    /**
     * Metodo  que obtiene el nombre de la materia de la CargaEstudiante para el combo box del panel CargaEstudiante
     * @param claveCarrera la clave de la carrera se va a comparar para tener el nombre de la materia
     * @return una lista con el nombre de las materias
     */
    
    public List<Materia> obtenerCargaMateria(String claveCarrera){
         List<Materia> datos = new ArrayList<Materia>();
        
        try{
            ResultSet rs = stmt.executeQuery("select NombreMateria, ClaveMateria from Materia where "
                   
                     +"ClaveCarrera='"+claveCarrera+"'" );
            while(rs.next()){
                
                String nombreMateria=rs.getString(1);
                String claveMateria=rs.getString(2);
                datos.add(new Materia(claveMateria,nombreMateria));
                
                
            }
            
            
            
        } catch (SQLException ex) {
            System.err.println("Error al obtener las materias:"
                    + ex.getMessage());
        } catch (NullPointerException npe) {

        }
        
        
        return datos;
    }
    /**
     * Metodo que obtiene el id_carga_estudiante e incrementarla para agregar una nueva CargaEstudiante
     * @return un entero con el id incrementado de la carga estudiante
     */
      public Integer obtenSigIdCargaEstudiante() {
        String incrementador = "SELECT MAX(id_carga_estudiante)+1 FROM CargaEstudiante";
        //String id = "";
        Integer idConvertido=null;
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
        }catch (NullPointerException npe) {

        }
       
        return idConvertido;

    }
     
      /**
       * Metodo que obtiene la Carga Esutudiante de un estudiante a partir de su Matricula
       * @param cargaEstu
       * @return Regresa una lista con los datos para la lista de carga estudiante
       */
       public List<ListaCargaEstudiante> obtenCargaEstudiante(CargaEstudiante cargaEstu) {
        List<ListaCargaEstudiante> datos = new ArrayList<ListaCargaEstudiante>();
        Integer clave = null;
        String matricula = null;
        //Integer periodo = null;
        String nombreMat = null;
        Integer year=null;
        Short periodo=null;
        String nombreMateria=null;
        matricula=cargaEstu.getMatricula();
        try{
            String sentencia="select CargaEstudiante.id_carga_estudiante, PeriodoEscolar.Year, PeriodoEscolar.Periodo, " +
                    "Materia.NombreMateria from CargaEstudiante inner join PeriodoEscolar,"
                    + " Materia where CargaEstudiante.Matricula=\""+cargaEstu.getMatricula()+"\" and"
                    + " PeriodoEscolar.id_periodo=CargaEstudiante.id_periodo and"
                    + " Materia.ClaveMateria=CargaEstudiante.ClaveMateria";
            ResultSet rs=stmt.executeQuery(sentencia);
            while(rs.next()){
                clave=rs.getInt(1);
                year=rs.getInt(2);
                periodo=rs.getShort(3);
                nombreMat=rs.getString(4);
                
                datos.add(new ListaCargaEstudiante(clave,nombreMat,periodo,year));
                
            }
            //datos.add(new CargaEstudiante(clave,nombreMateria,periodo,year));
            
            
                    
           
        } catch (SQLException e) {
            System.err.println("Error al obtener la carga de estudiante: "
                    + e.getMessage());
        } catch (NullPointerException npe) {

        }
        return datos;
    }
       /**
        * Metodo que obtiene el nombre de una carrera a partir de un string que simboliza 
        * un nombre de luna materia  de la base de datos
        * @param nombreMateria una cadena que simboliza el nombre de la materia de la base de datos
        * @return una cadena con el nombre de la materia
        */
     
       public String obtenerNombreCarrera(String nombreMateria){
           String clave=null;
           String nombre=null;
           
           try{
               String sentencia="SELECT ClaveCarrera from Materia where NombreMateria='"+
                       nombreMateria+"'";
               ResultSet rs=stmt.executeQuery(sentencia);
               while(rs.next()){
                   clave=rs.getString(1);
                   
               }
               String sentenciaNueva="Select NombreCarrera from Carrera where ClaveCarrera='"+
                       clave+"'";
               ResultSet rsNuevo=stmt.executeQuery(sentenciaNueva);
               while(rsNuevo.next()){
                   nombre=rsNuevo.getString(1);
                   
               }
               
               
           } catch (SQLException e) {
            System.err.println("Error al obtener la carga de estudiante: "
                    + e.getMessage());
           } catch (NullPointerException npe) {

           }
           return nombre;
       }
     


}
