package heiges.biz.javafx.libary.tableview;

import java.util.ArrayList;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * @author Hansjoachim Heiges
 * @since 0.1
 * @version 0.1
 */
public abstract class TableViewDataModelBinding {

	private SimpleBooleanProperty selectedProperty;

	private ArrayList registeresDetailViewColumns = new ArrayList();
	
	public TableViewDataModelBinding() {
		this.selectedProperty = new SimpleBooleanProperty(false);
	}
	
	public SimpleBooleanProperty selectedProperty() {
		return selectedProperty;
	}
	
	public void registerForFiltering() {
		 
	}
	
	public void registerForTableView() {
		 
	}
	
	public void registerForDetailView() {
		 
	}
	
}