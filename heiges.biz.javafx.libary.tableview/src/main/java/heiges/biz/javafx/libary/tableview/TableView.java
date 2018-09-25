package heiges.biz.javafx.libary.tableview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import heiges.biz.javafx.libary.commons.Fonts;
import heiges.biz.javafx.libary.tableview.cellfactories.ActionCellFactory;
import heiges.biz.javafx.libary.tableview.cellfactories.ComboBoxCellFactory;
import heiges.biz.javafx.libary.tableview.cellfactories.SelectThisRowCellFactory;
import javafx.beans.Observable;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class TableView<T extends TableViewDataModelBinding> {

	// A listener for changing an single item of the table
	private TableView<T>.ListChangeListenerImplementation listChangeListener = null;

	// An observable list with notifications when a single item in the list has been
	// changed.
	// Will be used together with the listChangeListener.
	private ObservableList<T> observableItems = null;

	private javafx.scene.control.TableView<T> table = null;

	private TableColumn<T, Boolean> selectARowColumn = null;

	private TableColumn<T, Boolean> actionCol = null;

	@SuppressWarnings("rawtypes")
	private List columns = new ArrayList<TableColumn>();

	private VBox vbox = null;

	ObservableList<T> items;

	private CheckBox selectAllRowsCheckBox = null;

	/**
	 * The main column, all other columns will be added as child columns and will be
	 * arranged below the main column. The main column will be used for all action
	 * buttons the affect all items (rows) in the table or the table itself (e.g.
	 * the delete button will only affect selected rows while the new button will
	 * add a new item to the table
	 */
	private TableColumn<T, String> headerCol = null;

	/**
	 * 
	 * @param items
	 * @param factory
	 */
	public TableView(ObservableList<T> items, ItemFactory factory) {

		this.items = items;

		// Build the table and the wrapping vertical box for the table.
		vbox = new VBox();
		table = new javafx.scene.control.TableView<T>(items);
		table.setId("tableview");
		vbox.getChildren().add(table);

		// Bind the height and width properties of vbox and table and set the resize
		// policy.
		table.prefHeightProperty().bind(vbox.heightProperty());
		table.prefWidthProperty().bind(vbox.widthProperty());
		table.setColumnResizePolicy(javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY);

		// Build the header column and add it to the table.
		headerCol = new TableColumn<T, String>("");
		table.getColumns().add(headerCol);

		// Build the selection box and the SelectionBoxCellFactory. The
		// SelectionBoxCellFactory will need the selection box for construction.
		selectAllRowsCheckBox = buildSelectAllRowsCheckBox();
		selectAllRowsCheckBox.setPadding(new Insets(0, 5, 0, 0));
		SelectThisRowCellFactory<T> selectionCheckBoxCellFactory = new SelectThisRowCellFactory<T>();

		// Build the selectARowColumn. This is the first column and will display the
		// selectIt action button for selecting or unselecting a row
		selectARowColumn = buildSelectedColumn();
		selectARowColumn.setCellFactory(selectionCheckBoxCellFactory);

		// NEU
		// FIXME what is the correkt Type for a row with buttons only?
		actionCol = new TableColumn<T, Boolean>("");
		actionCol.setId("actionCol");
		ActionCellFactory<T> actionCellFactory = new ActionCellFactory<T>();
		// FIXME get a property to bind for our actions in the meantime just use
		// selectedProperty
		actionCol.setCellValueFactory(new PropertyValueFactory<T, Boolean>("selected"));
		actionCol.setCellFactory(actionCellFactory);
		actionCol.setSortable(false);

		// Add selectAllRows check box to the selectARowColumn column header. The
		// checkBox will be connected with the checkBox cellFactory for displaying the
		// state of the check box.
		HBox selectARowColumnHeaderBox = new HBox();
		HBox.setMargin(selectAllRowsCheckBox, new Insets(5, 0, 5, 5));
		selectARowColumnHeaderBox.setAlignment(Pos.CENTER_RIGHT);
		selectARowColumnHeaderBox.getChildren().addAll(selectAllRowsCheckBox);
		selectARowColumn.setGraphic(selectARowColumnHeaderBox);

		// Build all needed action buttons and add it to headercolumn of the table. The
		// selectAllRowsCheckBox will be connected with the new action button for
		// changing the state if a new item is added to the table.
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

		// set table to editable false
		table.setEditable(true);

		updateObservableItems(items);
		buildListChangeListener(items);
		addListChangeListerToObservableItems();

	}

	private void addListChangeListerToObservableItems() {
		observableItems.addListener(listChangeListener);
	}

	private void buildListChangeListener(ObservableList<T> items) {
		listChangeListener = new ListChangeListenerImplementation(items);
	}

	/**
	 * Update the observable list with the current list of items in the table view.
	 * 
	 * @param items the current list of items in the table view.
	 */
	private void updateObservableItems(ObservableList<T> items) {
		observableItems = FXCollections.observableArrayList(new Callback<T, Observable[]>() {
			@Override
			public Observable[] call(T param) {
				return new Observable[] { param.selectedProperty(), };
			}
		});
		observableItems.addAll(items);
	}

	private Button buildEditButton() {
		// F023 Closed Lock
		// F009 Open Lock
		Label editLabel = new Label("\uF023");
		editLabel.setFont(Fonts.getFont("/fa/fontawesome-webfont.ttf", 15));
		Button editButton = new Button("", editLabel);
		editButton.setId("editButton");
		editButton.setOnAction(new EventHandler<ActionEvent>() {
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
		deleteLabel.setFont(Fonts.getFont("/fa/fontawesome-webfont.ttf", 15));
		Button buttonDelete = new Button("", deleteLabel);
		buttonDelete.setId("deleteElementButton");

		// Build the behavior when the delete button is clicked. All selected rows in
		// the table will be deleted and the selectAllCheckBox will be set to false.
		buttonDelete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {

				removeListChangeListenerFromObservableItems();

				ObservableList<T> items = table.getItems();
				Iterator<T> itemsIterator = items.iterator();
				while (itemsIterator.hasNext()) {
					T binding = (T) itemsIterator.next();
					if (binding.selectedProperty().getValue() == Boolean.TRUE) {
						itemsIterator.remove();
					}
				}

				if (items.size() == 0)
					selectAllRowsCheckBox.selectedProperty().set(false);

				updateObservableItems(items);
				addListChangeListerToObservableItems();
			}

			private void removeListChangeListenerFromObservableItems() {
				observableItems.removeListener(listChangeListener);
			}
		});
		return buttonDelete;
	}

	/**
	 * build the check box selectAllRowsButton.
	 * 
	 * @param selectedCol
	 * @param selectionCheckBoxCellFactory
	 * @return
	 */
	private CheckBox buildSelectAllRowsCheckBox() {

		CheckBox selectAllRows = new CheckBox();

		// Build the behavior when the selectAllRows is clicked.
		selectAllRows.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				observableItems.removeListener(listChangeListener);

				items.stream().forEach(binding -> binding.selectedProperty().set(selectAllRows.isSelected()));


				addListChangeListerToObservableItems();
			}
		});

		return selectAllRows;
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
		newLabel.setFont(Fonts.getFont("/fa/fontawesome-webfont.ttf", 15));
		Button buttonNew = new Button("", newLabel);
		buttonNew.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent e) {

				observableItems.removeListener(listChangeListener);

				table.getItems().add((T) factory.build());

				// after adding an item the check box selectAll must be
				// unchecked
				selectAll.setSelected(false);

				updateObservableItems(items);

				addListChangeListerToObservableItems();
			}
		});
		return buttonNew;
	}

	private TableColumn<T, Boolean> buildSelectedColumn() {
		TableColumn<T, Boolean> selectedCol = new TableColumn<T, Boolean>("");
		selectedCol.setCellValueFactory(new PropertyValueFactory<T, Boolean>("selected"));
		selectedCol.setSortable(false);
		return selectedCol;
	}

	@SuppressWarnings("unchecked")
	public void addStringColumn(String name, String property) {
		TableColumn<T, String> stringColumn = new TableColumn<T, String>(name);
		stringColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		stringColumn.setCellValueFactory(new PropertyValueFactory<T, String>(property));
		columns.add(stringColumn);
		sortColumns();
//		stringColumn.setEditable(false);
	}

	@SuppressWarnings("unchecked")
	public void addComboBoxColumn(String name, List<String> comboBoxList, String property) {
		TableColumn<T, String> comboBoxColumn = new TableColumn<T, String>(name);
		comboBoxColumn.setCellFactory(new ComboBoxCellFactory<>(comboBoxList));
		comboBoxColumn.setCellValueFactory(new PropertyValueFactory<T, String>(property));
		columns.add(comboBoxColumn);
		sortColumns();
//		comboBoxColumn.setEditable(false);
	}

	@SuppressWarnings("unchecked")
	public void addCheckBoxColumn(String name, String property) {
		TableColumn<T, String> comboBoxColumn = new TableColumn<T, String>(name);
		comboBoxColumn.setCellFactory(column -> new CheckBoxTableCell<>());
		comboBoxColumn.setCellValueFactory(new PropertyValueFactory<T, String>(property));
		columns.add(comboBoxColumn);
		sortColumns();
//		comboBoxColumn.setEditable(false);
	}

	@SuppressWarnings("unchecked")
	private void sortColumns() {
		headerCol.getColumns().clear();
		headerCol.getColumns().add(selectARowColumn);
		headerCol.getColumns().addAll(columns);
		headerCol.getColumns().add(actionCol);
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

	/**
	 * 
	 * @author Hansjoachim Heiges
	 *
	 */
	private final class ListChangeListenerImplementation implements ListChangeListener<T> {

		private final ObservableList<T> items;

		private ListChangeListenerImplementation(ObservableList<T> items) {
			this.items = items;
		}

		@Override
		public void onChanged(Change<? extends T> c) {
			while (c.next()) {
				if (c.wasUpdated()) {
					for (int i = c.getFrom(); i < c.getTo(); ++i) {

						boolean allRowsAreSelected = true;
						for (T item : items) {
							if (item.selectedProperty().getValue() == false) {
								allRowsAreSelected = false;
								break;
							}
						}
						selectAllRowsCheckBox.selectedProperty().set(allRowsAreSelected);
					}
				} 
			}
		}
	}
}
