package biz.heiges.javafx.libary.tableview.test;

import biz.heiges.javafx.libary.tableview.TableViewDataModelBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class TestTableViewBinding extends TableViewDataModelBinding {
	
	/**
	 * Property text
	 */
	private SimpleStringProperty text = new SimpleStringProperty("enter a value");

	public void setText(String value) {
		this.text.setValue(value);
	}
	
	public String getText() {
		return text.getValue();
	}
	
	public SimpleStringProperty textProperty() {
		return text;
	}

	/**
	 * Property checked
	 */	
	private SimpleBooleanProperty checked = new SimpleBooleanProperty(false);
	
	public void setChecked(Boolean value) {
		this.checked.setValue(value);
	}
	
	public boolean getChecked() {
		return this.checked.getValue();
	}
	
	public SimpleBooleanProperty checkedProperty() {
		return checked;
	}
	
	/**
	 * Property listSelected
	 */
	private SimpleStringProperty listSelected = new SimpleStringProperty("foo");
	
	public SimpleStringProperty getListSelected() {
		return listSelected;
	}

	public void setListSelected(SimpleStringProperty listSelected) {
		this.listSelected = listSelected;
	}

	public SimpleStringProperty listSelectedProperty() {
		return listSelected;
	}
}