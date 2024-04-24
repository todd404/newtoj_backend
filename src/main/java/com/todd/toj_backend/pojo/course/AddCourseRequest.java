package com.todd.toj_backend.pojo.course;

import lombok.Data;

@Data
public class AddCourseRequest {
    Integer id;
    String userId;
    String title;
    String coverFile;
}
