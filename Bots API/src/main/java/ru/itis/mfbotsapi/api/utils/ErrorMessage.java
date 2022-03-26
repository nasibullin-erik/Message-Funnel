package ru.itis.mfbotsapi.api.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage implements Message{

    protected String text;

}
