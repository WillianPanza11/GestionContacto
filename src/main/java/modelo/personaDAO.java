/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;

/**
 *
 * @author Willian
 */
public class personaDAO {

    // Declaración de atributos privados de la clase "personaDAO"
    private File archivo; // Archivo donde se almacenarán los datos de los contactos
    private persona persona; // Objeto "persona" que se gestionará

    // Constructor público de la clase "personaDAO" que recibe un objeto "persona"
    // como parámetro
    public personaDAO(persona persona) {
        this.persona = persona; // Asigna el objeto "persona" recibido al atributo de la clase
        archivo = new File("c:/gestionContactos"); // Establece la ruta donde se alojará el archivo
        // Llama al método para preparar el archivo
        prepararArchivo();
    }

    // Método privado para gestionar el archivo utilizando la clase File
    private void prepararArchivo() {
        // Verifica si el directorio existe
        if (!archivo.exists()) { // Si el directorio no existe, se crea
            archivo.mkdir();
        }

        // Accede al archivo "datosContactos.csv" dentro del directorio especificado
        archivo = new File(archivo.getAbsolutePath(), "datosContactos.csv");
        // Verifica si el archivo existe
        if (!archivo.exists()) { // Si el archivo no existe, se crea
            try {
                archivo.createNewFile();
                // Prepara el encabezado para el archivo de csv
                String encabezado = String.format("%s;%s;%s;%s;%s", "NOMBRE", "TELEFONO", "EMAIL", "CATEGORIA",
                        "FAVORITO");
                // persona.datosContacto(encabezado);
                escribir(encabezado);
            } catch (IOException e) {
                // Maneja la excepción de entrada/salida
                e.printStackTrace();
            }
        }
    }

    private void escribir(String texto) {
        // Prepara el archivo para escribir en la última línea
        FileWriter escribir;
        try {
            escribir = new FileWriter(archivo.getAbsolutePath(), true);
            escribir.write(texto + "\n"); // Escribe los datos del contacto en el archivo
            // Cierra el archivo
            escribir.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // Método público para escribir en el archivo
    public boolean escribirArchivo() {
        // // Prepara el archivo para escribir en la última línea
        // FileWriter escribir = new FileWriter(archivo.getAbsolutePath(), true);
        // escribir.write(persona.datosContacto() + "\n"); // Escribe los datos del
        // contacto en el archivo
        // // Cierra el archivo
        // escribir.close();
        escribir(persona.datosContacto());
        return true; // Retorna true si la escritura fue exitosa
    }

    // Método público para leer los datos del archivo
    public List<persona> leerArchivo() throws IOException {
        // Cadena que contendrá toda la data del archivo
        String contactos = "";
        // Abre el archivo para leer
        FileReader leer = new FileReader(archivo.getAbsolutePath());
        int c;
        while ((c = leer.read()) != -1) { // Lee hasta la última línea del archivo
            contactos += String.valueOf((char) c);
        }
        // Separa cada contacto por salto de línea
        String[] datos = contactos.split("\n");
        // Crea una lista que almacenará cada persona encontrada
        List<persona> personas = new ArrayList<>();
        // Recorre cada contacto
        for (String contacto : datos) {
            // Crea una instancia de persona
            persona p = new persona();
            p.setNombre(contacto.split(";")[0]); // Asigna el nombre
            p.setTelefono(contacto.split(";")[1]); // Asigna el teléfono
            p.setEmail(contacto.split(";")[2]); // Asigna el email
            p.setCategoria(contacto.split(";")[3]); // Asigna la categoría
            p.setFavorito(Boolean.parseBoolean(contacto.split(";")[4])); // Asigna si es favorito
            // Añade cada persona a la lista
            personas.add(p);
        }
        // Cierra el archivo
        leer.close();
        // Retorna la lista de personas
        return personas;
    }

    // Método público para guardar los contactos modificados o eliminados
    public void actualizarContactos(List<persona> personas) throws IOException {
        // Borra los datos del archivo
        archivo.delete();
        // Recorre los elementos de la lista
        for (persona p : personas) {
            // Instancia el DAO
            new personaDAO(p);
            // Escribe en el archivo
            escribirArchivo();
        }
    }

    //metodo para actalizar los contactos utilizando hilos pero de forma sincronizada
    public synchronized void updateContactos(List<persona> personas) throws IOException {
        FileWriter writer = new FileWriter(archivo);
        //writer.write("NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO\n");
        for (persona p : personas) {
            writer.write(p.datosContacto() + "\n");
        }
        writer.close();
    }

    public boolean exportarArchivoCSV(String rutaDestino) {
        File destino = new File(rutaDestino);
        try (FileInputStream in = new FileInputStream(this.archivo); FileOutputStream out = new FileOutputStream(destino)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("Archivo exportado exitosamente a: " + destino.getAbsolutePath());
            return true;

        } catch (IOException e) {
            System.err.println("Error al exportar el archivo: " + e.getMessage());
            return false;
        }
    }

    //exportar un contacto de forma sincronica, esto permite exportar verias contactos al presionar el boton 
    public synchronized boolean exportarArchivoCSVThreadSafe(String rutaDestino) {
        File destino = new File(rutaDestino);
        try (FileInputStream in = new FileInputStream(this.archivo); FileOutputStream out = new FileOutputStream(destino)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            return true;

        } catch (IOException e) {
            System.err.println("Error al exportar: " + e.getMessage());
            return false;
        }
    }

    public synchronized boolean exportarJSON(String ruta) {
        try (FileWriter writer = new FileWriter(ruta)) {
            List<persona> lista = leerArchivo();
            Gson gson = new Gson();
            gson.toJson(lista, writer);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public boolean exportarJSONHilo(String rutaDestino) {
    try (FileWriter writer = new FileWriter(rutaDestino)) {
        List<persona> personas = leerArchivo(); // obtiene todos los contactos
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(personas, writer);
        return true;
    } catch (IOException e) {
        System.err.println("Error al exportar JSON: " + e.getMessage());
        return false;
    }
}
    
    public boolean importarDesdeJSON(String rutaOrigen) {
    try (FileReader reader = new FileReader(rutaOrigen)) {
        Gson gson = new Gson();
        Type tipoLista = new TypeToken<List<persona>>(){}.getType();
        List<persona> listaImportada = gson.fromJson(reader, tipoLista);

        // Agregar todos los contactos importados al archivo CSV
        for (persona p : listaImportada) {
            new personaDAO(p).escribirArchivo();
        }

        return true;
    } catch (IOException e) {
        System.err.println("Error al importar JSON: " + e.getMessage());
        return false;
    }
}

    public boolean eliminarPersona(int indexAEliminar) {
    try {
        // Leer todos los contactos actuales
        List<persona> personas = leerArchivo();

        // Validar el índice
        if (indexAEliminar >= 0 && indexAEliminar < personas.size()) {
            personas.remove(indexAEliminar); // Eliminar por índice

            // Actualizar archivo
            actualizarContactos(personas);
            return true;
        } else {
            return false; // índice inválido
        }

    } catch (IOException e) {
        System.err.println("Error al eliminar contacto: " + e.getMessage());
        return false;
    }
}

}
