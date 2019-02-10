/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uaz.poo2.gui;

import edu.uaz.poo2.basedatos.ConectorBaseDeDatos;
import edu.uaz.poo2.basedatos.ConexionException;
import edu.uaz.poo2.entidades.CargaEstudiante;
import edu.uaz.poo2.entidades.Carrera;
import edu.uaz.poo2.entidades.Estado;
import edu.uaz.poo2.entidades.Estudiante;
import edu.uaz.poo2.entidades.ListaCargaEstudiante;
import edu.uaz.poo2.entidades.Materia;
import edu.uaz.poo2.entidades.Municipio;
import edu.uaz.poo2.entidades.PeriodoEscolar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.awt.image.ImageObserver.WIDTH;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Esta clase genera la interface basica del sistema de control escolar
 *
 * @author Jared Daniel Salinas Gonzalez
 *
 *
 */
public class GUIControlEscolar extends javax.swing.JFrame {

    /**
     *
     * Este metodo pide una confirmacion para terminar la aplicacion y es
     * llamado al seleccionar la opcion de salir del Menu Archivo o darle click
     * al icono de cerrar ventana
     */
    private void confirmaSalida() {
        int respuesta = JOptionPane.showConfirmDialog(null,
                bundle.getString("textoConfirmacionSalida"),
                bundle.getString("tituloConfirmacionSalida"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            this.dispose();
            System.exit(0);
        }
    }

    /**
     * Este metodo se encarga de inicializar todo lo relacionado con la
     * interface de Estudiante
     */
    private void inicializaEstudiantes() {

        modeloListaEstudiante = new DefaultListModel<Estudiante>();
        jTFMatriculaEstudiante.setEnabled(false);

        List<Estudiante> datos = conector.obtenEstudiantes();

        for (Estudiante m : datos) {
            modeloListaEstudiante.addElement(m);

        }

        jListEstudiante.setModel(modeloListaEstudiante);
        jBotonAgregarEstudiante.setVisible(false);
        // Llenamos el combo box de estados haciendo los siguientes pasos:
        // Creamos el modelo del combo box 
        modeloComboEstados = new DefaultComboBoxModel();
        // Obtenemos la lista de estados
        // a traves del objeto conector
        List<Estado> estados
                = conector.obtenEstados();
        // Cada uno de los estados obtenidos es
        // agregado al modelo del combo box
        for (Estado est : estados) {
            modeloComboEstados.addElement(est);
        }
        // Establecemos el modelo de combo box al combo box de estados
        jCBEstadoEstudiante.setModel(modeloComboEstados);

        // Ahora hacemos lo mismo para los municipios
        // Creamos un arreglo de tamaño 32 para contener
        // cada lista de municipios de los 32 estados del pais
        // para asi no tener que estar consultando a la
        // base de datos cada que se haga un cambio de estado
        datosMunicipios = new ArrayList[32];
        // Llenamos el combo box de Municipios
        // con los municipios de Zacatecas por default
        modeloComboMunicipios = new DefaultComboBoxModel();
        actualEstado = 32;  // 32 es el ID de Zacatecas
        // Obtenemos la lista de municipios del estado 32
        // a traves del objeto conector
        List<Municipio> muns
                = conector.obtenMunicipios(actualEstado);
        // Establecemos la posicion correspondiente en el arreglo
        datosMunicipios[actualEstado - 1] = muns;
        // Colocamos cada municipio obtenido 
        // en el modelo de combo box de municipios
        for (Municipio m : muns) {
            modeloComboMunicipios.addElement(m);
        }

        // Establecemos el modelo de combo box al combo box de municipios
        jCBMunicipioEstudiante.setModel(modeloComboMunicipios);

        // Se indica que no hay estado o municipio seleccionado
        // llamando al metodo setSelectedIndex con un -1 
        jCBEstadoEstudiante.setSelectedIndex(-1);
        jCBMunicipioEstudiante.setSelectedIndex(-1);
    }

    /*
     * METODOS Y CLASES INTERNAS PARA MANEJAR EVENTOS
     * DE LA INTERFACE DE USUARIO
     */
    /**
     * Clase interna para manejar el evento de cambiar de estado seleccionado
     */
    private class ManejadorComboEstados implements ItemListener {

        /**
         * Este metodo se ejecuta cada vez que se selecciona un estado del combo
         * box de estados del panel de estudiante
         *
         * @param e Objeto con la informacion del evento generado
         */
        public void itemStateChanged(ItemEvent e) {
            // Si el evento fue haber seleccionado un estado
            if (e.getStateChange()
                    == ItemEvent.SELECTED) {
                // entonces obtenemos el indice del estado seleccionado
                int indice
                        = jCBEstadoEstudiante.getSelectedIndex();
                // Si no fue -1 (-1 indica que no se selecciono ningun estado)
                if (indice != -1) {
                    // Obtenemos el objeto Estado correspondiente a esa
                    // posicion usando el modelo asociado al combo box
                    Estado est = modeloComboEstados.getElementAt(indice);
                    // y llamamos a actualizaMunicipios pasandole el ID del estado seleccionado
                    actualizaMunicipios(est.getIdEstado());
                }
            }
        }
    }

    /**
     *
     * Clase que hace que los JTF que usen numeros no se puedan introducir otros
     * caracteres que no son numeros COMO MATRICULA, TELEFONO, CODIGO POSTAL
     */
    private class ManejadorJTextFieldNumeros implements KeyListener {

        /**
         * Metodo que revisa que el caracter introducido sea un numero
         *
         * @param evt Evento que llega
         */
        @Override
        public void keyTyped(KeyEvent evt) {
            // SOLO NUMEROS 
            char c;
            //capturar el caracter digitado 
            c = evt.getKeyChar();
            if (c < '0' || c > '9') {
                evt.consume();//ignora el caracter digitado 
            }
        }

        /**
         * Metodo no utilizado
         *
         * @param e
         */
        @Override
        public void keyPressed(KeyEvent e) {

        }

        /**
         * Metodo no utilizado
         *
         * @param e
         */
        @Override
        public void keyReleased(KeyEvent e) {

        }

    }

    /**
     *
     * Clase que hace que los JTF que usen nombres u apellidos no se puedan
     * introducir otros caracteres que no son letras
     */
    private class ManejadorJTextFieldNombres implements KeyListener {

        /**
         * Metodo que revisa que el caracter introducido sea una letra
         *
         * @param evt Evento que llega
         */
        @Override
        public void keyTyped(KeyEvent evt) {
            // SOLO NUMEROS 
            char c;
            //capturar el caracter digitado 
            c = evt.getKeyChar();
            if ((c < 'a' || c > 'z') && (c < 'A' || c > 'z')) {
                evt.consume();//ignora el caracter digitado 
            }
        }

        /**
         * Metodo no utilizado
         *
         * @param e
         */
        @Override
        public void keyPressed(KeyEvent e) {

        }

        /**
         * Metodo no utilizado
         *
         * @param e
         */
        @Override
        public void keyReleased(KeyEvent e) {

        }

    }

    /**
     * Clase interna que maneja el text field del panel de estudiante, haciendo
     * que el usuario no introduzca letras mayusculas y otras cosas
     */
    private class ManejadorJTextFieldEmail implements KeyListener {

        /**
         * Metodo que revisa que el caracter introducido sea un numero, letra o
         * arrobas, puntos y lineas
         *
         * @param evt Evento que llega
         */
        @Override
        public void keyTyped(KeyEvent evt) {
            // SOLO NUMEROS 
            char caracter;
            //capturar el caracter digitado 
            caracter = evt.getKeyChar();
            if ((caracter < '0' || caracter > '9') && (caracter < 'a' || caracter > 'z')
                    && caracter != '@' //Minúsculas            
                    && caracter != '.'
                    && caracter != '-'
                    && caracter != '_') {
                evt.consume();//ignora el caracter digitado 
            }
        }

        /**
         * Metodo no utilizado
         *
         * @param e
         */
        @Override
        public void keyPressed(KeyEvent e) {

        }

        /**
         * Metodo no utilizado
         *
         * @param e
         */
        @Override
        public void keyReleased(KeyEvent e) {

        }

    }

    /**
     * Clase interna para manejar el combo de carreras
     *
     */
    private class ManejadorComboCarrerasEstudiante implements ItemListener {

        /**
         * Metodo de interfaz Action listener para Manejar Botones del panel de
         * carga estudiante
         *
         * @param e evento
         */
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int indice = jCBCarrera.getSelectedIndex();
                List<Materia> materias = new ArrayList<Materia>();
                if (indice != -1) {
                    Carrera carrera = modeloComboMateria.getElementAt(indice);
                    //System.out.println(carrera + "" + carrera.getClaveCarrera());
                    // Materia materia=

                    materias = conector.obtenerCargaMateria(carrera.getClaveCarrera());
                    modeloComboMateriaEstudiante.removeAllElements();
                    //jCBMateriaEstudiante.setEnabled(true);
                    for (Materia materia : materias) {
                        modeloComboMateriaEstudiante.addElement(materia);

                    }
                    if (indiceCarrera == true) {
                        jCBMateriaEstudiante.setEnabled(true);

                    }
                    //jCBMateriaEstudiante.setEnabled(true);
                    //modeloComboMateriaEstudiante.getIndexOf(-1);
                    //modeloComboMateriaEstudiante.getSelectedItem();

                } else {
                    jCBMateriaEstudiante.setEnabled(false);

                }
            }

        }

    }

    /**
     * Clase interna para manejar el combo de carreras
     *
     */
    private class ManejadorComboCarreras implements ItemListener {

        /**
         * Metodo de interfaz Action listener para Manejar Botones
         *
         * @param e evento
         */
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int indice = jCBMateriaEstudiante.getSelectedIndex();
                System.out.println(indice);
                if (indice != -1) {
                    try {

                        //Se selecciono un elemnto
                        Materia matSel;

                        matSel = modeloComboMateriaEstudiante.getElementAt(indice++);
                        //cSel=modeloListaCarreras.getElementAt(indice);

                        String clave = matSel.getClaveCarrera();//Aqui guradamos la clave

                        //Carrera car= new Carrera(clave);
                        String carreraSel = conector.obtenCarreraMateria(clave);//aqui guardamos el nombre de la carrera
                        Carrera car = new Carrera(clave, carreraSel);
                        System.out.println(clave);
                        System.out.println(car);
                    //System.out.println(carreraSel);

                        //modeloComboCarreraEstudiante.setSelectedItem(car);
                        //jCBCarreraEstudiante.setSelectedIndex(0);
                        modeloComboCarreraEstudiante.setSelectedItem(car);
                        //jCBCarreraEstudiante.setSelectedIndex(0);
                    } catch (IllegalArgumentException iae) {

                    }
                } else {
                    jCBMateriaEstudiante.setEnabled(false);

                }
            }

        }

    }

    /**
     * Clase interna para manejar el combo box de periodos
     *
     */
    private class ManejadorComboPeriodos implements ItemListener {

        /**
         * Metodo de interfaz IntemListener para manejar los el combo de
         * Periodos
         *
         * @param e evento
         */
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int indice = jCBSemestre.getSelectedIndex();
                if (indice != -1) {
                    //PeriodoEscolar mat=modeloComboPeriodo.getElementAt(indice);

                }
            }
        }

    }

    /**
     * Clase interna que maneja la lista del panel de Carreras
     *
     */
    private class ManejadorListaCarreras implements ListSelectionListener {

        /**
         * Metodo de interfaz Action listener para Manejar la lista del panel de
         * carrera
         *
         * @param e evento
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {//si selecciona un campo o no
                int indice = jListCarreras.getSelectedIndex();
                if (indice != -1) {
                    //Se selecciono un elemnto
                    jBotonModificarCarrera.setEnabled(true);
                    jBotonEliminarCarrera.setEnabled(true);
                    cSel = modeloListaCarreras.getElementAt(indice);
                    jTFNombreCarrera.setText(cSel.getNombreCarrera());
                    jTFClaveCarrera.setText(cSel.getClaveCarrera());
                    jTFClaveCarrera.setEditable(false);
                } else {
                    jBotonModificarCarrera.setEnabled(false);
                    jBotonEliminarCarrera.setEnabled(false);
                    jTFNombreCarrera.setText("");
                    jTFClaveCarrera.setText("");

                }

            }

        }

    }

    /**
     * Clase interna que maneja la lista del panel de materia
     *
     */
    private class ManejadorListaMaterias implements ListSelectionListener {

        /**
         * Metodo de interfaz Action listener para manejar la lista de materias
         * del panel de materias
         *
         * @param e evento
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {//si selecciona un campo o no
                int indice = jListMateria.getSelectedIndex();
                if (indice != -1) {
                    //Se selecciono un elemnto

                    jBotonModificarMateria.setEnabled(true);
                    jBotonEliminarMateria.setEnabled(true);
                    mSel = modeloListaMateria.getElementAt(indice);
                    //cSel=modeloListaCarreras.getElementAt(indice);
                    jTFNombreMateria.setText(mSel.getNombreMateria());
                    jTFClaveMateria.setText(mSel.getClaveMateria());
                    String clave = mSel.getClaveCarrera();//Aqui guradamos la clave

                    //Carrera car= new Carrera(clave);
                    String carreraSel = conector.obtenCarreraMateria(clave);//aqui guardamos el nombre de la carrera
                    Carrera car = new Carrera(clave, carreraSel);
                    //System.out.println(carreraSel);
                    jTFSemestreMateria.setText(mSel.getSemestre() + "");
                    jTFClaveMateria.setEditable(false);
                    modeloComboMateria.setSelectedItem(car);

                } else {
                    jBotonModificarMateria.setEnabled(false);
                    jBotonEliminarMateria.setEnabled(false);
                    jTFNombreMateria.setText("");
                    jTFClaveCarrera.setText("");

                }

            }

        }

    }

    /**
     * Clase interna que maneja la lista del panel de Periodos Escolar
     *
     */
    private class ManejadorListaPeriodosEscolar implements ListSelectionListener {

        /**
         * Metodo de interfaz Action listener para Manejar Botones del panel de
         * periodo escolar
         *
         * @param e evento
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {//si selecciona un campo o no
                int indice = jListPeriodoEscolar.getSelectedIndex();
                if (indice != -1) {
                    //Se selecciono un elemnto

                    jBotonModificarPeriodo.setEnabled(true);
                    jBotonEliminarPeriodo.setEnabled(true);
                    pSel = modeloListaPeriodo.getElementAt(indice);
                    jTFIdPeriodoEscolar.setText(pSel.getIdPeriodo() + "");
                    jTFAnoPeriodoEscolar.setText(pSel.getYear() + "");
                    short clave = pSel.getPeriodo();//Aqui guradamos la clave
                    jTFIdPeriodoEscolar.setEditable(false);
                    modeloComboPeriodo.setSelectedItem(conector.obtenPeriodoString(clave));

                } else {
                    jBotonModificarPeriodo.setEnabled(false);
                    jBotonEliminarPeriodo.setEnabled(false);
                    jTFIdPeriodoEscolar.setText("");
                    jTFAnoPeriodoEscolar.setText("");

                }

            }

        }

    }

    /**
     * Clase interna que maneja la lista de estudiantes del panel de Estudiante
     *
     */
    private class ManejadorListaEstudiante implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {//si selecciona un campo o no
                int indice = jListEstudiante.getSelectedIndex();
                modeloListaCargaEstu = new DefaultListModel();
                if (indice != -1) {
                    //Se selecciono un elemnto
                    jBotonModificarEstudiante.setEnabled(true);
                    jBotonEliminarEstudiante.setEnabled(true);
                    eSel = modeloListaEstudiante.getElementAt(indice);
                    jTFNombreEstudiante.setText(eSel.getNombre());
                    jTFMatriculaEstudiante.setText(eSel.getMatricula());
                    jTFApellPatEstudiante.setText(eSel.getApPaterno());
                    jTFApellMatEstudiante.setText(eSel.getApMaterno());
                    jTFEmailEstudiante.setText(eSel.getEmail());
                    jTFCalleEstudiante.setText(eSel.getCalle());
                    jTFColoniaEstudiante.setText(eSel.getColonia());
                    jTFCPEstudiante.setText(eSel.getCodPostal());
                    jTFTelefonoEstudiante.setText(eSel.getTelefono());
                    try {
                       
                        modeloComboEstados.setSelectedItem(
                                conector.obtenNombreEstado(eSel.getIdEstado()));
                         modeloComboMunicipios.setSelectedItem(
                                conector.obtenNombreMunicipio(eSel.getIdMunicipio()));
                        

                    } catch (NullPointerException npe) {

                    }

                    CargaEstudiante ce = new CargaEstudiante();
                    ce.setMatricula(eSel.getMatricula());
                    List<ListaCargaEstudiante> informacionEstudiante = conector.obtenCargaEstudiante(ce);
                    for (ListaCargaEstudiante carga : informacionEstudiante) {
                        modeloListaCargaEstu.addElement(carga);
                    }
                    jListCargaEstu.setModel(modeloListaCargaEstu);

                } else {
                    jBotonModificarEstudiante.setEnabled(false);
                    jBotonEliminarEstudiante.setEnabled(false);
                    jTFMatriculaEstudiante.setEditable(false);
                    jTFNombreEstudiante.setText("");
                    jTFMatriculaEstudiante.setText("");
                    jTFApellPatEstudiante.setText("");
                    jTFApellMatEstudiante.setText("");
                    jTFEmailEstudiante.setText("");
                    jTFCalleEstudiante.setText("");
                    jTFColoniaEstudiante.setText("");
                    jTFCPEstudiante.setText("");
                    jTFTelefonoEstudiante.setText("");

                }

            }

        }

    }

    /**
     * Clase interna que maneja la lista de Cargaestudiantes del panel de
     * CargaEstudiante
     *
     */
    private class ManejadorListaCargaEstudiante implements ListSelectionListener {

        /**
         * Metodo que maneja la lista de carga estudiante
         *
         * @param e
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {//si selecciona un campo o no
                int indice = jListCargaEstudiante.getSelectedIndex();
                modeloListaCargaEstudiante = new DefaultListModel();
                if (indice != -1) {
                    //Se selecciono un elemnto

                    jBotonEliminaCargaEstudiante.setEnabled(true);
                    listCargaEstu = modeloListaCarga.getElementAt(indice);
                    PeriodoEscolar pe;
                    Materia materia;
                    Carrera carrera = new Carrera();
                    //System.out.println("Lista");
                    carrera.setNombreCarrera(
                            conector.obtenerNombreCarrera(listCargaEstu.getNombreMateria()));
                    /*
                     System.out.println(carrera);
                     System.out.println(listCargaEstu.getNombreMateria());
                     System.out.println(listCargaEstu.obtenPeriodoStringCB(listCargaEstu.getPeriodo()));
                     System.out.println();
                     */
                    modeloComboCarreraEstudiante.setSelectedItem(carrera.getNombreCarrera());

                    modeloComboMateriaEstudiante.setSelectedItem(listCargaEstu.getNombreMateria());
                    modeloComboPeriodoEstudiante.setSelectedItem(listCargaEstu.obtenPeriodoStringCB(listCargaEstu.getPeriodo()));
                    //modeloComboPeriodoEscolarEstudiante.setSelectedItem(
                    //conector.obtenPeriodoString(listCargaEstu.getPeriodo()));
                    listCargaEstu.getClaveMateria();

                    /* 
                     PeriodoEscolar pe=new PeriodoEscolar();
                     Materia materia=new Materia();
                     */
                } else {
                    jBotonEliminaCargaEstudiante.setEnabled(false);
                }

            }

        }

    }

    /**
     * Clase interna que maneja los botones del panel de Carreras
     *
     */
    private class ManejadorBotonesCarrera implements ActionListener {

        /**
         * Metodo que obtiene los datos de los text field del panel de carreras
         *
         * @return c (Carrera)
         */
        private Carrera obtenDatos() {
            Carrera carrera = null;
            String clave = jTFClaveCarrera.getText();
            String nombre = jTFNombreCarrera.getText();
            if (clave.equals("") || nombre.equals("")) {
                JOptionPane.showMessageDialog(null,
                        bundle.getString("camposObligatorios"), bundle.getString("tituloError"),
                        WIDTH);
            } else {
                carrera = new Carrera(clave, nombre);

            }

            return carrera;
        }

        /**
         * Metodo de interfaz Action listener para Manejar Botones del panel de
         * carrera
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton origen = (JButton) e.getSource();
            if (origen == jBotonNuevaCarrera) {
                jBotonAgregarCarrera.setVisible(true);
                jListCarreras.clearSelection();
                jTFClaveCarrera.setEditable(true);
                jTFClaveCarrera.setText("");

            } else if (origen == jBotonEliminarCarrera) {
                int resp = JOptionPane.showConfirmDialog(null, bundle.getString("textoConfirmacionEliminacionCarrera"),
                        bundle.getString("tituloConfirmacionEliminacion"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    if (conector.eliminaCarrera(cSel.getClaveCarrera())) {
                        modeloListaCarreras.removeElement(cSel);//Lo eliminamos de la clave
                        modeloComboMateria.removeElement(cSel);
                        modeloComboCarreraEstudiante.removeElement(cSel);
                        inicializaMaterias();
                        cSel = null;//para decir que es nulo al eliminarlo
                        inicializaCargaEstudiante();
                    }
                }

            } else if (origen == jBotonModificarCarrera) {
                Carrera c = obtenDatos();
                if (conector.modificaCarrera(c)) {
                    modeloListaCarreras.removeElement(c);
                    modeloComboMateria.removeElement(c);
                    modeloComboCarreraEstudiante.removeElement(c);
                    modeloListaCarreras.addElement(c);
                    modeloComboMateria.addElement(c);
                    modeloComboCarreraEstudiante.addElement(c);
                    jTFClaveCarrera.setText("");
                    jTFNombreCarrera.setText("");

                } else {
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("textoErrorModificarCarrera"),
                            bundle.getString("tituloError"), WIDTH);
                    jTFClaveCarrera.setText("");
                    jTFNombreCarrera.setText("");
                }

            } else if (origen == jBotonAgregarCarrera) {
                Carrera carrera = obtenDatos();
                if (conector.agregaCarrera(carrera)) {
                    modeloListaCarreras.addElement(carrera);
                    modeloComboMateria.addElement(carrera);
                    modeloComboCarreraEstudiante.addElement(carrera);
                    jTFClaveCarrera.setText("");
                    jTFNombreCarrera.setText("");
                } else {
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("textoErrorAgregarCarrera"),
                            bundle.getString("tituloError"), WIDTH);
                    jTFClaveCarrera.setText("");
                    jTFNombreCarrera.setText("");
                }

                jBotonAgregarCarrera.setVisible(false);
                jTFClaveCarrera.setEditable(false);
                jListCarreras.clearSelection();
            }
        }

    }

    /**
     * Clase interna que maneja los botones del panel de materia que implementa
     * la interfaz action listener.
     */
    private class ManejadorBotonesMateria implements ActionListener {

        /**
         * Metodo que obtiene los datos de los campos del panel de materia que
         *
         * @return m (Materia)
         */
        private Materia obtenDatos() {
            Materia m = null;
            String nombre = jTFNombreMateria.getText();
            String claveMat = jTFClaveMateria.getText();
            Short semestre = null;
            try {
                semestre = Short.parseShort(jTFSemestreMateria.getText());

            } catch (NumberFormatException nfe) {

            }

            int indice = jCBCarrera.getSelectedIndex();

            Carrera claveCarrera = null;
            if (indice != -1) {
                claveCarrera = modeloComboMateria.getElementAt(indice);

            }

            //System.out.println(claveCarrera.getClaveCarrera());
            if (nombre.equals("") || claveMat.equals("")
                    || jTFSemestreMateria.getText().equals("") || indice == -1) {
                JOptionPane.showMessageDialog(null, bundle.getString("camposObligatorios"),
                        bundle.getString("tituloError"), WIDTH);

            } else {
                try {
                    m = new Materia(claveMat, nombre, semestre, claveCarrera.getClaveCarrera());

                } catch (NullPointerException npe) {

                }

            }

            return m;
        }

        /**
         * Metodo de interfaz Action listener para Manejar Botones del panel
         * materia
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton origen = (JButton) e.getSource();
            if (origen == jBotonNuevaMateria) {
                jBotonAgregarMateria.setVisible(true);
                jTFNombreMateria.setText("");
                jTFClaveMateria.setText("");
                jTFSemestreMateria.setText("");
                jListMateria.clearSelection();
                jTFClaveMateria.setEditable(true);

            } else if (origen == jBotonEliminarMateria) {
                int resp = JOptionPane.showConfirmDialog(null,
                        bundle.getString("textoConfirmacionEliminacionMateria"),
                        bundle.getString("tituloConfirmacionEliminacionMateria"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    if (conector.eliminaMateria(mSel.getClaveMateria())) {
                        modeloListaMateria.removeElement(mSel);//Lo eliminamos de la clave
                        modeloComboMateriaEstudiante.removeElement(mSel);
                        mSel = null;//para decir que es nulo al eliminarlo
                        inicializaCargaEstudiante();
                    }
                }

            } else if (origen == jBotonModificarMateria) {
                Materia m = obtenDatos();

                if (conector.modificaMateria(m)) {
                    modeloListaMateria.removeElement(m);
                    modeloComboMateriaEstudiante.removeElement(m);
                    modeloComboMateriaEstudiante.addElement(m);
                    modeloListaMateria.addElement(m);

                } else {
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("textoErrorModificarMateria"),
                            bundle.getString("tituloError"), WIDTH);

                }

            } else if (origen == jBotonAgregarMateria) {
                Materia m = obtenDatos();
                if (conector.agregaMateria(m)) {
                    modeloListaMateria.addElement(m);
                    modeloComboMateriaEstudiante.addElement(m);
                } else {
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("textoErrorAgregarMateria"),
                            bundle.getString("tituloError"), WIDTH);

                }
                jBotonAgregarMateria.setVisible(false);
                jListMateria.clearSelection();
                jTFClaveMateria.setEditable(false);
                jTFClaveMateria.setText("");
            }
        }

    }

    /**
     * Manejador de los botones del panel de periodo
     */
    private class ManejadorBotonesPeriodo implements ActionListener {

        private short ano;
        private int idDePeridoEscolar = 0;

        /**
         * Metodo que obtiene los datos del periodo escolar para agregar un
         * nuevo periodo escolar
         *
         * @param idDePeriodoEscolar entero que se le va asignar al atributo id
         * @return
         */
        private PeriodoEscolar obtenDatos(int idDePeriodoEscolar) {
            PeriodoEscolar pe = null;
            String year = jTFAnoPeriodoEscolar.getText();
            try {
                ano = Short.parseShort(year);

            } catch (NumberFormatException nfe) {

            }

            int id = idDePeriodoEscolar;//id del periodo escolar
            short periodo = 0;//periodo para el campo periodo
            int indice = jCBSemestre.getSelectedIndex();
            if (indice == 0) {
                periodo = 2;

            } else if (indice == 1) {
                periodo = 1;

            } else if (indice == 2) {
                periodo = 3;

            }

            if (indice == -1 || jTFAnoPeriodoEscolar.getText().equals("")) {
                JOptionPane.showMessageDialog(null,
                        bundle.getString("camposObligatorios"),
                        bundle.getString("tituloError"), WIDTH);
            } else {
                pe = new PeriodoEscolar(id, ano, periodo);

            }

            return pe;
        }

        /**
         * Metodo que obtiene un perido escolar a partir de los datos del panel
         * de periodo escolar
         *
         * @return pe PeriodoEscolar
         */
        private PeriodoEscolar obtenDatos() {

            PeriodoEscolar pe = null;//pe periodoEscolar
            String year = jTFAnoPeriodoEscolar.getText();
            try {
                ano = Short.parseShort(year);

            } catch (NumberFormatException npe) {

            }

            int idPer = Integer.parseInt(jTFIdPeriodoEscolar.getText());//id periodo escolar
            short periodo = 0;
            int indice = jCBSemestre.getSelectedIndex();
            if (indice == 0) {
                periodo = 2;

            } else if (indice == 1) {
                periodo = 1;
            } else if (indice == 2) {
                periodo = 3;
            }
            if (indice == -1 || jTFAnoPeriodoEscolar.getText().equals("")) {
                JOptionPane.showMessageDialog(null,
                        bundle.getString("camposObligatorios"),
                        bundle.getString("tituloError"), WIDTH);
            } else {
                pe = new PeriodoEscolar(idPer, ano, periodo);

            }

            return pe;
        }

        /**
         * Metodo de interfaz Action listener para Manejar Botones
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton origen = (JButton) e.getSource();

            if (origen == jBotonNuevoPeriodo) {
                idDePeridoEscolar = conector.obtenSigPeriodo();
                jBotonAgregarPeriodo.setVisible(true);
                jTFAnoPeriodoEscolar.setText("");

                jListPeriodoEscolar.clearSelection();

            } else if (origen == jBotonEliminarPeriodo) {
                int resp = JOptionPane.showConfirmDialog(null, bundle.getString("textoConfirmacionEliminacionPeriodo"),
                        bundle.getString("tituloConfirmacionEliminacionPeriodo"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    if (conector.eliminaPeriodo(pSel.getIdPeriodo())) {
                        modeloListaPeriodo.removeElement(pSel);//Lo eliminamos de la clave
                        modeloComboPeriodoEstudiante.removeElement(pSel);
                        pSel = null;//para decir que es nulo al eliminarlo
                        inicializaCargaEstudiante();
                        jCBSemestre.setSelectedIndex(-1);
                    }
                }

            } else if (origen == jBotonModificarPeriodo) {
                PeriodoEscolar m = obtenDatos();

                boolean agregar = true;
                if (agregar == conector.modificaPeriodo(m)) {
                    modeloListaPeriodo.removeElement(m);
                    modeloComboPeriodoEstudiante.removeElement(m);
                    modeloListaPeriodo.addElement(m);
                    modeloComboPeriodoEstudiante.addElement(m);

                } else {
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("errorModificarPeriodo"),
                            bundle.getString("tituloError"), WIDTH);

                }
                jCBSemestre.setSelectedIndex(-1);

            }

            if (origen == jBotonAgregarPeriodo) {

                PeriodoEscolar m = obtenDatos(idDePeridoEscolar);

                boolean agregar = true;
                if (agregar == conector.agregaPeriodo(m)) {
                    modeloListaPeriodo.addElement(m);
                    modeloComboPeriodoEstudiante.addElement(m);
                } else {
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("errorAgregarPeriodo"),
                            bundle.getString("tituloError"), WIDTH);

                }

                jListPeriodoEscolar.clearSelection();
                jBotonAgregarPeriodo.setVisible(false);
                jCBSemestre.setSelectedIndex(-1);

            }
        }

    }

    /**
     * Manejador de los botones del panel estudiante
     */
    private class ManejadorBotonesEstudiante implements ActionListener {

        /**
         * Metodo que obtiene los datos del panel de estudiante para agregar a
         * un estudiante
         *
         * @return devuelve un estudiante e
         */
        private Estudiante obtenDatos() {
            Estudiante e = null;
            String nombre = jTFNombreEstudiante.getText();
            String matricula = jTFMatriculaEstudiante.getText();
            String apPaterno = jTFApellPatEstudiante.getText();
            String apMaterno = jTFApellMatEstudiante.getText();
            String email = jTFEmailEstudiante.getText();
            String calle = jTFCalleEstudiante.getText();
            String colonia = jTFColoniaEstudiante.getText();
            String codPostal = jTFCPEstudiante.getText();
            String telefono = jTFTelefonoEstudiante.getText();
            if (matricula.equals("") || nombre.equals("") || email.equals("")
                    || apPaterno.equals("")) {
                JOptionPane.showMessageDialog(null,
                        bundle.getString("errorAgregarEstudiante"),
                        bundle.getString("tituloError"), WIDTH);

            } else {
                int indice = jCBEstadoEstudiante.getSelectedIndex();
                int indice2 = jCBMunicipioEstudiante.getSelectedIndex();
                Municipio claveMunicipio = new Municipio();
                Estado claveEstado = new Estado();
                claveEstado.setIdEstado(actualEstado);

                if (indice != -1) {
                    short indiceAux = (short) indice;
                    // claveMunicipio= modeloComboMunicipios.getElementAt(indice++);

                    System.out.println("indice: " + indice);

                }

                if (indice2 != -1) {
                    //claveEstado = modeloComboEstados.getElementAt(indice2);
                    //System.out.println("indice2: "+indice2);
                    claveMunicipio = modeloComboMunicipios.getElementAt(indice2++);
                    //System.out.println(claveMunicipio);

                }
                if (claveEstado.getIdEstado() == null || claveMunicipio.getIdMunicipio() == null) {
                    e = new Estudiante(matricula, nombre, apPaterno,
                            apMaterno, calle, colonia, codPostal, telefono, email
                    );

                } else {
                    e = new Estudiante(matricula, nombre, apPaterno,
                            apMaterno, calle, colonia, codPostal, telefono, email,
                            claveEstado.getIdEstado(), claveMunicipio.getIdMunicipio());

                }
                //System.out.println("claves: "+claveEstado.getIdEstado()+" "+ claveMunicipio.getIdMunicipio());

                //indice=null;
                //indice2=null;
                jCBEstadoEstudiante.setSelectedIndex(-1);
                jCBMunicipioEstudiante.setSelectedIndex(-1);

            }
            return e;

        }

        /**
         * Metodo que obtiene los datos del panel de estudiante para modificar a
         * un estudiante
         *
         * @return regresa un estudiante e
         */
        private Estudiante modificaEstudiante() {
            Estudiante e = null; // e estudiante
            String nombre = jTFNombreEstudiante.getText();
            String matricula = jTFMatriculaEstudiante.getText();
            String apPaterno = jTFApellPatEstudiante.getText();
            String apMaterno = jTFApellMatEstudiante.getText();
            String email = jTFEmailEstudiante.getText();
            String calle = jTFCalleEstudiante.getText();
            String colonia = jTFColoniaEstudiante.getText();
            String codPostal = jTFCPEstudiante.getText();
            String telefono = jTFTelefonoEstudiante.getText();
            int indice = jCBEstadoEstudiante.getSelectedIndex();
            int indice2 = jCBMunicipioEstudiante.getSelectedIndex();
            
            if (matricula.equals("") || nombre.equals("") || email.equals("")
                    || apPaterno.equals("")) {
                JOptionPane.showMessageDialog(null,
                        bundle.getString("errorModificarEstudiante"),
                        bundle.getString("tituloError"), WIDTH);

            } else {
                if (indice != -1 && indice2 != -2) {
                    //int indice = jCBEstadoEstudiante.getSelectedIndex();
                    //int indice2 = jCBMunicipioEstudiante.getSelectedIndex();
                    Municipio claveMunicipio = new Municipio();
                    Estado claveEstado = new Estado();
                    claveEstado.setIdEstado(actualEstado);

                    if (indice != -1) {
                        short indiceAux = (short) indice;
                        // claveMunicipio= modeloComboMunicipios.getElementAt(indice++);

                        //System.out.println("indice: " + indice);
                    }
                    if (indice2 != -1) {
                        //claveEstado = modeloComboEstados.getElementAt(indice2);
                        //System.out.println("indice2: "+indice2);
                        claveMunicipio = modeloComboMunicipios.getElementAt(indice2++);
                        //System.out.println(claveMunicipio);

                    }
                    e = new Estudiante(matricula, nombre, apPaterno,
                            apMaterno, calle, colonia, codPostal, telefono, email,
                            claveEstado.getIdEstado(), claveMunicipio.getIdMunicipio());

                } else {

                   
                    if(eSel.getIdEstado()==null||eSel.getIdMunicipio()==null){
                        e = new Estudiante(matricula, nombre, apPaterno,
                            apMaterno, calle, colonia, codPostal, telefono, email
                            );
                        
                    }
                    else{
                       
                         modeloComboMunicipios.setSelectedItem(
                            conector.obtenNombreMunicipio(eSel.getIdMunicipio()));
                    modeloComboEstados.setSelectedItem(
                            conector.obtenNombreEstado(eSel.getIdEstado()));
                        e = new Estudiante(matricula, nombre, apPaterno,
                            apMaterno, calle, colonia, codPostal, telefono, email,
                            eSel.getIdEstado(), eSel.getIdMunicipio());
                        
                    }
                    

                }

                jCBEstadoEstudiante.setSelectedIndex(-1);
                jCBMunicipioEstudiante.setSelectedIndex(-1);

            }
            return e;

        }

        /**
         * Metodo de interfaz Action listener para Manejar Botones para el panel
         * de Estudiante
         *
         * @param e evento
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton origen = (JButton) e.getSource();
            if (origen == jBotonNuevoEstudiante) {

                jTFMatriculaEstudiante.setEnabled(true);
                jTFMatriculaEstudiante.setEditable(true);
                jBotonAgregarEstudiante.setVisible(true);
                jCBEstadoEstudiante.setSelectedIndex(-1);
                jCBMunicipioEstudiante.setSelectedIndex(-1);
                jTFNombreEstudiante.setText("");
                jTFMatriculaEstudiante.setText("");
                //jTFMatriculaEstudiante.setEnabled(true);
                jTFApellPatEstudiante.setText("");
                jTFApellMatEstudiante.setText("");
                jTFEmailEstudiante.setText("");
                jTFCalleEstudiante.setText("");
                jTFColoniaEstudiante.setText("");
                jTFTelefonoEstudiante.setText("");
                jTFCPEstudiante.setText("");
                //jListCargaEstu.removeAll();
                inicializaCargaEstudiante();

                //jTFMatriculaEstudiante.setEditable(true);
            } else if (origen == jBotonEliminarEstudiante) {
                jTFMatriculaEstudiante.setEditable(false);
                int resp = JOptionPane.showConfirmDialog(null, bundle.getString(
                        "textoConfirmacionEliminacionEstudiante"),
                        bundle.getString("tituloConfirmacionEliminacionEstudiante"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (resp == JOptionPane.YES_OPTION) {
                    if (conector.eliminaEstudiante(eSel.getMatricula())) {
                        modeloListaEstudiante.removeElement(eSel);//Lo eliminamos de la clave
                        modeloComboEstudiante.removeElement(eSel);
                        eSel = null;//para decir que es nulo al eliminarlo
                        jCBEstadoEstudiante.setSelectedIndex(-1);
                        jCBMunicipioEstudiante.setSelectedIndex(-1);
                        inicializaCargaEstudiante();
                        System.out.println("Se ha eliminado el estudiante");
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("errorEliminarEstudiante"),
                            bundle.getString("tituloError"), WIDTH);
                }

            } else if (origen == jBotonModificarEstudiante) {
                Estudiante m = modificaEstudiante();
                jTFMatriculaEstudiante.setEditable(false);
                boolean consulta = true;

                if (conector.modificaEstudiante(m) == consulta) {
                    modeloListaEstudiante.removeElement(m);
                    modeloComboEstudiante.removeElement(m);
                    modeloListaEstudiante.addElement(m);
                    modeloComboEstudiante.addElement(m);
                    inicializaCargaEstudiante();

                } else {
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("errorModificarEstudiante"),
                            bundle.getString("tituloError"), WIDTH);
                }

            } else if (origen == jBotonAgregarEstudiante) {
                Estudiante m = obtenDatos();

                boolean consulta = true;
                if (conector.agregaEstudiante(m) == consulta) {
                    modeloListaEstudiante.addElement(m);
                    modeloComboEstudiante.addElement(m);
                    inicializaCargaEstudiante();
                } else {
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("errorAgregarEstudiante"),
                            bundle.getString("tituloError"), WIDTH);
                }
                jBotonAgregarEstudiante.setVisible(false);
                jTFMatriculaEstudiante.setEditable(false);

                jListEstudiante.clearSelection();

            }
        }

    }

    /**
     * Este metodo determina si el estado seleccionado es diferente al de los
     * municipios que se encuentra actualmente desplegados y si es asi, muestra
     * los municipios correspondientes
     *
     * @param idEstadoUsuario ID del estado seleccionado por el usuario
     */
    private void actualizaMunicipios(Short idEstadoUsuario) {
        // Determinamos si es necesario cargar municipios de otro estado
        // Por default se pone el estado 32 (zacatecas) en la base de datos
        // por lo tanto podemos asumir que nunca valdra 0        

        if (idEstadoUsuario != actualEstado) {
            // los cargamos de la base de datos si aun no estan cargados            
            actualEstado = idEstadoUsuario;
            if (datosMunicipios[idEstadoUsuario - 1]
                    == null) {
                datosMunicipios[idEstadoUsuario - 1]
                        = conector.obtenMunicipios(idEstadoUsuario);
            }
            modeloComboMunicipios.removeAllElements();
            for (Municipio m : datosMunicipios[idEstadoUsuario - 1]) {
                modeloComboMunicipios.addElement(m);

            }

        }
    }

    /**
     * Clase interna que maneja los botones del panel de la carga estudiante
     */
    private class ManejadorBotonesCargaEstudiante implements ActionListener {

        private CargaEstudiante obtenDatos() {
            CargaEstudiante cargaEstu = null;
            Estudiante estudiante = null;
            Materia materia = null;
            Carrera carrera = null;
            PeriodoEscolar periodoEscolar = null;
            Integer idPeriodo = null;
            String claveCarrera = null;
            String claveMateria = null;
            String matricula = null;
            //List<String> informacionEstudiante = new <String> ArrayList();
            CargaEstudiante ce = null;
            int indice = jCBEstudiante.getSelectedIndex();
            int indicePeriodo = jCBPeriodoEscolarEstudiante.getSelectedIndex();
            int indiceCarrera = jCBCarreraEstudiante.getSelectedIndex();
            int indiceMateria = jCBMateriaEstudiante.getSelectedIndex();

            if (indice != -1) {
                estudiante = modeloComboEstudiante.getElementAt(indice++);
                matricula = estudiante.getMatricula();

            }

            if (indicePeriodo != -1) {
                periodoEscolar = modeloComboPeriodoEstudiante.
                        getElementAt(indicePeriodo++);
                idPeriodo = periodoEscolar.getIdPeriodo();
                // System.out.println("id periodo " + idPeriodo);
            }
            if (indiceCarrera != -1) {
                carrera = modeloComboCarreraEstudiante.getElementAt(0);
                claveCarrera = carrera.getClaveCarrera();
                // System.out.println("Carrera " + carrera);
            }
            if (indiceMateria != -1) {
                materia = modeloComboMateriaEstudiante.
                        getElementAt(indiceMateria++);
                claveMateria = materia.getClaveMateria();
                // System.out.println("Matricula " + claveMateria);
            }
            if (claveMateria != null && idPeriodo != null) {
                try {

                    cargaEstu = new CargaEstudiante(
                            conector.obtenSigIdCargaEstudiante(), matricula,
                            claveMateria, idPeriodo);
                    cEstudiante = cargaEstu;
                } catch (NullPointerException npe) {

                }

            } else {
                cargaEstu = null;
            }
            return cargaEstu;
        }

        /**
         * Metodo de interfaz Action listener para Manejar Botones del panel de
         * la carga estudiante
         *
         * @param e evento
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton origen = (JButton) e.getSource();
            if (origen == jBotonManejaCargaEstudiante) {
                CargaEstudiante estudiante = new CargaEstudiante();
                Estudiante estu;

                int indiceEstudiante = jCBEstudiante.getSelectedIndex();

                modeloListaCarga = new DefaultListModel();
                if (indiceEstudiante != -1) {
                    estu = modeloComboEstudiante.getElementAt(indiceEstudiante++);
                    estudiante.setMatricula(estu.getMatricula());
                    //System.out.println("mat:"+estudiante.getMatricula());
                    List<ListaCargaEstudiante> carga = new ArrayList<ListaCargaEstudiante>();

                    carga = conector.obtenCargaEstudiante(estudiante);
                    // System.out.println("carga"+carga);

                    for (ListaCargaEstudiante listaCarga : carga) {

                        modeloListaCarga.addElement(listaCarga);

                    }
                    jListCargaEstudiante.setModel(modeloListaCarga);
                    //jListCargaEstu.setModel(modeloListaCarga);
                    jCBCarreraEstudiante.setSelectedIndex(-1);
                    jCBMateriaEstudiante.setSelectedIndex(-1);
                    jCBPeriodoEscolarEstudiante.setSelectedIndex(-1);
                    jPanelDetalleCargaEstudiante.setVisible(true);

                } else {
                    modeloListaCarga.removeAllElements();
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("errorSeleccionarEstudiante"),
                            bundle.getString("tituloError"), WIDTH);
                }

            //List<String> c = obtenDatos();

                /*for (String f : c) {
                 modeloListaCargaEstudiante.addElement(f);
                 }*/
                //jListCargaEstudiante.setModel(modeloListaCargaEstudiante);
                //jBotonAgregarCarrera.setVisible(false);
            }
            if (origen == jBotonNuevaCargaEstudiante) {

            //List<String> c = obtenDatos();

                /*for (String f : c) {
                 modeloListaCargaEstudiante.addElement(f);
                 }*/
                //jListCargaEstudiante.setModel(modeloListaCargaEstudiante);
                jCBPeriodoEscolarEstudiante.setEnabled(true);
                //jCBCarreraEstudiante.setEnabled(true);
                jCBMateriaEstudiante.setEnabled(true);
                jBotonAgregarCarga.setVisible(true);
                indiceCarrera = true;
                jCBCarreraEstudiante.setSelectedIndex(-1);
                jCBPeriodoEscolarEstudiante.setSelectedIndex(-1);
                jCBMateriaEstudiante.setSelectedIndex(-1);
                jListCargaEstudiante.clearSelection();
                //jBotonAgregarCarrera.setVisible(false);

            }
            if (origen == jBotonEliminaCargaEstudiante) {
                Estudiante es;
                String matricula;

                CargaEstudiante ce = new CargaEstudiante(listCargaEstu.getIdCargaEstudiante());
                // System.out.println("" + ce.getIdCargaEstudiante());

                int indice = jCBEstudiante.getSelectedIndex();

                if (indice != -1) {
                    //es = modeloComboEstudiante.getElementAt(indice++);
                    //matricula = es.getMatricula();
                    //ce = new CargaEstudiante();
                    //ce.setMatricula(matricula);
                    boolean info;
                    //info = conector.eliminaCargaEstudiante(ce);
                    int resp = JOptionPane.showConfirmDialog(null,
                            bundle.getString("textoConfirmacionEliminacionCargaEstudiante"),
                            bundle.getString("tituloConfirmacionEliminacionCargaEstudiante"),
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.YES_OPTION) {
                        if (conector.eliminaCargaEstudiante(ce)) {
                            //modeloListaEstudiante.removeElementAt(indice);//Lo eliminamos de la clave
                            //modeloComboEstudiante.removeElement(eSel);
                            // eSel = null;//para decir que es nulo al eliminarlo
                            listCargaEstu = null;
                            CargaEstudiante estudiante = new CargaEstudiante();
                            Estudiante estu;

                            int indiceEstudiante = jCBEstudiante.getSelectedIndex();

                            modeloListaCarga = new DefaultListModel();
                            if (indiceEstudiante != -1) {
                                estu = modeloComboEstudiante.getElementAt(indiceEstudiante++);
                                estudiante.setMatricula(estu.getMatricula());
                                //System.out.println("mat:"+estudiante.getMatricula());
                                List<ListaCargaEstudiante> carga = new ArrayList<ListaCargaEstudiante>();
                                carga = conector.obtenCargaEstudiante(estudiante);

                                for (ListaCargaEstudiante listaCarga : carga) {

                                    modeloListaCarga.addElement(listaCarga);

                                }
                                jListCargaEstudiante.setModel(modeloListaCarga);
                                jListCargaEstu.setModel(modeloListaCarga);

                            } else {
                                modeloListaCarga.removeAllElements();
                            }

                        }

                    }

                }
            } else if (origen == jBotonAgregarCarga) {
                CargaEstudiante carga = obtenDatos();

                boolean consulta = true;
                if (carga != null) {
                    if (conector.agregaCargaEstudiante(carga) == consulta) {

                        jBotonAgregarCarga.setVisible(false);
                        CargaEstudiante estudiante = new CargaEstudiante();
                        Estudiante estu;

                        int indiceEstudiante = jCBEstudiante.getSelectedIndex();

                        modeloListaCarga = new DefaultListModel();
                        if (indiceEstudiante != -1) {
                            estu = modeloComboEstudiante.getElementAt(indiceEstudiante++);
                            estudiante.setMatricula(estu.getMatricula());
                            //System.out.println("mat:"+estudiante.getMatricula());
                            List<ListaCargaEstudiante> cargaNueva = new ArrayList<ListaCargaEstudiante>();
                            cargaNueva = conector.obtenCargaEstudiante(estudiante);

                            for (ListaCargaEstudiante listaCarga : cargaNueva) {

                                modeloListaCarga.addElement(listaCarga);

                            }
                            jListCargaEstudiante.setModel(modeloListaCarga);
                            //jListCargaEstu.setModel(modeloListaCarga);

                        } else {
                            modeloListaCarga.removeAllElements();
                        }

                    } else {
                        JOptionPane.showMessageDialog(null,
                                bundle.getString("errorAgregarCargaEstudiante"),
                                bundle.getString("tituloError"), WIDTH);
                    }
                    //jBotonAgregarEstudiante.setVisible(false);
                    jListCargaEstudiante.clearSelection();

                } else {
                    jBotonAgregarCarga.setVisible(false);
                    JOptionPane.showMessageDialog(null,
                            bundle.getString("errorAgregarCargaEstudiante"),
                            bundle.getString("tituloError"), WIDTH);

                }

            }

        }
    }

    /**
     * Metodo que inicializa la lista del panel de carreras
     */
    private void inicializaCarreras() {
        modeloListaCarreras = new DefaultListModel<Carrera>();//inicializamos el modelo
        modeloComboCarreraEstudiante = new DefaultComboBoxModel<Carrera>();
        List<Carrera> datos = conector.obtenCarreras();//conseguimos la lista
        for (Carrera c : datos) {
            modeloListaCarreras.addElement(c);
            modeloComboCarreraEstudiante.addElement(c);
        }
        jListCarreras.setModel(modeloListaCarreras);
        //jCBCarreraEstudiante.setModel(modeloComboCarreraEstudiante);
        jBotonAgregarCarrera.setVisible(false);
        jTFClaveCarrera.setEditable(false);

    }

    /**
     * Muestra la informacion de las Materias asi como la lista del panel de
     * Materias
     */
    private void inicializaMaterias() {
        modeloListaMateria = new DefaultListModel<Materia>();
        modeloComboMateria = new DefaultComboBoxModel<Carrera>();
        modeloComboMateriaEstudiante = new DefaultComboBoxModel<Materia>();
        modeloComboCarreraEstudiante = new DefaultComboBoxModel<Carrera>();

        List<Materia> datos = conector.obtenMaterias();
        List<Carrera> dat = conector.obtenCarreras();
        for (Materia m : datos) {
            modeloListaMateria.addElement(m);
            modeloComboMateriaEstudiante.addElement(m);

        }
        for (Carrera c : dat) {

            modeloComboMateria.addElement(c);
            //modeloComboCarreraEstudiante.addElement(c);

        }
        jListMateria.setModel(modeloListaMateria);
        jCBCarrera.setModel(modeloComboMateria);
        //jCBCarreraEstudiante.setModel(modeloComboMateria);
        jCBMateriaEstudiante.setModel(modeloComboMateriaEstudiante);
        jCBMateriaEstudiante.setEnabled(false);
        jTFClaveMateria.setEditable(false);

        jBotonAgregarMateria.setVisible(false);
        jCBCarrera.setSelectedIndex(-1);
        //jCBMunicipioEstudiante.setSelectedIndex(-1);
    }

    /**
     * Metodo que inicializa la lista y el combobox de Periodo escolar
     */
    private void inicializaPeriodos() {
        modeloListaPeriodo = new DefaultListModel<PeriodoEscolar>();
        modeloComboPeriodo = new DefaultComboBoxModel<String>();
        modeloComboPeriodoEstudiante = new DefaultComboBoxModel<PeriodoEscolar>();
        //modeloComboPeriodo= new DefaultComboBoxModel<PeriodoEscolar>();
        List<PeriodoEscolar> datos = conector.obtenPeriodos();
        //SortedSet<String>dat=conector.periodo();
        List<Short> datosDeComboPeriodos = conector.periodoBox();
        jTFIdPeriodoEscolar.setEditable(false);
        jBotonAgregarPeriodo.setVisible(false);
        for (PeriodoEscolar pE : datos) {
            modeloListaPeriodo.addElement(pE);
            modeloComboPeriodoEstudiante.addElement(pE);
        }

        for (short periodo : datosDeComboPeriodos) {
            /*
             if (c == 1) {
             estacion = conector.obtenPeriodoString(periodo);

             } else if (c == 2) {
             //estacion = "Enero-Junio";
             estacion = conector.obtenPeriodoString(periodo);
             } else if (c == 3) {
             // estacion = "Verano";
             estacion = conector.obtenPeriodoString(periodo);
             }
             */
            estacion = conector.obtenPeriodoString(periodo);

            modeloComboPeriodo.addElement(estacion);

        }
        jCBPeriodoEscolarEstudiante.setModel(modeloComboPeriodoEstudiante);
        jListPeriodoEscolar.setModel(modeloListaPeriodo);
        jCBSemestre.setModel(modeloComboPeriodo);
        //jBotonAgregarPeriodo.setVisible(false);
        jCBSemestre.setSelectedIndex(-1);
        //jCBMunicipioEstudiante.setSelectedIndex(-1);
    }

    /**
     * Metodo que inicializa el panel de CargaEstudiante
     */
    private void inicializaCargaEstudiante() {
        jPanelDetalleCargaEstudiante.setVisible(false);
        jBotonAgregarCarga.setVisible(false);
        jCBPeriodoEscolarEstudiante.setEnabled(false);
        jCBCarreraEstudiante.setEnabled(false);
        jBotonEliminaCargaEstudiante.setEnabled(false);
        jCBMateriaEstudiante.setEnabled(false);
        modeloComboEstudiante = new DefaultComboBoxModel<Estudiante>();

        modeloListaCargaEstudiante = new DefaultListModel<CargaEstudiante>();//inicializamos el modelo
        List<Estudiante> datos = conector.obtenEstudiantes();

        for (Estudiante e : datos) {
            modeloComboEstudiante.addElement(e);

        }

        jCBEstudiante.setModel(modeloComboEstudiante);
        jCBCarreraEstudiante.setModel(modeloComboCarreraEstudiante);
        jCBEstudiante.setSelectedIndex(-1);
        jCBMateriaEstudiante.setSelectedIndex(-1);
        jCBPeriodoEscolarEstudiante.setSelectedIndex(-1);

        CargaEstudiante ce = new CargaEstudiante();
        /**
         * List<CargaEstudiante> datos2 =
         * conector.obtenCargaEstudiante();//conseguimos la lista for
         * (CargaEstudiante c : datos2) {
         * modeloListaCargaEstudiante.addElement(c); }
         */
        jListCargaEstudiante.setModel(modeloListaCargaEstudiante);
        //jBotonAgregarCarrera.setVisible(false);

    }

    /**
     * Esta clase se encarga de manejar los eventos sobre la ventana
     *
     */
    private class ManejadorVentana extends WindowAdapter {

        /**
         * Este metodo se ejecuta al dar click en el icoco de cerrar la ventana
         * y pedira la confirmacion de salida
         *
         * @param e Objeto WindowEvent con informacion del evento
         */
        @Override
        public void windowClosing(WindowEvent e) {
            confirmaSalida();
        }
    }

    /**
     * Clase interna para delimitar el numero de caracteres en un jTextField
     */
    private class JTextFieldLimit extends PlainDocument {

        private int limit;

        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        /**
         * Metodo que limita los caracteres que pueden entran en un text field
         *
         * @param offset
         * @param str
         * @param attr
         * @throws BadLocationException
         */
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) {
                return;
            }

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }

    /**
     * Creates new form GUIControlEscolar crea el gui y ademas carga con todo el
     * sistema
     */
    public GUIControlEscolar() {
        initComponents();

        //limitadores de espacios de j text field
        jTFNombreCarrera.setDocument(new JTextFieldLimit(50));
        jTFClaveCarrera.setDocument(new JTextFieldLimit(10));
        jTFNombreMateria.setDocument(new JTextFieldLimit(50));
        jTFClaveMateria.setDocument(new JTextFieldLimit(10));
        //jTFSemestreMateria.setDocument(new JTextFieldLimit());
        //jTFIdPeriodoEscolar.setDocument(new JTextFieldLimit(10));
        jTFNombreEstudiante.setDocument(new JTextFieldLimit(25));
        jTFMatriculaEstudiante.setDocument(new JTextFieldLimit(8));
        jTFApellPatEstudiante.setDocument(new JTextFieldLimit(20));
        jTFApellMatEstudiante.setDocument(new JTextFieldLimit(20));
        jTFCalleEstudiante.setDocument(new JTextFieldLimit(30));
        jTFColoniaEstudiante.setDocument(new JTextFieldLimit(30));
        jTFCPEstudiante.setDocument(new JTextFieldLimit(5));
        jTFTelefonoEstudiante.setDocument(new JTextFieldLimit(10));
        jTFEmailEstudiante.setDocument(new JTextFieldLimit(30));

        bundle = ResourceBundle.getBundle("Bundle");

        // Eventos de la ventana
        addWindowListener(new ManejadorVentana());
        indiceCarrera = false;
        // Eventos de key listener de jTextField
        jTFSemestreMateria.addKeyListener(new ManejadorJTextFieldNumeros());
        jTFTelefonoEstudiante.addKeyListener(new ManejadorJTextFieldNumeros());
        jTFCPEstudiante.addKeyListener(new ManejadorJTextFieldNumeros());
        jTFMatriculaEstudiante.addKeyListener(new ManejadorJTextFieldNumeros());
        jTFAnoPeriodoEscolar.addKeyListener(new ManejadorJTextFieldNumeros());
        jTFEmailEstudiante.addKeyListener(new ManejadorJTextFieldEmail());
        jTFNombreEstudiante.addKeyListener(new ManejadorJTextFieldNombres());
        jTFApellPatEstudiante.addKeyListener(new ManejadorJTextFieldNombres());
        jTFApellMatEstudiante.addKeyListener(new ManejadorJTextFieldNombres());

        // Eventos del combo box estado
        jCBEstadoEstudiante.addItemListener(
                new ManejadorComboEstados());
        //jCBCarreraEstudiante.addItemListener(new ManejadorComboCarreras());
        jCBMateriaEstudiante.addItemListener(new ManejadorComboCarreras());

        //listas botones y combo box
        jListCarreras.addListSelectionListener(new ManejadorListaCarreras());
        jListMateria.addListSelectionListener(new ManejadorListaMaterias());
        jListPeriodoEscolar.addListSelectionListener(new ManejadorListaPeriodosEscolar());
        jListEstudiante.addListSelectionListener(new ManejadorListaEstudiante());
        ManejadorBotonesEstudiante manBtnEstudiante = new ManejadorBotonesEstudiante();
        jListCargaEstudiante.addListSelectionListener(new ManejadorListaCargaEstudiante());
        ManejadorBotonesCarrera manBtnCarrera = new ManejadorBotonesCarrera();
        ManejadorBotonesMateria manBtnMateria = new ManejadorBotonesMateria();
        ManejadorBotonesPeriodo manBtnPeriodo = new ManejadorBotonesPeriodo();
        ManejadorBotonesCargaEstudiante manBtnManCarEstudiante
                = new ManejadorBotonesCargaEstudiante();
        jBotonAgregarCarga.addActionListener(manBtnManCarEstudiante);
        jBotonEliminaCargaEstudiante.addActionListener(manBtnManCarEstudiante);
        jBotonNuevaCargaEstudiante.addActionListener(manBtnManCarEstudiante);
        jBotonManejaCargaEstudiante.addActionListener(manBtnManCarEstudiante);
        jBotonAgregarEstudiante.addActionListener(manBtnEstudiante);
        jBotonNuevoEstudiante.addActionListener(manBtnEstudiante);
        jBotonModificarEstudiante.addActionListener(manBtnEstudiante);
        jBotonEliminarEstudiante.addActionListener(manBtnEstudiante);
        jBotonModificarPeriodo.addActionListener(manBtnPeriodo);
        jBotonEliminarPeriodo.addActionListener(manBtnPeriodo);
        jBotonNuevoPeriodo.addActionListener(manBtnPeriodo);
        jBotonAgregarPeriodo.addActionListener(manBtnPeriodo);
        jBotonEliminarMateria.addActionListener(manBtnMateria);
        jBotonNuevaMateria.addActionListener(manBtnMateria);
        jBotonModificarMateria.addActionListener(manBtnMateria);
        jBotonAgregarMateria.addActionListener(manBtnMateria);
        jBotonNuevaCarrera.addActionListener(manBtnCarrera);
        jBotonAgregarCarrera.addActionListener(manBtnCarrera);
        jBotonModificarCarrera.addActionListener(manBtnCarrera);
        jBotonEliminarCarrera.addActionListener(manBtnCarrera);

        // Se intenta conectar a la base de datos
        boolean conectado = false;
        try {
            jLabelLineaStatus.setText(bundle.getString("etiquetaIntentandoConectar"));
            conector = new ConectorBaseDeDatos(
                    bundle.getString("dirServidor"),
                    Integer.parseInt(bundle.getString("puertoServidor")),
                    bundle.getString("usuarioServidor"),
                    bundle.getString("claveServidor"));
            conectado = true;
            jLabelLineaStatus.setText(bundle.getString("etiquetaConexionExitosa"));
        } catch (ConexionException e) {
            jLabelLineaStatus.setText(bundle.getString("etiquetaConexionFallida")
                    + e.getMessage());
        }
        // Si la conexion fue exitosa, inicializamos datos de las interfaces
        if (conectado) {
            inicializaEstudiantes();
            inicializaCarreras();
            inicializaMaterias();
            inicializaPeriodos();
            //inicializaEstudiante();
            inicializaCargaEstudiante();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogAcercaDe = new javax.swing.JDialog();
        jLabelTexto = new javax.swing.JLabel();
        jLabelFoto = new javax.swing.JLabel();
        jBotonOK = new javax.swing.JButton();
        jTabbedEscolar = new javax.swing.JTabbedPane();
        jPanelCarreras = new javax.swing.JPanel();
        jPanelInfoCarreras = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTFNombreCarrera = new javax.swing.JTextField();
        jTFClaveCarrera = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListCarreras = new javax.swing.JList();
        jBotonNuevaCarrera = new javax.swing.JButton();
        jBotonModificarCarrera = new javax.swing.JButton();
        jBotonEliminarCarrera = new javax.swing.JButton();
        jBotonAgregarCarrera = new javax.swing.JButton();
        jPanelMaterias = new javax.swing.JPanel();
        jPanelMateria = new javax.swing.JPanel();
        jPanelInfoMateria = new javax.swing.JPanel();
        jLNombreMateria = new javax.swing.JLabel();
        jLClaveMateria = new javax.swing.JLabel();
        jTFNombreMateria = new javax.swing.JTextField();
        jTFClaveMateria = new javax.swing.JTextField();
        jLSemestre = new javax.swing.JLabel();
        jTFSemestreMateria = new javax.swing.JTextField();
        jLCarrera = new javax.swing.JLabel();
        jCBCarrera = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListMateria = new javax.swing.JList();
        jBotonAgregarMateria = new javax.swing.JButton();
        jBotonNuevaMateria = new javax.swing.JButton();
        jBotonModificarMateria = new javax.swing.JButton();
        jBotonEliminarMateria = new javax.swing.JButton();
        jPanelPeriodosEscolares = new javax.swing.JPanel();
        jPanelInfoPeriodos = new javax.swing.JPanel();
        jLIDPeriodoEscolar = new javax.swing.JLabel();
        jLabelSemestrePE = new javax.swing.JLabel();
        jCBSemestre = new javax.swing.JComboBox();
        jTFIdPeriodoEscolar = new javax.swing.JTextField();
        jLAno = new javax.swing.JLabel();
        jTFAnoPeriodoEscolar = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListPeriodoEscolar = new javax.swing.JList();
        jBotonNuevoPeriodo = new javax.swing.JButton();
        jBotonModificarPeriodo = new javax.swing.JButton();
        jBotonEliminarPeriodo = new javax.swing.JButton();
        jBotonAgregarPeriodo = new javax.swing.JButton();
        jPanelProfesores = new javax.swing.JPanel();
        jPanelInfoProfesor = new javax.swing.JPanel();
        jLNombre = new javax.swing.JLabel();
        jLabelRFC = new javax.swing.JLabel();
        jLApellidoPat = new javax.swing.JLabel();
        jLabelEmail = new javax.swing.JLabel();
        jLApellidoMat = new javax.swing.JLabel();
        jPanelDireccion = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTFCalle = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTFColonia = new javax.swing.JTextField();
        jLabelEstado = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabelMunicipio = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabelCodPostal = new javax.swing.JLabel();
        jTFCP = new javax.swing.JTextField();
        jLabelTelefono = new javax.swing.JLabel();
        jTFTelefono = new javax.swing.JTextField();
        jTFNombre = new javax.swing.JTextField();
        jTFApellidoPat = new javax.swing.JTextField();
        jTFApellidoMat = new javax.swing.JTextField();
        jTFRFC = new javax.swing.JTextField();
        jTFEmail = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButtonNuevo = new javax.swing.JButton();
        jButtonModificar = new javax.swing.JButton();
        jButtonEliminar = new javax.swing.JButton();
        jPanelCargaProf = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jPanelCargaProfesor = new javax.swing.JPanel();
        jPanelSeleccionProfesor = new javax.swing.JPanel();
        jLabelProf = new javax.swing.JLabel();
        jComboBoxProf = new javax.swing.JComboBox();
        jBotonManejaCarga = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabelPeriodoEscolar = new javax.swing.JLabel();
        jComboBoxPeriodos = new javax.swing.JComboBox();
        jLabelMateria = new javax.swing.JLabel();
        jComboBoxMateria = new javax.swing.JComboBox();
        jLabelCarrera = new javax.swing.JLabel();
        jComboBoxCarrera = new javax.swing.JComboBox();
        jScrollPane6 = new javax.swing.JScrollPane();
        jListCargas = new javax.swing.JList();
        jButtonNew = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jPanelEstudiantes = new javax.swing.JPanel();
        jPanelInfoEstudiante = new javax.swing.JPanel();
        jPanelDireccionEstudiante = new javax.swing.JPanel();
        jLCalleEstudiante = new javax.swing.JLabel();
        jTFCalleEstudiante = new javax.swing.JTextField();
        jLColoniaEstudiante = new javax.swing.JLabel();
        jTFColoniaEstudiante = new javax.swing.JTextField();
        jLabelCodPostalEstudiante = new javax.swing.JLabel();
        jTFCPEstudiante = new javax.swing.JTextField();
        jLabelTelefonoEstudiante = new javax.swing.JLabel();
        jTFTelefonoEstudiante = new javax.swing.JTextField();
        jLEstadoEstudiante = new javax.swing.JLabel();
        jCBEstadoEstudiante = new javax.swing.JComboBox();
        jLMunicipio = new javax.swing.JLabel();
        jCBMunicipioEstudiante = new javax.swing.JComboBox();
        jLNombreEstudiante = new javax.swing.JLabel();
        jTFNombreEstudiante = new javax.swing.JTextField();
        jLApellidoPatEstudiante = new javax.swing.JLabel();
        jTFApellPatEstudiante = new javax.swing.JTextField();
        jLApellidoMat1 = new javax.swing.JLabel();
        jTFApellMatEstudiante = new javax.swing.JTextField();
        jLabelEmailEstudiante = new javax.swing.JLabel();
        jTFEmailEstudiante = new javax.swing.JTextField();
        jLMatriculaEstudiante = new javax.swing.JLabel();
        jTFMatriculaEstudiante = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        jListEstudiante = new javax.swing.JList();
        jBotonNuevoEstudiante = new javax.swing.JButton();
        jBotonModificarEstudiante = new javax.swing.JButton();
        jBotonEliminarEstudiante = new javax.swing.JButton();
        jPanelCargaEstu = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jListCargaEstu = new javax.swing.JList();
        jBotonAgregarEstudiante = new javax.swing.JButton();
        jPanelCargaEstudiante = new javax.swing.JPanel();
        jPanelSeleccionEstudiante = new javax.swing.JPanel();
        jLEstudianteCarga = new javax.swing.JLabel();
        jCBEstudiante = new javax.swing.JComboBox();
        jBotonManejaCargaEstudiante = new javax.swing.JButton();
        jPanelDetalleCargaEstudiante = new javax.swing.JPanel();
        jLPeriodoEscolarEstudiante = new javax.swing.JLabel();
        jCBPeriodoEscolarEstudiante = new javax.swing.JComboBox();
        jLMateriasCargaEstudiante = new javax.swing.JLabel();
        jCBMateriaEstudiante = new javax.swing.JComboBox();
        jLCarreraEstudiante = new javax.swing.JLabel();
        jCBCarreraEstudiante = new javax.swing.JComboBox();
        jScrollPane7 = new javax.swing.JScrollPane();
        jListCargaEstudiante = new javax.swing.JList();
        jBotonNuevaCargaEstudiante = new javax.swing.JButton();
        jBotonEliminaCargaEstudiante = new javax.swing.JButton();
        jBotonAgregarCarga = new javax.swing.JButton();
        jLabelLineaStatus = new javax.swing.JLabel();
        jMenuBarControlEscolar = new javax.swing.JMenuBar();
        jMenuArchivo = new javax.swing.JMenu();
        jMenuItemSalir = new javax.swing.JMenuItem();
        jMenuAyuda = new javax.swing.JMenu();
        jMenuItemAcercaDe = new javax.swing.JMenuItem();

        jDialogAcercaDe.setTitle("Acerca de ....");
        jDialogAcercaDe.setMinimumSize(new java.awt.Dimension(626, 296));
        jDialogAcercaDe.setModal(true);
        jDialogAcercaDe.setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        jDialogAcercaDe.setResizable(false);

        jLabelTexto.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("Bundle"); // NOI18N
        jLabelTexto.setText(bundle.getString("etiquetaAutor")); // NOI18N

        jLabelFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/uaz/poo2/gui/foto.jpg"))); // NOI18N

        jBotonOK.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jBotonOK.setText(bundle.getString("etiquetaBotonOK")); // NOI18N
        jBotonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBotonOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogAcercaDeLayout = new javax.swing.GroupLayout(jDialogAcercaDe.getContentPane());
        jDialogAcercaDe.getContentPane().setLayout(jDialogAcercaDeLayout);
        jDialogAcercaDeLayout.setHorizontalGroup(
            jDialogAcercaDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogAcercaDeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTexto, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabelFoto)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogAcercaDeLayout.createSequentialGroup()
                .addContainerGap(299, Short.MAX_VALUE)
                .addComponent(jBotonOK)
                .addGap(274, 274, 274))
        );
        jDialogAcercaDeLayout.setVerticalGroup(
            jDialogAcercaDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAcercaDeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogAcercaDeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTexto, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                    .addComponent(jLabelFoto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBotonOK)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(bundle.getString("tituloAplicacion")); // NOI18N

        jTabbedEscolar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedEscolarStateChanged(evt);
            }
        });
        jTabbedEscolar.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                jTabbedEscolarVetoableChange(evt);
            }
        });

        jPanelInfoCarreras.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("etiquetaPanelInfoCarrera"))); // NOI18N

        jLabel3.setText(bundle.getString("etiquetaNombreCarrera")); // NOI18N

        jLabel4.setText(bundle.getString("etiquetaClaveCarrera")); // NOI18N

        jScrollPane1.setViewportView(jListCarreras);

        jBotonNuevaCarrera.setText(bundle.getString("etiquetaBotonNueva")); // NOI18N

        jBotonModificarCarrera.setText(bundle.getString("etiquetaBotonModificar")); // NOI18N
        jBotonModificarCarrera.setEnabled(false);

        jBotonEliminarCarrera.setText(bundle.getString("etiquetaBotonEliminar")); // NOI18N
        jBotonEliminarCarrera.setEnabled(false);

        jBotonAgregarCarrera.setText(bundle.getString("etiquetaBotonAgregar")); // NOI18N

        javax.swing.GroupLayout jPanelInfoCarrerasLayout = new javax.swing.GroupLayout(jPanelInfoCarreras);
        jPanelInfoCarreras.setLayout(jPanelInfoCarrerasLayout);
        jPanelInfoCarrerasLayout.setHorizontalGroup(
            jPanelInfoCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoCarrerasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelInfoCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInfoCarrerasLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFClaveCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBotonAgregarCarrera))
                    .addGroup(jPanelInfoCarrerasLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFNombreCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 765, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1015, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                .addGroup(jPanelInfoCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jBotonModificarCarrera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBotonNuevaCarrera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jBotonEliminarCarrera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelInfoCarrerasLayout.setVerticalGroup(
            jPanelInfoCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoCarrerasLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanelInfoCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTFNombreCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanelInfoCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTFClaveCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBotonAgregarCarrera))
                .addGap(18, 18, 18)
                .addGroup(jPanelInfoCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInfoCarrerasLayout.createSequentialGroup()
                        .addComponent(jBotonNuevaCarrera)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBotonModificarCarrera)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBotonEliminarCarrera))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(128, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelCarrerasLayout = new javax.swing.GroupLayout(jPanelCarreras);
        jPanelCarreras.setLayout(jPanelCarrerasLayout);
        jPanelCarrerasLayout.setHorizontalGroup(
            jPanelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCarrerasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelInfoCarreras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanelCarrerasLayout.setVerticalGroup(
            jPanelCarrerasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCarrerasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelInfoCarreras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedEscolar.addTab(bundle.getString("tituloTabCarreras"), jPanelCarreras); // NOI18N

        jPanelInfoMateria.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("infoPanelMateria"))); // NOI18N

        jLNombreMateria.setText(bundle.getString("etiquetaNombreMateria")); // NOI18N

        jLClaveMateria.setText(bundle.getString("etiquetaClaveMateria")); // NOI18N

        jTFClaveMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFClaveMateriaActionPerformed(evt);
            }
        });

        jLSemestre.setText(bundle.getString("etiquetaSemestre")); // NOI18N

        jTFSemestreMateria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTFSemestreMateriaKeyTyped(evt);
            }
        });

        jLCarrera.setText(bundle.getString("etiquetaCarrera")); // NOI18N

        jScrollPane2.setViewportView(jListMateria);

        jBotonAgregarMateria.setText(bundle.getString("etiquetaBotonAgregar")); // NOI18N

        javax.swing.GroupLayout jPanelInfoMateriaLayout = new javax.swing.GroupLayout(jPanelInfoMateria);
        jPanelInfoMateria.setLayout(jPanelInfoMateriaLayout);
        jPanelInfoMateriaLayout.setHorizontalGroup(
            jPanelInfoMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoMateriaLayout.createSequentialGroup()
                .addGroup(jPanelInfoMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInfoMateriaLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(jPanelInfoMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelInfoMateriaLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(jLCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCBCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelInfoMateriaLayout.createSequentialGroup()
                                .addComponent(jLClaveMateria)
                                .addGap(18, 18, 18)
                                .addComponent(jTFClaveMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLSemestre, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFSemestreMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(jBotonAgregarMateria))))
                    .addGroup(jPanelInfoMateriaLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLNombreMateria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFNombreMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 750, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelInfoMateriaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1033, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(190, Short.MAX_VALUE))
        );
        jPanelInfoMateriaLayout.setVerticalGroup(
            jPanelInfoMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoMateriaLayout.createSequentialGroup()
                .addGroup(jPanelInfoMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInfoMateriaLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanelInfoMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLNombreMateria)
                            .addComponent(jTFNombreMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelInfoMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelInfoMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLSemestre)
                                .addComponent(jTFSemestreMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelInfoMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLClaveMateria)
                                .addComponent(jTFClaveMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInfoMateriaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jBotonAgregarMateria)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelInfoMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLCarrera)
                    .addComponent(jCBCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelMateriaLayout = new javax.swing.GroupLayout(jPanelMateria);
        jPanelMateria.setLayout(jPanelMateriaLayout);
        jPanelMateriaLayout.setHorizontalGroup(
            jPanelMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMateriaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelInfoMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelMateriaLayout.setVerticalGroup(
            jPanelMateriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMateriaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelInfoMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jBotonNuevaMateria.setText(bundle.getString("etiquetaBotonNueva")); // NOI18N

        jBotonModificarMateria.setText(bundle.getString("etiquetaBotonModificar")); // NOI18N
        jBotonModificarMateria.setEnabled(false);

        jBotonEliminarMateria.setText(bundle.getString("etiquetaBotonEliminar")); // NOI18N
        jBotonEliminarMateria.setEnabled(false);

        javax.swing.GroupLayout jPanelMateriasLayout = new javax.swing.GroupLayout(jPanelMaterias);
        jPanelMaterias.setLayout(jPanelMateriasLayout);
        jPanelMateriasLayout.setHorizontalGroup(
            jPanelMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMateriasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 952, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                .addGroup(jPanelMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jBotonModificarMateria, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jBotonNuevaMateria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBotonEliminarMateria, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(108, 108, 108))
        );
        jPanelMateriasLayout.setVerticalGroup(
            jPanelMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelMateriasLayout.createSequentialGroup()
                .addGroup(jPanelMateriasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelMateriasLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jPanelMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelMateriasLayout.createSequentialGroup()
                        .addGap(185, 185, 185)
                        .addComponent(jBotonNuevaMateria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBotonModificarMateria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBotonEliminarMateria)))
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jTabbedEscolar.addTab(bundle.getString("tituloTabMaterias"), jPanelMaterias); // NOI18N

        jPanelPeriodosEscolares.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPanelPeriodosEscolaresFocusLost(evt);
            }
        });

        jPanelInfoPeriodos.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("infoPanelPeriodos"))); // NOI18N
        jPanelInfoPeriodos.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                jPanelInfoPeriodosComponentMoved(evt);
            }
        });

        jLIDPeriodoEscolar.setText(bundle.getString("etiquetaIDPeridoEscolar")); // NOI18N

        jLabelSemestrePE.setText(bundle.getString("etiquetaSemestre")); // NOI18N

        jLAno.setText(bundle.getString("etiquetaAno")); // NOI18N

        jScrollPane3.setViewportView(jListPeriodoEscolar);

        jBotonNuevoPeriodo.setText(bundle.getString("etiquetaBotonNueva")); // NOI18N

        jBotonModificarPeriodo.setText(bundle.getString("etiquetaBotonModificar")); // NOI18N
        jBotonModificarPeriodo.setEnabled(false);

        jBotonEliminarPeriodo.setText(bundle.getString("etiquetaBotonEliminar")); // NOI18N
        jBotonEliminarPeriodo.setEnabled(false);

        jBotonAgregarPeriodo.setText(bundle.getString("etiquetaBotonAgregar")); // NOI18N

        javax.swing.GroupLayout jPanelInfoPeriodosLayout = new javax.swing.GroupLayout(jPanelInfoPeriodos);
        jPanelInfoPeriodos.setLayout(jPanelInfoPeriodosLayout);
        jPanelInfoPeriodosLayout.setHorizontalGroup(
            jPanelInfoPeriodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoPeriodosLayout.createSequentialGroup()
                .addGroup(jPanelInfoPeriodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInfoPeriodosLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanelInfoPeriodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelSemestrePE)
                            .addComponent(jLIDPeriodoEscolar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelInfoPeriodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelInfoPeriodosLayout.createSequentialGroup()
                                .addComponent(jCBSemestre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(jBotonAgregarPeriodo))
                            .addGroup(jPanelInfoPeriodosLayout.createSequentialGroup()
                                .addComponent(jTFIdPeriodoEscolar, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLAno)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTFAnoPeriodoEscolar, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanelInfoPeriodosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1038, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelInfoPeriodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jBotonModificarPeriodo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBotonNuevoPeriodo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jBotonEliminarPeriodo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanelInfoPeriodosLayout.setVerticalGroup(
            jPanelInfoPeriodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoPeriodosLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanelInfoPeriodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLIDPeriodoEscolar)
                    .addComponent(jTFIdPeriodoEscolar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLAno)
                    .addComponent(jTFAnoPeriodoEscolar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelInfoPeriodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSemestrePE)
                    .addComponent(jCBSemestre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBotonAgregarPeriodo))
                .addGroup(jPanelInfoPeriodosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInfoPeriodosLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jBotonNuevoPeriodo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBotonModificarPeriodo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBotonEliminarPeriodo))
                    .addGroup(jPanelInfoPeriodosLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelPeriodosEscolaresLayout = new javax.swing.GroupLayout(jPanelPeriodosEscolares);
        jPanelPeriodosEscolares.setLayout(jPanelPeriodosEscolaresLayout);
        jPanelPeriodosEscolaresLayout.setHorizontalGroup(
            jPanelPeriodosEscolaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPeriodosEscolaresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelInfoPeriodos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        jPanelPeriodosEscolaresLayout.setVerticalGroup(
            jPanelPeriodosEscolaresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPeriodosEscolaresLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanelInfoPeriodos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelInfoPeriodos.getAccessibleContext().setAccessibleName(bundle.getString("infoPanelPeriodos")); // NOI18N

        jTabbedEscolar.addTab(bundle.getString("tituloTabPeriodos"), jPanelPeriodosEscolares); // NOI18N
        jPanelPeriodosEscolares.getAccessibleContext().setAccessibleName(bundle.getString("tituloTabPeriodos")); // NOI18N

        jPanelInfoProfesor.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("tituloPanelInfoProfesor"))); // NOI18N

        jLNombre.setText(bundle.getString("etiquetaNombre")); // NOI18N

        jLabelRFC.setText(bundle.getString("etiquetaRFC")); // NOI18N

        jLApellidoPat.setText(bundle.getString("etiquetaApellidoPat")); // NOI18N

        jLabelEmail.setText(bundle.getString("etiquetaEmail")); // NOI18N

        jLApellidoMat.setText(bundle.getString("etiquetaApellidoMat")); // NOI18N

        jPanelDireccion.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("infoPanelDireccion"))); // NOI18N

        jLabel5.setText(bundle.getString("etiquetaCalle")); // NOI18N

        jLabel6.setText(bundle.getString("etiquetaColonia")); // NOI18N

        jLabelEstado.setText(bundle.getString("etiquetaEstado")); // NOI18N

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabelMunicipio.setText(bundle.getString("etiquetaMunicipio")); // NOI18N

        jLabelCodPostal.setText(bundle.getString("etiquetaCP")); // NOI18N

        jLabelTelefono.setText(bundle.getString("etiquetaTelefono")); // NOI18N

        javax.swing.GroupLayout jPanelDireccionLayout = new javax.swing.GroupLayout(jPanelDireccion);
        jPanelDireccion.setLayout(jPanelDireccionLayout);
        jPanelDireccionLayout.setHorizontalGroup(
            jPanelDireccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDireccionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDireccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDireccionLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFColonia, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelDireccionLayout.createSequentialGroup()
                        .addComponent(jLabelEstado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelMunicipio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelCodPostal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFCP, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        jPanelDireccionLayout.setVerticalGroup(
            jPanelDireccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDireccionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDireccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTFCalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTFColonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanelDireccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEstado)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMunicipio)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCodPostal)
                    .addComponent(jTFCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTelefono)
                    .addComponent(jTFTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        jLabelMunicipio.getAccessibleContext().setAccessibleName(bundle.getString("etiquetaMunicipio")); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        jButtonNuevo.setText(bundle.getString("etiquetaBotonNueva")); // NOI18N

        jButtonModificar.setText(bundle.getString("etiquetaBotonModificar")); // NOI18N

        jButtonEliminar.setText(bundle.getString("etiquetaBotonEliminar")); // NOI18N

        jPanelCargaProf.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("infoPanelCargaProfesor"))); // NOI18N

        jScrollPane5.setViewportView(jList3);

        javax.swing.GroupLayout jPanelCargaProfLayout = new javax.swing.GroupLayout(jPanelCargaProf);
        jPanelCargaProf.setLayout(jPanelCargaProfLayout);
        jPanelCargaProfLayout.setHorizontalGroup(
            jPanelCargaProfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCargaProfLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5)
                .addContainerGap())
        );
        jPanelCargaProfLayout.setVerticalGroup(
            jPanelCargaProfLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCargaProfLayout.createSequentialGroup()
                .addGap(0, 8, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanelInfoProfesorLayout = new javax.swing.GroupLayout(jPanelInfoProfesor);
        jPanelInfoProfesor.setLayout(jPanelInfoProfesorLayout);
        jPanelInfoProfesorLayout.setHorizontalGroup(
            jPanelInfoProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoProfesorLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanelInfoProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelInfoProfesorLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelInfoProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonModificar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonNuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanelCargaProf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanelDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelInfoProfesorLayout.createSequentialGroup()
                        .addGroup(jPanelInfoProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelInfoProfesorLayout.createSequentialGroup()
                                .addComponent(jLabelRFC)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFRFC))
                            .addGroup(jPanelInfoProfesorLayout.createSequentialGroup()
                                .addComponent(jLNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelInfoProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelInfoProfesorLayout.createSequentialGroup()
                                .addComponent(jLabelEmail)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelInfoProfesorLayout.createSequentialGroup()
                                .addComponent(jLApellidoPat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFApellidoPat, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLApellidoMat, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFApellidoMat, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(335, Short.MAX_VALUE))
        );
        jPanelInfoProfesorLayout.setVerticalGroup(
            jPanelInfoProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoProfesorLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanelInfoProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLNombre)
                    .addComponent(jLApellidoPat)
                    .addComponent(jLApellidoMat)
                    .addComponent(jTFNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFApellidoPat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFApellidoMat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelInfoProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRFC)
                    .addComponent(jLabelEmail)
                    .addComponent(jTFRFC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTFEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanelInfoProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4)
                    .addGroup(jPanelInfoProfesorLayout.createSequentialGroup()
                        .addComponent(jButtonNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonModificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonEliminar))
                    .addComponent(jPanelCargaProf, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelProfesoresLayout = new javax.swing.GroupLayout(jPanelProfesores);
        jPanelProfesores.setLayout(jPanelProfesoresLayout);
        jPanelProfesoresLayout.setHorizontalGroup(
            jPanelProfesoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProfesoresLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanelInfoProfesor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelProfesoresLayout.setVerticalGroup(
            jPanelProfesoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProfesoresLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanelInfoProfesor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedEscolar.addTab(bundle.getString("tituloTabProfesores"), jPanelProfesores); // NOI18N

        jPanelSeleccionProfesor.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("infoPanelSeleccionProfesor"))); // NOI18N

        jLabelProf.setText(bundle.getString("etiquetaProfesor")); // NOI18N

        jComboBoxProf.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jBotonManejaCarga.setText(bundle.getString("etiquetaBotonManejaCarga")); // NOI18N

        javax.swing.GroupLayout jPanelSeleccionProfesorLayout = new javax.swing.GroupLayout(jPanelSeleccionProfesor);
        jPanelSeleccionProfesor.setLayout(jPanelSeleccionProfesorLayout);
        jPanelSeleccionProfesorLayout.setHorizontalGroup(
            jPanelSeleccionProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSeleccionProfesorLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabelProf)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxProf, javax.swing.GroupLayout.PREFERRED_SIZE, 448, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBotonManejaCarga)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelSeleccionProfesorLayout.setVerticalGroup(
            jPanelSeleccionProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSeleccionProfesorLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanelSeleccionProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelProf)
                    .addComponent(jComboBoxProf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBotonManejaCarga))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("infoPanelDetalleCargaProfesor"))); // NOI18N

        jLabelPeriodoEscolar.setText(bundle.getString("etiquetaPeriodoEscolar")); // NOI18N

        jComboBoxPeriodos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabelMateria.setText(bundle.getString("etiquetaMateria")); // NOI18N

        jComboBoxMateria.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxMateria.setEnabled(false);
        jComboBoxMateria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxMateriaActionPerformed(evt);
            }
        });

        jLabelCarrera.setText(bundle.getString("etiquetaCarrera")); // NOI18N

        jComboBoxCarrera.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxCarrera.setEnabled(false);

        jScrollPane6.setViewportView(jListCargas);

        jButtonNew.setText(bundle.getString("etiquetaBotonNueva")); // NOI18N

        jButtonDelete.setText(bundle.getString("etiquetaBotonEliminar")); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(109, 109, 109)
                        .addComponent(jScrollPane6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonDelete, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(jButtonNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(12, 12, 12))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(224, 224, 224)
                                .addComponent(jLabelCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelPeriodoEscolar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxPeriodos, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelMateria)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBoxMateria, javax.swing.GroupLayout.PREFERRED_SIZE, 685, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 55, Short.MAX_VALUE)))
                .addGap(39, 39, 39))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPeriodoEscolar)
                    .addComponent(jComboBoxPeriodos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMateria)
                    .addComponent(jComboBoxMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCarrera)
                    .addComponent(jComboBoxCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jButtonNew)
                        .addGap(27, 27, 27)
                        .addComponent(jButtonDelete))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelCargaProfesorLayout = new javax.swing.GroupLayout(jPanelCargaProfesor);
        jPanelCargaProfesor.setLayout(jPanelCargaProfesorLayout);
        jPanelCargaProfesorLayout.setHorizontalGroup(
            jPanelCargaProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCargaProfesorLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanelCargaProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelSeleccionProfesor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelCargaProfesorLayout.setVerticalGroup(
            jPanelCargaProfesorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCargaProfesorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelSeleccionProfesor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedEscolar.addTab(bundle.getString("tituloTabCargaProfesor"), jPanelCargaProfesor); // NOI18N

        jPanelInfoEstudiante.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("tituloPanelInfoEstudiante"))); // NOI18N

        jPanelDireccionEstudiante.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("infoPanelDireccion"))); // NOI18N

        jLCalleEstudiante.setText(bundle.getString("etiquetaCalle")); // NOI18N

        jLColoniaEstudiante.setText(bundle.getString("etiquetaColonia")); // NOI18N

        jLabelCodPostalEstudiante.setText(bundle.getString("etiquetaCP")); // NOI18N

        jTFCPEstudiante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFCPEstudianteActionPerformed(evt);
            }
        });

        jLabelTelefonoEstudiante.setText(bundle.getString("etiquetaTelefono")); // NOI18N

        jLEstadoEstudiante.setText(bundle.getString("etiquetaEstado")); // NOI18N

        jCBEstadoEstudiante.setToolTipText("");

        jLMunicipio.setText(bundle.getString("etiquetaMunicipio")); // NOI18N

        javax.swing.GroupLayout jPanelDireccionEstudianteLayout = new javax.swing.GroupLayout(jPanelDireccionEstudiante);
        jPanelDireccionEstudiante.setLayout(jPanelDireccionEstudianteLayout);
        jPanelDireccionEstudianteLayout.setHorizontalGroup(
            jPanelDireccionEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDireccionEstudianteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDireccionEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDireccionEstudianteLayout.createSequentialGroup()
                        .addComponent(jLCalleEstudiante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFCalleEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jLColoniaEstudiante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFColoniaEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 444, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelDireccionEstudianteLayout.createSequentialGroup()
                        .addComponent(jLEstadoEstudiante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBEstadoEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLMunicipio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBMunicipioEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(jLabelCodPostalEstudiante)
                        .addGap(27, 27, 27)
                        .addComponent(jTFCPEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTelefonoEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTFTelefonoEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))))
        );
        jPanelDireccionEstudianteLayout.setVerticalGroup(
            jPanelDireccionEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDireccionEstudianteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDireccionEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLCalleEstudiante)
                    .addComponent(jTFCalleEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLColoniaEstudiante)
                    .addComponent(jTFColoniaEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanelDireccionEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCodPostalEstudiante)
                    .addComponent(jTFCPEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTelefonoEstudiante)
                    .addComponent(jTFTelefonoEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLEstadoEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBEstadoEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBMunicipioEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jLNombreEstudiante.setText(bundle.getString("etiquetaNombre")); // NOI18N

        jLApellidoPatEstudiante.setText(bundle.getString("etiquetaApellidoPat")); // NOI18N

        jLApellidoMat1.setText(bundle.getString("etiquetaApellidoMat")); // NOI18N

        jLabelEmailEstudiante.setText(bundle.getString("etiquetaEmail")); // NOI18N

        jLMatriculaEstudiante.setText(bundle.getString("etiquetaMatricula")); // NOI18N

        jScrollPane8.setViewportView(jListEstudiante);

        jBotonNuevoEstudiante.setText(bundle.getString("etiquetaBotonNueva")); // NOI18N

        jBotonModificarEstudiante.setText(bundle.getString("etiquetaBotonModificar")); // NOI18N
        jBotonModificarEstudiante.setEnabled(false);

        jBotonEliminarEstudiante.setText(bundle.getString("etiquetaBotonEliminar")); // NOI18N
        jBotonEliminarEstudiante.setEnabled(false);

        jPanelCargaEstu.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("infoPanelCargaEstudiante"))); // NOI18N
        jPanelCargaEstu.setEnabled(false);

        jListCargaEstu.setEnabled(false);
        jScrollPane9.setViewportView(jListCargaEstu);

        javax.swing.GroupLayout jPanelCargaEstuLayout = new javax.swing.GroupLayout(jPanelCargaEstu);
        jPanelCargaEstu.setLayout(jPanelCargaEstuLayout);
        jPanelCargaEstuLayout.setHorizontalGroup(
            jPanelCargaEstuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCargaEstuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane9)
                .addContainerGap())
        );
        jPanelCargaEstuLayout.setVerticalGroup(
            jPanelCargaEstuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCargaEstuLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jBotonAgregarEstudiante.setText(bundle.getString("etiquetaBotonAgregar")); // NOI18N

        javax.swing.GroupLayout jPanelInfoEstudianteLayout = new javax.swing.GroupLayout(jPanelInfoEstudiante);
        jPanelInfoEstudiante.setLayout(jPanelInfoEstudianteLayout);
        jPanelInfoEstudianteLayout.setHorizontalGroup(
            jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoEstudianteLayout.createSequentialGroup()
                .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInfoEstudianteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelDireccionEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelInfoEstudianteLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLNombreEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLMatriculaEstudiante))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTFNombreEstudiante)
                            .addComponent(jTFMatriculaEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLApellidoPatEstudiante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelInfoEstudianteLayout.createSequentialGroup()
                                .addComponent(jLabelEmailEstudiante)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFEmailEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelInfoEstudianteLayout.createSequentialGroup()
                                .addComponent(jTFApellPatEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLApellidoMat1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTFApellMatEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanelInfoEstudianteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jBotonNuevoEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jBotonModificarEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addComponent(jBotonEliminarEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jBotonAgregarEstudiante))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelCargaEstu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        jPanelInfoEstudianteLayout.setVerticalGroup(
            jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelInfoEstudianteLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLNombreEstudiante)
                    .addComponent(jTFNombreEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLApellidoPatEstudiante)
                    .addComponent(jTFApellPatEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLApellidoMat1)
                    .addComponent(jTFApellMatEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInfoEstudianteLayout.createSequentialGroup()
                        .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLMatriculaEstudiante)
                            .addComponent(jTFMatriculaEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelEmailEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTFEmailEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(jPanelDireccionEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelInfoEstudianteLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jBotonNuevoEstudiante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBotonModificarEstudiante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBotonEliminarEstudiante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBotonAgregarEstudiante)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelInfoEstudianteLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelInfoEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanelCargaEstu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane8))
                        .addGap(220, 220, 220))))
        );

        javax.swing.GroupLayout jPanelEstudiantesLayout = new javax.swing.GroupLayout(jPanelEstudiantes);
        jPanelEstudiantes.setLayout(jPanelEstudiantesLayout);
        jPanelEstudiantesLayout.setHorizontalGroup(
            jPanelEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEstudiantesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelInfoEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        jPanelEstudiantesLayout.setVerticalGroup(
            jPanelEstudiantesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEstudiantesLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jPanelInfoEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jTabbedEscolar.addTab(bundle.getString("tituloTabEstudiantes"), jPanelEstudiantes); // NOI18N

        jPanelSeleccionEstudiante.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("infoPanelSeleccionEstudiante"))); // NOI18N

        jLEstudianteCarga.setText(bundle.getString("etiquetaEstudiante")); // NOI18N

        jBotonManejaCargaEstudiante.setText(bundle.getString("etiquetaBotonManejaCargaEstudiante")); // NOI18N

        javax.swing.GroupLayout jPanelSeleccionEstudianteLayout = new javax.swing.GroupLayout(jPanelSeleccionEstudiante);
        jPanelSeleccionEstudiante.setLayout(jPanelSeleccionEstudianteLayout);
        jPanelSeleccionEstudianteLayout.setHorizontalGroup(
            jPanelSeleccionEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSeleccionEstudianteLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLEstudianteCarga)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jBotonManejaCargaEstudiante)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelSeleccionEstudianteLayout.setVerticalGroup(
            jPanelSeleccionEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSeleccionEstudianteLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanelSeleccionEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLEstudianteCarga)
                    .addComponent(jCBEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBotonManejaCargaEstudiante))
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanelDetalleCargaEstudiante.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("infoPanelDetalleEstudiante"))); // NOI18N

        jLPeriodoEscolarEstudiante.setText(bundle.getString("etiquetaPeriodoEscolar")); // NOI18N

        jCBPeriodoEscolarEstudiante.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));

        jLMateriasCargaEstudiante.setText(bundle.getString("etiquetaMateria")); // NOI18N

        jCBMateriaEstudiante.setEnabled(false);

        jLCarreraEstudiante.setText(bundle.getString("etiquetaCarrera")); // NOI18N

        jScrollPane7.setViewportView(jListCargaEstudiante);

        jBotonNuevaCargaEstudiante.setText(bundle.getString("etiquetaBotonNueva")); // NOI18N

        jBotonEliminaCargaEstudiante.setText(bundle.getString("etiquetaBotonEliminar")); // NOI18N

        jBotonAgregarCarga.setText(bundle.getString("etiquetaBotonAgregar")); // NOI18N

        javax.swing.GroupLayout jPanelDetalleCargaEstudianteLayout = new javax.swing.GroupLayout(jPanelDetalleCargaEstudiante);
        jPanelDetalleCargaEstudiante.setLayout(jPanelDetalleCargaEstudianteLayout);
        jPanelDetalleCargaEstudianteLayout.setHorizontalGroup(
            jPanelDetalleCargaEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDetalleCargaEstudianteLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanelDetalleCargaEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDetalleCargaEstudianteLayout.createSequentialGroup()
                        .addComponent(jLPeriodoEscolarEstudiante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBPeriodoEscolarEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLMateriasCargaEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCBMateriaEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelDetalleCargaEstudianteLayout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                        .addGroup(jPanelDetalleCargaEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jBotonEliminaCargaEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBotonAgregarCarga, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBotonNuevaCargaEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(jPanelDetalleCargaEstudianteLayout.createSequentialGroup()
                .addGap(255, 255, 255)
                .addComponent(jLCarreraEstudiante)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCBCarreraEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(518, Short.MAX_VALUE))
        );
        jPanelDetalleCargaEstudianteLayout.setVerticalGroup(
            jPanelDetalleCargaEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDetalleCargaEstudianteLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanelDetalleCargaEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLPeriodoEscolarEstudiante)
                    .addComponent(jCBPeriodoEscolarEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCBMateriaEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLMateriasCargaEstudiante))
                .addGap(29, 29, 29)
                .addGroup(jPanelDetalleCargaEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCBCarreraEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLCarreraEstudiante))
                .addGroup(jPanelDetalleCargaEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelDetalleCargaEstudianteLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelDetalleCargaEstudianteLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jBotonNuevaCargaEstudiante)
                        .addGap(7, 7, 7)
                        .addComponent(jBotonAgregarCarga)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBotonEliminaCargaEstudiante)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelCargaEstudianteLayout = new javax.swing.GroupLayout(jPanelCargaEstudiante);
        jPanelCargaEstudiante.setLayout(jPanelCargaEstudianteLayout);
        jPanelCargaEstudianteLayout.setHorizontalGroup(
            jPanelCargaEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCargaEstudianteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCargaEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelSeleccionEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelDetalleCargaEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanelCargaEstudianteLayout.setVerticalGroup(
            jPanelCargaEstudianteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCargaEstudianteLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanelSeleccionEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelDetalleCargaEstudiante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedEscolar.addTab(bundle.getString("tituloTabCargaEstudiante"), jPanelCargaEstudiante); // NOI18N

        jMenuArchivo.setText(bundle.getString("menuArchivo")); // NOI18N

        jMenuItemSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSalir.setText(bundle.getString("etiquetaOpcionSalir")); // NOI18N
        jMenuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSalirActionPerformed(evt);
            }
        });
        jMenuArchivo.add(jMenuItemSalir);

        jMenuBarControlEscolar.add(jMenuArchivo);

        jMenuAyuda.setText(bundle.getString("menuAyuda")); // NOI18N

        jMenuItemAcercaDe.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemAcercaDe.setText(bundle.getString("etiquetaOpcionAcercaDe")); // NOI18N
        jMenuItemAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAcercaDeActionPerformed(evt);
            }
        });
        jMenuAyuda.add(jMenuItemAcercaDe);

        jMenuBarControlEscolar.add(jMenuAyuda);

        setJMenuBar(jMenuBarControlEscolar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedEscolar, javax.swing.GroupLayout.PREFERRED_SIZE, 1303, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelLineaStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(965, 965, 965))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedEscolar, javax.swing.GroupLayout.PREFERRED_SIZE, 539, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jLabelLineaStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalirActionPerformed
        confirmaSalida();
    }//GEN-LAST:event_jMenuItemSalirActionPerformed

    private void jMenuItemAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAcercaDeActionPerformed
        jDialogAcercaDe.setVisible(true);
    }//GEN-LAST:event_jMenuItemAcercaDeActionPerformed

    private void jBotonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBotonOKActionPerformed
        jDialogAcercaDe.setVisible(false);
    }//GEN-LAST:event_jBotonOKActionPerformed

    private void jTFClaveMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFClaveMateriaActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_jTFClaveMateriaActionPerformed

    private void jComboBoxMateriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxMateriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxMateriaActionPerformed

    private void jTFSemestreMateriaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTFSemestreMateriaKeyTyped
        // TODO add your handling code here:
        // SOLO NUMEROS 

