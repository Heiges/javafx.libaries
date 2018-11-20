package heiges.biz.javafx.libary.tableview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import heiges.biz.javafx.libary.commons.Fonts;
import heiges.biz.javafx.libary.tableview.cell.ActionCell;
import heiges.biz.javafx.libary.tableview.cell.SelectThisRowCell;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * @author Hansjoachim Heiges
 * @since 0.2
 * @version 0.1
 */
class TableViewInt<DATA_BINDING extends TableViewDataModelBinding> extends VBox {

	/**
	 * The wrapped table.
	 */
	private javafx.scene.control.TableView<DATA_BINDING> table = null;

	/**
	 * The main column, all other columns will be added as child columns and will be
	 * arranged below the main column. The main column will be used for all action
	 * buttons the affect all items (rows) in the table or the table itself (e.g.
	 * the delete button will only affect selected rows while the new button will
	 * add a new item to the table
	 */
	private TableColumn<DATA_BINDING, String> headerCol = null;

	/**
	 * The check box for selecting all rows in the table.
	 */
	private CheckBox selectAllRowsCheckBox = null;

	/**
	 * An observable list with notifications when a single item in the list has been
	 * changed. Will be used together with the listChangeListener.
	 */
	private ObservableList<DATA_BINDING> observableItems = null;

	private TableViewInt<DATA_BINDING>.ListChangeListenerImplementation listChangeListener = null;

	private TableColumn<DATA_BINDING, Boolean> selectARowColumn = null;

	private TableColumn<DATA_BINDING, Boolean> actionCol = null;

	private Boolean currentEditableState = false;

	private List<TableColumn<DATA_BINDING, ?>> columns = new ArrayList<>();

	private TableView<DATA_BINDING> parent = null;

