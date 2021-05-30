package biz.heiges.javafx.libary.tableview.main;

import java.util.ArrayList;

import biz.heiges.javafx.libary.tableview.ColumnType;
import biz.heiges.javafx.libary.tableview.TableView;
import biz.heiges.javafx.libary.tableview.test.TestItemFactory;
import biz.heiges.javafx.libary.tableview.test.TestTableViewBinding;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	
	ObservableList<TestTableViewBinding> data = FXCollections.observableArrayList();
		
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("TableView Test");
		primaryStage.setWidth(400);
		primaryStage.setHeight(500);
		
		Group root = new Group();
		Scene scene = new Scene(root);
		
		VBox primaryBox = new VBox();
		primaryBox.prefHeightProperty().bind(scene.heightProperty());
		primaryBox.prefWidthProperty().bind(scene.widthProperty());
		primaryBox.setPadding(new Insets(20, 20, 20, 20));
		
		TableView<TestTableViewBinding> table = new TableView<TestTableViewBinding>(buildItems(), new TestItemFactory());
		table.prefHeightProperty().bind(primaryBox.heightProperty());
		table.prefWidthProperty().bind(primaryBox.widthProperty());
		
		table.registerPropertyForView("text", "text", ColumnType.FIELD, ViewType.BOTH, null);
		table.registerPropertyForView("checked", "checked", ColumnType.CHECKBOX, ViewType.BOTH, null);
		
		ArrayList<String> list = new ArrayList<String>();
		list.add("val1");
		list.add("val2");
		list.add("val3");
		
		table.registerPropertyForView("list", "listSelected", ColumnType.LIST, ViewType.BOTH, list);
		
		primaryBox.getChildren().add(table.getRootNode());
		root.getChildren().add(primaryBox);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
	
		for (TestTableViewBinding testTableViewBinding : data) {
			System.out.println(
					  "row is selected = " + testTableViewBinding.selectedProperty().getValue()
					+ " || text = " + testTableViewBinding.getText() 
					+ " || checked = " + testTableViewBinding.getChecked()
			        + " || list selected = " + testTableViewBinding.getListSelected());
		}
	}
	
	private ObservableList<TestTableViewBinding> buildItems() {
		TestTableViewBinding binding = new TestTableViewBinding();
		binding.setText("zeile 1 - eins");
		binding.setChecked(Boolean.FALSE);
		TestTableViewBinding binding2 = new TestTableViewBinding();
		binding2.setText("zeile 1 - eins");
		binding2.setChecked(Boolean.FALSE);		
		data.addAll(binding, binding2);
		return data;
	}	
}
