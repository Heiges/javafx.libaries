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
import javafx.util.Callback;

/**
 * 
 * @author Hansjoachim Heiges
 *
 * @param <DATA_BINDING> The type of the TableView generic type (i.e. S == TableView&lt;S&gt;).
 *           This should also match with the first generic type in TableColumn.
 * @param <TYPE_OF_CELL_ITEM> The type of the item contained within the Cell.
 *
 */
public class ActionCell<DATA_BINDING extends TableViewDataModelBinding, TYPE_OF_CELL_ITEM> extends TableCell<DATA_BINDING, TYPE_OF_CELL_ITEM> {

	private HBox actionBox = new HBox();
	
	private Button editThisRowButton = null;
	
	private Button detailViewButton = null;
	
	private Font font = null;
	
	// FIXME Better parameters for the callback
	private Callback<DATA_BINDING, String> callBackForEditView = null;
	// FIXME Better parameters for the callback	
	private Callback<DATA_BINDING, String> callBackForDetailView = null;
	
	/**
	 * standard c-tor.
	 */
	public ActionCell(Callback<DATA_BINDING, String> edit, Callback<DATA_BINDING, String> detail) {
		
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
		
		setBehaviorForDetailView(detail);
		
		setBehaviorForEditView(edit);
	}

	
	public void setBehaviorForDetailView(Callback<DATA_BINDING, String> value) {
		this.callBackForDetailView = value;
	}
	
	public void setBehaviorForEditView(Callback<DATA_BINDING, String> value) {
		this.callBackForEditView = value;
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
				callBackForEditView.call(getTableRow().getItem());
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
				callBackForDetailView.call(getTableRow().getItem());
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
	protected void updateItem(TYPE_OF_CELL_ITEM item, boolean empty) {

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
