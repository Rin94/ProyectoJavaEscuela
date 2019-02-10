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
 * Clase que ofrece los servicios que permiten usar la tabla estudiante de la base de datos
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@Stateless
@Path("estudiante")
public class EstudianteFacadeREST extends AbstractFacade<Estudiante> {
    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;
    /**
     * Contructor vacio
     */
    public EstudianteFacadeREST() {
        super(Estudiante.class);
    }
    /**
     * Metodo que crea un estudiante de la tabla estudiante de la base de datos
     * @param entity objeto estudiante que se va crear
     */
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
    /**
     * Metodo que modifica un estudiante de la base de datos
     * @param id matricula
     * @param entity objeto modificado
     */
    @PUT
    @Path("modificaestudiante/{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") String id, Estudiante entity) {
        try {
            super.edit(entity);
            
        } catch (ServerErrorException e) {
        }
        
    }
    /**
     * Metodo que elimina un registro de estudiante de la base de datos
     * @param id matricula
     */
    @DELETE
    @Path("eliminaestudiante/{id}")
    public void remove(@PathParam("id") String id) {
        try {
            getEntityManager().createQuery("DELETE FROM CargaEstudiante c WHERE c.matricula= :matricula ")
                    .setParameter("matricula", id).executeUpdate();
            super.remove(super.find(id));
            
        } catch (ServerErrorException e) {
        }
        
    }

    
    /**
     * Metodo que encuentra todos los estudiantes de la base de datos 
     * @return una lista de estudiantes
     */
    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Estudiante> findAll() {
        return super.findAll();
    }

   

    
    /**
     * Metodo que permite manejar sentencias sql 
     * @return em
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

    

