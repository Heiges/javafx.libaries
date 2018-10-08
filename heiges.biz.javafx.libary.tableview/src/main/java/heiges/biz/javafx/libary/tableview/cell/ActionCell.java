package heiges.biz.javafx.libary.tableview.cell;

import heiges.biz.javafx.libary.commons.Fonts;
import heiges.biz.javafx.libary.tableview.TableViewDataModelBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * 
 * @author Hansjoachim Heiges
 *
 * @param <S>
 * @param <T>
 */
public class ActionCell<S extends TableViewDataModelBinding, T> extends TableCell<S, T> {

	private HBox actionBox = new HBox();
	
	private Button editThisRowButton = null;
	
	private Button detailViewButton = null;
	
	private Font font = null;

	public ActionCell() {
		
		font = Fonts.getFont("/fa/fontawesome-webfont.ttf", 15);

		buildDetailViewButton();

		buildEditThisRowButton();

		// Build the action box with all needed buttons.
		HBox.setMargin(detailViewButton, new Insets(0, 0, 0, 0));
		HBox.setMargin(editThisRowButton, new Insets(0, 0, 0, 0));
		actionBox.setAlignment(Pos.CENTER_LEFT);
		actionBox.getChildren().addAll(detailViewButton, editThisRowButton);

		setVisibiltyOfAllButtons(false);

		buildBehaviorForSetOnMouseEntered(actionBox);

		buildBehaviorForSetOnMouseExited(actionBox);
	}

	/**
	 * Build the editThisRow button
	 */
	private void buildEditThisRowButton() {
		Label editLabel = new Label("\uF044");
		editLabel.setFont(font);
		editThisRowButton = new Button("", editLabel);
		
		editThisRowButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("edit view");
			}
		});
		
		
		editThisRowButton.setAlignment(Pos.CENTER_LEFT);
		editThisRowButton.setStyle("-fx-background-color: transparent;");
		editThisRowButton.setPadding(Insets.EMPTY);
	}

	/**
	 * Build the detail view button
	 */
	private void  buildDetailViewButton() {
		Label detailViewLabel = new Label("\uF002");
		detailViewLabel.setFont(font);
		detailViewButton = new Button("", detailViewLabel);
		
		
		detailViewButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				System.out.println("detail view");
			}
		});
		
		
		detailViewButton.setAlignment(Pos.CENTER_LEFT);
		detailViewButton.setStyle("-fx-background-color: transparent;");
		detailViewButton.setPadding(new Insets(0,5,0,5));
	}

	private void setVisibiltyOfAllButtons(boolean visibility) {
		editThisRowButton.setVisible(visibility);
		detailViewButton.setVisible(visibility);
	}

	/**
	 * Set the visibility of all buttons when the mouse enters the cell.
	 */
	private void buildBehaviorForSetOnMouseEntered(HBox actionbox) {

		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setVisibiltyOfAllButtons(true);
			}
		});
	}

	/**
	 * Set the visibility of all buttons when the mouse leaves the cell.
	 */
	private void buildBehaviorForSetOnMouseExited(HBox actionbox) {

		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				setVisibiltyOfAllButtons(false);
			}
		});
	}

	@Override
	protected void updateItem(T item, boolean empty) {

		super.updateItem(item, empty);

		if (empty || item == null) {

			setText(null);
			setGraphic(null);
		} else {

			setText(null);
			setGraphic(actionBox);
		}
	}
}
