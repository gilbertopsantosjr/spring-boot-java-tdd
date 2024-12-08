package com.example.rqchallenge.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GenericResponse<T> {
    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private T data;
}

