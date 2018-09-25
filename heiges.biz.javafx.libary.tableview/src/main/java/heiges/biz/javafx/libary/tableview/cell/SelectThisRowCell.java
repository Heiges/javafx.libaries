package heiges.biz.javafx.libary.tableview.cell;

import heiges.biz.javafx.libary.tableview.TableViewDataModelBinding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class SelectThisRowCell<S extends TableViewDataModelBinding, T> extends TableCell<S, T> {

	// The horizontal box will be used for spanning the entire table cell and to
	// bind the needed mouse events
	private HBox box = new HBox();

	// the check box if this row is selected or not
	private CheckBox selectThisRowCheckBox = null;

	// the flag if a mouse is hovering over the table cell
	private Boolean isMouseStillInCell = false;

	/**
	 * A table cell with a check box that is only visible when the mouse is hovering
	 * over the table cell.
	 * 
	 * @param selectAllRows the parameter is the check box in the table header
	 *                      indicating if all rows are selected. If all rows are
	 *                      indeed selected that check box will be set to checked.
	 */
	public SelectThisRowCell() {

		// Build the selectThisRow check box
		selectThisRowCheckBox = new CheckBox();
		selectThisRowCheckBox.setAlignment(Pos.CENTER_RIGHT);

		// the check box will be wrapped in a horizontal box, therefore the style must
		// be applied to the box not to the checkbox.
		box.getStyleClass().add("check-box-table-cell");

		// Build the horizontal box with all needed buttons.
		HBox.setMargin(selectThisRowCheckBox, new Insets(2, 4, 0, 0));
		box.setAlignment(Pos.CENTER_RIGHT);
		box.getChildren().addAll(selectThisRowCheckBox);

		// default visibility is false - not visible
		selectThisRowCheckBox.setVisible(false);

		buildBehaviorForSelectThisRowPropertyChanged();

		buildBehaviorForSetOnMouseEntered(box);

		buildBehaviorForSetOnMouseExited(box);
	}

	/**
	 * Build the behavior when the selectThisRow check box has been changed. The
	 * position of the mouse pointer must be considered because the check box must
	 * stay visibility when the mouse pointer is still over the cell.
	 */
	private void buildBehaviorForSelectThisRowPropertyChanged() {

		selectThisRowCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				// Set the visibility of the selectThisRow check box. The
				// position of the mouse pointer must be considered because the
				// check box must stay visibility when the mouse pointer is
				// still over the cell.
				if (isMouseStillInCell) {
					selectThisRowCheckBox.setVisible(Boolean.valueOf(true));
				} else {
					selectThisRowCheckBox.setVisible(Boolean.valueOf(newValue));
				}

				// FIXME total falsch hier
				//updateAllRowsAreSelectedCheckBox(selectAllRows);
			}
		});
	}

//	/**
//	 * Update the allRowsAreSelected check box. Set the allRowsAreSelected to true
//	 * if all rows are selected now or to false if a single row is not selected.
//	 * 
//	 * @param selectAllRows
//	 */
//	private void updateAllRowsAreSelectedCheckBox(CheckBox selectAllRows) {
//
//		ObservableList<S> items = getTableView().getItems();
//		// FIXME use lambda for this
//		boolean allRowsAreSelected = true;
//		for (S item : items) {
//			if (item.selectedProperty().getValue() == false) {
//				allRowsAreSelected = false;
//				break;
//			}
//		}
//		selectAllRows.selectedProperty().set(allRowsAreSelected);
//	}

	/**
	 * Set the visibility of all buttons when the mouse enters the cell.
	 */
	private void buildBehaviorForSetOnMouseEntered(HBox actionbox) {

		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				isMouseStillInCell = true;
				selectThisRowCheckBox.setVisible(true);
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
				isMouseStillInCell = false;
				if (selectThisRowCheckBox.isSelected() == false)
					selectThisRowCheckBox.setVisible(false);
			}
		});
	}

	/**
	 * THE PART FROM CHECKBOXTABLECELL
	 */

	private ObservableValue<Boolean> booleanProperty;

    // --- selected state callback property
    private ObjectProperty<Callback<Integer, ObservableValue<Boolean>>>
            selectedStateCallback =
            new SimpleObjectProperty<Callback<Integer, ObservableValue<Boolean>>>(
            this, "selectedStateCallback");
	
    /**
     * Property representing the {@link Callback} that is bound to by the
     * CheckBox shown on screen.
     * @return the property representing the {@link Callback} that is bound to
     * by the CheckBox shown on screen
     */
    public final ObjectProperty<Callback<Integer, ObservableValue<Boolean>>> selectedStateCallbackProperty() {
        return selectedStateCallback;
    }

    /**
     * Sets the {@link Callback} that is bound to by the CheckBox shown on screen.
     * @param value the {@link Callback} that is bound to by the CheckBox shown
     * on screen
     */
    public final void setSelectedStateCallback(Callback<Integer, ObservableValue<Boolean>> value) {
        selectedStateCallbackProperty().set(value);
    }
	
    /**
     * Returns the {@link Callback} that is bound to by the CheckBox shown on screen.
     * @return the {@link Callback} that is bound to by the CheckBox shown on screen
     */
    public final Callback<Integer, ObservableValue<Boolean>> getSelectedStateCallback() {
        return selectedStateCallbackProperty().get();
    }

	
    private ObservableValue<?> getSelectedProperty() {
        return getSelectedStateCallback() != null ?
                getSelectedStateCallback().call(getIndex()) :
                getTableColumn().getCellObservableValue(getIndex());
    }	
	
	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	protected void updateItem(T item, boolean empty) {

		super.updateItem(item, empty);

		if (empty || item == null) {

			setText(null);
			setGraphic(null);
		} else {

			setText(null);
			setGraphic(box);

			if (booleanProperty instanceof BooleanProperty) {
				selectThisRowCheckBox.selectedProperty().unbindBidirectional((BooleanProperty) booleanProperty);
			}
			ObservableValue<?> obsValue = getSelectedProperty();
			if (obsValue instanceof BooleanProperty) {
				booleanProperty = (ObservableValue<Boolean>) obsValue;
				selectThisRowCheckBox.selectedProperty().bindBidirectional((BooleanProperty) booleanProperty);
			}

			selectThisRowCheckBox.disableProperty().bind(Bindings.not(getTableView().editableProperty()
					.and(getTableColumn().editableProperty()).and(editableProperty())));
		}
	}
}
