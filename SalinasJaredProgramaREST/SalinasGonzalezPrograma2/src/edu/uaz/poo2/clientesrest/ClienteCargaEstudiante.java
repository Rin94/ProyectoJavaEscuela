/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.clientesrest;

import edu.uaz.poo2.entidades.ListaCargaEstudiante;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:CargaEstudianteFacadeREST
 * [cargaestudiante]<br>
 * USAGE:
 * <pre>
 *        ClienteCargaEstudiante client = new ClienteCargaEstudiante();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
public class ClienteCargaEstudiante {
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/ServidorRESTControlEscolarJSG/webresources";

    public ClienteCargaEstudiante() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("cargaestudiante");
    }

    public boolean remove(String id) throws ClientErrorException {
       Response r= webTarget.path(java.text.MessageFormat.format
        ("eliminacargaestudiante/{0}", new Object[]{id})).request().delete();
       if(r.getStatus()/100==2){
           return true;
       }
       else{
           return false;
       }
    }
    
    public <T> T encuentraListaCarga_XML(GenericType<T> responseType, String matricula) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("obtenlistacargaestudiante/{0}", new Object[]{matricula}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }
    
    /*
    public List<ListaCargaEstudiante> encuentraListaCarga_XML(List<ListaCargaEstudiante> responseType, String matricula) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("obtenlistacargaestudiante/{0}", new Object[]{matricula}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(List.class);
    }
    */

    public <T> T encuentraListaCarga_JSON(Class<T> responseType, String matricula) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("obtenlistacargaestudiante/{0}", new Object[]{matricula}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T findAll_XML(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T encuentraCarga_XML(GenericType <T> responseType, String matricula) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("obtencargaestudiante/{0}", new Object[]{matricula}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T encuentraCarga_JSON(Class<T> responseType, String matricula) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("obtencargaestudiante/{0}", new Object[]{matricula}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public boolean create_XML(Object requestEntity) throws ClientErrorException {
        Response r=
                webTarget.path("agregacargaestudiante").request(javax.ws.rs.core.MediaType.
                        APPLICATION_XML).post(javax.ws.rs.client.Entity.entity
        (requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        if(r.getStatus()/100==2){
           return true;
       }
       else{
           return false;
       }
    }

    public void create_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.path("agregacargaestudiante").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public void close() {
        client.close();
    }
    
}
