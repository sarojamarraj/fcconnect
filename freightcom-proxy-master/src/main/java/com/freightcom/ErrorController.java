package com.freightcom;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController
{

    @Value("${error.path:/error}")
    private String errorPath;

    @Override
    public String getErrorPath()
    {
        // TODO Auto-generated method stub
        return errorPath;
    }

    @RequestMapping(value = "${error.path:/error}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<InputStreamResource> errorHandler(
            HttpServletResponse response) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            InputStream x = ErrorController.class.getResourceAsStream("/static/index.html");

            if (x == null) {
                throw new Exception("Can't find index.html");
            }

            return ResponseEntity.ok().headers(headers)
                    // may need to set content length here
                    // .contentLength(pdfFile.contentLength())
                    .body(new InputStreamResource(x));

        } catch (Exception ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }


}
