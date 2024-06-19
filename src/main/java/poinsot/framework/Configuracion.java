package poinsot.framework;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Configuracion {

	private FileReader fileReader;
	private List<String> clasesAcciones;

	public Configuracion(FileReader fileReader) {
		this.fileReader = fileReader;
		cargarConfiguracion();
	}

	private void cargarConfiguracion() {
		// Parsea el JSON desde un FileReader y obtiene el objeto JSON raíz.
		JsonObject jsonObject = JsonParser.parseReader(fileReader).getAsJsonObject();
		// Inicializa la lista 'clasesAcciones' como un nuevo ArrayList vacío.
		clasesAcciones = new ArrayList<>();
		// Obtiene el array JSON 'acciones' del objeto JSON raíz y lo itera.
		jsonObject.getAsJsonArray("acciones").forEach(jsonElement -> {
			// Añade la representación en String de cada elemento del array JSON a la lista 'clasesAcciones'.
			clasesAcciones.add(jsonElement.getAsString());
		});
	}
	public List<String> obtenerClases() {
		return clasesAcciones;
	}
}