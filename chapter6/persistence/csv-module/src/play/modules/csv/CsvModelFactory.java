package play.modules.csv;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Transient;

import play.Logger;
import play.db.Model;
import play.db.Model.Factory;
import play.db.Model.Property;
import play.exceptions.UnexpectedException;

public class CsvModelFactory implements Factory {

	private Class<? extends Model> clazz;

	public CsvModelFactory(Class<? extends Model> clazz) {
		this.clazz = clazz;
	}

	@Override
	public String keyName() {
		return keyField().getName();
	}

	@Override
	public Class<?> keyType() {
		return keyField().getClass();
	}

	@Override
	public Object keyValue(Model m) {
		try {
			return keyField().get(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
    private Field keyField() {
        Class c = clazz;
        try {
            while (!c.equals(Object.class)) {
                for (Field field : c.getDeclaredFields()) {
                    if (field.getName().equals("id")) {
                        field.setAccessible(true);
                        return field;
                    }
                }
                c = c.getSuperclass();
            }
        } catch (Exception e) {
            throw new UnexpectedException("No id field for class " + clazz);
        }
        throw new UnexpectedException("No id field for class " + clazz);
    }


	@Override
	public Model findById(Object id) {
		if (id == null) {
			return null;
		}

		Long longId = Long.valueOf(id.toString());
		return CsvHelper.getCsvHelper(clazz).findById(longId);
	}

	@Override
	public List<Model> fetch(int offset, int length, String orderBy,
			String orderDirection, List<String> properties, String keywords,
			String where) {
		Logger.error("Got fetch query with offset %s, limit %s, orderBy %s, orderDirection %s, properties %s, keywords %s, where %s", offset, length,
				orderBy, orderDirection, properties, keywords, where);
		// TODO: This is currently a "get all" - needs to be filtered
		Map<String, String> fieldMap = Collections.emptyMap();
		CsvQuery query = new CsvQuery(clazz, fieldMap);
		List<Model> results = query.fetch(length, offset);
		Logger.error("Got results %s", results);
		return results;
	}

	@Override
	public Long count(List<String> properties, String keywords, String where) {
		return (long) fetch(0, 0, null, null, properties, keywords, where).size();
	}

	@Override
	public void deleteAll() {
		CsvHelper.getCsvHelper(clazz).deleteAll();
	}

	@Override
	public List<Model.Property> listProperties() {
		List<Model.Property> properties = new ArrayList<Model.Property>();
		Set<Field> fields = new LinkedHashSet<Field>();
		Class<?> tclazz = clazz;
		while (!tclazz.equals(Object.class)) {
			Collections.addAll(fields, tclazz.getDeclaredFields());
			tclazz = tclazz.getSuperclass();
		}
		for (Field f : fields) {
			if (Modifier.isTransient(f.getModifiers())) {
				continue;
			}
			if (f.isAnnotationPresent(Transient.class)) {
				continue;
			}
			Model.Property mp = buildProperty(f);
			if (mp != null) {
				properties.add(mp);
			}
		}
		return properties;
	}


	/*
	 * helpers
	 */

	private Property buildProperty(final Field field) {
		Model.Property modelProperty = new Model.Property();
		modelProperty.type = field.getType();
		modelProperty.field = field;
		if (field.getName().equals("id")) {
			modelProperty.isGenerated = true;
		}
		if (Model.class.isAssignableFrom(field.getType())) {
			modelProperty.isRelation = true;
			modelProperty.relationType = field.getType();
			modelProperty.choices = new Model.Choices() {
				@SuppressWarnings("unchecked")
				public List<Object> list() {
					Map<String, String> fieldMap = Collections.emptyMap();
					CsvQuery query = new CsvQuery(field.getType(), fieldMap);
					return query.fetch();
				}
			};
		}

		/*
		// Special treatments for collections
		if (Collection.class.isAssignableFrom(field.getType())) {
			final Class<?> fieldType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
			modelProperty.isRelation = true;
			modelProperty.relationType = fieldType;
			modelProperty.isMultiple = true;
			modelProperty.choices = new Model.Choices() {
				@SuppressWarnings("unchecked")
				public List<Object> list() {
					return CsvHelper.getCsvHelper(field.getType().getClass()).findAll();
				}
			};
		}
		*/

		return modelProperty;
	}

}
