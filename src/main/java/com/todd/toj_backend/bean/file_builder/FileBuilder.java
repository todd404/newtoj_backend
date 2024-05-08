package com.todd.toj_backend.bean.file_builder;

import com.todd.toj_backend.pojo.problem.ProblemConfig;

import java.io.IOException;

public interface FileBuilder {
    void buildFile(ProblemConfig problemConfig, String outputPath) throws IOException;
}
