package com.todd.toj_backend.bean.file_builder;

import cn.hutool.core.io.FileUtil;
import com.todd.toj_backend.pojo.TypeMap;
import com.todd.toj_backend.pojo.problem.ProblemConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@Component("cppFileBuilder")
public class CppFileBuilder implements FileBuilder {
    @Autowired
    TypeMap typeMap;

    public void buildFile(ProblemConfig problemConfig, String outputPath) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        String basePath = "D:\\toj_files\\cpp_template\\";

        //头文件
        String headerFileContent = readFileToString(basePath + "headers.h") + "\n";

        fileContent.append(headerFileContent);
        fileContent.append("using namespace std;\n");

        //参数处理函数
        StringBuilder argFunctionContent = new StringBuilder();
        List<String> functionNames = new ArrayList<>();

        for(String arg : problemConfig.getArgumentTypeList()){
            Map<String, String> argumentFunctionMap = typeMap.getArgumentFunctionMap();

            String functionName = argumentFunctionMap.get(arg);
            functionNames.add(functionName);

            String function = readFileToString(basePath + functionName + ".cpp") + "\n";
            argFunctionContent.append(function);
        }
        argFunctionContent.append("\n");

        fileContent.append(argFunctionContent);

        //返回值处理函数
        String returnType = typeMap.getCppTypeMap().get(problemConfig.getReturnType());
        String returnFunctionName = typeMap.getReturnFunctionMap().get(problemConfig.getReturnType());
        String returnFunctionContent = readFileToString(basePath + "%s.cpp".formatted(returnFunctionName)) + "\n";

        fileContent.append(returnFunctionContent);

        fileContent.append("\n<code></code>\n");

        //cpp main函数
        StringBuilder cppSolutionFunction = new StringBuilder();
        cppSolutionFunction.append("solution.%s(".formatted(problemConfig.getFunctionName()));
        for(int i  = 0; i < functionNames.size(); i++){
            cppSolutionFunction.append("%s(input_args[%d]),".formatted(functionNames.get(i), i));
        }
        cppSolutionFunction.deleteCharAt(cppSolutionFunction.length() - 1);
        cppSolutionFunction.append(")");

        String mainTemplate = readFileToString(basePath + "main.cpp");
        String mainContent = mainTemplate.formatted(problemConfig.getArgumentTypeList().size(),
                returnType,
                cppSolutionFunction,
                "%s(result)".formatted(returnFunctionName));

        fileContent.append(mainContent);

        outputFile(fileContent.toString(), outputPath);
    }

    private String readFileToString(String path){
        String result = "";

        try {
            result = Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private void outputFile(String content, String path) throws IOException {
        FileUtil.writeUtf8String(content, path);
    }

}
