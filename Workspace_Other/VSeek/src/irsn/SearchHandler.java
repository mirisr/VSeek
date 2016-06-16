package irsn;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.*;


public class SearchHandler implements HttpHandler{
	
	@Override
	public void handle(HttpExchange he) throws IOException {
		
		System.out.println("inside search handler");
        
		 Map<String, Object> parameters = new HashMap<String, Object>();
         URI requestedUri = he.getRequestURI();
         String query = requestedUri.getRawQuery();

 		System.out.println(requestedUri);
		System.out.println(query);
	
		
         parseQuery(query, parameters);

         //Call Query to get search result
         String response = "";
         for (String key : parameters.keySet())
                  response += key + " = " + parameters.get(key) + "\n";
         
         
         he.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
         he.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With"); 
         he.getResponseHeaders().set("Content-Type", "text/plain");
         he.sendResponseHeaders(200, response.length());
         OutputStream os = he.getResponseBody();
         os.write(response.toString().getBytes());

         os.close();
 
 
}
	
	public void parseQuery(String query, Map<String, 
			Object> parameters) throws UnsupportedEncodingException {

		         if (query != null) {
		                 String pairs[] = query.split("[&]");
		                 for (String pair : pairs) {
		                          String param[] = pair.split("[=]");
		                          String key = null;
		                          String value = null;
		                          if (param.length > 0) {
		                          key = URLDecoder.decode(param[0], 
		                          	System.getProperty("file.encoding"));
		                          }

		                          if (param.length > 1) {
		                                   value = URLDecoder.decode(param[1], 
		                                   System.getProperty("file.encoding"));
		                          }

		                          if (parameters.containsKey(key)) {
		                                   Object obj = parameters.get(key);
		                                   if (obj instanceof List<?>) {
		                                            List<String> values = (List<String>) obj;
		                                            values.add(value);

		                                   } else if (obj instanceof String) {
		                                            List<String> values = new ArrayList<String>();
		                                            values.add((String) obj);
		                                            values.add(value);
		                                            parameters.put(key, values);
		                                   }
		                          } else {
		                                   parameters.put(key, value);
		                          }
		                 }
		         }
		}
	
}
