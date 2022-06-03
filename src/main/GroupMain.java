package main;

import edu.ucla.se.utils.*;
import edu.ucla.se.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;

public class GroupMain {
    public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException {

        String linkFileName = "./output/CLIDIFFTEST/9dfd6f7e97adf435c8d3bfe61d42b43e0b4e0713/link.json";
        String groupingFileName = "./src/edu/ucla/se/utils/grouping.txt";
        String sourceCodeFile = "A.java";
        ParserHelper ph = new ParserHelper();
        // Parse json file and generate unionfind/disjoint set
        ph.parseLinkJson(linkFileName);
        // parse function and generate function groups
        List<List<Integer>> relatedLinkGroups = ph.parseDiffFile(groupingFileName, sourceCodeFile);

        List<Integer> M1 = Arrays.asList(1, 2, 3, 4, 1, 2, 3);
        List<Integer> M2 = Arrays.asList(1, 2, 3, 1);
        List<Integer> M3 = Arrays.asList(4, 1, 1, 2, 3);
        List<List<Integer>> testLinkGroups = Arrays.asList(M1,M2,M3);

        GroupLinkedDiffs linkedDiffGrouper = new GroupLinkedDiffs(testLinkGroups);
        HashMap<Integer, Integer> linkedStmt2Group = linkedDiffGrouper.getStmtGroupMap();

        System.out.println(linkedStmt2Group);

    }
}
