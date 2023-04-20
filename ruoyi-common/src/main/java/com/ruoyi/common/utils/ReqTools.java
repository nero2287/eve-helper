package com.ruoyi.common.utils;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class ReqTools {

    /**
     * 获得当前时间（date类型）
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获得当前时间（字符串类型）
     * @return
     */
    public String getCurrentStringDate() {
        Date date=new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date).toString();
    }

    /**
     * 获得当前年.月.日
     * @return
     */
    public String getCurrentYear_month_day() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH)+1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(year)+"."+String.valueOf(month)+"."+String.valueOf(day);
    }

    /**
     * 检查时间格式是否为"年.月"
     * @param date
     * @return
     */
    public boolean checkDateTime(String date) {
        boolean flag = false;
        Pattern pattern = Pattern.compile("[0-9]*");
        if(date.length()==7) {
            if(pattern.matcher(date.substring(0,4)).matches()&&pattern.matcher(date.substring(5,7)).matches()&&date.substring(4,5).equals(".")){
                flag=true;
            }
        }
        return flag;
    }

    /**
     * 获得当前的年
     * @return
     */
    public String getCurrentYear() {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        return String.valueOf(year);
    }

    /**
     * 获得当前月
     * @return
     */
    public String getCurrentMouth() {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH)+1;
        return String.valueOf(month);
    }

    /**
     * 获得当前日
     * @return
     */
    public String getCurrentDay() {
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(day);
    }

    /**
     * 检查值是否为空,不为空为true
     * @return
     */
    public boolean checkEmpty(Object obj) {
        boolean flag = false;
        if(obj!=null) {
            if(obj instanceof ArrayList<?>) {
                if(!((List<?>) obj).isEmpty()) {
                    flag = true;
                }
            }else if(obj instanceof Integer){
                if((int)obj!=0) {
                    flag = true;
                }
            }else if(obj instanceof String) {
                if(obj!="") {
                    if(obj.toString().length()>0) {
                        flag = true;
                    }
                }
            }else {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获得指定位数的随机数
     * @return
     */
    public String getRandomNumber(int num){
        StringBuffer str = new StringBuffer();
        for(int i=0;i<num;i++){
            if(i==0){
                str.append(String.valueOf((int)(1+Math.random()*10)));
                i++;
            }else{
                str.append(String.valueOf((int)(Math.random()*10)));
            }
        }
        return str.toString();
    }

    /**
     * 分割集合，将一个list分割成List<List<?>>的形式，n为list<?>数量
     * @param list
     * @param n
     * @return
     */
    public List<List<?>> subList(List<?> list, int n){
        int remainder = list.size() % n;
        int size  = (list.size() /n);
        List<List<?>> typeListList = new ArrayList<List<?>>();
        for(int i=0;i<size;i++){
            List<?> subList = list.subList(i*n,(i+1)*n);
            typeListList.add(subList);
        }
        if(remainder >0){
            List<?> subList = list.subList(size*n,size*n+remainder);
            typeListList.add(subList);
        }
        return typeListList;
    }

    /**
     * 将Object的集合形式转化为数组
     * @param obj
     * @return
     */
    public List<T> objectToList(Object obj){
        List<T> list = new ArrayList<T>();
        if(obj instanceof ArrayList<?>){
            for (Object o : (List<T>) obj) {
                list.add((T)o);
            }
        }
        return list;
    }
}
