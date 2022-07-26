package com.github.NicolaiKopka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StreamingStatusDTO {

    private String movieName;
    private Map<String, Boolean> streamingServiceStatus = new HashMap<>();
    private Map<String, String> albumLinks = new HashMap<>();
    private Map<String, String> albumIds = new HashMap<>();

}
