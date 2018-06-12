package biz.heiges.javafx.libary.tableview.test;

import heiges.biz.javafx.libary.tableview.ItemFactory;
import heiges.biz.javafx.libary.tableview.TableViewDataModelBinding;

public class TestItemFactory implements ItemFactory {

	@Override
	public TableViewDataModelBinding build() {
		return new TestTableViewBinding();
	}

}