	public TableViewInt(TableView<DATA_BINDING> parent, ObservableList<DATA_BINDING> items, ItemFactory factory) {

		this.parent = parent;

		table = new javafx.scene.control.TableView<DATA_BINDING>(items);
		this.getChildren().add(table);
		table.prefHeightProperty().bind(this.heightProperty());
		table.prefWidthProperty().bind(this.widthProperty());
		table.setColumnResizePolicy(javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY);

		// Build the header column and add it to the table.
		headerCol = new TableColumn<DATA_BINDING, String>("");
		table.getColumns().add(headerCol);

		// Build the selection box and the SelectionBoxCellFactory. The
		// SelectionBoxCellFactory will need the selection box for construction.
		selectAllRowsCheckBox = buildSelectAllRowsCheckBox();
		selectAllRowsCheckBox.setPadding(new Insets(0, 5, 0, 0));

		// Build the selectARowColumn. This is the first column and will display the
		// selectIt action button for selecting or unselecting a row
		selectARowColumn = buildSelectedColumn();
		selectARowColumn.setCellFactory(cellFactory -> new SelectThisRowCell<>());

		// NEU
		// FIXME what is the correct Type for a row with buttons only?
		actionCol = new TableColumn<DATA_BINDING, Boolean>("");
		actionCol.setId("actionCol");
		// FIXME get a property to bind for our actions in the meantime just use
		// selectedProperty
		actionCol.setCellValueFactory(new PropertyValueFactory<DATA_BINDING, Boolean>("selected"));
//		actionCol.setCellFactory(cellFactory -> new ActionCell<>(null, null));  // compiles
//		actionCol.setCellFactory(cellFactory -> new ActionCell<T, Boolean>(null, null));  // compiles too
//		
//		ActionCell<T, Boolean> actionCell = new ActionCell<T, Boolean>(null, null);
//		actionCol.cellFactoryProperty().set(actionCell);  // compiles not casting error
//		actionCol.setCellFactory(actionCell);  // compiles not casting error
//		
		// FIXME instead of setting the callbacks via constructor, use a prebuild
		// instance, but handle the casting problem then.
		actionCol.setCellFactory(cellFactory -> new ActionCell<DATA_BINDING, Boolean>(buildCallbackForEditView(),
				buildCallbackForDetailView()));
		actionCol.setSortable(false);

		// Add selectAllRows check box to the selectARowColumn column header. The
		// checkBox will be connected with the checkBox cellFactory for displaying the
		// state of the check box.
		HBox selectARowColumnHeaderBox = new HBox();
		HBox.setMargin(selectAllRowsCheckBox, new Insets(5, 0, 5, 5));
		selectARowColumnHeaderBox.setAlignment(Pos.CENTER_RIGHT);
		selectARowColumnHeaderBox.getChildren().addAll(selectAllRowsCheckBox);
		selectARowColumn.setGraphic(selectARowColumnHeaderBox);

		// Build all needed action buttons and add it to header column of the table. The
		// selectAllRowsCheckBox will be connected with the new action button for
		// changing the state if a new item is added to the table.
		Button buttonNew = buildButtonNew(factory, selectAllRowsCheckBox);
		Button buttonDelete = buildButtonDelete();
		Button editButton = buildButtonEdit();
		HBox actionBox = new HBox();
		HBox.setMargin(buttonNew, new Insets(5, 0, 5, 5));
		HBox.setMargin(buttonDelete, new Insets(5, 0, 5, 5));
		HBox.setMargin(editButton, new Insets(5, 0, 5, 5));
		actionBox.setAlignment(Pos.TOP_LEFT);
		actionBox.getChildren().addAll(buttonNew, buttonDelete, editButton);
		headerCol.setGraphic(actionBox);

		// set table to editable true, else the selectThisRowCheckBox will not be
		// usable.
		table.setEditable(true);

		updateObservableItems(items);

		buildListChangeListener(items);

		addListChangeListerToObservableItems();
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

				table.getItems().stream()
						.forEach(binding -> binding.selectedProperty().set(selectAllRows.isSelected()));

				addListChangeListerToObservableItems();
			}
		});

		return selectAllRows;
	}

	protected void addColumn(String name, String property, ColumnType type, List<String> comboBoxList) {

		switch (type) {
		case FIELD:
			TableColumn<DATA_BINDING, String> stringColumn = new TableColumn<DATA_BINDING, String>(name);
			stringColumn.setCellFactory(TextFieldTableCell.forTableColumn());
			stringColumn.setCellValueFactory(new PropertyValueFactory<DATA_BINDING, String>(property));
			columns.add(stringColumn);
			break;
		case CHECKBOX:
			TableColumn<DATA_BINDING, String> checkBoxColumn = new TableColumn<DATA_BINDING, String>(name);
			checkBoxColumn.setCellFactory(column -> new CheckBoxTableCell<>());
			checkBoxColumn.setCellValueFactory(new PropertyValueFactory<DATA_BINDING, String>(property));
			columns.add(checkBoxColumn);
			break;
		case LIST:
			if (comboBoxList == null || comboBoxList.isEmpty())	throw new IllegalStateException("List must not be empty!");
			TableColumn<DATA_BINDING, String> comboBoxColumn = new TableColumn<DATA_BINDING, String>(name);
			comboBoxColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(comboBoxList)));
			comboBoxColumn.setCellValueFactory(new PropertyValueFactory<DATA_BINDING, String>(property));
			columns.add(comboBoxColumn);
			break;
		default:
			break;
		}
		sortColumns();
	}

	protected void sortColumns() {
		setEditableState();
		headerCol.getColumns().clear();
		headerCol.getColumns().add(selectARowColumn);
		headerCol.getColumns().addAll(columns);
		headerCol.getColumns().add(actionCol);
	}

	/**
	 * build the new button
	 * 
	 * @param factory
	 * @param selectAll
	 * @return
	 */
	private Button buildButtonNew(ItemFactory factory, CheckBox selectAll) {
		Label newLabel = new Label("\uF067");
		newLabel.setFont(Fonts.getFont("/fa/fontawesome-webfont.ttf", 15));
		Button buttonNew = new Button("", newLabel);
		buttonNew.setOnAction(new EventHandler<ActionEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(ActionEvent e) {

				observableItems.removeListener(listChangeListener);

				table.getItems().add((DATA_BINDING) factory.build());

				// after adding an item the check box selectAll must be
				// unchecked
				selectAll.setSelected(false);

				updateObservableItems(table.getItems());

				addListChangeListerToObservableItems();
			}
		});
		return buttonNew;
	}

	/**
	 * build the delete button
	 * 
	 * @return
	 */
	private Button buildButtonDelete() {
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

				ObservableList<DATA_BINDING> items = table.getItems();
				Iterator<DATA_BINDING> itemsIterator = items.iterator();
				while (itemsIterator.hasNext()) {
					DATA_BINDING binding = itemsIterator.next();
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

	private Button buildButtonEdit() {
		// F023 Closed Lock
		// F009 Open Lock
		Label editLabelClosedLock = new Label("\uF023");
		Label editLabelOpenLock = new Label("\uF09c");
		editLabelClosedLock.setFont(Fonts.getFont("/fa/fontawesome-webfont.ttf", 15));
		editLabelOpenLock.setFont(Fonts.getFont("/fa/fontawesome-webfont.ttf", 15));
		Button editButton = new Button("", editLabelClosedLock);
		editButton.setId("editButton");
		editButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				currentEditableState = !currentEditableState;
				if (currentEditableState == false) {
					editButton.setGraphic(editLabelClosedLock);
				} else {
					editButton.setGraphic(editLabelOpenLock);
				}
				setEditableState();
			}
		});
		return editButton;
	}

	private void setEditableState() {
		table.setEditable(currentEditableState);
		for (Iterator<TableColumn<DATA_BINDING, ?>> iterator = columns.iterator(); iterator.hasNext();) {
			TableColumn<DATA_BINDING, ?> column = iterator.next();
			column.setEditable(currentEditableState);
		}
		table.refresh();
	}

	private Callback<DATA_BINDING, String> buildCallbackForEditView() {

		return new Callback<DATA_BINDING, String>() {
			@Override
			public String call(DATA_BINDING param) {
				System.out.println("edit view : Input parameter is " + param);
				parent.changeView(0); // 0 == Details 1 == table
				parent.updateDetailView(param);
				return null;
			}
		};
	}

	private Callback<DATA_BINDING, String> buildCallbackForDetailView() {

		return new Callback<DATA_BINDING, String>() {
			@Override
			public String call(DATA_BINDING param) {
				System.out.println("detail  view : Input parameter is " + param);

				parent.changeView(0); // 0 == Details 1 == table
				parent.updateDetailView(param);
				return null;
			}
		};
	}

	private TableColumn<DATA_BINDING, Boolean> buildSelectedColumn() {
		TableColumn<DATA_BINDING, Boolean> selectedCol = new TableColumn<DATA_BINDING, Boolean>("");
		selectedCol.setCellValueFactory(new PropertyValueFactory<DATA_BINDING, Boolean>("selected"));
		selectedCol.setSortable(false);
		return selectedCol;
	}

	private void addListChangeListerToObservableItems() {
		observableItems.addListener(listChangeListener);
	}

	private void buildListChangeListener(ObservableList<DATA_BINDING> items) {
		listChangeListener = new ListChangeListenerImplementation(items);
	}

	/**
	 * Update the observable list with the current list of items in the table view.
	 * 
	 * @param items the current list of items in the table view.
	 */
	private void updateObservableItems(ObservableList<DATA_BINDING> items) {
		observableItems = FXCollections.observableArrayList(new Callback<DATA_BINDING, Observable[]>() {
			@Override
			public Observable[] call(DATA_BINDING param) {
				return new Observable[] { param.selectedProperty(), };
			}
		});
		observableItems.addAll(items);
	}

	/**
	 * A ListChangeListener with a callback for notification if items itself
	 * changed. Used for notifying if the selected property has been clicked.
	 */
	private final class ListChangeListenerImplementation implements ListChangeListener<DATA_BINDING> {

		private final ObservableList<DATA_BINDING> items;

		private ListChangeListenerImplementation(ObservableList<DATA_BINDING> items) {
			this.items = items;
		}

		@Override
		public void onChanged(Change<? extends DATA_BINDING> c) {
			while (c.next()) {
				if (c.wasUpdated()) {
					for (int i = c.getFrom(); i < c.getTo(); ++i) {

						boolean allRowsAreSelected = true;
						for (DATA_BINDING item : items) {
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
