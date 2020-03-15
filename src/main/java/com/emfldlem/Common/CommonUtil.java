package com.emfldlem.Common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;

public class CommonUtil {

    public static int readFileId(File file) {

        StringBuilder strText = new StringBuilder();
        int nBuffer;
        try {
            BufferedReader buffRead = new BufferedReader(new FileReader(file));
            while ((nBuffer = buffRead.read()) != -1) {
                strText.append((char) nBuffer);
            }
            buffRead.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("====================" + strText);
        return Integer.parseInt(strText.toString());
    }


    public static String getDayOfWeek() {

        Calendar cal = Calendar.getInstance();

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        String enDayOfWeek = "";
        switch (dayOfWeek) {
            case 1:
                enDayOfWeek = "sun";
                break;
            case 2:
                enDayOfWeek = "mon";
                break;
            case 3:
                enDayOfWeek = "tue";
                break;
            case 4:
                enDayOfWeek = "wed";
                break;
            case 5:
                enDayOfWeek = "thu";
                break;
            case 6:
                enDayOfWeek = "fri";
                break;
            case 7:
                enDayOfWeek = "sat";
                break;
        }
        return enDayOfWeek;
    }
}
