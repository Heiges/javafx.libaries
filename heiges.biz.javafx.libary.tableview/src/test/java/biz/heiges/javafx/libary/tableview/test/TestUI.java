package biz.heiges.javafx.libary.tableview.test;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;

import heiges.biz.javafx.libary.tableview.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;;

@Category(TestFX.class)
public class TestUI extends GuiTest {

	ObservableList<TestTableViewBinding> data = FXCollections.observableArrayList();
	
	@Override
	protected Parent getRootNode() {
		TableView<TestTableViewBinding> table = new TableView<TestTableViewBinding>(buildItems(), new TestItemFactory());
		table.addStringColumn("a", "Field1StringProperty");
		table.addStringColumn("b", "Field2StringProperty");
		table.addStringColumn("c", "StringProperty");
		return table;
	}

	@Test	
	public void allUIElementsPresent(){
		find("#selectAllCheckBox");
		find("#newElementButton");
		find("#deleteElementButton");
	}
	
	private ObservableList<TestTableViewBinding> buildItems() {
		TestTableViewBinding binding = new TestTableViewBinding();
		binding.setField1StringProperty("eins");
		binding.setField2StringProperty("zwei");
		binding.setStringProperty("drei");
		TestTableViewBinding binding2 = new TestTableViewBinding();
		binding2.setField1StringProperty("d");
		binding2.setField2StringProperty("e");
		binding2.setStringProperty("f");
		data.addAll(binding, binding2);
		return data;
	}
}
