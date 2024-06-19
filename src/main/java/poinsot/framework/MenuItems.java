package poinsot.framework;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MenuItems {
	private List<Accion> acciones = new ArrayList<>();

	public MenuItems(FileReader fileReader) {
		cargarAcciones(fileReader);
	}

	private void cargarAcciones(FileReader fileReader) {
		Configuracion configuracion = new Configuracion(fileReader);
		List<String> clasesAcciones = configuracion.obtenerClases();
		for (String clase : clasesAcciones) {
			try {
				Class<?> clazz = Class.forName(clase);
				Accion accion = (Accion) clazz.getDeclaredConstructor().newInstance();
				acciones.add(accion);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException |
					 InvocationTargetException e) {
				throw new RuntimeException("Error al cargar la clase: " + clase, e);
			}
		}
	}

	public void iniciar() {
		try {
			// Crear terminal y pantalla con Lanterna
			DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
			Screen screen = terminalFactory.createScreen();
			screen.startScreen();

			// Crear GUI y ventana
			WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
			BasicWindow mainWindow = new BasicWindow("Menú de Opciones");

			// Crear panel con layout
			Panel mainPanel = new Panel();
			mainPanel.setLayoutManager(new GridLayout(1));

			// Agregar componentes al panel principal
			mainPanel.addComponent(new Label("Bienvenido, estas son sus opciones:"));

			for (int i = 0; i < acciones.size(); i++) {
				Accion accion = acciones.get(i);
				int optionNumber = i + 1;
				mainPanel.addComponent(new Button(optionNumber + ". " + accion.nombreItemMenu() + " - " + accion.descripcionItemMenu(), () -> {
					ejecutarAccion(accion);
				}));
			}

			mainPanel.addComponent(new Button("0. Salir", () -> {
				try {
					screen.stopScreen();
				} catch (IOException e) {
					throw new RuntimeException("Error:", e);
				}
				System.out.println("Saliendo...");
				System.exit(0);
			}));

			// Agregar panel principal a la ventana principal
			mainWindow.setComponent(mainPanel);

			// Mostrar ventana principal y esperar a que el usuario interactúe con ella
			textGUI.addWindowAndWait(mainWindow);

			// Detener la pantalla al salir
			screen.stopScreen();
		} catch (IOException e) {
			throw new RuntimeException("Error:", e);
		}
	}

	private void ejecutarAccion(Accion accion) {
		try {
			// Crear terminal y pantalla con Lanterna para la ventana emergente
			DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
			Screen screen = terminalFactory.createScreen();
			screen.startScreen();

			// Crear GUI y ventana emergente
			WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
			BasicWindow popupWindow = new BasicWindow("Acción Ejecutada");

			// Crear panel con layout
			Panel popupPanel = new Panel();
			popupPanel.setLayoutManager(new GridLayout(1));

			// Ejecutar la acción y obtener la descripción
			accion.ejecutar();
			String descripcionAccion = accion.descripcionItemMenu();

			// Agregar componentes al panel emergente
			popupPanel.addComponent(new Label("Se ejecutó la acción:"));
			popupPanel.addComponent(new Label(accion.nombreItemMenu()));
			popupPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
			popupPanel.addComponent(new Label(descripcionAccion));
			popupPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
			popupPanel.addComponent(new Button("Aceptar", popupWindow::close));

			// Agregar panel emergente a la ventana emergente
			popupWindow.setComponent(popupPanel);

			// Mostrar ventana emergente y esperar a que el usuario interactúe con ella
			textGUI.addWindowAndWait(popupWindow);

			// Detener la pantalla emergente al cerrar
			screen.stopScreen();
		} catch (IOException e) {
			throw new RuntimeException("Error", e);
		}
	}

}
