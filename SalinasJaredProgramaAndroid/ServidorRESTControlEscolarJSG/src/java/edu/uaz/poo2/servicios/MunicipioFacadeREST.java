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
 * Clase que ofrece los servicios relacionados con la tabla municipio
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@Stateless
@Path("municipio")
public class MunicipioFacadeREST extends AbstractFacade<Municipio> {
    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;
    /**
     * Contructor vacio
     */
    public MunicipioFacadeREST() {
        super(Municipio.class);
    }  
    /**
     * Metdo que encuentra un municipio 
     * @param id clave del municipio
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Municipio find(@PathParam("id") Integer id) {
        return super.find(id);
    }
    /**
     * Metodo que encuentra todos los municipios
     * @return 
     */
    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Municipio> findAll() {
        return super.findAll();
    }
    /**
     * Metodo que encuentra los municipios del combo box
     * @param idEstado id Estado
     * @return  lista con los municipios del estado seleccionado
     */
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


    /**
     * Metodo que permite usar sentencias sql
     * @return 
     */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

