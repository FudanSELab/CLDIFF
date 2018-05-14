package edu.fdu.se.main.astdiff;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MyHttpServer {

    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(12007), 0);
        server.createContext("/DiffMiner/main", new TestHandler());
        server.start();
    }

    static  class TestHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            
            InputStream is = exchange.getRequestBody();
            byte[] cache = new byte[10];
            int res;
            String postString = "";
            while((res = is.read(cache))!=-1){
            	postString += (new String(cache)).substring(0,res);
            }
            String[] data = postString.split("&");
            System.out.println(postString);
            String response = postString;
            os.write(response.getBytes());
            os.close();
        }
    }
}
