package com.example.ming.bluetoothcollect.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StringTool {
    //16字符串字符串转时间
    public static String getDateStringByString(String date){
        //获取年
        String year=  String.valueOf(Integer.parseInt("07"+ date.substring(2,4),16)) ;
        //获取月
        String month= String.valueOf(Integer.parseInt(date.substring(4,6),16));
        //获取日
        String day= String.valueOf(Integer.parseInt(date.substring(6,8),16));
        //获取时
        String hour= String.valueOf(Integer.parseInt(date.substring(8,10),16));
        //获取分
        String minutes= String.valueOf(Integer.parseInt(date.substring(10,12),16));
        //获取秒
        String second= String.valueOf(Integer.parseInt(date.substring(12,14),16));

        return  year+"-"+month+"-"+day+" "+hour+":"+minutes+":"+second;
    }

    //获取当前时间字符串
    public  static String getStringByNowDate(){
        Calendar calendar = Calendar.getInstance();
        //获取年
        String yearString=Integer.toHexString(calendar.get(Calendar.YEAR));
        String year= (yearString.substring(yearString.length() -2,yearString.length())) ;
        //获取月
        String month= intToHex(calendar.get(Calendar.MONTH)+1);
        //获取日
        String day=  intToHex(calendar.get(Calendar.DAY_OF_MONTH));
        //获取时
        String hour=  intToHex(calendar.get(Calendar.HOUR_OF_DAY));
        //获取分
        String minutes= intToHex(calendar.get(Calendar.MINUTE));
        //获取秒
        String second= intToHex(calendar.get(Calendar.SECOND));
        return  "02"+year+month+day+hour+minutes+second+"00";
    }



    private static String intToHex(int n) {
        StringBuffer s = new StringBuffer();
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            s = s.append(b[n%16]);
            n = n/16;
        }
        a = s.reverse().toString();
        a  = add_zore(a,2);
        return a;
    }

    public static String add_zore(String str, int size){
        if (str.length()<size){
            str= "0"+str;
            str=add_zore(str,size);
            return str;
        }else {
            return str;
        }
    }
}
