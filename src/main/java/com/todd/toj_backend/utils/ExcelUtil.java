package com.todd.toj_backend.utils;


import com.todd.toj_backend.pojo.problem.ProblemAnswer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public class ExcelUtil {
    public static void printChoiceProblemAnswer(Sheet table, String title, List<String> answerList){
        int lastRowNum = table.getLastRowNum();

        Row row = table.createRow(lastRowNum + 2);
        row.createCell(0).setCellValue("单选题集：" + title);
        row = table.createRow(lastRowNum + 3);
        Row answerRow = table.createRow(lastRowNum + 4);
        for(int i = 0; i < answerList.size(); i++){
            row.createCell(i).setCellValue("第" + (i + 1) + "题");
            answerRow.createCell(i).setCellValue(answerList.get(i));
        }
    }

    public static void printProgramProblemAnswer(Sheet table, String title, ProblemAnswer problemAnswer){
        int lastRowNum = table.getLastRowNum();

        Row row = table.createRow(lastRowNum + 2);
        row.createCell(0).setCellValue("编程题：" + title);

        row = table.createRow(lastRowNum + 3);
        Row answerRow = table.createRow(lastRowNum + 4);

        row.createCell(0).setCellValue("代码");
        answerRow.createCell(0).setCellValue(problemAnswer.getCode());
        row.createCell(1).setCellValue("是否通过基本用例");
        answerRow.createCell(1).setCellValue(problemAnswer.getBasicPassed() ? "是" : "否");

        for(int i = 0; i < problemAnswer.getSpecialCasePassList().size(); i++){
            row.createCell(2 + i).setCellValue("特殊用例" + (i + 1));
            answerRow.createCell(2 + i).setCellValue(problemAnswer.getSpecialCasePassList().get(i) ? "通过" : "未通过");
        }
    }
}
