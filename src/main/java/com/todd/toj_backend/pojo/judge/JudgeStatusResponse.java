package com.todd.toj_backend.pojo.judge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeStatusResponse {
    Integer code;
    String msg = "";
}
