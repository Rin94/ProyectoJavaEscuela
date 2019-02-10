/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.servicios;

import edu.uaz.poo2.entidadesservidor.Estado;
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
 * Metodo que da los servicios de la tabla estado de la base de datos control escolar
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@Stateless
@Path("estado")
public class EstadoFacadeREST extends AbstractFacade<Estado> {
    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;
    
    /**
     * Contructor vacio de la clase estado facade rest
     */
    public EstadoFacadeREST() {
        super(Estado.class);
    }

    /**
     * Metodo que encuentra todos los estados de la base datos
     * @return lista con los estados
     */
    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Estado> findAll() {
        return super.findAll();
    }/**
     * Metodo que obtienen el nombre de estado de la base de datos
     * @param claveC ClaveEstado
     * @return  cadena con el valor del nombre del estado
     */
    @GET
   @Path("obtennombremunicipio/{claveEstado}")
   
   @Produces({"text/plain"})
   public String obtenNombreEstado(@PathParam("claveEstado") int claveC){
        String res="";
        List<String> valor;
        try {
            
            
            valor= getEntityManager().createQuery
        ("SELECT e.nombreEstado FROM Estado e WHERE e.idEstado= :clave").setParameter("clave", claveC).getResultList();
            for (String k: valor) {
                res=k;
                
            }
            
            
        } catch (NullPointerException e) {
            System.err.println("No hay ESTADOS en la base de datos");
        }
        catch (ServerErrorException e) {
            System.err.println("Hubo problemas con el servidor");
        }
        return res;
        
        
    }
     
    
   

   
   /**
    * Metodo que nos permite usar sentencias sql
    * @return  em
    */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
