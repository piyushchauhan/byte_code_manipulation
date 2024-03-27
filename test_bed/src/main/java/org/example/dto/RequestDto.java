package org.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
public class RequestDto {
    // Take from post_name and post_contents
    @NotBlank
    @JsonProperty("post_name")
    private String postName;

    @JsonProperty("post_contents")
    private String postContents;

}
