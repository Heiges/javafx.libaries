package heiges.biz.javafx.libary.tableview.cellfactories;

import heiges.biz.javafx.libary.tableview.TableViewDataModelBinding;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

public class SelectionCheckBoxCellFactory<T extends TableViewDataModelBinding>
		implements Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>> {

	private boolean entered = false;

	private CheckBox selectAll = null;

	@Override
	public TableCell<T, Boolean> call(TableColumn<T, Boolean> param) {

		final CheckBoxTableCell<T, Boolean> cell = new CheckBoxTableCell<T, Boolean>() {

			@Override
			public void updateItem(Boolean selected, boolean empty) {

				super.updateItem(selected, empty);

				/**
				 * This if-then-else is needed to make javafx happy.
				 * See also javadoc for the function updateItem
				 */
				if (empty || selected == null) {
			    	 
					setText(null);
			        setGraphic(null);
			    } 
			    else {
			
			    	// This column will never show any text
			    	setText(null);
			    	
			    	/**
			    	 *  start with the needed functionality for handling the checkbox 
			    	 */
			    	
			    	setAlignment(Pos.CENTER_RIGHT);
	
					// get the binding for the row
					TableViewDataModelBinding binding = null;
					if (this.getTableRow() != null && this.getTableRow().getItem() != null
							&& this.getTableRow().getItem() instanceof TableViewDataModelBinding) {
						binding = (TableViewDataModelBinding) this.getTableRow().getItem();
					}
	
					if (entered == false && binding != null) {
						// currently not inside a cell with a checkbox and a binding exists, therefore force checkbox depending on selected state
						super.updateItem(selected, !binding.getSelectedProperty().getValue());
					} else if (entered == false && binding == null) {
						// currently not inside the cell with a checkbox and a  binding does not exist, therefore force a empty cell
						super.updateItem(selected, true);
					} else if (entered == true && binding == null) {
						// currently inside a cell with a checkbox and binding does not exist, therefore force a empty cell
						super.updateItem(selected, true);
					} else if (entered == true && binding != null) {
						// currently inside a cell with a checkbox and a binding does exist, therefore force a checkbox
						super.updateItem(selected, false);
					}
	
					// set the selectAll checkbox depending in the state of all items in the tableview
					if (getSelectAll() != null && binding != null) {
						ObservableList<T> items = getTableView().getItems();
						boolean allItemsAreSelected = true;
						for (T item : items) {
							if (item.getSelectedProperty().getValue() == false) {
								allItemsAreSelected = false;
								break;
							}
						}
						getSelectAll().selectedProperty().set(allItemsAreSelected);
					}
				}
			}
		};

		cell.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				entered = true;
				// force the checkbox, therefore empty must be set to false!
				cell.updateItem(cell.getItem(), false);
			}
		});

		cell.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				entered = false;
				// keep the checkbox if bounded property is true (means checkbox
				// is checked)
				if (cell.getTableRow() != null && cell.getTableRow().getItem() != null
						&& cell.getTableRow().getItem() instanceof TableViewDataModelBinding) {
					TableViewDataModelBinding item = (TableViewDataModelBinding) cell.getTableRow().getItem();
					cell.updateItem(cell.getItem(), !item.getSelectedProperty().getValue());
				}
			}
		});

		return cell;
	}

	public CheckBox getSelectAll() {
		return selectAll;
	}

	public void setSelectAll(CheckBox selectAll) {
		this.selectAll = selectAll;
	}
}
