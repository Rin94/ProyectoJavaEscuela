/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uaz.poo2.servicios;

import edu.uaz.poo2.entidadesservidor.CargaEstudiante;
import edu.uaz.poo2.entidadesservidor.ListaCargaEstudiante;
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

@Path("cargaestudiante")
public class CargaEstudianteFacadeREST extends AbstractFacade<CargaEstudiante> {

    @PersistenceContext(unitName = "ServidorRESTControlEscolarJSGPU")
    private EntityManager em;

    public CargaEstudianteFacadeREST() {
        super(CargaEstudiante.class);
    }

    @POST
    @Override
    @Path("agregacargaestudiante")
    @Consumes({"application/xml", "application/json"})
    public void create(CargaEstudiante entity) {
        try {
            super.create(entity);

        } catch (Exception e) {
        }

    }

    @DELETE
    @Path("eliminacargaestudiante/{id}")
    public void remove(@PathParam("id") Integer id) {
        try {
            super.remove(super.find(id));

        } catch (ServerErrorException e) {
        }

    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<CargaEstudiante> findAll() {
        return super.findAll();
    }

    @GET
    //@Override
    @Path("obtencargaestudiante/{matricula}")
    @Produces({"application/xml", "application/json"})
    public List<CargaEstudiante> encuentraCarga(
            @PathParam("matricula") String matricula) {
        List<CargaEstudiante> resultado = null;

        try {
             //Integer valorMin=idEstado*1000;
            //Integer valorMax=(idEstado+1)*1000;
            resultado = getEntityManager().createQuery("SELECT c from CargaEstudiante c"
                    + " WHERE c.matricula=:Mat "
            )
                    .setParameter("Mat", matricula)
                    .getResultList();

        } catch (NullPointerException e) {
        } catch (ServerErrorException e) {
        }

        return resultado;
    }
    
    @GET
    //@Override
    @Path("obtenlistacargaestudiante/{matricula}")
    @Produces({"application/xml", "application/json"})
    public List<ListaCargaEstudiante> encuentraListaCarga(
            @PathParam("matricula") String matricula) {
        List<CargaEstudiante> resultado = null;
        List<ListaCargaEstudiante> lista=new ArrayList<ListaCargaEstudiante>();
        Integer idPeriodo = null;
        String claveMat;
        String claveCarr;
        String nombreCarrera = null;
        String nombreMateria = null;
        Short ano;
        Short periodo;

        try {
             //Integer valorMin=idEstado*1000;
            //Integer valorMax=(idEstado+1)*1000;
            resultado = getEntityManager().createQuery("SELECT c from CargaEstudiante c"
                    + " WHERE c.matricula=:Mat "
            )
                    .setParameter("Mat", matricula)
                    .getResultList();

            for (CargaEstudiante k : resultado) {
                idPeriodo = k.getIdPeriodo();
                periodo = (Short) getEntityManager().createQuery("SELECT p.periodo from PeriodoEscolar p where p.idPeriodo="
                        + " :clav ").setParameter("clav", idPeriodo).getSingleResult();
                ano = (Short) getEntityManager().createQuery("SELECT p.year from PeriodoEscolar p where p.idPeriodo="
                        + " :clav ").setParameter("clav", idPeriodo).getSingleResult();
                String perString = null;
                if (periodo == 1) {
                    perString = "Agosto-Diciembre";

                } else if (periodo == 2) {
                    perString = "Enero-Junio";
                } else if (periodo == 3) {
                    perString = "Verano";
                }

                claveMat = k.getClaveMateria();
                nombreMateria = (String) getEntityManager().createQuery("SELECT m.nombreMateria from Materia m where m.claveMateria="
                        + " :clav ").setParameter("clav", claveMat).getSingleResult();
                claveCarr = (String) getEntityManager().createQuery("SELECT m.claveCarrera from Materia m where m.claveMateria="
                        + " :clav ").setParameter("clav", claveMat).getSingleResult();
                nombreCarrera = (String) getEntityManager().createQuery("SELECT c.nombreCarrera from Carrera c where c.claveCarrera= :carr ").setParameter("carr", claveCarr).getSingleResult();
                //lista.add(new ListaCargaEstudiante(k.getIdCargaEstudiante(),nombreMateria,periodo,ano,k.getIdPeriodo(),k.getClaveMateria(),claveCarr));
                lista.add(new ListaCargaEstudiante(k.getIdCargaEstudiante(),nombreMateria,periodo,ano));
             /*int year,int idPeriodo, String claveMateria, String claveCarrera*/

            }

        } catch (NullPointerException e) {
        } 
        String sentencia = idPeriodo + " " + nombreCarrera;

        return lista;
    }
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
