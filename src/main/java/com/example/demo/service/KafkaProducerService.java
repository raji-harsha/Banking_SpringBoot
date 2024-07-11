package com.example.demo.service;

public interface KafkaProducerService {

    public void sendMessage(String topic, String message);
}