package heiges.biz.javafx.libary.tableview;

import java.io.InputStream;
import java.util.Arrays;

import heiges.biz.javafx.libary.tableview.cellfactories.SelectionCheckBoxCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
//import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class TableView<T extends TableViewDataModelBinding> extends javafx.scene.control.TableView<T> {

	private Font awesomeFont = null;

	private TableColumn<T, String> headerCol = new TableColumn<T, String>("");

	public TableView(ItemFactory factory) {

		InputStream awsome = TableView.class.getResourceAsStream("/fa/fontawesome-webfont.ttf");
		awesomeFont = Font.loadFont(awsome, 15);

		setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		setEditable(true);

		TableColumn<T, Boolean> selectedCol = new TableColumn<T, Boolean>("");
		selectedCol.setCellFactory(new SelectionCheckBoxCellFactory<>());
		selectedCol.setCellValueFactory(new PropertyValueFactory<T, Boolean>("SelectedProperty"));
		selectedCol.setSortable(false);

		headerCol.getColumns().addAll(Arrays.asList(selectedCol));

		getColumns().add(headerCol);

		/**
		 * build the checkbox selectAll
		 */
		CheckBox selectAll = new CheckBox();
		selectedCol.setGraphic(selectAll);
		selectAll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				for (T binding : getItems()) {
					binding.getSelectedProperty().set(selectAll.isSelected());
				}
			}
		});

		/**
		 * build the graphics box with the checkboxes
		 */
		HBox headerBox = new HBox();
		HBox.setMargin(selectAll, new Insets(5, 0, 5, 5));
		headerBox.setAlignment(Pos.CENTER_RIGHT);
		headerBox.getChildren().addAll(selectAll);
		selectedCol.setGraphic(headerBox);

		/**
		 * build the new button
		 */
		Label newLabel = new Label("\uF067");
		newLabel.setFont(awesomeFont);
		Button buttonNew = new Button("", newLabel);
		buttonNew.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent e) {
				getItems().add((T) factory.build());

				// after adding an item the checkbox selectAll must be unchecked
				if (selectAll.isSelected() == true) {
					selectAll.setSelected(false);
				}
				// FIXME uncheck all checked lines?
			}
		});

		Label deleteLabel = new Label("\uF014");
		deleteLabel.setFont(awesomeFont);
		Button buttonDelete = new Button("", deleteLabel);
		buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				ObservableList<T> newList = FXCollections.observableArrayList();
				for (T binding : getItems()) {
					if (binding.getSelectedProperty().getValue() == Boolean.TRUE) {
					} else {
						newList.add(binding);
					}
				}
				// deleting the items in the attached list will result in
				// ConcurrentModificationExceptions.
				setItems(newList);

				// after deleting an item the checkbox selectAll must be
				// unchecked
				if (selectAll.isSelected() == true) {
					selectAll.setSelected(false);
				}

			}
		});

		headerBox = new HBox();
		HBox.setMargin(buttonNew, new Insets(5, 0, 5, 5));
		HBox.setMargin(buttonDelete, new Insets(5, 0, 5, 5));
		headerBox.setAlignment(Pos.TOP_LEFT);
		headerBox.getChildren().addAll(buttonNew, buttonDelete);
		headerCol.setGraphic(headerBox);
	}

	public void addStringColumn(String name, String property) {
		TableColumn<T, String> stringColumn = new TableColumn<T, String>(name);
		stringColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		stringColumn.setCellValueFactory(new PropertyValueFactory<T, String>(property));
		stringColumn.setCellValueFactory(new PropertyValueFactory<T, String>(property));
		headerCol.getColumns().addAll(Arrays.asList(stringColumn));
	}
}
