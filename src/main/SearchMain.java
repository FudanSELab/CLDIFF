package main;

import edu.ucla.se.GenerateRegex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SearchMain {
    public static void main(String[] args) {
        System.out.println("Start running search...");

        HashMap<String, List<Integer>> fileToLine = new HashMap<>();
        fileToLine.put("a.java", Arrays.asList(7,8));
        fileToLine.put("b.java", Arrays.asList(7,8));

        HashMap<Integer, HashMap<String, List<Integer>>> group = new HashMap<>();
        group.put(1, fileToLine);

        HashMap<Integer, String> a = new HashMap<>();
        a.put(7, "event.item = item;");
        a.put(8, "sendEvent(true, event);");

        HashMap<Integer, String> b = new HashMap<>();
        b.put(7, "event.item = item;");
        b.put(8, "sendEvent(true, ev);");

        HashMap<String, HashMap<Integer, String>> dict = new HashMap<>();
        dict.put("a.java", a);
        dict.put("b.java", b);

        GenerateRegex generator = new GenerateRegex(group, dict);
        HashMap<Integer,ArrayList<ArrayList<String>>> code = generator.getCodeSnippet();
        System.out.println(code);

    }
}
