package main;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import edu.fdu.se.base.common.Global;
import edu.fdu.se.cldiff.CLDiffAPI;
import edu.fdu.se.cldiff.CLDiffCore;
import edu.fdu.se.fileutil.FileUtil;
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
 * html api for CLDIFF.
 * Deploy CLDIFF-WEB and run CLDIFFServerOffline and the web visualization should be ready.
 *
 */
public class CLDIFFServerOffline {
    static final String DIVIDER = "--xxx---fdse---xxx";
    static String globalPath;
    static String repoPath;
    static String projectName;

    public static void main(String[] arg) throws Exception {
        globalPath = arg[0];
        Global.globalPath = arg[0];
        repoPath = arg[1]; // XXX/.git
        String[] data = repoPath.split("/");
        projectName = data[data.length-2];
        HttpServer server = HttpServer.create(new InetSocketAddress(8082), 0);
        server.createContext("/", new FetchMetaCacheHandler());
        server.createContext("/fetchFile", new FetchFileContentHandler());
        server.start();
    }


    /**
     * 获取文件内容 link diff
     */
    static class FetchFileContentHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("FetchFileContentHandler");
            InputStream is = exchange.getRequestBody();
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
            // author、commit_hash、parent_commit_hash、project_name、prev_file_path、curr_file_path
            String author = "";

            String commit_hash = mMap.get("commit_hash");
            String parent_commit_hash = mMap.get("parent_commit_hash");
            String project_name = mMap.get("project_name");
            String fileName = mMap.get("file_name");
            String[] fileNames = fileName.split("---");
            int id = Integer.valueOf(fileNames[0]);
            //文件路径为global_Path/project_name/commit_id/meta.json
            String metaStr = FileUtil.read(globalPath + project_name + "/" + commit_hash + "/meta.json");
            Meta meta = new Gson().fromJson(metaStr, Meta.class);
            CommitFile file = meta.getFiles().get(id);
            String action = meta.getActions().get(id);
            String curr_file_path = "";
            String prev_file_path = "";
            String currFileContent = "";
            String prevFileContent = "";
            String diff = null;
            if ("modified".equals(action)) {
                prev_file_path = file.getPrev_file_path();
                curr_file_path = file.getCurr_file_path();
                currFileContent = FileUtil.read(globalPath + project_name + "/" + commit_hash + "/" + curr_file_path);
                prevFileContent = FileUtil.read(globalPath + project_name + "/" + commit_hash + "/" + prev_file_path);
                if (!CLDiffCore.isFilter(prev_file_path)) {
                    List<CommitFile> commitFileList = meta.getFiles();
                    String diffPath = "";
                    for (CommitFile commitFile : commitFileList) {
                        if (commitFile.getCurr_file_path().equals(curr_file_path)) {
                            diffPath = commitFile.getDiffPath();
                            break;
                        }
                    }
                    diff = FileUtil.read(diffPath);
                }
            } else if ("added".equals(action)) {
                curr_file_path = file.getCurr_file_path();
                currFileContent = FileUtil.read(globalPath + project_name + "/" + commit_hash + "/" + curr_file_path);
            } else if ("deleted".equals(action)) {
                prev_file_path = file.getPrev_file_path();
                prevFileContent = FileUtil.read(globalPath + project_name + "/" + commit_hash + "/" + prev_file_path);
            }
            String link = FileUtil.read(meta.getLinkPath());
            Content content = new Content(prevFileContent, currFileContent, diff, link);
            String contentResultStr = new Gson().toJson(content);
//            System.out.println(contentResultStr);
//            System.out.println(String.valueOf(contentResultStr.length()));
            byte[] bytes = contentResultStr.getBytes();
            exchange.sendResponseHeaders(201, bytes.length);
            try (BufferedOutputStream out = new BufferedOutputStream(exchange.getResponseBody())) {
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



    /**
     * 获取Meta缓存
     */
    static class FetchMetaCacheHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("FetchMetaCacheHandler");
            InputStream is = exchange.getRequestBody();
            OutputStream os = exchange.getResponseBody();
            byte[] cache = new byte[100];
            int res;
            StringBuilder postString = new StringBuilder();
            while ((res = is.read(cache)) != -1) {
                String a = new String(cache).substring(0, res);
                postString.append(a);
            }
            System.out.println("PostContent: "+postString);
            String commitHash = postString.toString().substring(4);
            File metaFile = new File(globalPath + projectName + "/" + commitHash + "/meta.json");
            if (!metaFile.exists()) {
                //生成文件
                //文件路径为global_Path/project_name/commit_id/meta.txt
                String result = generateCLDIFFResult(commitHash,metaFile);
            }
            String meta = FileUtil.read(globalPath + projectName + "/" + commitHash + "/meta.json");
            System.out.println(meta);
            exchange.sendResponseHeaders(200, meta.length());
            os.write(meta.getBytes());
            os.close();
        }
    }

    public static String generateCLDIFFResult(String commitHash,File metaFile) {
        Meta meta = new Meta();
        //git 读取保存，生成meta
        CLDiffAPI diff = new CLDiffAPI(globalPath, meta);
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
        FileUtil.createFile(metaFile.getAbsolutePath(), new GsonBuilder().setPrettyPrinting().create().toJson(meta),new File(metaFile.getParent()));
        String response = new Gson().toJson(meta);
        return response;
    }


    static class ClearCacheHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.println("clear cache");
            Runtime runtime = Runtime.getRuntime();
//            String[] args = new String[] {"rm -rf", "/c", String.format("rm -rf %s", global_Path)};
            runtime.exec("rm -rf " + globalPath);
            OutputStream os = exchange.getResponseBody();
            String success = "SUCCESS\n";
            exchange.sendResponseHeaders(200, success.length());
            os.write(success.getBytes());
            os.close();
        }
    }

}
