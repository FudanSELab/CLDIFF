package main;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffLocal;
import edu.fdu.se.fileutil.FileUtil;
import edu.fdu.se.fileutil.PathUtil;
import edu.fdu.se.net.MyNetUtil;
import edu.fdu.se.server.CommitFile;
import edu.fdu.se.server.Content;
import edu.fdu.se.server.Meta;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/8/23.
 *
 * html api for CLDIFF.
 * Deploy CLDIFF-WEB and run CLDIFFServerOffline and the web visualization should be ready.
 *
 */
public class CLDIFFServerOffline {


    public static void main(String[] arg) throws Exception {
        Global.runningMode = 1;
        Global.outputDir = PathUtil.unifyPathSeparator(arg[0]);
        Global.repoPath = PathUtil.unifyPathSeparator(arg[1]); // XXX/.git
        String[] data = Global.repoPath.split("/");
        Global.projectName = data[data.length-2];
        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
        //传meta文件，如果没有meta，则调用生成
        server.createContext("/fetchMeta", new FetchMetaCacheHandler());
        server.createContext("/fetchFile", new FetchFileContentHandler());
        server.createContext("/clearCommitRecord",new ClearCacheHandler());
        server.start();
    }

    /**
     * 获取Meta缓存
     */
    static class FetchMetaCacheHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            System.out.println("FetchMetaCacheHandler");
            OutputStream os = exchange.getResponseBody();
            try {
                InputStream is = exchange.getRequestBody();
                String postString = MyNetUtil.getPostString(is);
                System.out.println("PostContent: " + postString);
                String commitHash = postString.substring(4);
                File metaFile = new File(Global.outputDir + "/" + Global.projectName + "/" + commitHash + "/meta.json");
                String meta = null;
                if (!metaFile.exists()) {
                    //生成文件
                    //文件路径为global_Path/project_name/commit_id/meta.txt
                    meta = generateCLDIFFResult(commitHash, metaFile, Global.outputDir);
                } else {
                    meta = FileUtil.read(Global.outputDir + "/" + Global.projectName + "/" + commitHash + "/meta.json");
                }
                System.out.println(meta);
                exchange.sendResponseHeaders(200, meta.length());
                os.write(meta.getBytes());
                os.close();
            }catch(Exception e){
                e.printStackTrace();
                try {
                    exchange.sendResponseHeaders(200, "error".length());
                    os.write("error".getBytes());
                    os.close();
                }catch (Exception e2){
                    e2.printStackTrace();
                }
            }
        }
    }

    public static String generateCLDIFFResult(String commitHash,File metaFile,String outputDir) {
        CLDiffLocal clDiffLocal = new CLDiffLocal();
        clDiffLocal.run(commitHash,Global.repoPath,outputDir);
        Meta meta =  clDiffLocal.meta;
        //git 读取保存，生成meta
//        List<String> filePathList = Global.outputFilePathList;
//        int diffFileSize = filePathList.size() - 1;
        //写入meta文件
        FileUtil.createFile("meta.json", new GsonBuilder().setPrettyPrinting().create().toJson(meta),new File(metaFile.getParent()));
        String response = new Gson().toJson(meta);
        return response;
    }


    /**
     * 获取文件内容 link diff
     */
    static class FetchFileContentHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            System.out.println("FetchFileContentHandler");
            OutputStream os = exchange.getResponseBody();
            try {
                InputStream is = exchange.getRequestBody();
                Map<String,String> mMap = MyNetUtil.parsePostedKeys(is);
                // mMap keys: author,file_name,parent_commit_hash,project_name,commit_hash
                String commit_hash = mMap.get("commit_hash");
                String project_name = mMap.get("project_name");
                String fileName = mMap.get("file_name");
                String[] fileNames = fileName.split("---");
                int id = Integer.valueOf(fileNames[0]);
                //文件路径为global_Path/project_name/commit_id/meta.json
                String metaStr = FileUtil.read(Global.outputDir + "/" + project_name + "/" + commit_hash + "/meta.json");
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
                    currFileContent = FileUtil.read(Global.outputDir + "/" + project_name + "/" + commit_hash + "/" + curr_file_path);
                    prevFileContent = FileUtil.read(Global.outputDir + "/" + project_name + "/" + commit_hash + "/" + prev_file_path);
                } else if ("added".equals(action)) {
                    curr_file_path = file.getCurr_file_path();
                    currFileContent = FileUtil.read(Global.outputDir + "/" + project_name + "/" + commit_hash + "/" + curr_file_path);
                } else if ("deleted".equals(action)) {
                    prev_file_path = file.getPrev_file_path();
                    prevFileContent = FileUtil.read(Global.outputDir + "/" + project_name + "/" + commit_hash + "/" + prev_file_path);
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
                MyNetUtil.writeResponseInBytes(os,bytes);
            }catch (Exception e){
                e.printStackTrace();
                byte[] bytes2= "error".getBytes();
                try {
                    exchange.sendResponseHeaders(200, bytes2.length);
                    MyNetUtil.writeResponseInBytes(os, bytes2);
                }catch (Exception e2){
                    e2.printStackTrace();
                }
            }
        }
    }


    static class ClearCacheHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            System.out.println("clear cache");
            try {
                File f = new File(Global.outputDir);
                f.delete();
                OutputStream outs = exchange.getResponseBody();
                String success = "SUCCESS\n";
                exchange.sendResponseHeaders(200, success.length());
                outs.write(success.getBytes());
                outs.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
