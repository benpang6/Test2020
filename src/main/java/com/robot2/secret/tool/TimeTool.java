package com.robot2.secret.tool;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author qiemengyan
 */
public class TimeTool {

    /**
     * 字符串转日期+时间
     */
    public static   LocalDateTime stringToDate(String time) {
        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time,format2);

    }

    /**
     * 日期+时间转字符串
     * @param date
     * @return
     */
    public static String DateToString(LocalDateTime date){
        DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //日期转字符串
        return date.format(format1);
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static long InstantNow(){
        Instant timesstamp=Instant.now();
        return timesstamp.toEpochMilli();
    }

    /**
     * 计算两个日期之间天数、月数、年数
     * @param year 将来某天的年份
     * @param month 将来某天的月份
     * @param day 将来某天
     * @param type 返回类型、1：天数；2：月数 3：年数
     * @return
     */
    public static int TimeBetweenNumber(int year,int month,int day,int type){
        LocalDate today=LocalDate.now();
        LocalDate futureDay=LocalDate.of(year,month,day);
        Period period=Period.between(today,futureDay);
        if(type==1){
            return period.getDays();
        }else if(type==2){
            return period.getMonths();
        }else{
            return period.getYears();
        }
    }

    /**
     * 把本时区的时间转换成另一个时区的时间
     * @return
     */
    public static String TimeZoneTransform(){
        ZoneId america = ZoneId.of("America/New_York");
        LocalDateTime localtDateAndTime = LocalDateTime.now();
        ZonedDateTime dateAndTimeInNewYork  = ZonedDateTime.of(localtDateAndTime, america);
        DateTimeFormatter format2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return format2.format(dateAndTimeInNewYork);
    }

    /**
     * 两个日期比较，localDate1<localDate2,返回2,localDate1>localDate2,返回1,相等返回0
     * @param localDate1
     * @param localDate2
     * @return
     */
    public static int DateCompare(LocalDate localDate1,LocalDate localDate2){
        if(localDate1.isBefore(localDate2)){
            return 2;
        }else if(localDate1.isAfter(localDate2)){
            return 1;
        }else{
            return 0;
        }
    }
    /**
     * 两个时间比较，localtime1<localDate2,返回2,localDate1>localtime2,返回1,相等返回0
     * @param localTime1
     * @param localTime2
     * @return
     */
    public static  int TimeCompare(LocalTime localTime1,LocalTime localTime2){
        if(localTime1.isBefore(localTime2)){
            return 2;
        }else if(localTime1.isAfter(localTime2)){
            return 1;
        }else{
           return 0;
        }
    }

    /**
     * 两个时间比较，localDateTime1<localDateTime2,返回2,localDateTime1>localDateTime2,返回1,相等返回0
     * @param localDateTime1
     * @param localDateTime2
     * @return
     */
    public static  int DateTimeCompare(LocalDateTime localDateTime1,LocalDateTime localDateTime2){
        if(localDateTime1.isBefore(localDateTime2)){
            return 2;
        }else if(localDateTime1.isAfter(localDateTime2)){
            return 1;
        }else{
            return 0;
        }
    }
    /**
     * 计算几年或者几个月或者几天前的日期
     * @param number 数量
     * @param type  类型，1表示年；2表示月份 3表示天
     * @param localDate 指定日期
     * @return
     */
     public static String BeforeTime(LocalDate localDate,int number,int type){
         LocalDate pervious;
         DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(type==1){
            pervious=localDate.minus(number, ChronoUnit.YEARS);
        }else if(type==2){
            pervious=localDate.minus(number,ChronoUnit.MONTHS);

        }else{
            pervious=localDate.minus(number,ChronoUnit.DAYS);
        }
         return format1.format(pervious);
     }
    /**
     * 获取当前日期、时间、日期和时间
     * @param type 类型，1表示获取日期；2表示获取时间；3表示获取日期和时间
     * @return
     */
    public static String getNowDay(int type) {
        if(type==1){
            LocalDate localDate=LocalDate.now();
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return format1.format(localDate);
        }else if(type==2){
            LocalTime localTime=LocalTime.now();
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("HH:mm:ss");
            return format1.format(localTime);
        }else{
            LocalDateTime localDateTime=LocalDateTime.now();
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return format1.format(localDateTime);
        }

    }

    /**
     * 根据时间增加年、月、日,
     * @param number 增加的数目
     * @param type 1，表示年；2.表示月；3.表示时间
     * @return
     */
    public static  String addDateTime(LocalDateTime localDateTime,int number,int type){
        if(type==1){
            LocalDateTime l1=localDateTime.plus(number, ChronoUnit.YEARS);
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return format1.format(l1);
        }else if(type==2){
            LocalDateTime l1=localDateTime.plus(number, ChronoUnit.MONTHS);
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return format1.format(l1);
        }else if(type==3){
            LocalDateTime l1=localDateTime.plus(number, ChronoUnit.DAYS);
            DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return format1.format(l1);
        }
         return null;
    }

}
