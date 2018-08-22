package heiges.biz.javafx.libary.tableview.cellfactories;

import java.util.ArrayList;
import java.util.List;

import heiges.biz.javafx.libary.tableview.TableViewDataModelBinding;
import javafx.collections.FXCollections;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;

public class ComboBoxCellFactory<T extends TableViewDataModelBinding> implements Callback<TableColumn<T, String>, TableCell<T, String>> {

	private List<String> comboBoxList = new ArrayList<String>();

	public ComboBoxCellFactory(List<String> comboBoxList) {
		this.comboBoxList = comboBoxList;
	}

	@Override
	public TableCell<T, String> call(TableColumn<T, String> param) {

		final ComboBoxTableCell<T, String> cell = new ComboBoxTableCell<T, String>() {

			@Override
			public void updateItem(String item, boolean empty) {

				super.updateItem(item, empty);

				if (empty || item == null) {
					
					setText(null);
					setGraphic(null);
				} 
				else {
					
					getItems().clear();
					getItems().addAll(FXCollections.observableArrayList(comboBoxList));

					if (comboBoxList.contains(item) == false) {
						System.out.println(item);
						comboBoxList.add(item);
					}
				}
			}
		};

		cell.setComboBoxEditable(true);

		cell.getItems().addAll(FXCollections.observableArrayList(comboBoxList));

		return cell;
	}
}
