package edu.fdu.se.base.common;

import edu.fdu.se.server.Meta;

import java.util.List;
import java.util.Map;

/**
 * Created by huangkaifeng on 2018/4/7.
 *
 *
 */
public class Global {

    public static Meta mmeta;

    public static int changeEntityId = 0;

    public static int RQ2 = 0;

    public static String fileName;


    public static String parentCommit;

    public static List<String> outputFilePathList;


    public static Map<Integer,String> changeEntityFileNameMap;


    /**
     * input configs
     */
    public static String outputDir;
    public static String repoPath; // null in online mode
    public static String projectName;

}
