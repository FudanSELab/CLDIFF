package edu.fdu.se.main.astdiff;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;


public class MyHttpServer {
    static final String DIVIDER = "--xxx---------------xxx";
    static final String global_Path = "output/";

    public static void main(String[] arg) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(12007), 0);
        server.createContext("/DiffMiner/main", new TestHandler());
        server.createContext("/DiffMiner/main/fetchMetaCache", new MetaCacheHandler());
        server.createContext("/DiffMiner/main/fetchContent", new ContentHandler());
        server.start();
    }

    /**
     * 获取文件内容 link diff
     */
    static class ContentHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("ContentHandler");
            InputStream is = exchange.getRequestBody();
            OutputStream os = exchange.getResponseBody();
            byte[] cache = new byte[100];
            int res;
            StringBuilder postString = new StringBuilder();
            while ((res = is.read(cache)) != -1) {
                String a = new String(cache).substring(0, res);
                postString.append(a);
                // postString += (new String(cache)).substring(0, res);
            }
            System.out.println(postString);
            JSONObject postJson = new JSONObject(postString.toString());
            // author、commit_hash、parent_commit_hash、project_name、prev_file_path、curr_file_path
            String author = "";
            String commit_hash = postJson.getString("commit_hash");
            String parent_commit_hash = postJson.getString("parent_commit_hash");
            String project_name = postJson.getString("project_name");
            String prev_file_path = postJson.getString("prev_file_path");
            String curr_file_path = postJson.getString("curr_file_path");

            String currFileContent = FileUtil.read(global_Path+project_name + "/" + commit_hash + "/" + curr_file_path+".txt");
            String prevFileContent = FileUtil.read(global_Path+project_name + "/" + commit_hash + "/" + prev_file_path+".txt");
            String diff = "";
            String link = "";
            Content content = new Content(prevFileContent, currFileContent, diff, link);
            String contentResultStr = new Gson().toJson(content);
            exchange.sendResponseHeaders(200, contentResultStr.length());
            os.write(contentResultStr.getBytes());
            os.close();
        }
    }

    /**
     * 获取Meta缓存
     */
    static class MetaCacheHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("MetaCacheHandler");
            InputStream is = exchange.getRequestBody();
            OutputStream os = exchange.getResponseBody();
            byte[] cache = new byte[100];
            int res;
            StringBuilder postString = new StringBuilder();
            while ((res = is.read(cache)) != -1) {
                String a = new String(cache).substring(0, res);
                postString.append(a);
                // postString += (new String(cache)).substring(0, res);
            }
            System.out.println(postString);
            //获得commit_hash
            String commitHash = new JSONObject(postString.toString()).getString("commit_hash");
            String projectName = new JSONObject(postString.toString()).getString("project_name");
            //读取文件
            //文件路径为global_Path/project_name/commit_id/meta.txt
            String meta = FileUtil.read(global_Path + projectName + "/" + commitHash + "/meta.txt");
            System.out.println(meta);
            exchange.sendResponseHeaders(200, meta.length());
            os.write(meta.getBytes());
            os.close();
        }
    }

    static class TestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("TestHandler");
            OutputStream os = exchange.getResponseBody();
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
            //文件夹命名为commit_hash
            //文件名以name字段的hash值
            File folder = FileUtil.createFolder(global_Path + meta.getProject_name() + "/" + meta.getCommit_hash());
            //代码文件
            FileUtil.convertCodeToFile(data, folder, meta);
            //meta 文件
            FileUtil.createFile("meta", new Gson().toJson(meta), folder);
//            //TODO 前后分开 这部分负责response
            String response = new Gson().toJson(meta);
            exchange.sendResponseHeaders(200, response.length());
            os.write(response.getBytes());
            os.close();
        }

    }
}
