package heiges.biz.javafx.libary.tableview;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import heiges.biz.javafx.libary.commons.Fonts;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * @author Hansjoachim Heiges
 * @since 0.2
 * @version 0.1
 */
class DetailView<DATA_BINDING extends TableViewDataModelBinding> extends VBox {

	private GridPane grid = new GridPane();

	private TableView<DATA_BINDING> parent;

	public DetailView(TableView<DATA_BINDING> parent) {

		this.parent = parent;

		// Build the horizontal box for all needed buttons for the detail view.
		// Set the alignment and bind the width to the wrapping vertical box.
		HBox hboxForTopButtons = new HBox();
		hboxForTopButtons.setAlignment(Pos.TOP_RIGHT);
		hboxForTopButtons.prefWidthProperty().bind(this.widthProperty());

		// Build the close button and the behavior and add it to the horizontal box.
		Label closeLabelCross = new Label("\uF00D");
		closeLabelCross.setFont(Fonts.getFont("/fa/fontawesome-webfont.ttf", 15));
		Button closeButton = new Button("", closeLabelCross);
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				parent.changeView(1);
			}
		});
		hboxForTopButtons.getChildren().add(closeButton);

		// Set the layout for the grid containing the controls for the detail view
		grid.setHgap(10);
		grid.setVgap(5);

		// add the horizontal box to the vertical box.
		this.getChildren().addAll(hboxForTopButtons, grid);
	}

	protected void updateDetailView(DATA_BINDING value) {

		grid.getChildren().clear();

		int index = 0;
		for (Iterator<TableProperty> iterator = parent.getPropertiesForDetailView().iterator(); iterator.hasNext();) {
			TableProperty v = (TableProperty) iterator.next();
			grid.add(new Text(v.getName()), 0, index);

			String text = null;
			try {
				Method method = value.getClass().getMethod(v.getProperty() + "Property");

				if (v.getType().equals(ColumnType.FIELD)) {
					SimpleStringProperty invoke = (SimpleStringProperty) method.invoke(value);
					text = invoke.getValue();
				}

				if (v.getType().equals(ColumnType.CHECKBOX)) {
					SimpleBooleanProperty invoke = (SimpleBooleanProperty) method.invoke(value);
					text = invoke.getValue().toString();
				}
				
				if(v.getType().equals(ColumnType.LIST)) {
					SimpleStringProperty invoke = (SimpleStringProperty) method.invoke(value);
					text = invoke.getValue();
				}

			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			grid.add(new Text(text), 1, index++);
		}
	}
}
