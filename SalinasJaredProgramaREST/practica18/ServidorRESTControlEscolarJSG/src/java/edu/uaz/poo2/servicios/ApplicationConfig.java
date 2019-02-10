/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uaz.poo2.servicios;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Lic. Jared Daniel Salinas Gonzalez
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(edu.uaz.poo2.servicios.CargaEstudianteFacadeREST.class);
        resources.add(edu.uaz.poo2.servicios.CargaProfesorFacadeREST.class);
        resources.add(edu.uaz.poo2.servicios.CarreraFacadeREST.class);
        resources.add(edu.uaz.poo2.servicios.EstadoFacadeREST.class);
        resources.add(edu.uaz.poo2.servicios.EstudianteFacadeREST.class);
        resources.add(edu.uaz.poo2.servicios.MateriaFacadeREST.class);
        resources.add(edu.uaz.poo2.servicios.MunicipioFacadeREST.class);
        resources.add(edu.uaz.poo2.servicios.PeriodoEscolarFacadeREST.class);
        resources.add(edu.uaz.poo2.servicios.ProfesorFacadeREST.class);
    }
    
}
