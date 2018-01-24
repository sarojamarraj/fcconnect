package com.freightcom.api.services;

import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.freightcom.api.resources.DocumentContent;
import com.freightcom.api.services.dataobjects.ApiCommand;
import com.freightcom.api.services.dataobjects.ApiResult;

public interface DocumentManager
{
    String login() throws Exception;

    DocumentManagerImpl.LoginResult roots() throws Exception;

    DocumentContent getDocument(String id) throws Exception;

    Map<String,Object> upload(String path, String name, MultipartFile file) throws Exception;

    ResponseEntity<InputStreamResource> stream(String documentId) throws Exception;

    Map<String, Object> upload(String path, String name, MultipartFile file, String title) throws Exception;

    ApiResult executeCommand(ApiCommand command) throws Exception;
}
