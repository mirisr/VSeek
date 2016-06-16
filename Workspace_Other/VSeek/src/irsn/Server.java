package irsn;


import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;


public class Server {

    private static final int PORT = 8080;
    private static final int MAX_WAITING_CONNECTIONS = 10; 

	 
    public static void main(final String... args) throws IOException {
        
    	
    	HttpServer server = HttpServer.create(new InetSocketAddress(PORT),MAX_WAITING_CONNECTIONS) ;
       
    	server.createContext("/search", new SearchHandler());
    	server.setExecutor(null);
    	
    	System.out.println("Starting Server on port 8080");
        server.start();
    }
    
}
