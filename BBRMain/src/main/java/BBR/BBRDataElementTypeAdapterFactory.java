package BBR;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class BBRDataElementTypeAdapterFactory implements TypeAdapterFactory {

	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		
		@SuppressWarnings("unchecked")
		final Class<T> rawType = (Class<T>) type.getRawType();
		Class<?> bbrDEClass = BBRDataElement.class;
		if (!bbrDEClass.isAssignableFrom(rawType))
			return null;
		
		return  new TypeAdapter<T>() {
			final Gson embedded = new Gson();

			@Override
			public void write(JsonWriter out, T value) throws IOException {
	             if (value == null) {
		             out.nullValue();
	             } else {
	            	 try {
		            	 if (rawType.getMethod("toJson").getDeclaringClass().equals(rawType))
		            		 out.value(((BBRDataElement) value).toJson());
		            	 else
		            		 embedded.toJson(embedded.toJsonTree(value), out);
	            	 } catch (Exception ex) {
	            		 embedded.toJson(embedded.toJsonTree(value), out);
	            	 }
	             }
			}

			@Override
			public T read(JsonReader in) throws IOException {
	             if (in.peek() == JsonToken.NULL) {
		             in.nextNull();
		             return null;
		         } else {
		             return fromJson(in.nextString());
		         }
			}
		};
	}
}