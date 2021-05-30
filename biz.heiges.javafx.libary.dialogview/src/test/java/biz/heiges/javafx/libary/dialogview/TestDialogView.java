package biz.heiges.javafx.libary.dialogview;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestDialogView extends Application {
		
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("DialogView Test");
		primaryStage.setWidth(400);
		primaryStage.setHeight(500);
		
		Group root = new Group();
		Scene scene = new Scene(root);
		
		VBox primaryBox = new VBox();
		primaryBox.prefHeightProperty().bind(scene.heightProperty());
		primaryBox.prefWidthProperty().bind(scene.widthProperty());
		primaryBox.setPadding(new Insets(20, 20, 20, 20));
		
		primaryBox.getChildren().add(new DialogView());
		
		root.getChildren().add(primaryBox);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
	}
}
