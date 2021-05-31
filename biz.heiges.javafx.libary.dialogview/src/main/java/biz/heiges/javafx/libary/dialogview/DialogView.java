package biz.heiges.javafx.libary.dialogview;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class DialogView extends VBox {

	private GridPane grid = new GridPane();

	public DialogView() {

		// Set the layout for the grid containing the controls for the detail view
		grid.setHgap(10);
		grid.setVgap(5);
		grid.setPadding(new Insets(10, 10, 10, 10));

		// FIXME do we need to bind width and height property?
		grid.prefHeightProperty().bind(this.heightProperty());
		grid.prefWidthProperty().bind(this.widthProperty());

		// FIXME temporary for testing
		TextField f1 = new TextField("label", "value");
		TextField f2 = new TextField("label2", "value2");
		TextField f3 = new TextField("label3", "value3");
		grid.add(f1, 0, 0);
		grid.add(f2, 0, 1);
		grid.add(f3, 0, 2);
		this.getChildren().add(grid);
	}
}
