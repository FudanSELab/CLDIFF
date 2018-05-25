package edu.fdu.se.main.astdiff;

import com.google.gson.Gson;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件管理
 */
public class FileUtil {

    public static File createFolder(String folder) {
        File directory = new File(folder);
        directory.mkdirs();
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
        File file = new File(folder.getPath() + "/" + name.hashCode() + ".txt");
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

    /**
     * 把代码写入文件
     *
     * @param data
     */
    public static void convertCodeToFile(String[] data, File folder) {
        for (String content : data) {
            if (content.isEmpty()) {
                //过滤掉第一个空string
                continue;
            }
            String info = content.split("\r\n")[1];//标题
            String codeContent = content.substring(info.length()); //正文
            // "Content-Disposition: form-data; name=\"https://github.com/amitshekhariitbhu/Android-Debug-Database/commit/43e48d15e6ee435
            // ed0b1abc6d76638dc8bf0217d/debug-db/src/main/java/com/amitshekhar/server/RequestHandler.java\"
            //匹配name字段
            String regex = "https://github.com[a-zA-z0-9\\_\\-/.]+";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(info);
            if (matcher.find()) {
                String name = matcher.group();
                FileUtil.createFile(name, codeContent, folder);
            }
        }

    }
}
