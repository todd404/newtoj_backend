package com.todd.toj_backend.utils.add_problem;

import java.util.List;

public class ConvertUtil {
    public static String intListToString(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }

        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i));
            if (i != list.size() - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    public static String intToString(Integer number){
        return number.toString();
    }
}
