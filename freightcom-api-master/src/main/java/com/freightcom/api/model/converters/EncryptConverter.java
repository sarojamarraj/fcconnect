/******************************************************************
              WebPal Product Suite Framework Libraries
-------------------------------------------------------------------
(c) 2002-present: all copyrights are with Palomino System Innovations Inc.
(Palomino Inc.) of Toronto, Canada

Unauthorized reproduction, licensing or disclosure of this source
code will be prosecuted. WebPal is a registered trademark of
Palomino System Innovations Inc. To report misuse please contact
info@palominosys.com or call +1 416 964 7333.
*******************************************************************/
package com.freightcom.api.model.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Converter
public class EncryptConverter implements AttributeConverter<String, String>
{
    private String password = "foo bar was here";

    @Override
    public String convertToDatabaseColumn(String text)
    {
        if (text == null) {
            return null;
        }

        TextEncryptor encryptor = Encryptors.queryableText(password, "ABCDE012");

        return encryptor.encrypt(text);
    }

    @Override
    public String convertToEntityAttribute(String value)
    {
        if (value == null) {
            return null;
        }

        TextEncryptor encryptor = Encryptors.queryableText(password, "ABCDE012");

        try {
            return encryptor.decrypt(value);
        } catch (Exception e) {
            // Legacy data in credit card column
            return null;
        }
    }
}
