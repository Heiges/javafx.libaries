package heiges.biz.javafx.libary.tableview;

/**
 * Factory for building the data for a new line
 * 
 * @author Hansjoachim Heiges
 *
 */
public interface ItemFactory {

	/**
	 * return the datamodel of a line of the table
	 * @return
	 */
	public TableViewDataModelBinding build();
}
