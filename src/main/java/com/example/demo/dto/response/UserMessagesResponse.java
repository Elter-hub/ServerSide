package com.example.demo.dto.response;

import com.example.demo.models.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserMessagesResponse {
    private List<Message> message;
}
