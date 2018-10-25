package heiges.biz.javafx.libary.tableview;

/**
 * Factory for building the data for a new line
 * 
 * @author Hansjoachim Heiges
 * @since 0.1
 * @version 0.1
 */
public interface ItemFactory {

	/**
	 * return the datamodel of a line of the table
	 * @return
	 */
	public TableViewDataModelBinding build();
}
/**
 * @author Hansjoachim Heiges

 */