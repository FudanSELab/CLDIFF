package edu.fdu.se.net;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/10/21.
 *
 */
public class MyNetUtil {

    public static Map<String,String> parsePostedKeys(InputStream is) throws IOException {
        byte[] cache = new byte[100];
        int res;
        StringBuilder postString = new StringBuilder();
        while ((res = is.read(cache)) != -1) {
            String a = new String(cache).substring(0, res);
            postString.append(a);
        }
        System.out.println(postString);
        String[] entries = postString.toString().split("&");
        Map<String, String> mMap = new HashMap<>();

        for (String entry : entries) {
            String[] kvs = entry.split("=");
            mMap.put(kvs[0], kvs[1]);
        }
        return mMap;
    }


    public static String getPostString(InputStream is) throws IOException {
        byte[] cache = new byte[100];
        int res;
        StringBuilder postString = new StringBuilder();
        while ((res = is.read(cache)) != -1) {
            String a = new String(cache).substring(0, res);
            postString.append(a);
        }
        return postString.toString();
    }

    public static void writeResponseInBytes(OutputStream os, byte[] bytes) throws IOException{

        try (BufferedOutputStream out = new BufferedOutputStream(os)) {
            try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
                byte[] buffer = new byte[1000];
                int count;
                while ((count = bis.read(buffer)) != -1) {
                    out.write(buffer, 0, count);
                }
                out.close();
            }
        }
    }
}