import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Hashtable;


public class BBRParams {
	private Hashtable<String, String> parameters = new Hashtable<String, String>();
	
	private void readString(String queryString) {
		String[] params;
		String[] param;

		if (queryString == null)
			return;
		
		try {
			params = URLDecoder.decode(queryString, "UTF-8").split("&");
		} catch (UnsupportedEncodingException ex) {
			params = queryString.split("&");
		}
		
		for (int i = 0; i < params.length; i++) {
			param = params[i].split("=");
			if (param.length >= 2) {
				parameters.put(param[0], param[1]);
			} else
				if (param.length == 1) {
					parameters.put(param[0], "");
				}
		}		
	}
	
	public BBRParams(String queryString) {
		readString(queryString);
	}
	
	public BBRParams(BufferedReader reader) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) { /*report an error*/ }

		readString(jb.toString());
	}
	
	public String get(String param) {
		return parameters.get(param);
	}
	
	public int getLength() {
		return parameters.size();
	}

	public String getComplex(String param) {
		return parameters.get(param);
	}

}
