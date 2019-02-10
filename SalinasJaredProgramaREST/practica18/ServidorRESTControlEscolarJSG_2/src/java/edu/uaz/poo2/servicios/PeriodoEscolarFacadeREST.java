/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.servicios;

import edu.uaz.poo2.entidadesservidor.PeriodoEscolar;
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
@Path("periodoescolar")
public class PeriodoEscolarFacadeREST extends AbstractFacade<PeriodoEscolar> {
    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;

    public PeriodoEscolarFacadeREST() {
        super(PeriodoEscolar.class);
    }

    @POST
    @Override
    @Path("agregarperiodoescolar")
    @Consumes({"application/xml", "application/json"})
    public void create(PeriodoEscolar entity) {
        try {
            super.create(entity);
            
        } catch (ServerErrorException e) {
        }
        
    }

    @PUT
    @Path("modificarperiodoescolar/{id}")
    @Consumes({"application/xml", "application/json"})
    public void edit(@PathParam("id") Integer id, PeriodoEscolar entity) {
        try {
            super.edit(entity);
            
        } catch (ServerErrorException e) {
        }
        
    }

    @DELETE
    @Path("eliminarperiodoescolar/{id}")
    public void remove(@PathParam("id") Integer id) {
        try {
            super.remove(super.find(id));
            
        } catch (ServerErrorException e) {
        }
        
    }
    
    @GET
    @Path("buscasigidperiodo")
    
    @Produces({"text/plain"})
    public Integer obtenSigPeriodo(){
        int res=0;
        List<Integer> valor;
        try {
            
            String sql="SELECT MAX(p.idPeriodo)+1 from PeriodoEscolar p"; 
            valor= getEntityManager().createQuery(sql).getResultList();
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
    
    @GET
    @Path("periodobox")
    
    @Produces({"text/plain"})
    public Short periodoBox(){
        Short res=0;
        List<Short> valor;
        try {
            
             
            valor= getEntityManager().createQuery
        ("SELECT DISTINCT p.periodo from PeriodoEscolar p").getResultList();
            for (Short k: valor) {
                res=(k);
                
            }
            
            
        } catch (NullPointerException e) {
            System.err.println("No hay periodos escolares en la base de datos");
        }
        catch (ServerErrorException e) {
            System.err.println("Hubo problemas con el servidor");
        }
        return res;
        
        
    }
    
    
   

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<PeriodoEscolar> findAll() {
        return super.findAll();
    }

   
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
   

    
}