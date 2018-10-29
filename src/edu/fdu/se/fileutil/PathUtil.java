package edu.fdu.se.fileutil;

/**
 * Created by huangkaifeng on 2018/8/24.
 *
 */
public class PathUtil {

    public static String getGitProjectNameFromGitFullPath(String path){
        if(path.contains("\\")){
            path = path.replace('\\','/');
        }
        if(path.endsWith("/.git")){
            String[] data = path.split("/");
            return data[data.length-2];
        }
        return null;
    }

    /**
     * replacing "\\" with "/"
     *
     * /foo/bar/  C:\\foo\\bar  C:\\foot\\bar/foo/bar
     * @param path
     * @return
     */
    public static String unifyPathSeparator(String path){
        return path.replace('\\','/');
    }
}
