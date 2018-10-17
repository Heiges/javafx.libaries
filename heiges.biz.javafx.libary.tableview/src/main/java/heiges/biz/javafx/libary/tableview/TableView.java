package heiges.biz.javafx.libary.tableview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import heiges.biz.javafx.libary.commons.Fonts;
import heiges.biz.javafx.libary.tableview.cell.ActionCell;
import heiges.biz.javafx.libary.tableview.cell.SelectThisRowCell;
import heiges.biz.javafx.libary.tableview.cellfactories.ComboBoxCellFactory;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * A tableview with additional functionality.
 * 
 * @author Hansjoachim Heiges
 *
 * @param <DATA_BINDING> the data model for the items of the table view.
 */
public class TableView<DATA_BINDING extends TableViewDataModelBinding> {

	private Text field1 = new Text("Inhalt 1");
	
	
	// A listener for changing an single item of the table
	private TableView<DATA_BINDING>.ListChangeListenerImplementation listChangeListener = null;

	// An observable list with notifications when a single item in the list has been
	// changed. Will be used together with the listChangeListener.
	private ObservableList<DATA_BINDING> observableItems = null;

	private javafx.scene.control.TableView<DATA_BINDING> table = null;

	private TableColumn<DATA_BINDING, Boolean> selectARowColumn = null;

	private TableColumn<DATA_BINDING, Boolean> actionCol = null;

	private List<TableColumn<DATA_BINDING, ?>> columns = new ArrayList<>();
	
	StackPane root = null;
	
	ObservableList<DATA_BINDING> items;

	private CheckBox selectAllRowsCheckBox = null;

	private Boolean currentEditableState = false;

	/**
	 * The main column, all other columns will be added as child columns and will be
	 * arranged below the main column. The main column will be used for all action
	 * buttons the affect all items (rows) in the table or the table itself (e.g.
	 * the delete button will only affect selected rows while the new button will
	 * add a new item to the table
	 */
	private TableColumn<DATA_BINDING, String> headerCol = null;

	/**
	 * c-tor.
	 * 
	 * @param items
	 * @param factory
	 */
	public TableView(ObservableList<DATA_BINDING> items, ItemFactory factory) {

		this.items = items;
		
		VBox vboxForDetailsView = buildDetailView();
		
		VBox vboxForTable = buildTableView(items, factory);
		
		// Build the stack pane containing the table and the details view.
		// Bind the height and width properties of the stack pane and the vertical boxes of the table and details views
		root = new StackPane();
		vboxForTable.prefHeightProperty().bind(root.heightProperty());
		vboxForTable.prefWidthProperty().bind(root.widthProperty());
		vboxForDetailsView.prefHeightProperty().bind(root.heightProperty());
		vboxForDetailsView.prefWidthProperty().bind(root.widthProperty());
		
		// Add the table and the detail view to the stack pane.
		root.getChildren().addAll(vboxForDetailsView, vboxForTable);
	}



