package heiges.biz.javafx.libary.tableview;

import java.util.List;

/**
 * @author Hansjoachim Heiges
 * @since 0.2
 * @version 0.1
 */
class TableProperty {

	public String name;
	public String property;
	public ColumnType type;
	public List<String> comboBoxList;

	public String getName() {
		return name;
	}

	public String getProperty() {
		return property;
	}

	public ColumnType getType() {
		return type;
	}

	public List<String> getComboBoxList() {
		return comboBoxList;
	}

	public TableProperty(String name, String property, ColumnType type, List<String> comboBoxList) {
		this.name = name;
		this.property = property;
		this.type = type;
		this.comboBoxList = comboBoxList;
	}
}
