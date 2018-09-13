package heiges.biz.javafx.libary.tableview.cell;

import heiges.biz.javafx.libary.commons.Fonts;
import heiges.biz.javafx.libary.tableview.TableViewDataModelBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class ActionCell<S extends TableViewDataModelBinding, T> extends TableCell<S, T> {

	private HBox actionBox = new HBox();

	private CheckBox selectThisRow = null;

	private Boolean isMouseStillInCell = false;

	public ActionCell(CheckBox selectAllRows) {

		/**
		 * Build the editThisRow button
		 */
		Font font = Fonts.getFont("/fa/fontawesome-webfont.ttf", 15);
		Label editLabel = new Label("\uF044");
		editLabel.setFont(font);
		Button editThisRowButton = new Button("", editLabel);
		editThisRowButton.setAlignment(Pos.CENTER_RIGHT);
		editThisRowButton.setStyle("-fx-background-color: transparent;");

		/**
		 * Build the selectThisRow check box
		 */
		selectThisRow = new CheckBox();
		selectThisRow.setAlignment(Pos.CENTER_RIGHT);

		/**
		 * Build the action box with all needed buttons.
		 */
		HBox.setMargin(editThisRowButton, new Insets(0, 0, 0, 0));
		HBox.setMargin(selectThisRow, new Insets(0, 0, 0, 0));
		actionBox.setAlignment(Pos.CENTER_RIGHT);
		actionBox.getChildren().addAll(editThisRowButton, selectThisRow);

		/**
		 * Set all buttons to invisible.
		 */
		editThisRowButton.setVisible(false);
		selectThisRow.setVisible(false);

		buildBehaviorForSelectThisRowPropertyChanged(selectAllRows);

		buildBehaviorForSetOnMouseEntered(editThisRowButton);

		buildBehaviorForSetOnMouseExited(editThisRowButton);
	}

	/**
	 * Build the behavior when the selectThisRow check box has been changed. The
	 * position of the mouse pointer must be considered because the check box
	 * must stay visibility when the mouse pointer is still over the cell.
	 */
	private void buildBehaviorForSelectThisRowPropertyChanged(CheckBox selectAllRows) {

		selectThisRow.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				/**
				 * Update the underlying tableViewDataModelBinding with the new
				 * value.
				 */
				TableViewDataModelBinding binding = (TableViewDataModelBinding) getTableRow().getItem();
				if (binding != null) {
					binding.getSelectedProperty().setValue(selectThisRow.selectedProperty().getValue());
				}

				/**
				 * Set the visibility of the selectThisRow check box. The
				 * position of the mouse pointer must be considered because the
				 * check box must stay visibility when the mouse pointer is
				 * still over the cell.
				 */
				if (isMouseStillInCell) {
					selectThisRow.setVisible(Boolean.valueOf(true));
				} else {
					selectThisRow.setVisible(Boolean.valueOf(newValue));
				}

				updateAllRowsAreSelectedCheckBox(selectAllRows);
			}
		});
	}

	/**
	 * Update the allRowsAreSelected check box. Set the allRowsAreSelected to
	 * true if all rows are selected now or to false if a single row is not
	 * selected.
	 * 
	 * @param selectAllRows
	 */
	private void updateAllRowsAreSelectedCheckBox(CheckBox selectAllRows) {

		ObservableList<S> items = getTableView().getItems();
		// FIXME use lambda for this
		boolean allRowsAreSelected = true;
		for (S item : items) {
			if (item.getSelectedProperty().getValue() == false) {
				allRowsAreSelected = false;
				break;
			}
		}
		selectAllRows.selectedProperty().set(allRowsAreSelected);
	}

	/**
	 * Set the visibility of all buttons when the mouse enters the cell.
	 */
	private void buildBehaviorForSetOnMouseEntered(Button editThisRowButton) {

		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				isMouseStillInCell = true;
				editThisRowButton.setVisible(true);
				selectThisRow.setVisible(true);
			}
		});
	}

	/**
	 * Set the visibility of all buttons when the mouse leaves the cell.
	 */
	private void buildBehaviorForSetOnMouseExited(Button editThisRowButton) {

		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				isMouseStillInCell = false;
				editThisRowButton.setVisible(false);
				if (selectThisRow.isSelected() == false)
					selectThisRow.setVisible(false);
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

			if (item instanceof Boolean) selectThisRow.selectedProperty().set((Boolean) item);
		}
	}
}
