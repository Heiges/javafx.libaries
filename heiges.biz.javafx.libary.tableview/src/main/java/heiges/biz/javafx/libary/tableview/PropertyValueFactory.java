package heiges.biz.javafx.libary.tableview;

import java.lang.reflect.Method;

import javafx.beans.NamedArg;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
public class PropertyValueFactory<S, T> implements Callback<CellDataFeatures<S, T>, ObservableValue<T>> {

	private final String property;

	/**
	 * FIXME this ugly, replace this class with some official javafx PropertyValueFactories
	 * 
	 * Creates a default PropertyValueFactory.
	 *
	 * @param property
	 *            The name of the property.
	 */
	public PropertyValueFactory(@NamedArg("property") String property) {
		this.property = property;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public ObservableValue<T> call(CellDataFeatures<S, T> param) {

		TableViewDataModelBinding binding = (TableViewDataModelBinding) param.getValue();
		
		try {
			Class<?> clazz = binding.getClass();
			Method getProptery = clazz.getMethod("get" + property);
//			getProptery.sAccessible(true);
			Object invokeGetProperty = getProptery.invoke(binding);

//			if (invokeGetProperty == null) {
//				Method setProptery = clazz.getMethod("set" + property, new Class[]{String.class});
//				setProptery.invoke(binding, "");
//				invokeGetProperty = getProptery.invoke(binding);
//			}

			if (invokeGetProperty == null) {
				throw new RuntimeException("property is null");
			}

			if (invokeGetProperty instanceof ObservableValue<?>) {
				return (ObservableValue<T>) invokeGetProperty;
			}
		} catch (Exception e) {
			// FIXME
			System.out.println(e);
		}
		return null;
	}

	/**
	 * Returns the property name provided in the constructor.
	 */
	public final String getProperty() {
		return property;
	}
}
