package biz.heiges.javafx.libary.tableview.test;

import heiges.biz.javafx.libary.tableview.TableView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestTableView extends Application {
	
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
		table.addStringColumn("a", "Field1StringProperty");
		table.addStringColumn("b", "Field2StringProperty");
		table.addStringColumn("c", "StringProperty");

		primaryBox.getChildren().add(table);
		root.getChildren().add(primaryBox);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
	
		for (TestTableViewBinding testTableViewBinding : data) {
			System.out.println(testTableViewBinding.getField1StringProperty() 
					+ " - " + testTableViewBinding.getField2StringProperty() 
					+ " - " + testTableViewBinding.getStringProperty());			
		}
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
