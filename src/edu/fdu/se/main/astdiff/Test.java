package edu.fdu.se.main.astdiff;

import edu.fdu.se.defaultdiffminer.DiffMinerGitHubAPI;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Test {

    public static void main(String[] args) {
        String outputPath = "output/";
        String s = readMetaJson(outputPath + "/ThirdPartyLibraryAnalysis/meta.json");
        JSONObject jo = new JSONObject(s);
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
