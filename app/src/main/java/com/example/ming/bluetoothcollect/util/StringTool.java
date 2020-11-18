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
    public  static byte[] getBytesByNowDate(){
        byte[] bytes = new byte[8];
        Calendar calendar = Calendar.getInstance();
        bytes[0] = (byte) 0x02;
        //获取年
        bytes[1] = (byte) calendar.get(Calendar.YEAR);
        //获取月
        bytes[2] = (byte) (calendar.get(Calendar.MONTH)+1);
        //获取日
        bytes[3] = (byte) calendar.get(Calendar.DAY_OF_MONTH);
        //获取时
        bytes[4] = (byte) calendar.get(Calendar.HOUR_OF_DAY);
        //获取分
        bytes[5] = (byte) calendar.get(Calendar.MINUTE);
        //获取秒
        bytes[6] = (byte) calendar.get(Calendar.SECOND);
        return  bytes;
    }

    //获取获取时间字节数组
    public  static byte[] getBytesByGetDate(){
        byte[] bytes = new byte[8];
        bytes[0] = (byte) 0x01;
        return bytes;
    }

    //获取获取时间字节数组
    public  static byte[] getBytesByGetBattery(){
        byte[] bytes = new byte[8];
        bytes[0] = (byte) 0x04;
        return bytes;
    }

    //获取获取时间字节数组
    public  static byte[] getBytesBySetInterval(int dayinterval, int negihtinterval){
        byte[] bytes = new byte[20];
        bytes[6] = (byte) dayinterval;
        bytes[7] = (byte) negihtinterval;
        return bytes;
    }

    //根据bytes获取Date
    public static Double getBatteryByBytes(byte[] value){
        try {
            Double battery=Double.valueOf(byte2short(value,1));
            return battery;
        }catch (Exception e){
            return  null;
        }
    }

    //根据bytes获取Date
    public static Date getDataByBytes(byte[] value,int StarIndex){
       try {
           byte[] yearall=new byte[2];
           yearall[0]=(byte) 0x07;
           yearall[1]=value[StarIndex];
           int yyyy=byte2short(yearall,0);
           int MM=ByteInt_Single(value[StarIndex+1]);
           int dd=ByteInt_Single(value[StarIndex+2]);
           int HH=ByteInt_Single(value[StarIndex+3]);
           int mm=ByteInt_Single(value[StarIndex+4]);
           int ss=ByteInt_Single(value[StarIndex+5]);

           SimpleDateFormat simFormat = new SimpleDateFormat("yyyyMMddHHmmss");
           Date dtBeg = simFormat.parse(String.valueOf(yyyy)+add_zore(MM)+add_zore(dd)+add_zore(HH)+add_zore(mm)+add_zore(ss));
           return dtBeg;
       }catch (Exception e){
           return  null;
       }
    }

    public static String add_zore(int intstr){
        String str=String.valueOf(intstr);
        if (str.length()<2){
            str= "0"+str;
            return str;
        }else {
            return str;
        }
    }

    /**
     * 转换byte数组为short（大端）
     *
     * @return
     */
    public static short byte2short(byte[] b,int startindex){
        short l = 0;
        for (int i = 0; i < 2; i++) {
            l<<=8; //<<=和我们的 +=是一样的，意思就是 l = l << 8
            l |= (b[startindex+i] & 0xff); //和上面也是一样的  l = l | (b[i]&0xff)
        }
        return l;
    }

    /**
     * 转换byte为int (单字节)
     *
     * @return
     */
    public static int ByteInt_Single(byte byte1) {
        int iRst = (byte1 & 0xFF);
        return iRst;
    }
}
