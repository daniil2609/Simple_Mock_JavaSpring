package com.example.Mock_Lekciya.model;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
//Можно самому не писать конструктор (с 21 строки) а поставить эту аннотацию
//@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {  //структура нашего ответа

    private String rqUID;
    private String clientId;
    private String account;
    private String currency;
    private BigDecimal balance;
    private BigDecimal maxLimit;

    public ResponseDTO(String rqUID, String clientId, String account, String currency, BigDecimal balance, BigDecimal maxLimit) {
        this.rqUID = rqUID;
        this.clientId = clientId;
        this.account = account;
        this.currency = currency;
        this.balance = balance;
        this.maxLimit = maxLimit;
    }
}
