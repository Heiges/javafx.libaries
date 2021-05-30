package biz.heiges.javafx.libary.dialogview;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

class TextField extends HBox {

	private Label label = null;

	private javafx.scene.control.TextField textField = null;

	public TextField(String labelName, String data) {

		this.label = new Label(labelName);
		this.textField = new javafx.scene.control.TextField(data);

		this.getChildren().addAll(label, textField);
	}
}
