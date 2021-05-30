package heiges.biz.javafx.libary.tableview;

import java.util.ArrayList;
import java.util.List;

import biz.heiges.javafx.libary.tableview.main.ViewType;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

/**
 * A table view with additional functionality.
 * 
 * @author Hansjoachim Heiges
 * @since 0.1
 * @version 0.1
 * @param <DATA_BINDING> the data model for the items of the table view.
 */
public class TableView<DATA_BINDING extends TableViewDataModelBinding> {

	private DetailView<DATA_BINDING> detailView = null;

	private TableViewInt<DATA_BINDING> tableView = null;

	private StackPane root = null;

	/**
	 * c-tor.
	 * 
	 * @param items
	 * @param factory
	 */
	public TableView(ObservableList<DATA_BINDING> items, ItemFactory factory) {

		tableView = new TableViewInt<>(this, items, factory);

		detailView = new DetailView<>(this);

		// Build the stack pane containing the table and the details view.
		// Bind the height and width properties of the stack pane and the vertical boxes
		// of the table and details views
		root = new StackPane();
		tableView.prefHeightProperty().bind(root.heightProperty());
		tableView.prefWidthProperty().bind(root.widthProperty());
		detailView.prefHeightProperty().bind(root.heightProperty());
		detailView.prefWidthProperty().bind(root.widthProperty());

		// Add the table and the detail view to the stack pane.
		root.getChildren().addAll(detailView, tableView);
	}

	protected void changeView(Integer viewIndex) {

		ObservableList<Node> childs = root.getChildren();

		Node topNode = childs.get(1);
		Node newTopNode = childs.get(0);

		topNode.setVisible(false);
		topNode.toBack();
		newTopNode.setVisible(true);
	}

	private ArrayList<TableProperty> propertiesForDetailView = new ArrayList<TableProperty>();

	protected ArrayList<TableProperty> getPropertiesForDetailView() {
		return propertiesForDetailView;
	}

	protected ArrayList<TableProperty> getPropertiesForFiltering() {
		return propertiesForFiltering;
	}

	private ArrayList<TableProperty> propertiesForFiltering = new ArrayList<TableProperty>();

	public void registerPropertyForView(String name, String property, ColumnType type, ViewType viewType,
			List<String> comboBoxList) {

		if (ViewType.BOTH == viewType) {
			// add property as a column for viewtype == TABLE or BOTH
			tableView.addColumn(name, property, type, comboBoxList);
			propertiesForDetailView.add(new TableProperty(name, property, type, comboBoxList));
		} else if (ViewType.DETAIL == viewType) {
			// for viewtype DETAIL only add the property to the registry
			propertiesForDetailView.add(new TableProperty(name, property, type, comboBoxList));
		} else if (ViewType.TABLE == viewType) {
			tableView.addColumn(name, property, type, comboBoxList);
		}
	}

	public void registerPropertyForFiltering(String name, String property, ColumnType type, List<String> comboBoxList) {
		propertiesForFiltering.add(new TableProperty(name, property, type, comboBoxList));
	}

	public Property<Number> prefHeightProperty() {
		return root.prefHeightProperty();
	}

	public Property<Number> prefWidthProperty() {
		return root.prefWidthProperty();
	}

	public Node getRootNode() {
		return root;
	}

	protected void updateDetailView(DATA_BINDING param) {

		detailView.updateDetailView(param);
	}
}
