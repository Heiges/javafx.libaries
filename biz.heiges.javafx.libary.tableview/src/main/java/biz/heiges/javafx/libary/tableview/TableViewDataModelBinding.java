package biz.heiges.javafx.libary.tableview;

import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author Hansjoachim Heiges
 * @since 0.1
 * @version 0.1
 */
public abstract class TableViewDataModelBinding {

	private SimpleBooleanProperty selectedProperty;
	
	public TableViewDataModelBinding() {
		this.selectedProperty = new SimpleBooleanProperty(false);
	}
	
	public SimpleBooleanProperty selectedProperty() {
		return selectedProperty;
	}
}