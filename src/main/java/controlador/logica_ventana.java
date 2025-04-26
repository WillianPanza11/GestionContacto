/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import com.gestioncontactos.vista.vistaContactos;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import modelo.persona;
import modelo.personaDAO;

/**
 *
 * @author Willian
 */
public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {

    private String nombres, email, telefono, categoria = ""; // Variables para almacenar datos del contacto.
    private vistaContactos delegado;
    private persona persona;
    private List<persona> contactos;
    private boolean favorito = false;
    public JList lst_contactos; // Lista para mostrar los contactos.
    int userSelection = 0; // Variable para almacenar la selección del usuario en el JFileChooser.
    personaDAO personaObj = new personaDAO(new persona());
    // public DefaultTableModel modelo = new DefaultTableModel(); // Modelo de tabla
    // para la lista de contactos.
    // DefaultTableModel modelo = (DefaultTableModel)
    // delegado.getTbl_contactos().getModel();
    public JFileChooser fileChooser = new JFileChooser();
    public JProgressBar barraProgreso = new JProgressBar(0, 100);

    public logica_ventana(vistaContactos delegado) {
        this.delegado = delegado;
        delegado.setVisible(true);
        // delegado.setLocation(null);

    }

    public void iniciaControl() {
        colocarImage();
        cargarContactosRegistrados();
        // Registra los ActionListener para los botones de la GUI.
        this.delegado.getBtn_add().addActionListener(this);
        this.delegado.getBtn_eliminar().addActionListener(this);
        this.delegado.getBtn_modificar().addActionListener(this);
        this.delegado.getBtn_exp().addActionListener(this);
        this.delegado.getBtnLimpiar().addActionListener(this);

        // Registra los ItemListener para el JComboBox de categoría y el JCheckBox de
        // favoritos.
        this.delegado.getCmb_categoria().addItemListener(this);
        this.delegado.getChb_favorito().addItemListener(this);

        this.delegado.getTbl_contactos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.delegado.getTbl_contactos().getSelectionModel().addListSelectionListener(this);

    }

    private void colocarImage() {
        ImageIcon iconoSave = new ImageIcon(getClass().getResource("/resources/gurdar.png"));
        ImageIcon iconoEdit = new ImageIcon(getClass().getResource("/resources/edit.png"));
        ImageIcon iconoLimpiar = new ImageIcon(getClass().getResource("/resources/clean.png"));
        ImageIcon iconoSearch = new ImageIcon(getClass().getResource("/resources/find.png"));
        ImageIcon iconoExportar = new ImageIcon(getClass().getResource("/resources/export.png"));
        ImageIcon iconoDelete = new ImageIcon(getClass().getResource("/resources/delete.png"));

        // Escalar si es necesario (por ejemplo, 32x32 píxeles)
        Image imagenEscaladaSave = iconoSave.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon iconoEscaladoSave = new ImageIcon(imagenEscaladaSave);

        Image imagenEscaladaEdit = iconoEdit.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon iconoEscaladoEdit = new ImageIcon(imagenEscaladaEdit);

        Image imagenEscaladaLimpiar = iconoLimpiar.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon iconoEscaladoLimpiar = new ImageIcon(imagenEscaladaLimpiar);

        Image imagenEscaladaSearch = iconoSearch.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon iconoEscaladoSearch = new ImageIcon(imagenEscaladaSearch);

        Image imagenEscaladaExport = iconoDelete.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon iconoEscaladoExport = new ImageIcon(imagenEscaladaExport);

        Image imagenEscaladaDelete = iconoDelete.getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH);
        ImageIcon iconoEscaladoDelete = new ImageIcon(imagenEscaladaDelete);

        // Asignar al botón
        this.delegado.getBtn_add().setIcon(iconoEscaladoSave);
        this.delegado.getBtn_modificar().setIcon(iconoEscaladoEdit);
        this.delegado.getBtnLimpiar().setIcon(iconoEscaladoLimpiar);
        this.delegado.getBtn_eliminar().setIcon(iconoEscaladoDelete);
        this.delegado.getBtn_exp().setIcon(iconoEscaladoExport);
        this.delegado.getBtnBuscar().setIcon(iconoEscaladoSearch);

    }

    private void cargarContactosRegistrados() {
        try {
            // Lee los contactos desde el archivo
            contactos = new personaDAO(new persona()).leerArchivo();

            // Obtener modelo de la tabla visual
            DefaultTableModel modelo = (DefaultTableModel) delegado.getTbl_contactos().getModel();

            // Limpia las filas existentes del modelo de la tabla
            modelo.setRowCount(0);

            // Agrega los contactos al modelo de la tabla
            for (persona contacto : contactos) {
                Object[] fila = {
                        contacto.getNombre(),
                        contacto.getTelefono(),
                        contacto.getEmail(),
                        contacto.getCategoria(),
                        contacto.isFavorito() ? "Sí" : "No", };
                modelo.addRow(fila);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(delegado, "Existen problemas al cargar todos los contactos");

        }
    }

    // Método privado para limpiar los campos de entrada en la GUI y reiniciar
    // variables.
    private void limpiarCampos() {
        // Limpia los campos de nombres, email y teléfono en la GUI.
        delegado.getTxt_nombres().setText("");
        delegado.getTxt_telefono().setText("");
        delegado.getTxt_email().setText("");
        // Reinicia las variables de categoría y favorito.
        categoria = "";
        favorito = false;
        // Desmarca la casilla de favorito y establece la categoría por defecto.
        delegado.getChb_favorito().setSelected(favorito);
        delegado.getCmb_categoria().setSelectedIndex(0);
        // Reinicia las variables con los valores actuales de la GUI.
        incializacionCampos();
        // Recarga los contactos en la lista de contactos de la GUI.
        cargarContactosRegistrados();
    }

    // Método privado para inicializar las variables con los valores ingresados en
    // la GUI.
    private void incializacionCampos() {
        // Obtiene el texto ingresado en los campos de nombres, email y teléfono de la
        // GUI.
        nombres = delegado.getTxt_nombres().getText();
        email = delegado.getTxt_email().getText();
        telefono = delegado.getTxt_telefono().getText();
    }

    // Método privado para cargar los datos del contacto seleccionado en los campos
    // de la GUI.
    private void cargarContacto(int index) {
        // Establece el nombre del contacto en el campo de texto de nombres.
        delegado.getTxt_nombres().setText(contactos.get(index).getNombre());
        // Establece el teléfono del contacto en el campo de texto de teléfono.
        delegado.getTxt_telefono().setText(contactos.get(index).getTelefono());
        // Establece el correo electrónico del contacto en el campo de texto de correo
        // electrónico.
        delegado.getTxt_email().setText(contactos.get(index).getEmail());
        // Establece el estado de favorito del contacto en el JCheckBox de favorito.
        delegado.getChb_favorito().setSelected(contactos.get(index).isFavorito());
        // Establece la categoría del contacto en el JComboBox de categoría.
        delegado.getCmb_categoria().setSelectedItem(contactos.get(index).getCategoria());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        incializacionCampos(); // Inicializa las variables con los valores actuales de la GUI.
        // Verifica si el evento proviene del botón "Agregar".
        if (e.getSource() == delegado.getBtn_add()) {
            agregarContacto();
        } else if (e.getSource() == delegado.getBtn_eliminar()) {

        } else if (e.getSource() == delegado.getBtn_modificar()) {
            // Actualiza el contacto seleccionado en la lista.
        } else if (e.getSource() == delegado.getBtn_exp()) {
            // Exporta los contactos a un archivo.
            exportarConDialogo();
        } else if (e.getSource() == delegado.getBtnLimpiar()) {
            limpiarCampos();
        }
    }

    public void agregarContacto() {
        incializacionCampos();
        // Verifica si los campos de nombres, teléfono y email no están vacíos.
        if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {
            // Verifica si se ha seleccionado una categoría válida.
            if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {
                // Crea un nuevo objeto persona con los datos ingresados y lo guarda.
                persona = new persona(nombres, telefono, email, categoria, favorito);
                new personaDAO(persona).escribirArchivo();
                // Limpia los campos después de agregar el contacto.
                limpiarCampos();
                // Muestra un mensaje de éxito.
                JOptionPane.showMessageDialog(delegado, "Contacto Registrado!!!");
            } else {
                // Muestra un mensaje de advertencia si no se ha seleccionado una categoría
                // válida.
                JOptionPane.showMessageDialog(delegado, "Elija una Categoria!!!");
            }
        } else {
            // Muestra un mensaje de advertencia si algún campo está vacío.
            JOptionPane.showMessageDialog(delegado, "Todos los campos deben ser llenados!!!");
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        // Verifica que no esté en proceso de ajuste (esto evita múltiples llamados)
        if (!e.getValueIsAdjusting()) {
            int filaSeleccionada = delegado.getTbl_contactos().getSelectedRow();

            // Verifica si hay una fila seleccionada
            if (filaSeleccionada != -1) {
                cargarContacto(filaSeleccionada);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Verifica si el evento proviene del JComboBox de categoría.
        if (e.getSource() == delegado.getCmb_categoria()) {
            // Obtiene el elemento seleccionado en el JComboBox y lo convierte en una
            // cadena.
            categoria = delegado.getCmb_categoria().getSelectedItem().toString();
            // Actualiza la categoría seleccionada en la variable "categoria".
        } else if (e.getSource() == delegado.getChb_favorito()) {
            // Verifica si el evento proviene del JCheckBox de favorito.
            favorito = delegado.getChb_favorito().isSelected();
            // Obtiene el estado seleccionado del JCheckBox y actualiza el estado de
            // favorito en la variable "favorito".
        }
    }

    public void exportarConDialogo() {
        this.fileChooser.setDialogTitle("Exportar archivo CSV");
        this.fileChooser.setSelectedFile(new File("contactos.csv"));
        int userSelection = this.fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            if (contactos.size() > 0) {
                // Mostrar la barra de progreso
                barraProgreso.setValue(0);
                barraProgreso.setVisible(true);

                // Usamos SwingWorker para procesar en segundo plano
                SwingWorker<Void, Integer> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        String destino = fileChooser.getSelectedFile().getAbsolutePath();

                        // Simula progreso (porque el método real no lo reporta por partes)
                        int pasos = 5; // podemos simular 5 pasos del 0% al 100%
                        for (int i = 1; i <= pasos; i++) {
                            Thread.sleep(150); // simula tiempo de espera
                            publish(i * (100 / pasos)); // publica progreso
                        }

                        // Llama al método de exportación real
                        personaObj.exportarArchivoCSV(destino);

                        return null;
                    }

                    @Override
                    protected void process(List<Integer> chunks) {
                        // Actualiza la barra con el último valor publicado
                        int progresoActual = chunks.get(chunks.size() - 1);
                        barraProgreso.setValue(progresoActual);
                    }

                    @Override
                    protected void done() {
                        barraProgreso.setVisible(false); // Oculta la barra
                        JOptionPane.showMessageDialog(delegado, "Exportación exitosa!!!");
                    }
                };

                worker.execute(); // Ejecuta el hilo
            } else {
                JOptionPane.showMessageDialog(delegado, "No hay contactos para exportar!!!");
            }
        }
    }

}