	private VBox buildDetailView() {
		
		// Build the vertical wrapping box for the detail view. 
		VBox vboxForDetailsView = new VBox();
		// TODO build a nice border similar to the border of the table view.
		
		// Build the horizontal box for all needed buttons for the detail view.
		// Set the alignment and bind the width to the wrapping vertical box.
		HBox hboxForTopButtons = new HBox();	
		hboxForTopButtons.setAlignment(Pos.TOP_RIGHT);
		hboxForTopButtons.prefWidthProperty().bind(vboxForDetailsView.widthProperty());
		
		// Build the close button and the behavior and add it to the horizontal box.
		Label closeLabelCross = new Label("\uF00D");
		closeLabelCross.setFont(Fonts.getFont("/fa/fontawesome-webfont.ttf", 15));
		Button closeButton = new Button("", closeLabelCross);
		closeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				changeView(1);
			}
		});
		hboxForTopButtons.getChildren().add(closeButton);
		
		// Build some demo stuff for the detail view for experiemental purposes
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(5);

	    grid.add(new Text("Field 1"), 0, 0); 	grid.add(field1, 1, 0);
	    grid.add(new Text("Field 2"), 0, 1); 	grid.add(new Text("Inhalt 2"), 1, 1);
	    grid.add(new Text("Field 2"), 0, 2); 	grid.add(new Text("Inhalt 3"), 1, 2);
	    
		// add the horizontal box to the vertical box.
		vboxForDetailsView.getChildren().addAll(hboxForTopButtons, grid);
		return vboxForDetailsView;
	}

	private void updateDetailView(DATA_BINDING value) {
		
		field1.setText("changed");
	}
	
	private Callback<DATA_BINDING, String> buildCallbackForEditView() {

		return new Callback<DATA_BINDING, String>() {
			@Override
			public String call(DATA_BINDING param) {
				System.out.println("edit view : Input parameter is " + param);
				changeView(0); // 0 == Details 1 == table
				updateDetailView(param);
				return null;
			}
		};
	}
	
	private Callback<DATA_BINDING, String> buildCallbackForDetailView() {
		
		return new Callback<DATA_BINDING, String>() {
			@Override
			public String call(DATA_BINDING param) {
				System.out.println("detail  view : Input parameter is " + param);
				
				changeView(0); // 0 == Details 1 == table
				updateDetailView(param);
				return null;
			}
		};
	}
	
	
	private void changeView(Integer viewIndex) {

		ObservableList<Node> childs = root.getChildren();
			
		Node topNode = childs.get(1);
		Node newTopNode = childs.get(0);
   
		topNode.setVisible(false);
		topNode.toBack();          
		newTopNode.setVisible(true);
	}
	
	
	private VBox buildTableView(ObservableList<DATA_BINDING> items, ItemFactory factory) {
		
		// Build the table and the wrapping vertical box for the table.
		VBox vboxForTable = new VBox();
		table = new javafx.scene.control.TableView<DATA_BINDING>(items);
		vboxForTable.getChildren().add(table);
		table.prefHeightProperty().bind(vboxForTable.heightProperty());
		table.prefWidthProperty().bind(vboxForTable.widthProperty());
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
		// FIXME instead of setting the callbacks via constructor, use a prebuild instance, but handle the casting problem then.
		actionCol.setCellFactory(cellFactory -> new ActionCell<DATA_BINDING, Boolean>(buildCallbackForEditView(), buildCallbackForDetailView()));
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

		// set table to editable true, else the selectThisRowCheckBox will not be usable.
		table.setEditable(true);

		updateObservableItems(items);
		
		buildListChangeListener(items);
		
		addListChangeListerToObservableItems();
		
		return vboxForTable;
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

	private Button buildEditButton() {
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

				ObservableList<DATA_BINDING> items = table.getItems();
				Iterator<DATA_BINDING> itemsIterator = items.iterator();
				while (itemsIterator.hasNext()) {
					DATA_BINDING binding = (DATA_BINDING) itemsIterator.next();
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

				table.getItems().add((DATA_BINDING) factory.build());

				// after adding an item the check box selectAll must be
				// unchecked
				selectAll.setSelected(false);

				updateObservableItems(items);

				addListChangeListerToObservableItems();
			}
		});
		return buttonNew;
	}

	private TableColumn<DATA_BINDING, Boolean> buildSelectedColumn() {
		TableColumn<DATA_BINDING, Boolean> selectedCol = new TableColumn<DATA_BINDING, Boolean>("");
		selectedCol.setCellValueFactory(new PropertyValueFactory<DATA_BINDING, Boolean>("selected"));
		selectedCol.setSortable(false);
		return selectedCol;
	}

	
	public enum ColumnType {

		FIELD, DATE, LIST, CHECKBOX;
	}

	public void addColumn(String name, String property, ColumnType type, List<String> comboBoxList) {
		
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
			if (comboBoxList == null || comboBoxList.isEmpty()) throw new IllegalStateException("List must not be empty!");
			TableColumn<DATA_BINDING, String> comboBoxColumn = new TableColumn<DATA_BINDING, String>(name);
			comboBoxColumn.setCellFactory(new ComboBoxCellFactory<>(comboBoxList));
			comboBoxColumn.setCellValueFactory(new PropertyValueFactory<DATA_BINDING, String>(property));
			columns.add(comboBoxColumn);
			break;
		default:
			break;
		}
		sortColumns();
	}

	private void sortColumns() {
		setEditableState();
		headerCol.getColumns().clear();
		headerCol.getColumns().add(selectARowColumn);
		headerCol.getColumns().addAll(columns);
		headerCol.getColumns().add(actionCol);
	}

	@SuppressWarnings("rawtypes")
	private void setEditableState() {
		// FIXME parameterize raw type
		for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
			TableColumn column = (TableColumn) iterator.next();
			column.setEditable(currentEditableState);
		}
	}

	public Property<Number> prefHeightProperty() {
		return root.prefHeightProperty();
	}

	public Property<Number> prefWidthProperty() {
		return root.prefWidthProperty();
	}

	public Node getRootNode() {
		return root;
	}

	/**
	 * A ListChangeListener with a callback for notification if items itself
	 * changed. Used for notifying if the selected property has been clicked.
	 * 
	 * @author Hansjoachim Heiges
	 *
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
