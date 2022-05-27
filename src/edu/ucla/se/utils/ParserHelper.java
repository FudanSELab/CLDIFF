package edu.ucla.se.utils;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ParserHelper {
    private UnionFind uf;
    private List<Component> listOfComponent;
    public ParserHelper() {
        uf = new UnionFind();
        listOfComponent = new ArrayList<>();
    }
    public void parseLinkJson(String fileName) throws IOException, ParseException, org.json.simple.parser.ParseException {
        JSONArray ob = (JSONArray)((JSONObject) new JSONParser().parse(new FileReader(fileName))).get("links");
        JSONArray subob = (JSONArray) ((JSONObject) ob.get(0)).get("links");

        //        JsonObject jsonObject = new JsonParser().parse(fileName).getAsJsonObject();
//        Object obj = parser.parse(new FileReader());
        for (int i = 0; i < subob.size(); i++) {
            JSONObject temp = (JSONObject) subob.get(i);
            int from = ((Long) temp.get("from")).intValue();
            int to = ((Long) temp.get("to")).intValue();
            String changeType =(String) temp.get("desc");
            if (changeType.contains("Systematic")) {
                uf.add(from);
                uf.add(to);
                uf.merge(from, to);
                System.out.println(changeType);
            }

        }

        System.out.println();
    }

    public List<List<Integer>> parseDiffFile(String filename) {
        boolean flag = true;
        Component currComponent = new Component(uf);
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                List<String> list = List.of(data.split(" "));
                if (list.size() == 1) {
                    // finish reading a component
                    flag = false;
                    currComponent.calculateRoots();
                    listOfComponent.add(currComponent);
                } else if (list.get(0).equals("CLDIFFCmd.")) {
                    // start a new component
                    flag = true;
                    currComponent = new Component(uf);
                    currComponent.setFunctionName(list.get(2));
                    continue;
                }

                if (flag ) {
                    int idx = Integer.parseInt(list.get(0).
                                substring(0, list.get(0).length() - 1));
                    if (uf.father.containsKey(idx)) {
                        currComponent.addChangeIndex(idx);
                    }

                }
            }
            currComponent.calculateRoots();
            listOfComponent.add(currComponent);

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        List<List<Integer>> result = new ArrayList<>();
        for (Component c : listOfComponent) {
            result.add(new ArrayList<>(c.getRoots()));
        }
        return result;
    }



    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {

        String linkFileName = "/Users/junyu/Desktop/CLDIFF/output/JavaCLIDiff/785b5c927f4396e6b2562e617492904a7664c853/link.json";
        String groupingFileName = "/Users/junyu/Desktop/CLDIFF/src/edu/ucla/se/utils/grouping.txt";
        ParserHelper ph = new ParserHelper();
        ph.parseLinkJson(linkFileName);
        List<List<Integer>> result = ph.parseDiffFile(groupingFileName);
        System.out.println(result);
    }
}

