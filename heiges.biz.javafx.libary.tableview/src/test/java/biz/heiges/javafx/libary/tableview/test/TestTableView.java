package biz.heiges.javafx.libary.tableview.test;

import java.util.ArrayList;
import java.util.List;

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
		
		table.addComboBoxColumn("ComboBoxList", buildComboBoxList(), "ComboBoxListProperty");
		table.addStringColumn("Spalte 1", "Field1StringProperty");
		table.addStringColumn("Spalte 2", "Field2StringProperty");
		table.addStringColumn("Spalte 3", "StringProperty");

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
					+ " - " + testTableViewBinding.getStringProperty()
					+ " - " + testTableViewBinding.getComboBoxListProperty());
		}
	}
	
	private ObservableList<TestTableViewBinding> buildItems() {
		TestTableViewBinding binding = new TestTableViewBinding();
		binding.setField1StringProperty("zeile 1 - eins");
		binding.setField2StringProperty("zweile 1 - zwei");
		binding.setStringProperty("zeile 1 - drei");
		binding.setComboBoxListProperty("val1");
		TestTableViewBinding binding2 = new TestTableViewBinding();
		binding2.setField1StringProperty("zeile 2 - eins");
		binding2.setField2StringProperty("zeile 2 - zwei");
		binding2.setStringProperty("zeile 2 - drei");
		binding2.setComboBoxListProperty("val1");
		data.addAll(binding, binding2);
		return data;
	}	
	
//	private ObservableList<String> buildComboBoxList() {
//		List<String> l = new ArrayList<String>();
//		l.add("val1");
//		l.add("val2");
//		l.add("val3");
//		return FXCollections.observableArrayList(l);
//	}
	
	private List<String> buildComboBoxList() {
		List<String> l = new ArrayList<String>();
		l.add("val1");
		l.add("val2");
		l.add("val3");
		return l;
	}
}
