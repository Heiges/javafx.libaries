package heiges.biz.javafx.libary.tableview.cellfactories;

import heiges.biz.javafx.libary.tableview.TableViewDataModelBinding;
import heiges.biz.javafx.libary.tableview.cell.SelectThisRowCell;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class SelectThisRowCellFactory<T extends TableViewDataModelBinding>
		implements Callback<TableColumn<T, Boolean>, TableCell<T, Boolean>> {

	/**
	 * The check box that delegate to the check box in the table.
	 * 
	 */
	private CheckBox selectAllRowsInTable = null;

	/**
	 * Build the SelectionBoxCellFactory.
	 * 
	 * @param selectAllRowsInTable
	 *            if this check box is set to true, all lines in the table must
	 *            be set to true using selectThisRow. That check box is build in
	 *            the table itself and handed over to this
	 *            SelectionBoxCellFactory.
	 */
	public SelectThisRowCellFactory(CheckBox selectAllRowsInTable) {

		this.selectAllRowsInTable = selectAllRowsInTable;
	}

	@Override
	public TableCell<T, Boolean> call(TableColumn<T, Boolean> param) {

		/**
		 * Build the cell with the delegate to the selectAllRowsInTable.
		 */
		return new SelectThisRowCell<T, Boolean>(selectAllRowsInTable);	
	}
}
