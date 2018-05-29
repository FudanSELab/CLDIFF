package edu.fdu.se.main.astdiff;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

public class MyHttpServer {
    static final String DIVIDER = "--xxx---------------xxx";

    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(12007), 0);
        server.createContext("/DiffMiner/main", new TestHandler());
        server.start();
    }

    static class TestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("123");
            String output = "output/";
            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, 1);
            InputStream is = exchange.getRequestBody();
            byte[] cache = new byte[1000 * 1024];
            int res;
            StringBuilder postString = new StringBuilder();
            while ((res = is.read(cache)) != -1) {
                //todo
                //这里字符串拼接最好改成StringBuilder拼接，在循环里做str+str操作可能会有内存问题
                String a = new String(cache).substring(0, res);
                postString.append(a);
                // postString += (new String(cache)).substring(0, res);
            }
            System.out.println(postString);

            //保存为文件
            String[] data = postString.toString().split(DIVIDER);
            if (data.length <= 1) {
                return;
            }
            int size = data.length;
            //找到meta信息
            Meta meta = FileUtil.filterMeta(data[size - 2]);
            //建立一个文件夹
            //文件夹命名为project_name/commit_hash
            //文件名以name字段的hash值
            File folder = FileUtil.createFolder(output + meta.getProject_name() + "/" + meta.getCommit_hash());
            //根据parent commit id创建文件夹
//            List<String> parentCommits = meta.getParents();
//            for (String parent : parentCommits) {
//                FileUtil.createFolder(folder.getPath() + "/" + parent + "/prev/");
//                FileUtil.createFolder(folder.getPath() + "/" + parent + "/curr/");
//            }
            //代码文件
            FileUtil.convertCodeToFile(data, folder, meta);
            //meta 文件
            FileUtil.createFile("meta", new Gson().toJson(meta), folder);
//            //TODO 前后分开 这部分负责response
//            String response = postString;
//            os.write(response.getBytes());
//            os.close();
        }

    }
}
