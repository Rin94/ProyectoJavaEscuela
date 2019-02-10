/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.servicios;

import edu.uaz.poo2.entidadesservidor.CargaProfesor;
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
@Path("cargaprofesor")
public class CargaProfesorFacadeREST extends AbstractFacade<CargaProfesor> {

    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;

    public CargaProfesorFacadeREST() {
        super(CargaProfesor.class);
    }

    @POST
    @Path("agregarcargaprofesor/{id}")
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(CargaProfesor entity) {
        try {
            super.create(entity);

        } catch (ServerErrorException e) {
        }

    }

    @DELETE
    @Path("eliminarcargaprofesor/{id}")
    public void remove(@PathParam("id") Integer id) {
        try {
            super.remove(super.find(id));

        } catch (ServerErrorException e) {
        }

    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<CargaProfesor> findAll() {
        return super.findAll();
    }

    @GET
    @Path("obtencargaprofesor/{rfc}")
    @Produces({"application/xml", "application/json"})
    public List<CargaProfesor> encuentraCarga(
            @PathParam("rfc") String rfc) {
        List<CargaProfesor> resultado = null;
        try {
            //Integer valorMin=idEstado*1000;
            //Integer valorMax=(idEstado+1)*1000;
            resultado = getEntityManager().createQuery("SELECT c from CargaProfesor c"
                    + " WHERE c.rfc=:rfc "
            )
                    .setParameter("rfc", rfc)
                    .getResultList();

        } catch (NullPointerException e) {
        }
        catch (ServerErrorException e) {
        }

        return resultado;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
    

