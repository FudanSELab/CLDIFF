package main;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffCore;
import edu.fdu.se.cldiff.CLDiffAPI;
import edu.fdu.se.fileutil.FileUtil;
import edu.fdu.se.net.MyNetUtil;
import edu.fdu.se.server.CommitFile;
import edu.fdu.se.server.Content;
import edu.fdu.se.server.Meta;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/8/23.
 *
 * html api for CLDIFF. Only works when CLDIFF-WEB and CommitCrawler is on.
 *
 */
public class CLDiffServerOnline {
    static final String DIVIDER = "--xxx---fdse---xxx";

    public static void main(String[] arg) throws Exception {
        Global.runningMode = 2;
        Global.outputDir = arg[0];
        HttpServer server = HttpServer.create(new InetSocketAddress(12007), 0);
        server.createContext("/DiffMiner/main/genCache", new CacheGeneratorHandler());
        server.createContext("/DiffMiner/main/fetchMetaCache", new FetchMetaCacheHandler());
        server.createContext("/DiffMiner/main/fetchContent", new FetchFileContentHandler());
        server.createContext("/DiffMiner/main/clearCache",new ClearCacheHandler());
        server.start();

    }

    static class CacheGeneratorHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            System.out.println("CacheHandler");
            try {
                InputStream is = exchange.getRequestBody();
                String postString = MyNetUtil.getPostString(is);
                System.out.println(postString);
                //保存为文件
                String[] data = postString.split(DIVIDER);
                if (data.length <= 1) {
                    return;
                }
                int size = data.length;
                //找到meta信息
                Meta meta = FileUtil.filterMeta(data[size - 2]);
                //建立一个文件夹
                //文件夹命名为commit_hash
                //文件名以name字段的hash值
                File folder = FileUtil.createFolder(Global.outputDir + meta.getProject_name() + "/" + meta.getCommit_hash());
                //代码文件
                FileUtil.convertCodeToFile(data, folder, meta);
                CLDiffAPI diff = new CLDiffAPI(Global.outputDir, meta);
                diff.generateDiffMinerOutput();
                List<String> filePathList = Global.outputFilePathList;
                //diff
                int diffFileSize = filePathList.size() - 1;
                for (int i = 0; i < diffFileSize; i++) {
                    String diffPath = filePathList.get(i);
                    meta.getFiles().get(i).setDiffPath(diffPath);
                }
                //link
                meta.setLinkPath(filePathList.get(diffFileSize));
                //写入meta文件
                meta.setOutputDir(Global.outputDir);
                FileUtil.createFile("meta.json", new GsonBuilder().setPrettyPrinting().create().toJson(meta), folder);
                String response = new Gson().toJson(meta);
                System.out.println(response);
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取Meta缓存
     */
    static class FetchMetaCacheHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            System.out.println("FetchMetaCacheHandler");
            try {
                InputStream is = exchange.getRequestBody();
                String postString = MyNetUtil.getPostString(is);
                OutputStream os = exchange.getResponseBody();
                System.out.println(postString);
                //获得commit_hash
                String commitHash = new JSONObject(postString).getString("commit_hash");
                String projectName = new JSONObject(postString).getString("project_name");
                //读取文件 文件路径为global_Path/project_name/commit_id/meta.txt
                String meta = FileUtil.read(Global.outputDir + projectName + "/" + commitHash + "/meta.json");
                System.out.println(meta);
                exchange.sendResponseHeaders(200, meta.length());
                os.write(meta.getBytes());
                os.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    /**
     * 获取文件内容 link diff
     */
    static class FetchFileContentHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            System.out.println("FetchFileContentHandler");
            try {
                InputStream is = exchange.getRequestBody();
                Map<String,String> mMap = MyNetUtil.parsePostedKeys(is);
                // mMap keys: author,file_name,parent_commit_hash,project_name,commit_hash
                // author、commit_hash、parent_commit_hash、project_name、prev_file_path、curr_file_path
                String commit_hash = mMap.get("commit_hash");
                String project_name = mMap.get("project_name");
                String fileName = mMap.get("file_name");
                String[] fileNames = fileName.split("---");
                int id = Integer.valueOf(fileNames[0]);
                //文件路径为global_Path/project_name/commit_id/meta.json
                String metaStr = FileUtil.read(Global.outputDir+"/" + project_name + "/" + commit_hash + "/meta.json");
                Meta meta = new Gson().fromJson(metaStr, Meta.class);
                CommitFile file = meta.getFiles().get(id);
                String action = meta.getActions().get(id);
                String curr_file_path;
                String prev_file_path;
                String currFileContent = "";
                String prevFileContent = "";
                String diff = null;
                if ("modified".equals(action)) {
                    prev_file_path = file.getPrev_file_path();
                    curr_file_path = file.getCurr_file_path();
                    currFileContent = FileUtil.read(Global.outputDir +"/"+ project_name + "/" + commit_hash + "/" + curr_file_path);
                    prevFileContent = FileUtil.read(Global.outputDir +"/"+ project_name + "/" + commit_hash + "/" + prev_file_path);
                } else if ("added".equals(action)) {
                    curr_file_path = file.getCurr_file_path();
                    currFileContent = FileUtil.read(Global.outputDir +"/"+ project_name + "/" + commit_hash + "/" + curr_file_path);
                } else if ("deleted".equals(action)) {
                    prev_file_path = file.getPrev_file_path();
                    prevFileContent = FileUtil.read(Global.outputDir +"/"+ project_name + "/" + commit_hash + "/" + prev_file_path);
                }
                if(file.getDiffPath()!=null){
                    diff = FileUtil.read(file.getDiffPath());
                }
                String link = FileUtil.read(meta.getLinkPath());
                Content content = new Content(prevFileContent, currFileContent, diff, link);
                String contentResultStr = new Gson().toJson(content);
//              System.out.println(contentResultStr);
//              System.out.println(String.valueOf(contentResultStr.length()));
                byte[] bytes = contentResultStr.getBytes();
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                MyNetUtil.writeResponseInBytes(os,bytes);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    static class ClearCacheHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            System.out.println("clear cache");
            try {
                Runtime runtime = Runtime.getRuntime();
//            String[] args = new String[] {"rm -rf", "/c", String.format("rm -rf %s", global_Path)};
                runtime.exec("rm -rf " + Global.outputDir);
                OutputStream os = exchange.getResponseBody();
                String success = "SUCCESS\n";
                exchange.sendResponseHeaders(200, success.length());
                os.write(success.getBytes());
                os.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }





}
