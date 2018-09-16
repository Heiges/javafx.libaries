package heiges.biz.javafx.libary.tableview.cellfactories;

import heiges.biz.javafx.libary.tableview.TableViewDataModelBinding;
import heiges.biz.javafx.libary.tableview.cell.ActionCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ActionCellFactory<T extends TableViewDataModelBinding>
		implements Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>> {

	@Override
	public TableCell<T, Boolean> call(TableColumn<T, Boolean> param) {

		/**
		 * Build the cell with the delegate to the selectAllRowsInTable.
		 */
		return new ActionCell<T, Boolean>();	
	}
}
