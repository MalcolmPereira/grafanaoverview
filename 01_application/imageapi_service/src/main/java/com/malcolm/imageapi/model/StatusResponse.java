package com.malcolm.imageapi.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
public class StatusResponse {
    @Getter
    @Setter
    private int statusCode;

    @Getter
    @Setter
    private String error;

    @Getter
    @Setter
    private String message;
}
