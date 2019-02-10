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
 * Metodo que permite manipular la tabla profesor de la base de datos
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@Stateless
@Path("profesor")
public class ProfesorFacadeREST extends AbstractFacade<Profesor> {
    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;
    /**
     * Contructor vacio de la clase
     */
    public ProfesorFacadeREST() {
        super(Profesor.class);
    }
    /**
     * Metodo que crea un profesor de la base de datos 
     * @param entity profesor agregado
     */
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
    /**
     * Metodo que edita un profesor de la base de datos
     * @param id rfc
     * @param entity  profesor editado
     */
    @PUT
    @Path("modificarprofesor/{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Profesor entity) {
        try {
            super.edit(entity);
            
        } catch (ServerErrorException e) {
            
        }
        
    }
    /**
     * Metodo que elimina un profesor de la base de datos
     * @param id rfc
     */
    @DELETE
    @Path("eliminarprofesor/{id}")
    public void remove(@PathParam("id") String id) {
        try {
            super.remove(super.find(id));
            
        } catch (ServerErrorException e) {
        }
        
    }

   
    /**
     * Metodo que encuentra todos los profesores de la base datos
     * @return lista de profesores
     */
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

    

    
    /**
     * Metodo que permite usar sentenicias sql
     * @return em
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
