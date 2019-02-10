/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.servicios;

import edu.uaz.poo2.entidadesservidor.Carrera;
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
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;

/**
 * Clase que ofrece los servicios que permiten manipular y controlar la tabla de 
 * carrera de la base de datos de control escolar
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@Stateless
@Path("carrera")
public class CarreraFacadeREST extends AbstractFacade<Carrera> {
    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;
    /**
     * Constructor vacio la clase CarreraFacadeRest
     */
    public CarreraFacadeREST() {
        super(Carrera.class);
    }
    /**
     * Metodo que crea un registro de la tabla carrera de la base de datos
     * @param entity  objeto carrera que se va registrar
     */
    @POST
    @Override
    @Path("agregarcarrera")
    @Consumes({"application/xml", "application/json"})
    public void create(Carrera entity) {
        try {
            super.create(entity);
            
        } catch (ServerErrorException e) {
        }
        
    }
    /**
     * Metodo que modifica un registro de la tabla carrera
     * @param id claveCarrera 
     * @param entity Carrera que se va modificar y sus modificaciones
     */
    @PUT
    @Path("modificacarrera/{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Carrera entity) {
        try {
            super.edit(entity);
            
        } catch (ServerErrorException e) {
        }
        
    }
    /**
     * Metodo que elimina una carrera de la base de datos
     * @param id clavecarrera de la carrera a eliminar
     */
    @DELETE
    @Path("eliminacarrera/{id}")
    public void remove(@PathParam("id") String id) {
        try {
            /*
          String valor=(String) 
                  getEntityManager().createQuery(
                          "SELECT m.claveMateria from Materia m where m.claveCarrera= :cmat"
                  ).setParameter("cmat", id).getSingleResult();
          getEntityManager().createQuery(
                    "DELETE FROM CargaEstudiante c where c.claveMateria= :clave").setParameter("clave", valor)
                    .executeUpdate();
          
          
            getEntityManager().createQuery(
                    "DELETE FROM Materia m where m.claveCarrera= :clav")
            
                    .setParameter("clav", id).executeUpdate();
            */
             List <String> valor;
            String res=null;
            
           valor= 
                  getEntityManager().createQuery(
                          "SELECT m.claveMateria from Materia m where m.claveCarrera= :cmat"
                  ).setParameter("cmat", id).getResultList();
           for (String k:valor){
               res=valor.get(0);
               
           }
          
          getEntityManager().createQuery(
                    "DELETE FROM CargaEstudiante c where c.claveMateria= :clave").setParameter("clave",res )
                    .executeUpdate();
          
          
            getEntityManager().createQuery(
                    "DELETE FROM Materia m where m.claveCarrera= :clav")
                    .setParameter("clav", id).executeUpdate();
            
            super.remove(super.find(id));
            
        } catch (ServerErrorException e) {
        }
        
    }

   
    /**
     * Metodo que encuentra todas las carreras de la base de datos
     * @return lista de carreras
     */
    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Carrera> findAll() {
        return super.findAll();
    }
    /**
     * Metodo que obtiene el nombre de una carrera de la base de datos
     * @param claveC clave carrera
     * @return una cadena con el nombre de la carrera
     */
    @GET
    @Path("obtennombrecarrera/{claveCarrera}")
   
    @Produces({"text/plain"})
    public String obtenNombreCarrera(@PathParam("claveCarrera") String claveC){
        String res="";
        List<String> valor;
        try {
            
            
            valor= getEntityManager().createQuery
        ("SELECT c.nombreCarrera FROM Carrera c WHERE c.claveCarrera= :clave").setParameter("clave", claveC).getResultList();
            for (String k: valor) {
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
     * Metodo que permite usar sentencias sql
     * @return em
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

   
