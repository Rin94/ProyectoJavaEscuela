/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.clientesrest;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey REST client generated for REST resource:EstadoFacadeREST [estado]<br>
 * USAGE:
 * <pre>
 *        ClienteEstado client = new ClienteEstado();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
public class ClienteEstado {
    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/ServidorRESTControlEscolarJSG/webresources";

    public ClienteEstado() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("estado");
    }
    public String obtenNombreMunicipio(Integer claveMunicipio) throws ClientErrorException {
        WebTarget resource = webTarget;
        String salva="";
        try {
            resource = resource.path(java.text.MessageFormat.format("obten/{0}", new Object[]{claveMunicipio}));
        System.out.println("Clave="+claveMunicipio);
        
        salva=resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
        
            
        } catch (NotFoundException e) {
            salva="Jerez";
        }
        
        return salva;
    }
    public String obtenNombreEstado(Short claveEstado) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("obtennombremunicipio/{0}", new Object[]{claveEstado}));
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

    public void close() {
        client.close();
    }
    
}
