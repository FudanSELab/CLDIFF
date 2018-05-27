package edu.fdu.se.main.astdiff;

import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件管理
 */
public class FileUtil {

    static final String PARENT_DIVIDER = "----";

    public static File createFolder(String folder) {
        File directory = new File(folder);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    /**
     * 创建文件
     *
     * @param name
     * @param codeContent
     * @param folder
     */
    public static void createFile(String name, String codeContent, File folder) {
        File file = new File(folder.getPath() + "/" + name + ".txt");
        FileOutputStream is = null;
        try {
            is = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter osw = new OutputStreamWriter(is);
        Writer w = new BufferedWriter(osw);
        try {
            w.write(codeContent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                w.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 解析meta信息
     *
     * @param datum
     * @return
     */
    public static Meta filterMeta(String datum) {
        //{"autho
        String[] metaInfo = datum.split("\r\n");
        //匹配
        for (String meta : metaInfo) {
            if (meta.startsWith("{\"")) {
                //找到当前meta信息
                Meta metaObj = new Gson().fromJson(meta, Meta.class);
                return metaObj;
            }
        }
        return null;
    }

    public static JSONObject filterMeta2(String datum){
        //{"autho
        String[] metaInfo = datum.split("\r\n");
        //匹配
        for (String meta : metaInfo) {
            if (meta.startsWith("{\"")) {
                //找到当前meta信息
                return new JSONObject(meta);
            }
        }
        return null;
    }

    /**
     * 把代码写入文件
     *
     * @param data
     * @param meta
     */
    public static void convertCodeToFile(String[] data, File folder, Meta meta) {
        for (String content : data) {
            if (content.isEmpty()) {
                //过滤掉第一个空string
                continue;
            }
            String info = content.split("\r\n")[1];//标题
            String codeContent = content.substring(info.length()); //正文
            // "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android
            // -Debug-Database/commit/43e48d15e6ee435
            // ed0b1abc6d76638dc8bf0217d/debug-db/src/main/java/com/amitshekhar/server/RequestHandler.java\"
            //匹配name字段
            String regex = "commit/[\\s\\S]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(info);
            if (matcher.find()) {
                //name: commit/commit_id/src/xxx/xxx/A.java
                String fileInfo = matcher.group();
                String commitId = fileInfo.split("/")[1];
                String path = fileInfo.substring(("commit/").length() + commitId.length());
                String[] pathList = path.split("/");
                String fileName = pathList[pathList.length - 1];
                path = path.substring(0, path.length() - fileName.length());
                //去除最后的引号
                fileName = fileName.substring(0, fileName.length() - 1);
                //如果是parentcommit,只需要在commit_id/parent_commit_id/prev/下新建
                //如果是childCommit，需要在所有的commit_id/parent_commit/curr/新建

                boolean isParent = !commitId.equals(meta.getCommit_hash());
                String filePath = "";
                if (isParent) {
                    //如果是parentcommit,只需要在commit_id/parent_commit_id/prev/下新建
                    filePath = folder.getPath() + "/" + commitId + "/prev/" + path;
                    //如果是parent,需要将文件名形式A.java____parent0改为A.java
                    fileName = fileName.split(PARENT_DIVIDER)[0];
                    File directory = FileUtil.createFolder(filePath);
                    FileUtil.createFile(fileName, codeContent, directory);
                } else {
                    //如果是childCommit，需要在所有的commit_id/parent_commit_id/curr/新建
                    List<String> parentCommitIds = meta.getParents();
                    for (String parentCommitId : parentCommitIds) {
                        filePath = folder.getPath() + "/" + parentCommitId + "/curr/" + path;
                        File directory = FileUtil.createFolder(filePath);
                        FileUtil.createFile(fileName, codeContent, directory);
                    }
                    // FileUtil.createFile(fileName, codeContent, folder, filePath);

                }
            }
        }

    }
}
