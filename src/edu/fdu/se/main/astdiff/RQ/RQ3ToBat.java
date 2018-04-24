package edu.fdu.se.main.astdiff.RQ;
import edu.fdu.se.astdiff.preprocessingfile.data.FileOutputLog;

import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class RQ3ToBat {

    public ArrayList<String> fileList = new ArrayList<String>();

    public static void main(String [] args){

//        String putpath = "D:\\Workspace\\CodeDiff\\RQ3bat";

        String rootpath= getRootPath();
//        String rootpath = "D:\\Workspace\\CodeDiff\\RQ";
        String rq3path = rootpath +"\\RQ3";
        String gumtreepath = rootpath + "\\gumtree\\bin\\";

        RQ3ToBat tobat = new RQ3ToBat();
        ArrayList<String> files = tobat.getFile(new File(rq3path),".java");
        HashSet<String> pathes = new HashSet<String>();

        for (int i=0;i<files.size();i++){
            String filename = files.get(i);
            String filepath = "";
            if(filename.contains("\\curr\\")) {
                filepath = filename.substring(0, filename.lastIndexOf("curr"));
            }else if(filename.contains("\\prev\\")){
                filepath = filename.substring(0, filename.lastIndexOf("prev"));
            }
            pathes.add(filepath);
        }

        Iterator it =pathes.iterator();
        while (it.hasNext() ){
            String path = (String)it.next();
            File currdir = new File(path + "\\curr");
            File[] currfiles = currdir.listFiles();
            String repath  = path.substring(path.lastIndexOf("RQ3")+4, path.length());
            String writepath = rootpath + "\\RQ3bat\\" + repath;
            try{
                for (int i =0;i<currfiles.length;i++){
                    if (currfiles[i].isDirectory()) {
                        continue;
                    }
                    File currf1 = currfiles[i];
                    String name = currf1.getName();
                    String prevFile = path+ "prev\\" + name;
                    String currFile = currf1.getAbsolutePath();
                    String commondString = gumtreepath +"gumtree webdiff "+prevFile +" " + currFile;
                    String subname = name.substring(0, name.lastIndexOf("."));
                   // System.out.println(subname);
                    saveDataToFile(writepath,subname,commondString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


//        for (int i=0;i<files.size();i++){
//
//            String filename = files.get(i);
//            File file = new File(filename);
//            String name = file.getName();
//            //String repath  = filename.substring(filename.lastIndexOf("RQ3"), filename.length());
//
//            String filepath = "";
//            String prevfile = "";
//            String currfile = "";
//
//
////            File directory = new File(filename);
//            if(filename.contains("\\curr\\")){
//                filepath = filename.substring(0, filename.lastIndexOf("curr"));
//                prevfile = filepath + "\\pre\\" + name;
//                currfile = filename;
//
//            }else if(filename.contains("\\pre\\")){
//                filepath = filename.substring(0, filename.lastIndexOf("prev"));
//                prevfile = filename;
//                currfile = filepath + "\\curr\\" + name;
//            }
//
//           // String filepath = directory.getParent();
//            String commend = ".gumtree\\bin";
//            System.out.println(filepath);
//            //System.out.println(repath);
//        }
//        System.out.println();

    }


    public static String getRootPath() {
        URL url = RQ3ToBat.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.endsWith(".jar")) {// 可执行jar包运行的结果里包含".jar"
            // 截取路径中的jar包名
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        }

        File file = new File(filePath);

        // /If this abstract pathname is already absolute, then the pathname
        // string is simply returned as if by the getPath method. If this
        // abstract pathname is the empty abstract pathname then the pathname
        // string of the current user directory, which is named by the system
        // property user.dir, is returned.
        filePath = file.getAbsolutePath();//得到windows下的正确路径
        return filePath;
    }

//    public static List<String> getFiles(String path,String filetype) {
//        List<String> files = new ArrayList<String>();
//        File file = new File(path);
//        File[] tempList = file.listFiles();
//
//        for (int i = 0; i < tempList.length; i++) {
//            if (tempList[i].isFile()) {
////              System.out.println("文     件：" + tempList[i]);
//                if(tempList[i].toString().endsWith(filetype)) {
//                    files.add(tempList[i].toString());
//                }
//            }
//            if (tempList[i].isDirectory()) {
////              System.out.println("文件夹：" + tempList[i]);
//            }
//        }
//        return files;
//    }

    public  ArrayList<String> getFile(File path,String fileType) {
        File[] listFile = path.listFiles();
        for (File a : listFile) {
            if (a.isDirectory()) {

                // 递归调用getFile()方法
                getFile(new File(a.getAbsolutePath()),fileType);

            } else if (a.isFile()) {
                if(a.getAbsolutePath().endsWith(fileType) && !a.getAbsolutePath().contains("\\gen\\")) {
                    this.fileList.add(a.getAbsolutePath());
                }

            }

        }
        return fileList;
    }

    public static void saveDataToFile(String path,String fileName,String data) {
        BufferedWriter writer = null;
        try {
            File dirPath = new File(path);
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
        } catch (Exception e) {
            System.out.println("创建多层目录操作出错: "+e.getMessage());
            e.printStackTrace();
        }

        File file = new File(path+ fileName + ".bat");
        //如果文件不存在，则新建一个
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8"));
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("batch file success: "+fileName);
    }
}
