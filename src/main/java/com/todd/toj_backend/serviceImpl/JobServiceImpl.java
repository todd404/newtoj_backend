package com.todd.toj_backend.serviceImpl;

import cn.hutool.core.io.FileUtil;
import com.todd.toj_backend.mapper.JobMapper;
import com.todd.toj_backend.pojo.job.AddJobRequest;
import com.todd.toj_backend.pojo.job.Job;
import com.todd.toj_backend.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    JobMapper jobMapper;

    @Value("${file-path.base-file-path}")
    String baseFilePath;

    @Override
    public List<Job> getUnexpiredJobList() {
        List<Job> allJobList = jobMapper.queryAllJob();
        Date now = new Date();
        List<Job> result = allJobList.stream()
                .filter((Job job)-> job.getStartDate().before(now) & job.getEndDate().after(now))
                .toList();
        return result;
    }

    @Override
    public List<Integer> getJobEnrollList(Integer userId) {
        return jobMapper.queryJobEnroll(userId);
    }

    @Override
    public List<Job> getOwnJobList(Integer userId) {
        return jobMapper.queryOwnJob(userId);
    }

    @Override
    @Transactional
    public Integer addJob(AddJobRequest job) {
        var result = jobMapper.insertJob(job);
        FileUtil.move(Paths.get(baseFilePath + "/temp/" + job.getCoverFile()),
                Paths.get(baseFilePath + "/cover/job/" + job.getId() + ".jpg"), true);

        return result;
    }

    @Override
    public Integer addJobEnroll(Integer userId, Integer jobId) {
        return jobMapper.insertJobEnroll(userId, jobId);
    }

    @Override
    public Integer removeJobEnroll(Integer userId, Integer jobId) {
        return jobMapper.deleteJobEnroll(userId, jobId);
    }
}
