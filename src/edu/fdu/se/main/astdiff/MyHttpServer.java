package edu.fdu.se.main.astdiff;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import edu.fdu.se.defaultdiffminer.DiffMinerGitHubAPI;
import edu.fdu.se.fileutil.*;
import edu.fdu.se.fileutil.FileWriter;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyHttpServer {
    static final String DIVIDER = "--xxx---------------xxx";

    private static String outputPath;
    public static void main(String[] arg) throws Exception {
        outputPath = arg[0];
//        HttpServer server = HttpServer.create(new InetSocketAddress(12007), 0);
//        server.createContext("/DiffMiner/main", new TestHandler());
//        server.start();
        //test
        String s = readMetaJson(outputPath+"/spring-framework/3c1adf7f6af0dff9bda74f40dabe8cf428a62003/meta.json");
        JSONObject jo = new JSONObject(s);
        DiffMinerGitHubAPI diff = new DiffMinerGitHubAPI(outputPath, jo);
        diff.generateDiffMinerOutput();
    }
    public static String readMetaJson(String path){
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while((line = br.readLine())!=null){
                sb.append(line);
            }
            return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    static class TestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("123");
            OutputStream os = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, 1);
            InputStream is = exchange.getRequestBody();
            byte[] cache = new byte[1000 * 1024];
            int res;
            StringBuilder postString = new StringBuilder();
            while ((res = is.read(cache)) != -1) {
                String a = new String(cache).substring(0, res);
                postString.append(a);
            }
            System.out.println(postString);

            //保存为文件
            String[] data = postString.toString().split(DIVIDER);
            if (data.length <= 1) {
                return;
            }
            int size = data.length;
            //找到meta信息
//            Meta meta = FileUtil.filterMeta(data[size - 2]);
            JSONObject meta = FileUtil.filterMeta2(data[size-2]);
            //建立一个文件夹
            //文件夹命名为commit_hash
            //文件名以name字段的hash值
//            File folder = FileUtil.createFolder(outputPath + meta.getCommit_hash());
            DiffMinerGitHubAPI diff = new DiffMinerGitHubAPI(outputPath,meta);
            //meta文件
            edu.fdu.se.fileutil.FileWriter.writeInAll(diff.baseDiffMiner.mFileOutputLog.metaLinkPath+"/meta.json",meta.toString(4));
            //meta 文件
//            FileUtil.createFile("meta", new Gson().toJson(meta), folder);
            //代码文件
            File metaDir = new File(diff.baseDiffMiner.mFileOutputLog.metaLinkPath);
//todo //            FileUtil.convertCodeToFile(data, metaDir,meta);
//            DiffMinerGitHubAPI diff = new DiffMinerGitHubAPI(outputPath+meta.getCommit_hash());
//            diff.generateDiffMinerOutput();
            //读取输出文件
//            String response = postString;
//            os.write(response.getBytes());
//            os.close();
        }

    }
}
