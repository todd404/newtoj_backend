package com.todd.toj_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.todd.toj_backend.pojo.run.RunRequest;
import com.todd.toj_backend.pojo.run.RunStatusResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface RunService {
    String runForResult(RunRequest runRequest) throws IOException;
    RunStatusResponse getRunStatus(String uuid) throws JsonProcessingException;
}
