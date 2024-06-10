package com.todd.toj_backend.utils.add_problem;

import cn.hutool.core.util.RandomUtil;
import com.todd.toj_backend.pojo.problem.add_problem.PerArgAutoCaseConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RandomCase {
    public static String getRandomCase(String type, PerArgAutoCaseConfig perArgAutoCaseConfig, Integer maxCaseCount, Integer currentCase){

        return switch (type) {
            case "int" -> getRandomInt(perArgAutoCaseConfig.getItemMin(), perArgAutoCaseConfig.getItemMax());
            case "int[]" ->
                    getRandomIntArrayString(perArgAutoCaseConfig.getArrayItemNumMin(), perArgAutoCaseConfig.getArrayItemNumMax(),
                            perArgAutoCaseConfig.getItemMin(), perArgAutoCaseConfig.getItemMax(),
                            maxCaseCount, currentCase);
            case "int[][]" ->
                    getRandomIntTwoDimArrayString(perArgAutoCaseConfig.getTwoDimArrayItemMin(), perArgAutoCaseConfig.getTwoDimArrayItemMax(),
                            perArgAutoCaseConfig.getArrayItemNumMin(), perArgAutoCaseConfig.getArrayItemNumMax(),
                            perArgAutoCaseConfig.getItemMin(), perArgAutoCaseConfig.getItemMax(),
                            maxCaseCount, currentCase);
            case "double" -> getRandomDouble(perArgAutoCaseConfig.getItemMin(), perArgAutoCaseConfig.getItemMax());
            case "double[]" ->
                    getRandomDoubleArrayString(perArgAutoCaseConfig.getArrayItemNumMin(), perArgAutoCaseConfig.getArrayItemNumMax(),
                            perArgAutoCaseConfig.getItemMin(), perArgAutoCaseConfig.getItemMax(),
                            maxCaseCount, currentCase);
            case "double[][]" ->
                    getRandomDoubleTwoDimArrayString(perArgAutoCaseConfig.getTwoDimArrayItemMin(), perArgAutoCaseConfig.getTwoDimArrayItemMax(),
                            perArgAutoCaseConfig.getArrayItemNumMin(), perArgAutoCaseConfig.getArrayItemNumMax(),
                            perArgAutoCaseConfig.getItemMin(), perArgAutoCaseConfig.getItemMax(),
                            maxCaseCount, currentCase);
            default -> throw new RuntimeException("获取随机用例失败：类型错误");
        };
    }

    private static String getRandomInt(int min, int max){
        return String.valueOf(RandomUtil.randomInt(min, max));
    }

    private static List<Integer> getRandomIntArray(int minItemNum, int maxItemNum,
                                            int min, int max,
                                            Integer maxCaseCount, Integer currentCase)
    {
        List<Integer> result = new ArrayList<>();
        Integer realMinItemNum = minItemNum, realMaxItemNum = maxItemNum;
        if(currentCase < maxCaseCount - 2){
            if(maxItemNum > 100){
                realMaxItemNum = 100;
            }
        }else{
            realMinItemNum = maxItemNum - 1;
        }

        Integer itemNum = RandomUtil.randomInt(realMinItemNum, realMaxItemNum);
        for(int i = 0; i < itemNum; i++){
            result.add(RandomUtil.randomInt(min, max));
        }

        return result;
    }


    private static String getRandomIntArrayString(int minItemNum, int maxItemNum,
                                                  int min, int max,
                                                  Integer maxCaseCount, Integer currentCase)
    {
        return getRandomIntArray(minItemNum, maxItemNum, min, max, maxCaseCount, currentCase).toString();
    }


    private static List<List<Integer>> getRandomIntTwoDimArray(int minTwoDimItem, int maxTwoDimItem,
                                                               int minItemNum, int maxItemNum,
                                                               int min, int max,
                                                               Integer maxCaseCount, Integer currentCase)
    {
        List<List<Integer>> result = new ArrayList<>();
        int realMinTwoDimItemNum = minTwoDimItem, realMaxTwoDimItemNum = maxTwoDimItem;
        if(currentCase < maxCaseCount - 2){
            if(maxTwoDimItem > 10){
                realMaxTwoDimItemNum = 10;
            }

            if(minItemNum == 10){
                realMaxTwoDimItemNum = minTwoDimItem + 1;
            }
        }else{
            realMinTwoDimItemNum = maxItemNum - 1;
        }

        Integer itemNum = RandomUtil.randomInt(realMinTwoDimItemNum, realMaxTwoDimItemNum);
        for(int i = 0; i < itemNum; i++){
            result.add(getRandomIntArray(minItemNum, maxItemNum, min, max, itemNum, i));
        }

        return result;
    }

    private static String getRandomIntTwoDimArrayString(int minTwoDimItem, int maxTwoDimItem,
                                                        int minItemNum, int maxItemNum,
                                                        int min, int max,
                                                        Integer maxCaseCount, Integer currentCase)
    {
        List<List<Integer>> result = getRandomIntTwoDimArray(minTwoDimItem, maxTwoDimItem, minItemNum, maxItemNum, min, max, maxCaseCount, currentCase);
        return result.toString();
    }

    private static String getRandomDouble(int min, int max){
        return String.valueOf(RandomUtil.randomDouble(min, max));
    }

    private static List<Double> getRandomDoubleArray(int minItemNum, int maxItemNum,
                                                   int min, int max,
                                                   Integer maxCaseCount, Integer currentCase)
    {
        List<Double> result = new ArrayList<>();
        Integer realMinItemNum = minItemNum, realMaxItemNum = maxItemNum;
        if(currentCase < maxCaseCount - 2){
            if(maxItemNum > 100){
                realMaxItemNum = 100;
            }
        }else{
            realMinItemNum = maxItemNum - 1;
        }

        Integer itemNum = RandomUtil.randomInt(realMinItemNum, realMaxItemNum);
        for(int i = 0; i < itemNum; i++){
            result.add(RandomUtil.randomDouble(min, max));
        }

        return result;
    }


    private static String getRandomDoubleArrayString(int minItemNum, int maxItemNum,
                                                  int min, int max,
                                                  Integer maxCaseCount, Integer currentCase)
    {
        return getRandomDoubleArray(minItemNum, maxItemNum, min, max, maxCaseCount, currentCase).toString();
    }


    private static List<List<Double>> getRandomDoubleTwoDimArray(int minTwoDimItem, int maxTwoDimItem,
                                                               int minItemNum, int maxItemNum,
                                                               int min, int max,
                                                               Integer maxCaseCount, Integer currentCase)
    {
        List<List<Double>> result = new ArrayList<>();
        int realMinTwoDimItemNum = minTwoDimItem, realMaxTwoDimItemNum = maxTwoDimItem;
        if(currentCase < maxCaseCount - 2){
            if(maxTwoDimItem > 10){
                realMaxTwoDimItemNum = 10;
            }

            if(minItemNum == 10){
                realMaxTwoDimItemNum = minTwoDimItem + 1;
            }
        }else{
            realMinTwoDimItemNum = maxItemNum - 1;
        }

        Integer itemNum = RandomUtil.randomInt(realMinTwoDimItemNum, realMaxTwoDimItemNum);
        for(int i = 0; i < itemNum; i++){
            result.add(getRandomDoubleArray(minItemNum, maxItemNum, min, max, itemNum, i));
        }

        return result;
    }

    private static String getRandomDoubleTwoDimArrayString(int minTwoDimItem, int maxTwoDimItem,
                                                        int minItemNum, int maxItemNum,
                                                        int min, int max,
                                                        Integer maxCaseCount, Integer currentCase)
    {
        List<List<Double>> result = getRandomDoubleTwoDimArray(minTwoDimItem, maxTwoDimItem, minItemNum, maxItemNum, min, max, maxCaseCount, currentCase);
        return result.toString();
    }
}
