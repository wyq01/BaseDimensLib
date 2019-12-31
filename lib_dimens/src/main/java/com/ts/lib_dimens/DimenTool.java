package com.ts.lib_dimens;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Joe.Wang on 2017/6/30.
 */
public class DimenTool {

    private static void gen() {
        File file = new File("./lib_dimens/src/main/res/values/dimens.xml");
        int[] dimens = {
                240,
                360, 384, 392,
                400, 410, 411, 420, 432, 440, 480,
                533, 560, 592,
                600, 640, 662,
                720, 768,
                800, 811, 820,
                960, 961,
                1024,
                1280,
                1365
        };
        StringBuilder[] sbs = new StringBuilder[dimens.length];
        for (int i = 0; i < dimens.length; i++) {
            sbs[i] = new StringBuilder();
        }
        BufferedReader reader = null;
        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("</dimen>")) {
                    //tempString = tempString.replaceAll(" ", "");
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    float num = Float.valueOf(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));

                    for (int i = 0; i < dimens.length; i++) {
                        BigDecimal number = new BigDecimal(num);
                        BigDecimal result = number.multiply(new BigDecimal(dimens[i])).divide(new BigDecimal(360), 2, RoundingMode.HALF_UP);
                        sbs[i].append(start).append(result.toString()).append(end).append("\n");
                    }
                } else {
                    for (int i = 0; i < dimens.length; i++) {
                        sbs[i].append(tempString).append("\n");
                    }
                }
                line++;
            }
            reader.close();

            for (int i = 0; i < dimens.length; i++) {
                writeFile("./lib_dimens/src/main/res/values-sw" + dimens[i] + "dp", "dimens.xml", sbs[i].toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static void writeFile(String fileDir, String fileName, String text) {
        File dir = new File(fileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(fileDir, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file.getPath())));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (out != null) {
            out.close();
        }
    }

    public static void main(String[] args) {
        gen();
    }
}