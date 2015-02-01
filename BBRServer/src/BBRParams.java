import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Hashtable;


public class BBRParams {
	private Hashtable<String, String> parameters = new Hashtable<String, String>();
		
	public BBRParams(String queryString) {
		String[] params;
		String[] param;

		try {
			params = URLDecoder.decode(queryString, "UTF-8").split("&");
		} catch (UnsupportedEncodingException ex) {
			params = queryString.split("&");
		}
		
		for (int i = 0; i < params.length; i++) {
			param = params[i].split("=");
			parameters.put(param[0], param[1]);
		}
	}
	
	public String get(String param) {
		return parameters.get(param);
	}
}
