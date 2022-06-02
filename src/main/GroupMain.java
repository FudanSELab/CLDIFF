package main;

import edu.ucla.se.utils.*;
import edu.ucla.se.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupMain {
    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {

        String linkFileName = "/Users/jiayuesun/2022/SP-2022/CLDIFF/output/JavaCLIDiff/785b5c927f4396e6b2562e617492904a7664c853/link.json";
        String groupingFileName = "/Users/jiayuesun/2022/SP-2022/CLDIFF/src/edu/ucla/se/utils/grouping.txt";
        ParserHelper ph = new ParserHelper();
        ph.parseLinkJson(linkFileName);
        List<List<Integer>> result = ph.parseDiffFile(groupingFileName, "");

        GroupLinkedDiffs linkedDiffGrouper = new GroupLinkedDiffs(result);
        HashMap<Integer, Integer> linkedStmt2Group = linkedDiffGrouper.getStmtGroupMap();

        System.out.println("Start running search...");

    }
}
