/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.servicios;

import edu.uaz.poo2.entidadesservidor.Municipio;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;

/**
 *
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@Stateless
@Path("municipio")
public class MunicipioFacadeREST extends AbstractFacade<Municipio> {
    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;

    public MunicipioFacadeREST() {
        super(Municipio.class);
    }  

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Municipio find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Municipio> findAll() {
        return super.findAll();
    }
    @GET
    @Path("obtenmunicipios/{id}")
    @Produces({"application/xml", "application/json"})
    public List<Municipio> encuentraMunicipios(
            @PathParam("id") Integer idEstado) {
        List <Municipio> resultado;
        
        Integer valorMin=idEstado*1000;
        Integer valorMax=(idEstado+1)*1000;
        resultado=getEntityManager().createQuery("SELECT m from Municipio m"+
                " WHERE m.idMunicipio> :Min "
        +"AND m.idMunicipio< :Max")
                .setParameter("Min",valorMin)
                .setParameter("Max",valorMax).getResultList();
        
        
        
        return resultado;
    }
    
   @GET
   @Path("nombremunicipio/{id}") 
   @Produces({"text/plain"})
   public String obten(@PathParam("id") int claveC){
        String res="";
        List<String> valor;
        System.out.println(claveC);    
            valor= getEntityManager().createQuery
        ("SELECT m.nombreMunicipio FROM Municipio m WHERE m.idMunicipio= :clave").setParameter("clave", claveC).getResultList();
            for (String k: valor) {
                res=k;
                
            }     
        return res;     
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

