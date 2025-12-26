package com.example.Mock_Lekciya.controller;

import com.example.Mock_Lekciya.model.RequestDTO;
import com.example.Mock_Lekciya.model.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@RestController
public class MainController {
    private Logger log = LoggerFactory.getLogger(MainController.class);
    private ObjectMapper mapper = new ObjectMapper();

    //Можно разные методы заглушек использовать:
    //@GetMapping
    //@PutMapping
    //@DeleteMapping
    @PostMapping(
            value = "/info/postBalances",                       //путь до нашей заглушки
            produces = MediaType.APPLICATION_JSON_VALUE,        //тип отправляемых данных
            consumes = MediaType.APPLICATION_JSON_VALUE         //тип получаемых данных
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO){  //функция для приема и обработки запроса
        try {
            String clientId = requestDTO.getClientId();         //получаем строку с номером клиента из запроса
            char firstDigit = clientId.charAt(0);               //Находим первый символ в строке номера клиента
            BigDecimal maxLimit;                                //устанавливаем переменную для максимального лимита
            String currency;
            Random random = new Random();
            BigDecimal randomBalance;

            //Эмулируем (преобразуем) необходимые данные:
            if (firstDigit == '8'){
                maxLimit = new BigDecimal(2000);
                currency = new String("US");
                //Создаем рандомный BigDecimal balance
                randomBalance = new BigDecimal("10.0").add(new BigDecimal(Math.random()).multiply(new BigDecimal("2000.0"))).setScale(2, RoundingMode.HALF_UP);
            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal(1000);
                currency = new String("EU");
                randomBalance = new BigDecimal("10.0").add(new BigDecimal(Math.random()).multiply(new BigDecimal("1000.0"))).setScale(2, RoundingMode.HALF_UP);
            } else {
                maxLimit = new BigDecimal(10000);
                currency = new String("RUB");
                randomBalance = new BigDecimal("10.0").add(new BigDecimal(Math.random()).multiply(new BigDecimal("10000.0"))).setScale(2, RoundingMode.HALF_UP);
            }

            //После эмуляции данных передаем ответ:

  /*        //Первый вариант конструктора
            ResponseDTO responseDTO = new ResponseDTO();            //создаем экземпляр класса для ответа

            responseDTO.setRqUID(requestDTO.getRqUID());            //передаем из запроса в ответ RqUID
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currency);                      //передаем рассчитанную переменную currency
            responseDTO.setBalance(new BigDecimal(777));
            responseDTO.setMaxLimit(maxLimit);*/


            //Второй вариант конструктора (тут нужно создать конструктор в классе ResponceDTO)
            ResponseDTO responseDTO = new ResponseDTO(
                    requestDTO.getRqUID(),                          //передаем из запроса в ответ RqUID
                    clientId,                                       //передаем рассчитанную переменную clientId
                    requestDTO.getAccount(),
                    currency,
                    randomBalance,                                //передаем рандомный съэмулированный BigDecimal баланс
                    //new BigDecimal(777),                        //передаем только что созданное значение BigDecimal для balance
                    maxLimit
            );

            //Делаем рандомную задержку от 1сек до 2сек
            int minSleep = 1000;
            int maxSleep = 2000;
            int randomSleep = random.nextInt(maxSleep - minSleep + 1) + minSleep;
            Thread.sleep(randomSleep);

            //Делаем логирование
            log.info("********** Запрос/RequestDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));  //преобразуем ссылочный тип нашего requestDTO в строку
            log.info("********** Задержка: " + randomSleep + " **********\n********** Ответ/ResponseDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO)); //преобразуем ссылочный тип нашего responseDTO в строку




            //Возвращаем Object responseDTO
            return responseDTO;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());  //Возвращаем статус и тело нашей ошибки
        }
    }


}
