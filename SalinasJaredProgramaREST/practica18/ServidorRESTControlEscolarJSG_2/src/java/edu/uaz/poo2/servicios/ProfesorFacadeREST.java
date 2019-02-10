/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.servicios;

import edu.uaz.poo2.entidadesservidor.Profesor;
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
 *
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@Stateless
@Path("profesor")
public class ProfesorFacadeREST extends AbstractFacade<Profesor> {
    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;

    public ProfesorFacadeREST() {
        super(Profesor.class);
    }

    @POST
    @Override
    @Path("agregarprofesor")
    @Consumes({"application/xml", "application/json"})
    public void create(Profesor entity) {
        try {
            super.create(entity);
            
        } catch (ServerErrorException e) {
        }
        
    }

    @PUT
    @Path("modificarprofesor/{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Profesor entity) {
        try {
            super.edit(entity);
            
        } catch (ServerErrorException e) {
            
        }
        
    }

    @DELETE
    @Path("eliminarprofesor/{id}")
    public void remove(@PathParam("id") String id) {
        try {
            super.remove(super.find(id));
            
        } catch (ServerErrorException e) {
        }
        
    }

   

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Profesor> findAll() {
        List<Profesor> profesores= new ArrayList();
        try {
             profesores= super.findAll();
            
        } catch (ServerErrorException e) {
        }
        catch (NullPointerException e) {
        }
        
        return profesores;
        
    }

    

    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
