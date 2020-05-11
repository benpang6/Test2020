package com.robot2.secret.tool;

import java.io.*;

public class SecretFileTool {
    /**
     * 生成证书文件。存入一个json文件，模拟。
     */
    public synchronized void createSercet(String sercet) {
        String myDate = sercet;
        String path = "d:/sercet";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = "sercet.json";
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(path + File.separator + fileName);
            fileWriter.write(myDate);
            fileWriter.flush();
            fileWriter.close();
            //以下是将文件发送给emial
           // this.sendEmailTool.sendEmailAndFile(new File(path + File.separator + fileName));

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    /**
     * 读取证书文件,内容为json格式。测试。
     *
     * @param fileName
     * @return
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            Reader reader = new InputStreamReader(new FileInputStream("d:/sercet/sercet.txt"), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
