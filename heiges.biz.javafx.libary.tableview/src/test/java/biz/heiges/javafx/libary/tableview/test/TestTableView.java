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
		
		table.addStringColumn("text", "text");
		table.addCheckBoxColumn("checked", "checked");

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
					+ " || checked = " + testTableViewBinding.getChecked()); 					
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
	
//	private List<String> buildComboBoxList() {
//		List<String> l = new ArrayList<String>();
//		l.add("val1");
//		l.add("val2");
//		l.add("val3");
//		return l;
//	}
}
