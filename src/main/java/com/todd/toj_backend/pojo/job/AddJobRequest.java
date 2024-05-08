package com.todd.toj_backend.pojo.job;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class AddJobRequest extends Job{
    String coverFile;
}
