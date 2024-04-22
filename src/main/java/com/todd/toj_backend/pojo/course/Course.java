package com.todd.toj_backend.pojo.course;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class Course {
    Integer id;
    String userId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String nickname;
    String title;
}
