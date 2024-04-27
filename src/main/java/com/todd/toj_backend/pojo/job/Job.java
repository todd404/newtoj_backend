package com.todd.toj_backend.pojo.job;

import lombok.Data;

import java.util.Date;

@Data
public class Job {
    Integer id;
    Integer userId;
    String title;
    String require;
    String salary;
    Date startDate;
    Date endDate;
}
