package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.TreeMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotalSellingResponse {
    private TreeMap<String, Integer> sum;
}
