package biz.heiges.javafx.libary.tableview.test;

import biz.heiges.javafx.libary.tableview.ItemFactory;
import biz.heiges.javafx.libary.tableview.TableViewDataModelBinding;

public class TestItemFactory implements ItemFactory {

	@Override
	public TableViewDataModelBinding build() {
		return new TestTableViewBinding();
	}

}
