/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uaz.poo2.servicios;

import edu.uaz.poo2.entidadesservidor.CargaEstudiante;
import edu.uaz.poo2.entidadesservidor.ListaCargaEstudiante;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;

/**
 * Clase que ofrece los servicios relacionados a la clase entidas carga estudiante y lista 
 * carga estudiante
 * @author Lic. Jared Daniel Salinas Gonzalez
 */

@Stateless

@Path("cargaestudiante")
public class CargaEstudianteFacadeREST extends AbstractFacade<CargaEstudiante> {

    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;

    public CargaEstudianteFacadeREST() {
        super(CargaEstudiante.class);
    }
    
    /**
     * Metodo que crea un registro de la tabla carga estudiante
     * @param entity objeto de la clase CargaEstudiante
     */
    @POST
    @Override
    @Path("agregacargaestudiante")
    @Consumes({"application/xml", "application/json"})
    public void create(CargaEstudiante entity) {
        try {
            super.create(entity);

        } catch (Exception e) {
        }

    }
    /**
     * Metodo que elimina un registro de la tabla carga estudiante
     * @param id clave de la carga estudiante
     */
    @DELETE
    @Path("eliminacargaestudiante/{id}")
    public void remove(@PathParam("id") Integer id) {
        try {
            super.remove(super.find(id));

        } catch (ServerErrorException e) {
        }

    }
    /**
     * Metodo  que encuentra una lista de registros de CargaEstudiante
     * @return lista de objetos carga estudiante
     */
    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<CargaEstudiante> findAll() {
        return super.findAll();
    }
    /**
     * Metodo que encuentra una carga de estudiante a partir de si una matricula que se le da como
     * parametro coincide con el de un estudiante de la base de datos de control escolar
     * @param matricula cadena que representa la matricula de un estudiante
     * @return Una lista de cargas estudiantes
     */

    @GET
    //@Override
    @Path("obtencargaestudiante/{matricula}")
    @Produces({"application/xml", "application/json"})
    public List<CargaEstudiante> encuentraCarga(
            @PathParam("matricula") String matricula) {
        List<CargaEstudiante> resultado = null;

        try {
             //Integer valorMin=idEstado*1000;
            //Integer valorMax=(idEstado+1)*1000;
            resultado = getEntityManager().createQuery("SELECT c from CargaEstudiante c"
                    + " WHERE c.matricula=:Mat "
            )
                    .setParameter("Mat", matricula)
                    .getResultList();

        } catch (NullPointerException e) {
        } catch (ServerErrorException e) {
        }

        return resultado;
    }
    
    /**
     * Metodo que obtiene una lista de la clase entidad ListaCargaEstudiante
     * @param matricula cadena que representa una matricula de un estudiante de la base de datos
     * @return regresa una lista que lista carga estudiante
     */
    @GET
    //@Override
    @Path("obtenlistacargaestudiante/{matricula}")
    @Produces({"application/xml", "application/json"})
    public List<ListaCargaEstudiante> encuentraListaCarga(
            @PathParam("matricula") String matricula) {
        List<CargaEstudiante> resultado = null;
        List<ListaCargaEstudiante> lista=new ArrayList<ListaCargaEstudiante>();
        Integer idPeriodo = null;
        String claveMat;
        String claveCarr;
        String nombreCarrera = null;
        String nombreMateria = null;
        Short ano;
        Short periodo;

        try {
             //Integer valorMin=idEstado*1000;
            //Integer valorMax=(idEstado+1)*1000;
            resultado = getEntityManager().createQuery("SELECT c from CargaEstudiante c"
                    + " WHERE c.matricula=:Mat "
            )
                    .setParameter("Mat", matricula)
                    .getResultList();

            for (CargaEstudiante k : resultado) {
                idPeriodo = k.getIdPeriodo();
                periodo = (Short) getEntityManager().createQuery("SELECT p.periodo from PeriodoEscolar p where p.idPeriodo="
                        + " :clav ").setParameter("clav", idPeriodo).getSingleResult();
                ano = (Short) getEntityManager().createQuery("SELECT p.year from PeriodoEscolar p where p.idPeriodo="
                        + " :clav ").setParameter("clav", idPeriodo).getSingleResult();
                String perString = null;
                if (periodo == 1) {
                    perString = "Agosto-Diciembre";

                } else if (periodo == 2) {
                    perString = "Enero-Junio";
                } else if (periodo == 3) {
                    perString = "Verano";
                }

                claveMat = k.getClaveMateria();
                nombreMateria = (String) getEntityManager().createQuery("SELECT m.nombreMateria from Materia m where m.claveMateria="
                        + " :clav ").setParameter("clav", claveMat).getSingleResult();
                claveCarr = (String) getEntityManager().createQuery("SELECT m.claveCarrera from Materia m where m.claveMateria="
                        + " :clav ").setParameter("clav", claveMat).getSingleResult();
                nombreCarrera = (String) getEntityManager().createQuery("SELECT c.nombreCarrera from Carrera c where c.claveCarrera= :carr ").setParameter("carr", claveCarr).getSingleResult();
                //lista.add(new ListaCargaEstudiante(k.getIdCargaEstudiante(),nombreMateria,periodo,ano,k.getIdPeriodo(),k.getClaveMateria(),claveCarr));
                lista.add(new ListaCargaEstudiante(k.getIdCargaEstudiante(),nombreMateria,periodo,ano));
             /*int year,int idPeriodo, String claveMateria, String claveCarrera*/

            }

        } catch (NullPointerException e) {
        } 
        String sentencia = idPeriodo + " " + nombreCarrera;

        return lista;
    }
    /**
     * Metodo que obtiene el siguiente id de carga estudiante
     * @return regresa un entero con el valor del id carga estudiante
     */
     @GET
    @Path("buscasigidestudiante")
    
    @Produces({"text/plain"})
    public Integer obtenSigIDEstudiante(){
        int res=0;
        List<Integer> valor;
        try {
            
            
            valor= getEntityManager().createQuery
        ("SELECT MAX(c.idCargaEstudiante)+1 from CargaEstudiante c").getResultList();
            for (int k: valor) {
                res=k;
                
            }
            
            
        } catch (NullPointerException e) {
            System.err.println("No hay periodos escolares en la base de datos");
        }
        catch (ServerErrorException e) {
            System.err.println("Hubo problemas con el servidor");
        }
        return res;
        
        
    }
    
    /**
     * Metodo que nos permite usar sentencias que nos ayudan a manipular la base de datos
     * @return 
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
