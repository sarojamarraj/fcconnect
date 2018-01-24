package com.freightcom.api.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.freightcom.api.resources.DocumentContent;
import com.freightcom.api.services.dataobjects.ApiCommand;
import com.freightcom.api.services.dataobjects.ApiResult;
import com.freightcom.api.util.Empty;

@Service
@ConfigurationProperties(prefix = "webpal")
public class DocumentManagerImpl implements DocumentManager
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RestTemplate restTemplate;

    @Value("${webpal.url}")
    private String webpalUrl;

    @Value("${webpal.login}")
    private String webpalLogin;

    @Value("${webpal.password}")
    private String webpalPassword;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    public DocumentManagerImpl(RestTemplate restTemplate) throws Exception
    {
        this.restTemplate = restTemplate;
    }

    public String login() throws Exception
    {
        LoginResult result = restTemplate.postForObject(webpalUrl + "/webservice/xml-service.php",
                "<request command=\"login\"  user=\"" + webpalLogin + "\" password=\"" + webpalPassword + "\"/>",
                LoginResult.class);

        if (result.getError() != null) {
            return result.getError();
        } else {
            httpSession.setAttribute("dm-session", result.getSessionId());
            return result.getSessionId();
        }
    }

    public String getSessionId() throws Exception
    {
        String sessionId = Empty.asString(httpSession.getAttribute("dm-session"), null);

        if (sessionId == null) {
            sessionId = login();
        }

        return sessionId;
    }

    @Override
    public ApiResult executeCommand(ApiCommand command) throws Exception
    {
        command.setSessionID(getSessionId());

        String result = restTemplate.postForObject(webpalUrl + "/webservice/xml-service.php", marshal(command),
                String.class);

        logger.debug("COMMAND RESULT " + command + " => " + result);

        return unmarshall(result, LoginResult.class);
    }

    public LoginResult roots() throws Exception
    {
        String sessionID = login();
        ApiRequest apiRequest = new ApiRequest();

        logger.debug("SESSION ID = " + sessionID);

        apiRequest.setCommand("listRoots");
        apiRequest.setSessionID(sessionID);

        logger.debug("CALLING ROOTS " + apiRequest.toString());

        String result = restTemplate.postForObject(webpalUrl + "/webservice/xml-service.php", marshal(apiRequest),
                String.class);

        logger.debug("ROOTS RESULT " + result);

        return unmarshall(result, LoginResult.class);
    }

    @Override
    public DocumentContent getDocument(String id) throws Exception
    {
        URL url = new URL(webpalUrl + "/webservice/xml-service.php");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/plain");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("X_DM_SESSION_ID", login());
        connection.connect();

        String postData = "<request command=\"download\" path=\"" + id + "\"/>";
        byte[] postDataBytes = postData.toString()
                .getBytes("UTF-8");
        connection.getOutputStream()
                .write(postDataBytes);

        return new DocumentContent(connection.getInputStream(), connection.getHeaderFields());
    }

    @Override
    public Map<String, Object> upload(String path, String name, MultipartFile file) throws Exception
    {
        return upload(path, name, file, null);
    }

    @Override
    public Map<String, Object> upload(String path, String name, MultipartFile file, String title) throws Exception
    {
        URL url = new URL(webpalUrl + "/webservice/xml-service.php");

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        Map<String, Object> response = new HashMap<String, Object>();

        String sessionID = login();
        ApiRequest apiRequest = new ApiRequest();

        logger.debug("SESSION ID = " + sessionID);

        apiRequest.setCommand("upload");
        apiRequest.setSessionID(sessionID);
        apiRequest.setDir(path);
        apiRequest.setPretty("yes");
        apiRequest.setName(name);
        apiRequest.setFullPath(name);

        map.add("xml", apiRequest);
        map.add("file", new MultipartFileResource(file.getInputStream(), name));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);

        String returnedText = restTemplate.postForObject(url.toURI(), requestEntity, String.class);

        logger.debug("UPLOAD RESULT\n" + returnedText);

        LoginResult result = unmarshall(returnedText, LoginResult.class);

        if (result.getError() != null) {
            response.put("error", result.getError());
        } else {
            response.put("status", result.getStatus());
            List<ResponsePath> paths = result.getResult()
                    .getPaths();

            if (paths != null) {
                if (paths.size() > 0) {
                    response.put("path", paths.get(0)
                            .getPath());
                    response.put("prettyName", paths.get(0)
                            .getPrettyName());
                    response.put("mimeType", paths.get(0)
                            .getMimeType());
                }
            }
        }

        return response;
    }

    private String marshal(Object object) throws Exception
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(ApiRequest.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter writer = new StringWriter();

        jaxbMarshaller.marshal(object, writer);

        return writer.toString();
    }

    private <T> T unmarshall(String text, Class<T> clazz) throws Exception
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(text));
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        return clazz.cast(unmarshaller.unmarshal(xsr));
    }

    @XmlRootElement(name = "request")
    private static class ApiRequest implements ApiCommand
    {
        private String command = null;
        private String sessionID = null;
        private String includeMetaData = "yes";
        private String fullPath = null;
        private String name = null;
        private String pretty = "no";
        private String dir = null;
        private String title = null;

        @XmlAttribute
        public String getCommand()
        {
            return command;
        }

        public void setCommand(String command)
        {
            this.command = command;
        }

        @XmlAttribute
        public String getSessionID()
        {
            return sessionID;
        }

        public void setSessionID(String sessionID)
        {
            this.sessionID = sessionID;
        }

        public String toString()
        {
            return "<request sessionID=\"" + sessionID + "\" command=\"" + command + "\"/>";
        }

        @XmlAttribute
        public String getIncludeMetaData()
        {
            return includeMetaData;
        }

        @XmlAttribute
        public String getFullPath()
        {
            return fullPath;
        }

        public void setFullPath(String fullPath)
        {
            this.fullPath = fullPath;
        }

        @XmlAttribute
        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        @XmlAttribute
        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        @XmlAttribute
        public String getPretty()
        {
            return pretty;
        }

        public void setPretty(String pretty)
        {
            this.pretty = pretty;
        }

        @XmlAttribute
        public String getDir()
        {
            return dir;
        }

        public void setDir(String dir)
        {
            this.dir = dir;
        }

    }

    @XmlRootElement(name = "response")
    public static class LoginResult implements ApiResult
    {
        private String status;
        private String error = null;
        private String sessionId = null;
        private ResponseResult result;

        @XmlAttribute(name = "status")
        public String getStatus()
        {
            return status;
        }

        public void setStatus(String status)
        {
            this.status = status;
        }

        @XmlElement(name = "error")
        public String getError()
        {
            return error;
        }

        public void setError(String error)
        {
            this.error = error;
        }

        @XmlElement(name = "sessionID")
        public String getSessionId()
        {
            return sessionId;
        }

        public void setSessionId(String sessionId)
        {
            this.sessionId = sessionId;
        }

        @XmlElement(name = "result")
        public ResponseResult getResult()
        {
            return result;
        }

        public void setResult(ResponseResult result)
        {
            this.result = result;
        }
    }

    private static class ResponseResult
    {
        private List<ResponsePath> paths;

        @XmlElement(name = "path")
        public List<ResponsePath> getPaths()
        {
            return paths;
        }

        @SuppressWarnings("unused")
        public void setPaths(List<ResponsePath> paths)
        {
            this.paths = paths;
        }

    }

    private static class ResponsePath
    {
        private String path;
        private String prettyName;
        private String mimeType;

        @XmlAttribute
        public String getPath()
        {
            return path;
        }

        @SuppressWarnings("unused")
        public void setPath(String path)
        {
            this.path = path;
        }

        @XmlAttribute
        public String getPrettyName()
        {
            return prettyName;
        }

        @SuppressWarnings("unused")
        public void setPrettyName(String prettyName)
        {
            this.prettyName = prettyName;
        }

        @XmlAttribute(name = "mime-type")
        public String getMimeType()
        {
            return mimeType;
        }

        @SuppressWarnings("unused")
        public void setMimeType(String mimeType)
        {
            this.mimeType = mimeType;
        }
    }

    private class MultipartFileResource extends InputStreamResource
    {
        private String filename;

        public MultipartFileResource(InputStream inputStream, String filename)
        {
            super(inputStream);
            this.filename = filename;
        }

        @Override
        public String getFilename()
        {
            return this.filename;
        }

        @Override
        public long contentLength() throws IOException
        {
            return -1; // we do not want to generally read the whole stream into
                       // memory ...
        }
    }

    @Override
    public ResponseEntity<InputStreamResource> stream(String documentId) throws Exception
    {
        try {
            DocumentContent result = getDocument(documentId);
            logger.debug("DOWNLOAD DOCUMENT MANAGER DOCMENT " + documentId);

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

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(result.getStream()));

        } catch (IOException ex) {
            logger.info("Error writing file to output stream. Filename was '{}'", ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

}
