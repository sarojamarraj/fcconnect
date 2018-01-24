package com.freightcom.api.util;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class CreditCardSerializer extends JsonSerializer<String>
{
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException, JsonProcessingException
    {
        if (value != null) {
            StringBuilder builder = new StringBuilder();
            int n = value.length();

            for (int i = 0; i < n - 4; i++) {
                builder.append("*");
            }

            int index = n - 4;

            if (index >= 0) {
                builder.append(value.substring(index));
            }

            gen.writeString(builder.toString());
        } else {
            gen.writeString("");
        }
    }
}
