package com.todd.toj_backend.utils.add_problem;

import cn.hutool.core.util.RandomUtil;
import com.todd.toj_backend.pojo.problem.add_problem.PerArgAutoCaseConfig;

import java.util.ArrayList;
import java.util.List;

public class RandomCase {
    public static String getRandomCase(String type, PerArgAutoCaseConfig perArgAutoCaseConfig, Integer maxCaseCount, Integer currentCase){

        switch (type){
            case "int":
                return getRandomInt(perArgAutoCaseConfig.getItemMin(), perArgAutoCaseConfig.getItemMax());
            case "int[]":
                return getRandomIntArray(perArgAutoCaseConfig.getArrayItemNumMin(), perArgAutoCaseConfig.getArrayItemNumMax(),
                        perArgAutoCaseConfig.getItemMin(), perArgAutoCaseConfig.getItemMax(),
                        maxCaseCount, currentCase);
            default: throw new RuntimeException("类型错误");
        }
    }

    private static String getRandomInt(int min, int max){
        return String.valueOf(RandomUtil.randomInt(min, max));
    }

    private static String getRandomIntArray(int minItemNum, int maxItemNum, int min, int max, Integer maxCaseCount, Integer currentCase){
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

        return result.toString();
    }
}
