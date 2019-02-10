/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.clientesrest;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:MateriaFacadeREST
 * [materia]<br>
 * USAGE:
 * <pre>
 *        ClienteMateria client = new ClienteMateria();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
public class ClienteMateria {
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/ServidorRESTControlEscolarJSG/webresources";

    public ClienteMateria() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("materia");
    }
    public String obtenNombreCarreraCB(String nombreMateria) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("obtenerCBCarrera/{0}", new Object[]{nombreMateria}));
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public <T> T obtenNombreMateria_XML(GenericType<T> responseType, String claveCarrera) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("obtennombremateria/{0}", new Object[]{claveCarrera}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public boolean remove(String id) throws ClientErrorException {
        Response r=webTarget.path(java.text.MessageFormat.
                format("eliminamateria/{0}", new Object[]{id})).request().delete();
         if(r.getStatus()/100==2){
           return true;
       }
       else{
           return false;
       }
    }
     public String obtenNombreCarrera(String claveCarrera) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("obtennombrecarrera/{0}", new Object[]{claveCarrera}));
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public <T> T findAll_XML(GenericType<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll_JSON(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public boolean edit_XML(Object requestEntity, String id) throws ClientErrorException {
        Response r=webTarget.path(java.text.MessageFormat.
                format("modificamateria/{0}", new Object[]{id})).
                request(javax.ws.rs.core.MediaType.APPLICATION_XML).
                put(javax.ws.rs.client.Entity.entity(requestEntity,
                        javax.ws.rs.core.MediaType.APPLICATION_XML));
         if(r.getStatus()/100==2){
           return true;
       }
       else{
           return false;
       }
    }

    public void edit_JSON(Object requestEntity, String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("modificamateria/{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public boolean create_XML(Object requestEntity) throws ClientErrorException {
        Response r=webTarget.path("agregarmateria").
                request(javax.ws.rs.core.MediaType.APPLICATION_XML).
                post(javax.ws.rs.client.Entity.entity(requestEntity,
                        javax.ws.rs.core.MediaType.APPLICATION_XML));
         if(r.getStatus()/100==2){
           return true;
       }
       else{
           return false;
       }
    }

    public void create_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.path("agregarmateria").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public void close() {
        client.close();
    }
    
}
