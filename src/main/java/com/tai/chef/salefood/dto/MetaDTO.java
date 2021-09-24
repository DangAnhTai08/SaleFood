package com.tai.chef.salefood.dto;

import com.tai.chef.salefood.constant.MetaData;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class MetaDTO {
    private int code;
    private String message;

    public MetaDTO() {
    }

    public MetaDTO(MetaData metaData) {
        this.code = metaData.getMetaCode();
        this.message = metaData.getMessage();
    }

    public MetaDTO(HttpStatus httpStatus) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
    }
}
