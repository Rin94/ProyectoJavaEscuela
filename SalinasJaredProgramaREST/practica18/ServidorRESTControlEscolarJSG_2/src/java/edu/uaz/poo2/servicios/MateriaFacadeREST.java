/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.servicios;

import edu.uaz.poo2.entidadesservidor.Carrera;
import edu.uaz.poo2.entidadesservidor.Materia;
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
 *
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@Stateless
@Path("materia")
public class MateriaFacadeREST extends AbstractFacade<Materia> {
    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;

    public MateriaFacadeREST() {
        super(Materia.class);
    }

    @POST
    @Override
    @Path("agregarmateria")
    @Consumes({"application/xml", "application/json"})
    public void create(Materia entity) {
        try {
            super.create(entity);
            
        } catch (ServerErrorException e) {
        }
        
    }

    @PUT
    @Path("modificamateria/{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Materia entity) {
        try {
             super.edit(entity);
            
        } catch (ServerErrorException e) {
        }
       
    }

    @DELETE
    @Path("eliminamateria/{id}")
    public void remove(@PathParam("id") String id) {
        try {
            super.remove(super.find(id));
            
        } catch (Exception e) {
        }
        
    }

   

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Materia> findAll() {
        return super.findAll();
    }
    
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
            System.err.println("No hay materias en la base de datos");
        }
        catch (ServerErrorException e) {
            System.err.println("Hubo problemas con el servidor");
        }
        return res;
        
        
    }
    

   

   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
