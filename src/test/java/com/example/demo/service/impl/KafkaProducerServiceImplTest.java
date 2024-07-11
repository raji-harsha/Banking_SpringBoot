package com.example.demo.service.impl;

import com.example.demo.service.KafkaProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;

class KafkaProducerServiceImplTest {

    @InjectMocks
    private KafkaProducerServiceImpl kafkaProducerService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void sendMessage() {
        kafkaProducerService.sendMessage("topic", "message");
    }
}