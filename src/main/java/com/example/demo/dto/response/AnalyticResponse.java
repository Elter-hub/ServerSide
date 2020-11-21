package com.example.demo.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.TreeMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyticResponse {
    private TreeMap<String, Integer> sum;
    private TreeMap<String, TreeMap<LocalDateTime, Integer>> singleItem;
}
