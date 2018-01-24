package com.freightcom.api.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class CreditCardDeserializer extends JsonDeserializer<String>
{

    @Override
    public String deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException
    {
        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);

        String password = "foo bar was here";
        MessageDigest md;

        try {
            md = MessageDigest.getInstance("SHA-256");

            md.update(password.getBytes());
            
            TextEncryptor encryptor = Encryptors.queryableText(password, "ABCDE012");

            return encryptor.encrypt(node.textValue());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }

}
