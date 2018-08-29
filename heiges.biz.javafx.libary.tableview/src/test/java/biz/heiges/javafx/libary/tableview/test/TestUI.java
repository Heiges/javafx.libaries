package biz.heiges.javafx.libary.tableview.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;
import org.loadui.testfx.controls.TableViews;

import heiges.biz.javafx.libary.tableview.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;;

@Category(TestFX.class)
public class TestUI extends GuiTest {

	ObservableList<TestTableViewBinding> data = null;// FXCollections.observableArrayList();

	@Override
	protected Parent getRootNode() {
		
		System.out.println("getRootNode");

		TableView<TestTableViewBinding> table = new TableView<TestTableViewBinding>(buildItems(), new TestItemFactory());
		table.addStringColumn("Spalte 1", "Field1StringProperty");
		table.addStringColumn("Spalte 2", "Field2StringProperty");
		table.addStringColumn("Spalte 3", "StringProperty");
		return (Parent) table.getRootNode();
	}

	@Test
	public void allUIElementsPresent() {
		System.out.println("allUIElementsPresent");

		find("#newElementButton");
		find("#deleteElementButton");
		find("#selectAllCheckBox");
		find("#tableview");
		assertEquals("wrong count of rows", 2, TableViews.numberOfRowsIn("#tableview"));
	}

	/**
	 * build the testdata used for all ui-tests
	 * 
	 * @return the testdata
	 */
	private ObservableList<TestTableViewBinding> buildItems() {

		if (data == null) {
			data = FXCollections.observableArrayList();
			System.out.println("items build");
			TestTableViewBinding binding = new TestTableViewBinding();
			binding.setField1StringProperty("zeile 1 - eins");
			binding.setField2StringProperty("zweile 1 - zwei");
			binding.setStringProperty("zeile 1 - drei");
			TestTableViewBinding binding2 = new TestTableViewBinding();
			binding2.setField1StringProperty("zeile 2 - eins");
			binding2.setField2StringProperty("zeile 2 - zwei");
			binding2.setStringProperty("zeile 2 - drei");
			data.addAll(binding, binding2);
		} else {
			System.out.println("was already builded");
		}
		return data;
	}
}
