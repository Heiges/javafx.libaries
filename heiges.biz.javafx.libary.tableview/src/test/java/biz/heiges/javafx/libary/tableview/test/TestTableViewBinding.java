package biz.heiges.javafx.libary.tableview.test;

import heiges.biz.javafx.libary.tableview.TableViewDataModelBinding;
import javafx.beans.property.SimpleStringProperty;

public class TestTableViewBinding extends TableViewDataModelBinding {

	private SimpleStringProperty field1String = new SimpleStringProperty("enter a value");
	
	private SimpleStringProperty field2String = new SimpleStringProperty("enter a value");

	private SimpleStringProperty stringProperty = new SimpleStringProperty("enter a value");
	
	public void setStringProperty(String value) {
		this.stringProperty = new SimpleStringProperty(value);
	}
	
	public SimpleStringProperty getStringProperty() {
		return stringProperty;
	}

	public void setField1StringProperty(String value) {
		this.field1String = new SimpleStringProperty(value);
	}
	
	public SimpleStringProperty getField1StringProperty() {
		return field1String;
	}
	
	public void setField2StringProperty(String value) {
		this.field2String = new SimpleStringProperty(value);
	}
	
	public SimpleStringProperty getField2StringProperty() {
		return field2String;
	}
}