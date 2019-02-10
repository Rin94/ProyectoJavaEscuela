/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uaz.poo2.clientesrest;

import edu.uaz.poo2.entidades.PeriodoEscolar;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:PeriodoEscolarFacadeREST
 * [periodoescolar]<br>
 * USAGE:
 * <pre>
 *        ClientePeriodoEscolar client = new ClientePeriodoEscolar();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
public class ClientePeriodoEscolar {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/ServidorRESTControlEscolarJSG/webresources";

    public ClientePeriodoEscolar() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("periodoescolar");
    }

    public boolean remove(String id) throws ClientErrorException {
        Response r = webTarget.path(java.text.MessageFormat.
                format("eliminarperiodoescolar/{0}", new Object[]{id})).request().delete();
        if (r.getStatus() / 100 == 2) {
            return true;
        } else {
            return false;
        }
    }

    public <T> T findAll_XML(GenericType<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll_JSON(GenericType<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T obtenSigPeriodo(Class<T> responseType) throws ClientErrorException {
        
        WebTarget resource = webTarget;
        resource = resource.path("buscasigidperiodo");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(responseType);
    }
     public Short obtenSigPeriodo() throws ClientErrorException {
        
        WebTarget resource = webTarget;
        resource = resource.path("buscasigidperiodo");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(Short.class);
    }
    

    public boolean edit_XML(Object requestEntity, String id) throws ClientErrorException {
        Response r
                = webTarget.path(java.text.MessageFormat.
                        format("modificarperiodoescolar/{0}", new Object[]{id})).
                request(javax.ws.rs.core.MediaType.APPLICATION_XML).
                put(javax.ws.rs.client.Entity.entity(requestEntity,
                                javax.ws.rs.core.MediaType.APPLICATION_XML));
        if (r.getStatus() / 100 == 2) {
            return true;
        } else {
            return false;
        }

    }

    public void edit_JSON(Object requestEntity, String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("modificarperiodoescolar/{0}", new Object[]{id})).request(javax.ws.rs.core.MediaType.APPLICATION_JSON).put(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public boolean create_XML(Object requestEntity) throws ClientErrorException {
        Response r = webTarget.
                path("agregarperiodoescolar").
                request(javax.ws.rs.core.MediaType.APPLICATION_XML).
                post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_XML));
        if (r.getStatus() / 100 == 2) {
            return true;
        } else {
            return false;
        }

    }

    public void create_JSON(Object requestEntity) throws ClientErrorException {
        webTarget.path("agregarperiodoescolar").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON));
    }

    public List <Short> periodoBox() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("periodobox");
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(List.class);
    }

    public void close() {
        client.close();
    }

}
