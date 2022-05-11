package it.tim.gup.common.builder;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HxxBuilder {

	@Autowired
	private PropertyLogger propertyLogger;

	public String createMsg(String propertyPrefix, Object clazz) {

		StringBuffer sb = new StringBuffer();

		Stream<HxxProperties> elenco = propertyLogger.getElencoHxxProperties().stream()
				.filter(x -> x.getKey().startsWith(propertyPrefix));

		elenco.iterator().forEachRemaining(x -> {

			String key = x.getKey(); // hxx.h80.codiceCanale
			String value = x.getValue(); // 11,5

			Integer start = Integer.valueOf(value.split(",")[0]);
			Integer end = Integer.valueOf(value.split(",")[1]);

			String nomeAttributo = key.split("\\.")[2];

			try {

				String valueFromBean = "";

				Field field = getField(clazz, nomeAttributo);
				if (field == null) {
					field = getFieldFromParent(clazz, nomeAttributo);
				}
				if (field != null) {
					field.setAccessible(true);
					valueFromBean = field.get(clazz).toString();
				}

				sb.append(addBlank(end - start, valueFromBean));
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		return sb.toString();
	}

	private String addBlank(int sizeString, String valueInput) {
		int diff = sizeString - valueInput.length();
		valueInput = String.format("%1$" + (diff + valueInput.length()) + "s", valueInput);
		return valueInput;
	}

	public Object readMsg(String msg, String propertyPrefix, Object clazz) {

		Stream<HxxProperties> elenco = propertyLogger.getElencoHxxProperties().stream()
				.filter(x -> x.getKey().startsWith(propertyPrefix));

		elenco.iterator().forEachRemaining(x -> {

			String key = x.getKey(); // hxx.h80.codiceCanale
			String value = x.getValue(); // 11,5

			Integer start = Integer.valueOf(value.split(",")[0]);
			Integer end = Integer.valueOf(value.split(",")[1]);

			try {

				String nomeAttributo = key.split("\\.")[2];
				Field field = getField(clazz, nomeAttributo);
				if (field == null) {
					field = getFieldFromParent(clazz, nomeAttributo);
				}
				if (field != null) {
					field.setAccessible(true);
					field.set(clazz, msg.substring(start, end).trim());
				}

			} catch (Throwable e) {
				e.printStackTrace();
			}

		});

		return clazz;
	}

	public Field getField(Object clazz, String fieldName) {
		Field field = null;
		try {
			field = clazz.getClass().getDeclaredField(fieldName);
		} catch (Throwable e) {
			// TODO: handle exception
		}

		return field;
	}

	public Field getFieldFromParent(Object clazz, String fieldName) {
		Field field = null;
		try {
			field = clazz.getClass().getSuperclass().getDeclaredField(fieldName);
		} catch (Throwable e) {
			// TODO: handle exception
		}

		return field;
	}

}
