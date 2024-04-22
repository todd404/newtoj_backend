package com.todd.toj_backend.pojo.course;

import lombok.Data;

@Data
public class CourseFile {
    Integer id;
    String courseId;
    String file;
    String fileType;
}
