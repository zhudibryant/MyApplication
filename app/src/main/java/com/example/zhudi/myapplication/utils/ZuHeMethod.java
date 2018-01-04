package com.example.zhudi.myapplication.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ww on 2017/12/27.
 */

public class ZuHeMethod {

    /**
     * 组合选择（从列表中选择n个组合）
     * @param dataList 待选列表
     * @param n 选择个数
     */
    private static List<String> list;
    public static List<String> combinationSelect(String[] dataList, int n) {
        list = new ArrayList<>();
        //System.out.println(String.format("C(%d, %d) = %d", dataList.length, n, combination(dataList.length, n)));
        combinationSelect(dataList, 0, new String[n], 0);
        return list;
    }

    /**
     * 组合选择
     * @param dataList 待选列表
     * @param dataIndex 待选开始索引
     * @param resultList 前面（resultIndex-1）个的组合结果
     * @param resultIndex 选择索引，从0开始
     */
    private static void combinationSelect(String[] dataList, int dataIndex, String[] resultList, int resultIndex) {
        int resultLen = resultList.length;
        int resultCount = resultIndex + 1;
        if (resultCount > resultLen) { // 全部选择完时，输出组合结果
            StringBuffer sb = new StringBuffer();

            for (int x = 0; x < resultList.length; x++) {
                if(x==0){
                    sb.append(resultList[x]);
                }else if(x==resultList.length-1){
                    sb.append(","+resultList[x]);
                }else{
                    sb.append(","+resultList[x]);
                }

            }
            list.add(sb.toString());
            return;
        }

        // 递归选择下一个
        for (int i = dataIndex; i < dataList.length + resultCount - resultLen; i++) {
            resultList[resultIndex] = dataList[i];
            combinationSelect(dataList, i + 1, resultList, resultIndex + 1);
        }
    }

    /**
     * 计算组合数，即C(n, m) = n!/((n-m)! * m!)
     * @param n
     * @param m
     * @return
     */
    public static long combination(int n, int m) {
        return (n >= m) ? factorial(n) / factorial(n - m) / factorial(m) : 0;
    }

    /**
     * 计算阶乘数，即n! = n * (n-1) * ... * 2 * 1
     * @param n
     * @return
     */
    public static long factorial(int n) {
        return (n > 1) ? n * factorial(n - 1) : 1;
    }

}
