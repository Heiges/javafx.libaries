package heiges.biz.javafx.libary.tableview;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import heiges.biz.javafx.libary.tableview.cellfactories.ComboBoxCellFactory;
import heiges.biz.javafx.libary.tableview.cellfactories.SelectionCheckBoxCellFactory;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class TableView<T extends TableViewDataModelBinding> extends javafx.scene.control.TableView<T> {

	private Font awesomeFont = null;

	private TableColumn<T, String> headerCol = new TableColumn<T, String>("");

	public TableView(ObservableList<T> items, ItemFactory factory) {

		super(items);

		setId("tableview");
		
		loadFontAwesome();

		setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		setEditable(true);

		getColumns().add(headerCol);

		// build the first column with the checkboxes for selecting a line in the tableview
		TableColumn<T, Boolean> selectedCol = buildSelectedColumn();
		SelectionCheckBoxCellFactory<T> selectionCheckBoxCellFactory = new SelectionCheckBoxCellFactory<T>();
		selectedCol.setCellFactory(selectionCheckBoxCellFactory);
		
		/**
		 * add the selected column to the header column
		 */
		headerCol.getColumns().addAll(Arrays.asList(selectedCol));

		CheckBox selectAll = buildSelectAllCheckBox(selectedCol, selectionCheckBoxCellFactory);

		Button buttonNew = buildNewButton(factory, selectAll);

		Button buttonDelete = buildDeleteButton();
		
		/**
		 * build the graphics box with the checkboxes
		 */
		HBox headerBox = new HBox();
		HBox.setMargin(selectAll, new Insets(5, 0, 5, 5));
		headerBox.setAlignment(Pos.CENTER_RIGHT);
		headerBox.getChildren().addAll(selectAll);
		selectedCol.setGraphic(headerBox);
		
		/**
		 * add the buttons to the headerBox
		 */
		headerBox = new HBox();
		HBox.setMargin(buttonNew, new Insets(5, 0, 5, 5));
		HBox.setMargin(buttonDelete, new Insets(5, 0, 5, 5));
		headerBox.setAlignment(Pos.TOP_LEFT);
		headerBox.getChildren().addAll(buttonNew, buttonDelete);
		headerCol.setGraphic(headerBox);
	}

	/**
	 * build the delete button
	 * @return
	 */
	private Button buildDeleteButton() {
		Label deleteLabel = new Label("\uF014");
		deleteLabel.setFont(awesomeFont);
		Button buttonDelete = new Button("", deleteLabel);
		buttonDelete.setId("deleteElementButton");
		buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Iterator<T> itemsIterator = getItems().iterator();
				while (itemsIterator.hasNext()) {
					T binding = (T) itemsIterator.next();
					if (binding.getSelectedProperty().getValue() == Boolean.TRUE) {
						itemsIterator.remove();
					}
				}
			}
		});
		return buttonDelete;
	}

	/**
	 * build the checkbox selectAll
	 * @param selectedCol
	 * @param selectionCheckBoxCellFactory
	 * @return
	 */
	private CheckBox buildSelectAllCheckBox(TableColumn<T, Boolean> selectedCol,
			SelectionCheckBoxCellFactory<T> selectionCheckBoxCellFactory) {
		CheckBox selectAll = new CheckBox();
		selectAll.setId("selectAllCheckBox");
		selectedCol.setGraphic(selectAll);
		selectAll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				getItems().stream().forEach(binding -> binding.getSelectedProperty().set(selectAll.isSelected()));
			}
		});
		selectionCheckBoxCellFactory.setSelectAll(selectAll);
		return selectAll;
	}

	/**
	 * build the new button
	 * @param factory
	 * @param selectAll
	 * @return
	 */
	private Button buildNewButton(ItemFactory factory, CheckBox selectAll) {
		Label newLabel = new Label("\uF067");
		newLabel.setFont(awesomeFont);
		Button buttonNew = new Button("", newLabel);
		buttonNew.setId("newElementButton");
		buttonNew.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent e) {
				getItems().add((T) factory.build());

				// after adding an item the checkbox selectAll must be unchecked
				selectAll.setSelected(false);

				// if after adding an item all other items must be unselected
				// do it here with the following code snippet
				/* getItems().stream().forEach(binding -> binding.getSelectedProperty().setValue(false)); */
			}
		});
		return buttonNew;
	}

	/**
	 * build the column for the selected property
	 * @return
	 */
	private TableColumn<T, Boolean> buildSelectedColumn() {	
		TableColumn<T, Boolean> selectedCol = new TableColumn<T, Boolean>("");
		selectedCol.setId("selectedColumn");
		selectedCol.setCellValueFactory(new PropertyValueFactory<T, Boolean>("SelectedProperty"));
		selectedCol.setSortable(false);
		return selectedCol;
	}

	private void loadFontAwesome() {
		InputStream awesome = TableView.class.getResourceAsStream("/fa/fontawesome-webfont.ttf");
		awesomeFont = Font.loadFont(awesome, 15);
	}

	public void addStringColumn(String name, String property) {
		TableColumn<T, String> stringColumn = new TableColumn<T, String>(name);
		stringColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		stringColumn.setCellValueFactory(new PropertyValueFactory<T, String>(property));
		headerCol.getColumns().addAll(Arrays.asList(stringColumn));
	}

	
	public void addComboBoxColumn(String name, List<String> comboBoxList, String property) {
		TableColumn<T, String> comboBoxColumn = new TableColumn<T, String>(name);
		comboBoxColumn.setCellFactory(new ComboBoxCellFactory<>(comboBoxList));
		comboBoxColumn.setCellValueFactory(new PropertyValueFactory<T, String>(property));
		comboBoxColumn.setEditable(true);
		headerCol.getColumns().add(comboBoxColumn);
	}	
}
