package com.freightcom.api.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freightcom.api.resources.DocumentContent;
import com.freightcom.api.services.DocumentManager;
import com.freightcom.api.services.DocumentManagerImpl;

@Controller
public class DocumentManagerController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DocumentManager documentManager;

    @Autowired
    public void setDocumentManager(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    @RequestMapping(value = "/dm/login", method = RequestMethod.GET)
    @ResponseBody
    public String testLogin() throws Exception {
        return documentManager.login();
    }

    @RequestMapping(value = "/dm/roots", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public DocumentManagerImpl.LoginResult roots() throws Exception {
        return documentManager.roots();
    }

    @RequestMapping(value = "/dm/content/{id}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> getFile(@PathVariable("id") String id, HttpServletResponse response)
            throws Exception {
        try {
            // get your file as InputStream
            DocumentContent result = documentManager.getDocument(id);

            Map<String, List<String>> headerMap = result.getHeaders();
            HttpHeaders headers = new HttpHeaders();

            for (String key : headerMap.keySet()) {
                logger.debug(key);

                if (key != null && (key.equalsIgnoreCase("content-disposition")
                        || key.equalsIgnoreCase("content-length") || key.equalsIgnoreCase("content-type"))) {
                    for (String value : headerMap.get(key)) {
                        logger.debug(value);
                        headers.add(key, value);
                    }
                }
            }

            return ResponseEntity.ok().headers(headers)
                    // may need to set content length here
                    // .contentLength(pdfFile.contentLength())
                    .body(new InputStreamResource(result.getStream()));

        } catch (IOException ex) {
            logger.info("Error writing file to output stream. Filename was '{}'", ex);
            throw new RuntimeException("IOError writing file to output stream");
        }

    }

}