//capturar el caracter digitado 
        //ignora el caracter digitado 

    }//GEN-LAST:event_jTFSemestreMateriaKeyTyped

    private void jTFCPEstudianteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFCPEstudianteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFCPEstudianteActionPerformed

    private void jPanelPeriodosEscolaresFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPanelPeriodosEscolaresFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_jPanelPeriodosEscolaresFocusLost

    private void jPanelInfoPeriodosComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanelInfoPeriodosComponentMoved
        // TODO add your handling code here:

    }//GEN-LAST:event_jPanelInfoPeriodosComponentMoved

    private void jTabbedEscolarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedEscolarStateChanged
        // TODO add your handling code here:
        // jListPeriodoEscolar.clearSelection();
        // jListMateria.clearSelection();
        // jListEstudiante.clearSelection();
        //jListCargaEstudiante.clearSelection();
        //inicializaCargaEstudiante();
    }//GEN-LAST:event_jTabbedEscolarStateChanged

    private void jTabbedEscolarVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_jTabbedEscolarVetoableChange
        // TODO add your handling code here:


    }//GEN-LAST:event_jTabbedEscolarVetoableChange

    /**
     * @param args the command line arguments Incializador del control escolar
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUIControlEscolar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUIControlEscolar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUIControlEscolar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUIControlEscolar.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUIControlEscolar().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBotonAgregarCarga;
    private javax.swing.JButton jBotonAgregarCarrera;
    private javax.swing.JButton jBotonAgregarEstudiante;
    private javax.swing.JButton jBotonAgregarMateria;
    private javax.swing.JButton jBotonAgregarPeriodo;
    private javax.swing.JButton jBotonEliminaCargaEstudiante;
    private javax.swing.JButton jBotonEliminarCarrera;
    private javax.swing.JButton jBotonEliminarEstudiante;
    private javax.swing.JButton jBotonEliminarMateria;
    private javax.swing.JButton jBotonEliminarPeriodo;
    private javax.swing.JButton jBotonManejaCarga;
    private javax.swing.JButton jBotonManejaCargaEstudiante;
    private javax.swing.JButton jBotonModificarCarrera;
    private javax.swing.JButton jBotonModificarEstudiante;
    private javax.swing.JButton jBotonModificarMateria;
    private javax.swing.JButton jBotonModificarPeriodo;
    private javax.swing.JButton jBotonNuevaCargaEstudiante;
    private javax.swing.JButton jBotonNuevaCarrera;
    private javax.swing.JButton jBotonNuevaMateria;
    private javax.swing.JButton jBotonNuevoEstudiante;
    private javax.swing.JButton jBotonNuevoPeriodo;
    private javax.swing.JButton jBotonOK;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonEliminar;
    private javax.swing.JButton jButtonModificar;
    private javax.swing.JButton jButtonNew;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JComboBox jCBCarrera;
    private javax.swing.JComboBox jCBCarreraEstudiante;
    private javax.swing.JComboBox jCBEstadoEstudiante;
    private javax.swing.JComboBox jCBEstudiante;
    private javax.swing.JComboBox jCBMateriaEstudiante;
    private javax.swing.JComboBox jCBMunicipioEstudiante;
    private javax.swing.JComboBox jCBPeriodoEscolarEstudiante;
    private javax.swing.JComboBox jCBSemestre;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBoxCarrera;
    private javax.swing.JComboBox jComboBoxMateria;
    private javax.swing.JComboBox jComboBoxPeriodos;
    private javax.swing.JComboBox jComboBoxProf;
    private javax.swing.JDialog jDialogAcercaDe;
    private javax.swing.JLabel jLAno;
    private javax.swing.JLabel jLApellidoMat;
    private javax.swing.JLabel jLApellidoMat1;
    private javax.swing.JLabel jLApellidoPat;
    private javax.swing.JLabel jLApellidoPatEstudiante;
    private javax.swing.JLabel jLCalleEstudiante;
    private javax.swing.JLabel jLCarrera;
    private javax.swing.JLabel jLCarreraEstudiante;
    private javax.swing.JLabel jLClaveMateria;
    private javax.swing.JLabel jLColoniaEstudiante;
    private javax.swing.JLabel jLEstadoEstudiante;
    private javax.swing.JLabel jLEstudianteCarga;
    private javax.swing.JLabel jLIDPeriodoEscolar;
    private javax.swing.JLabel jLMateriasCargaEstudiante;
    private javax.swing.JLabel jLMatriculaEstudiante;
    private javax.swing.JLabel jLMunicipio;
    private javax.swing.JLabel jLNombre;
    private javax.swing.JLabel jLNombreEstudiante;
    private javax.swing.JLabel jLNombreMateria;
    private javax.swing.JLabel jLPeriodoEscolarEstudiante;
    private javax.swing.JLabel jLSemestre;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelCarrera;
    private javax.swing.JLabel jLabelCodPostal;
    private javax.swing.JLabel jLabelCodPostalEstudiante;
    private javax.swing.JLabel jLabelEmail;
    private javax.swing.JLabel jLabelEmailEstudiante;
    private javax.swing.JLabel jLabelEstado;
    private javax.swing.JLabel jLabelFoto;
    private javax.swing.JLabel jLabelLineaStatus;
    private javax.swing.JLabel jLabelMateria;
    private javax.swing.JLabel jLabelMunicipio;
    private javax.swing.JLabel jLabelPeriodoEscolar;
    private javax.swing.JLabel jLabelProf;
    private javax.swing.JLabel jLabelRFC;
    private javax.swing.JLabel jLabelSemestrePE;
    private javax.swing.JLabel jLabelTelefono;
    private javax.swing.JLabel jLabelTelefonoEstudiante;
    private javax.swing.JLabel jLabelTexto;
    private javax.swing.JList jList3;
    private javax.swing.JList jListCargaEstu;
    private javax.swing.JList jListCargaEstudiante;
    private javax.swing.JList jListCargas;
    private javax.swing.JList jListCarreras;
    private javax.swing.JList jListEstudiante;
    private javax.swing.JList jListMateria;
    private javax.swing.JList jListPeriodoEscolar;
    private javax.swing.JMenu jMenuArchivo;
    private javax.swing.JMenu jMenuAyuda;
    private javax.swing.JMenuBar jMenuBarControlEscolar;
    private javax.swing.JMenuItem jMenuItemAcercaDe;
    private javax.swing.JMenuItem jMenuItemSalir;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelCargaEstu;
    private javax.swing.JPanel jPanelCargaEstudiante;
    private javax.swing.JPanel jPanelCargaProf;
    private javax.swing.JPanel jPanelCargaProfesor;
    private javax.swing.JPanel jPanelCarreras;
    private javax.swing.JPanel jPanelDetalleCargaEstudiante;
    private javax.swing.JPanel jPanelDireccion;
    private javax.swing.JPanel jPanelDireccionEstudiante;
    private javax.swing.JPanel jPanelEstudiantes;
    private javax.swing.JPanel jPanelInfoCarreras;
    private javax.swing.JPanel jPanelInfoEstudiante;
    private javax.swing.JPanel jPanelInfoMateria;
    private javax.swing.JPanel jPanelInfoPeriodos;
    private javax.swing.JPanel jPanelInfoProfesor;
    private javax.swing.JPanel jPanelMateria;
    private javax.swing.JPanel jPanelMaterias;
    private javax.swing.JPanel jPanelPeriodosEscolares;
    private javax.swing.JPanel jPanelProfesores;
    private javax.swing.JPanel jPanelSeleccionEstudiante;
    private javax.swing.JPanel jPanelSeleccionProfesor;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jTFAnoPeriodoEscolar;
    private javax.swing.JTextField jTFApellMatEstudiante;
    private javax.swing.JTextField jTFApellPatEstudiante;
    private javax.swing.JTextField jTFApellidoMat;
    private javax.swing.JTextField jTFApellidoPat;
    private javax.swing.JTextField jTFCP;
    private javax.swing.JTextField jTFCPEstudiante;
    private javax.swing.JTextField jTFCalle;
    private javax.swing.JTextField jTFCalleEstudiante;
    private javax.swing.JTextField jTFClaveCarrera;
    private javax.swing.JTextField jTFClaveMateria;
    private javax.swing.JTextField jTFColonia;
    private javax.swing.JTextField jTFColoniaEstudiante;
    private javax.swing.JTextField jTFEmail;
    private javax.swing.JTextField jTFEmailEstudiante;
    private javax.swing.JTextField jTFIdPeriodoEscolar;
    private javax.swing.JTextField jTFMatriculaEstudiante;
    private javax.swing.JTextField jTFNombre;
    private javax.swing.JTextField jTFNombreCarrera;
    private javax.swing.JTextField jTFNombreEstudiante;
    private javax.swing.JTextField jTFNombreMateria;
    private javax.swing.JTextField jTFRFC;
    private javax.swing.JTextField jTFSemestreMateria;
    private javax.swing.JTextField jTFTelefono;
    private javax.swing.JTextField jTFTelefonoEstudiante;
    private javax.swing.JTabbedPane jTabbedEscolar;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
    // Variables agregadas manualmente
    ResourceBundle bundle;  // Variable para accesar a los recursos del proyecto
    DefaultComboBoxModel<Estado> modeloComboEstados; // Modelo para contener los estados
    DefaultComboBoxModel<Municipio> modeloComboMunicipios;  // Modelo para contener los municipios
    private short actualEstado;  // Para determinar que municipios se estan mostrando ahorita
    private List<Municipio> datosMunicipios[];  // Arreglo que contendra las listas de municipios de los estados
    private ConectorBaseDeDatos conector;  // Objeto para hacer conexion a la base de datos
    //Carrera
    private Carrera cSel;//Mantiene la referencia la carrera seleccionado
    DefaultListModel<Carrera> modeloListaCarreras;//Es el model
    //Materia
    private DefaultComboBoxModel<Carrera> modeloComboMateria;
    private DefaultListModel<Materia> modeloListaMateria;
    private Materia mSel;
    //PeridoEscolar
    private PeriodoEscolar pSel;
    DefaultListModel<PeriodoEscolar> modeloListaPeriodo;
    DefaultComboBoxModel<String> modeloComboPeriodo;
    //DefaultComboBoxModel<PeriodoEscolar> modeloComboPeriodo;
    private String estacion = "";
    DefaultListModel<Estudiante> modeloListaEstudiante;
    private Estudiante eSel;
    private Estudiante eManejaSel;
    private Municipio munSel;
    private Estado esSel;
    private boolean indiceCarrera;
    DefaultListModel<CargaEstudiante> modeloListaCargaEstudiante;
    DefaultListModel<ListaCargaEstudiante> modeloListaCarga;
    DefaultListModel<ListaCargaEstudiante> modeloListaCargaEstu;

    private Estado est;
    private ListaCargaEstudiante listCargaEstu;
    private CargaEstudiante cEstudiante;
    DefaultComboBoxModel<Estudiante> modeloComboEstudiante;
    DefaultComboBoxModel<PeriodoEscolar> modeloComboPeriodoEstudiante;
    DefaultComboBoxModel<Materia> modeloComboMateriaEstudiante;
    DefaultComboBoxModel<Carrera> modeloComboCarreraEstudiante;
    private CargaEstudiante carga;

}
