package com.example.Mock_Lekciya.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestDTO {  //структура нашего запроса

    private String rqUID;
    private String clientId;
    private String account;
    private String openDate;
    private String closeDate;

}
