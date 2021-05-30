package heiges.biz.javafx.libary.commons;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import heiges.biz.javafx.libary.tableview.TableView;
import javafx.scene.text.Font;

public class Fonts {

	private static Map<String, Font> fonts = new HashMap<String, Font>();
	
	public static Font getFont(String pathOfFont, Integer size) {
		Font font = fonts.get(pathOfFont+String.valueOf(size));
		if (font == null) {
			addFont(pathOfFont, size);
		}
		return fonts.get(pathOfFont+String.valueOf(size));
	}
	
	private static void addFont(String pathOfFont, Integer size) {
		InputStream input = TableView.class.getResourceAsStream(pathOfFont);
		Font font = Font.loadFont(input, size);
		fonts.put(pathOfFont+String.valueOf(size), font);
	}
}
