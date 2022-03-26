package ru.itis.mfbotsapi.api.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Reply {

    protected String userId;
    protected String message;

}
