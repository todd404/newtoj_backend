package com.todd.toj_backend.service;

import com.todd.toj_backend.pojo.job.AddJobRequest;
import com.todd.toj_backend.pojo.job.Job;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobService {
    List<Job> getUnexpiredJobList();
    List<Integer> getJobEnrollList(Integer userId);
    List<Job> getOwnJobList(Integer userId);
    Integer addJob(AddJobRequest job);
    Integer addJobEnroll(Integer userId, Integer jobId);
    Integer removeJobEnroll(Integer userId, Integer jobId);
}
