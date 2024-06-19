package poinsot.main;

import poinsot.framework.Start;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
		try {
			var reader = new FileReader("C:\\Users\\leonr\\IdeaProjects\\TPFrameworksVersion1.2\\src\\main\\resources\\configuracion.json");
            var start = new Start(reader);
            start.init();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
    }
}