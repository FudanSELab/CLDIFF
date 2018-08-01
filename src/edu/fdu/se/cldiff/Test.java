package edu.fdu.se.cldiff;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Test {

    public static void main(String[] args) {
        String outputPath = "output/";
//        String s = readMetaJson(outputPath + "/ThirdPartyLibraryAnalysis/meta.json");
        String s = "{\"author\": \\\"spring-projects\\\", \\\"parent_commit_hash\\\": \\\"9d63f805b3b3ad07f102f6df779b852b2d1f306c\\\", \\\"file_name\\\": \\\"0---ExecutorConfigurationSupport.java\\\", \\\"project_name\\\": \\\"spring-framework\\\", \\\"commit_hash\\\": \\\"3c1adf7f6af0dff9bda74f40dabe8cf428a62003\\\"}";
        JSONObject jo = new JSONObject(s);
        System.out.println("aaaa");
        //DiffMinerGitHubAPI diff = new DiffMinerGitHubAPI(outputPath, jo);
        //diff.generateDiffMinerOutput();
    }

    public static String readMetaJson(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
