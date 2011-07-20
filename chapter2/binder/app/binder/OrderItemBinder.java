package binder;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import models.OrderItem;
import play.Logger;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

@Global
public class OrderItemBinder implements TypeBinder<OrderItem> {

	@Override
	public Object bind(String name, Annotation[] annotations, String value,
			Class actualClass) throws Exception {
		Logger.error("Args: name %s, value %s, actualClass %s ", name, value, actualClass);
		OrderItem item = new OrderItem();
		// has to format ${itemId}-${7}-{piecesIncl}
		List<String> identifier = Arrays.asList(value.split("-", 3));
		if (identifier.size() >= 3) { item.piecesIncluded = Integer.parseInt(identifier.get(2)); }
		if (identifier.size() >= 2) {
			int c = Integer.parseInt(identifier.get(1));
			item.bulk = (c & 4) == 4;
			item.hazardous = (c & 2) == 2;
			item.toxic = (c & 1) == 1;
		}
		if (identifier.size() >= 1) { item.itemId = identifier.get(0); }
		Logger.error("Created order item: %s", item);
		return item;
	}

}
