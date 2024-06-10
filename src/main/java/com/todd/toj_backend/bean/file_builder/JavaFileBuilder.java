package com.todd.toj_backend.bean.file_builder;

import cn.hutool.core.io.FileUtil;
import com.todd.toj_backend.pojo.TypeMap;
import com.todd.toj_backend.pojo.problem.ProblemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("javaFileBuilder")
public class JavaFileBuilder implements FileBuilder{
    @Autowired
    TypeMap typeMap;

    @Value("${file-path.base-file-path}")
    String baseFilePath;

    @Override
    public void buildFile(ProblemConfig problemConfig, String outputPath) throws IOException {
        StringBuilder fileContent = new StringBuilder();
        String basePath = baseFilePath + "/java_template/";

        //import 文件
        String headerFileContent = FileUtil.readString(basePath + "import.java", StandardCharsets.UTF_8) + "\n";

        fileContent.append(headerFileContent);

        //所需代码
        fileContent.append("<code></code>\n\n");
        fileContent.append("public class Main {\n");

        //参数处理函数
        StringBuilder argFunctionContent = new StringBuilder();
        List<String> functionNames = new ArrayList<>();

        for(String arg : problemConfig.getArgumentTypeList()){
            Map<String, String> argumentFunctionMap = typeMap.getArgumentFunctionMap();

            String functionName = argumentFunctionMap.get(arg);
            functionNames.add(functionName);

            String function = FileUtil.readString(basePath + functionName + ".java", StandardCharsets.UTF_8) + "\n";
            argFunctionContent.append(function);
        }
        argFunctionContent.append("\n");
        fileContent.append(argFunctionContent);

        //返回值处理函数
        String returnType = typeMap.getJavaTypeMap().get(problemConfig.getReturnType());
        String returnFunctionName = typeMap.getReturnFunctionMap().get(problemConfig.getReturnType());
        String returnFunctionContent = FileUtil.readString(basePath + "%s.java".formatted(returnFunctionName), StandardCharsets.UTF_8)
                + "\n";

        fileContent.append(returnFunctionContent);


        //java main函数
        StringBuilder javaSolutionFunction = new StringBuilder();
        javaSolutionFunction.append("solution.%s(".formatted(problemConfig.getFunctionName()));
        for(int i  = 0; i < functionNames.size(); i++){
            javaSolutionFunction.append("%s(input_args[%d]),".formatted(functionNames.get(i), i));
        }
        javaSolutionFunction.deleteCharAt(javaSolutionFunction.length() - 1);
        javaSolutionFunction.append(")");

        String mainTemplate = FileUtil.readString(basePath + "main.java", StandardCharsets.UTF_8);
        String mainContent = mainTemplate.formatted(problemConfig.getArgumentTypeList().size(),
                returnType,
                javaSolutionFunction,
                "%s(result)".formatted(returnFunctionName));

        fileContent.append(mainContent);
        fileContent.append("\n}");

        FileUtil.writeString(fileContent.toString(), outputPath, StandardCharsets.UTF_8);
    }
}
