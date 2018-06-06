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
        File file = new File(folder.getPath() + "/" + name);
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
                w.flush();
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

    public static JSONObject filterMeta2(String datum) {
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
            String[] infos = content.split("\r\n");//标题
            if (infos.length < 2) {
                return;
            }
            String info = infos[1];
            String codeContent = content.substring(info.length() + "\r\n".length() * 3);
            codeContent = codeContent.substring(0, codeContent.length() - 1);//正文
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
                if (fileName.startsWith("AbstractSchedulingTaskExecutorTests")) {
                    int a = 1;
                }
                path = path.substring(0, path.length() - fileName.length());
                if (path.startsWith("/...")) {
                    path = "/" + path.substring(4);
                }
                //去除最后的引号
                fileName = fileName.substring(0, fileName.length() - 1);

                //如果是parentcommit,只需要在project_name/commit_id/prev/parent_commit_id/src/xx/xx下新建
                //如果是childCommit，需要在所有的commit_id/curr/parent_commit_id/src/xx/xx新建

                boolean isParent = !commitId.equals(meta.getCommit_hash());
                String filePath = "";
                CommitFile commitFile;
                if (isParent) {
                    //如果是parentcommit,只需要在project_name/commit_id/prev/parent_commit_id/src/xx/xx下新建
                    filePath = folder.getPath() + "/prev/" + commitId + "/" + path;
                    //如果是parent,需要将文件名形式A.java____parent0改为A.java
                    fileName = fileName.split(PARENT_DIVIDER)[0];
                    //先创建文件
                    File directory = FileUtil.createFolder(filePath);
                    FileUtil.createFile(fileName, codeContent, directory);
                } else {
                    //如果是childCommit，需要在所有的commit_id/curr/parent_commit_id/src/xx/xx新建
                    List<String> parentCommitIds = meta.getParents();
                    for (String parentCommitId : parentCommitIds) {
                        filePath = folder.getPath() + "/curr/" + parentCommitId + "/" + path;
                        File directory = FileUtil.createFolder(filePath);
                        FileUtil.createFile(fileName, codeContent, directory);

                        //添加到Meta中
                        commitFile = new CommitFile();
                        commitFile.setFile_name(fileName);
                        commitFile.setParent_commit(parentCommitId);
                        commitFile.setCurr_file_path("curr/" + parentCommitId + path + fileName);
                        commitFile.setPrev_file_path("prev/" + parentCommitId + path + fileName);
                        meta.addFile(commitFile);
                    }
                }
            }
        }
    }

    public static String read(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));

            String tempString = "";
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                result.append(tempString).append("\r\n");
                line++;
            }
            result = new StringBuilder(result.substring(0,result.length()-2));
            System.out.println(result);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    return result.toString();
                } catch (IOException e1) {
                }
            }
        }

        return null;
    }
}
