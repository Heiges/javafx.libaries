package heiges.biz.javafx.libary.tableview;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import heiges.biz.javafx.libary.tableview.cellfactories.ComboBoxCellFactory;
import heiges.biz.javafx.libary.tableview.cellfactories.SelectionCheckBoxCellFactory;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class TableView<T extends TableViewDataModelBinding> {

	private javafx.scene.control.TableView<T> table = null;

	private VBox vbox = null;

	private Map<String, Font> fonts = new HashMap<String, Font>();

	/**
	 * The main column, all other columns will be added as child columns and
	 * will be arranged below the main column. The main column will be used for
	 * all action buttons the affect all items (rows) in the table or the table
	 * itself (e.g. the delete button will only affect selected rows while the
	 * new button will add a new item to the table
	 */
	private TableColumn<T, String> headerCol = null;

	/**
	 * 
	 * @param items
	 * @param factory
	 */
	public TableView(ObservableList<T> items, ItemFactory factory) {

		/**
		 * Load font awesome used for build the actionButtons.
		 */
		loadFontAwesome();

		/**
		 * Build the table and the wrapping vbox for the table.
		 */
		vbox = new VBox();
		table = new javafx.scene.control.TableView<T>(items);
		table.setId("tableview");
		vbox.getChildren().add(table);

		/**
		 * Bind the height and width properties of vbox and table and set the
		 * resize policy.
		 */
		table.prefHeightProperty().bind(vbox.heightProperty());
		table.prefWidthProperty().bind(vbox.widthProperty());
		table.setColumnResizePolicy(javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY);

		/**
		 * Build the header column and add it to the table.
		 */
		headerCol = new TableColumn<T, String>("");
		table.getColumns().add(headerCol);

		/**
		 * Build the selectARowColumn. This is the first column and will display
		 * the selectIt action button for selecting or unselecting a row
		 */
		TableColumn<T, Boolean> selectARowColumn = buildSelectedColumn();
		SelectionCheckBoxCellFactory<T> selectionCheckBoxCellFactory = new SelectionCheckBoxCellFactory<T>();
		selectARowColumn.setCellFactory(selectionCheckBoxCellFactory);
		headerCol.getColumns().addAll(Arrays.asList(selectARowColumn));

		/**
		 * Build the selectAllRows checkbox and add it to the selectARowColumn
		 * column header. The checkBox will be connected with the checkBox cellFactory for
		 * displaying the state of the checkbox.
		 */
		CheckBox selectAllRowsCheckBox = buildSelectAllRowsCheckBox(selectARowColumn, selectionCheckBoxCellFactory);
		HBox selectARowColumnHeaderBox = new HBox();
		HBox.setMargin(selectAllRowsCheckBox, new Insets(5, 0, 5, 5));
		selectARowColumnHeaderBox.setAlignment(Pos.CENTER_RIGHT);
		selectARowColumnHeaderBox.getChildren().addAll(selectAllRowsCheckBox);
		selectARowColumn.setGraphic(selectARowColumnHeaderBox);

		/**
		 * Build all needed action buttons and add it to headercolumn of the
		 * table. The selectAllRowsCheckBox will be connected with the new
		 * action button for changing the state if a new item is added to the
		 * table.
		 */
		Button buttonNew = buildNewButton(factory, selectAllRowsCheckBox);
		Button buttonDelete = buildDeleteButton();
		Button editButton = buildEditButton();
		HBox actionBox = new HBox();
		HBox.setMargin(buttonNew, new Insets(5, 0, 5, 5));
		HBox.setMargin(buttonDelete, new Insets(5, 0, 5, 5));
		HBox.setMargin(editButton, new Insets(5, 0, 5, 5));
		actionBox.setAlignment(Pos.TOP_LEFT);
		actionBox.getChildren().addAll(buttonNew, buttonDelete, editButton);
		headerCol.setGraphic(actionBox);

		/**
		 * set table to editable false
		 */
		table.setEditable(true);
	}

	private Button buildEditButton() {
		Label editLabel = new Label("\uF044");
		editLabel.setFont(getFont("/fa/fontawesome-webfont.ttf", 15));
		Button editButton = new Button("", editLabel);
		editButton.setId("editButton");
		editButton.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent e) {
			}
		});
		return editButton;
	}

	/**
	 * build the delete button
	 * 
	 * @return
	 */
	private Button buildDeleteButton() {
		Label deleteLabel = new Label("\uF014");
		deleteLabel.setFont(getFont("/fa/fontawesome-webfont.ttf", 15));
		Button buttonDelete = new Button("", deleteLabel);
		buttonDelete.setId("deleteElementButton");
		buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Iterator<T> itemsIterator = table.getItems().iterator();
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
	 * 
	 * @param selectedCol
	 * @param selectionCheckBoxCellFactory
	 * @return
	 */
	private CheckBox buildSelectAllRowsCheckBox(TableColumn<T, Boolean> selectedCol,
			SelectionCheckBoxCellFactory<T> selectionCheckBoxCellFactory) {
		CheckBox selectAll = new CheckBox();
		selectAll.setId("selectAllCheckBox");
		selectAll.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				table.getItems().stream().forEach(binding -> binding.getSelectedProperty().set(selectAll.isSelected()));
			}
		});
		selectionCheckBoxCellFactory.setSelectAll(selectAll);
		return selectAll;
	}

	/**
	 * build the new button
	 * 
	 * @param factory
	 * @param selectAll
	 * @return
	 */
	private Button buildNewButton(ItemFactory factory, CheckBox selectAll) {
		Label newLabel = new Label("\uF067");
		newLabel.setFont(getFont("/fa/fontawesome-webfont.ttf", 15));
		Button buttonNew = new Button("", newLabel);
		buttonNew.setId("newElementButton");
		buttonNew.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent e) {
				table.getItems().add((T) factory.build());

				// after adding an item the checkbox selectAll must be unchecked
				selectAll.setSelected(false);

				// if after adding an item all other items must be unselected
				// do it here with the following code snippet
				/*
				 * getItems().stream().forEach(binding ->
				 * binding.getSelectedProperty().setValue(false));
				 */
			}
		});
		return buttonNew;
	}

	private TableColumn<T, Boolean> buildSelectedColumn() {
		TableColumn<T, Boolean> selectedCol = new TableColumn<T, Boolean>("");
		selectedCol.setId("selectedColumn");
		selectedCol.setCellValueFactory(new PropertyValueFactory<T, Boolean>("SelectedProperty"));
		selectedCol.setSortable(false);
		return selectedCol;
	}

	private void loadFontAwesome() {
		addFont("/fa/fontawesome-webfont.ttf", 15);
	}

	public void addFont(String pathOfFont, Integer size) {
		InputStream input = TableView.class.getResourceAsStream(pathOfFont);
		Font font = Font.loadFont(input, size);
		fonts.put(pathOfFont, font);
	}

	public Font getFont(String pathOfFont, Integer size) {
		return fonts.get(pathOfFont);
	}

	public void addStringColumn(String name, String property) {
		TableColumn<T, String> stringColumn = new TableColumn<T, String>(name);
		stringColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		stringColumn.setCellValueFactory(new PropertyValueFactory<T, String>(property));
		headerCol.getColumns().addAll(Arrays.asList(stringColumn));
		stringColumn.setEditable(false);
	}

	public void addComboBoxColumn(String name, List<String> comboBoxList, String property) {
		TableColumn<T, String> comboBoxColumn = new TableColumn<T, String>(name);
		comboBoxColumn.setCellFactory(new ComboBoxCellFactory<>(comboBoxList));
		comboBoxColumn.setCellValueFactory(new PropertyValueFactory<T, String>(property));
		headerCol.getColumns().add(comboBoxColumn);
		comboBoxColumn.setEditable(false);
	}

	public Property<Number> prefHeightProperty() {
		return vbox.prefHeightProperty();
	}

	public Property<Number> prefWidthProperty() {
		return vbox.prefWidthProperty();
	}

	public Node getRootNode() {
		return vbox;
	}
}
