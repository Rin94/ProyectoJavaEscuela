/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.servicios;

import edu.uaz.poo2.entidadesservidor.Estudiante;
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
@Path("estudiante")
public class EstudianteFacadeREST extends AbstractFacade<Estudiante> {
    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;

    public EstudianteFacadeREST() {
        super(Estudiante.class);
    }

    @POST
    @Override
    @Path("agregarestudiante")
    @Consumes({"application/xml", "application/json"})
    public void create(Estudiante entity) {
        try {
            super.create(entity);
            
        } catch (ServerErrorException e) {
        }
        
    }

    @PUT
    @Path("modificaestudiante/{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Estudiante entity) {
        try {
            super.edit(entity);
            
        } catch (ServerErrorException e) {
        }
        
    }

    @DELETE
    @Path("eliminaestudiante/{id}")
    public void remove(@PathParam("id") String id) {
        try {
            super.remove(super.find(id));
            
        } catch (ServerErrorException e) {
        }
        
    }

    

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Estudiante> findAll() {
        return super.findAll();
    }

   

    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

    

