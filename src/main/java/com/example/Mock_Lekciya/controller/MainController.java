package com.example.Mock_Lekciya.controller;

import com.example.Mock_Lekciya.model.RequestDTO;
import com.example.Mock_Lekciya.model.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
//import java.util.Random;

@RestController
public class MainController {
    private Logger log = LoggerFactory.getLogger(MainController.class);
    private ObjectMapper mapper = new ObjectMapper();
    
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    private static final String RESPONSE_TOPIC = "mock-responses";
    private static final String REQUEST_TOPIC = "mock-requests";

    @PostMapping(
            value = "/info/postBalances",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxLimit;
            String currency;
            //Random random = new Random();
            BigDecimal randomBalance;

            if (firstDigit == '8'){
                maxLimit = new BigDecimal(8888);
                currency = "US";
                randomBalance = new BigDecimal("10.0")
                    .add(new BigDecimal(Math.random()).multiply(new BigDecimal("2000.0")))
                    .setScale(2, RoundingMode.HALF_UP);
            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal(9999);
                currency = "EU";
                randomBalance = new BigDecimal("10.0")
                    .add(new BigDecimal(Math.random()).multiply(new BigDecimal("1000.0")))
                    .setScale(2, RoundingMode.HALF_UP);
            } else {
                maxLimit = new BigDecimal(1111);
                currency = "RUB";
                randomBalance = new BigDecimal("10.0")
                    .add(new BigDecimal(Math.random()).multiply(new BigDecimal("10000.0")))
                    .setScale(2, RoundingMode.HALF_UP);
            }

            ResponseDTO responseDTO = new ResponseDTO(
                    requestDTO.getRqUID(),
                    clientId,
                    requestDTO.getAccount(),
                    currency,
                    randomBalance,
                    maxLimit
            );

            // 1. Логируем
            String requestJson = mapper.writeValueAsString(requestDTO);
            String responseJson = mapper.writeValueAsString(responseDTO);
            
            log.info("********** Запрос **********\n{}", requestJson);
            log.info("********** Ответ **********\n{}", responseJson);

            // 2. Отправляем в Kafka (асинхронно, чтобы не блокировать HTTP ответ)
            sendToKafkaAsync(REQUEST_TOPIC, requestDTO.getRqUID(), requestJson);
            sendToKafkaAsync(RESPONSE_TOPIC, responseDTO.getRqUID(), responseJson);

            // 3. Возвращаем HTTP ответ
            return responseDTO;

        } catch (Exception e) {
            log.error("Ошибка: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    

    private void sendToKafkaAsync(String topic, String key, String message) {
        try {
            kafkaTemplate.send(topic, key, message)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.warn("Kafka FAIL [{}]: {}", topic, ex.getMessage());
                    } else {
                        log.info("Kafka OK [{}]: {}", topic, key);
                    }
                });
        } catch (Exception e) {
            // Защита HTTP-потока
            log.warn("Kafka exception ignored: {}", e.getMessage());
        }
    }
}