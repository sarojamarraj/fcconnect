package com.freightcom.api.controllers;

import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MockedRequests
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/mock1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String mock1() throws Exception {
        return "{\"foo\": 33 }";
    }

    @RequestMapping(value = "/mock/{resource}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getResource(@PathVariable("resource") String resource,
            HttpServletResponse response) throws Exception {
        try {
            HttpHeaders headers = new HttpHeaders();
            InputStream x = MockedRequests.class.getResourceAsStream("/mocks/" + resource + ".json");

            log.info("MOCK RESOURCE " + resource);
            log.info("MOCK RESOURCE STREAM " + x);

            if (x == null) {
                throw new Exception("NO SUCH MOCK " + resource);
            }

            return ResponseEntity.ok().headers(headers)
                    // may need to set content length here
                    // .contentLength(pdfFile.contentLength())
                    .body(new InputStreamResource(x));

        } catch (Exception ex) {
            log.info("Error writing file to output stream. Filename was '{" + resource + "}'", ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(value = "/accessorial_servicesx", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices1(HttpServletResponse response) throws Exception {
        return getResource("accessorial_services", response);
    }

    @RequestMapping(value = "/mock_address_book", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices2(HttpServletResponse response) throws Exception {
        return getResource("address_book", response);
    }

    @RequestMapping(value = "/carrier_services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices3(HttpServletResponse response) throws Exception {
        return getResource("carrier_services", response);
    }

    @RequestMapping(value = "/customersx", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices5(HttpServletResponse response) throws Exception {
        return getResource("customers", response);
    }

    @RequestMapping(value = "/freight_class", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices6(HttpServletResponse response) throws Exception {
        return getResource("freight_class", response);
    }

    @RequestMapping(value = "/insurance_types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices7(HttpServletResponse response) throws Exception {
        return getResource("insurance_types", response);
    }

    @RequestMapping(value = "/ordersx", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices8(HttpServletResponse response) throws Exception {
        return getResource("orders", response);
    }

    @RequestMapping(value = "/package_templates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices9(HttpServletResponse response) throws Exception {
        return getResource("package_templates", response);
    }

    @RequestMapping(value = "/pallet_templates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices10(HttpServletResponse response) throws Exception {
        return getResource("pallet_templates", response);
    }

    @RequestMapping(value = "/product_book", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices11(HttpServletResponse response) throws Exception {
        return getResource("product_book", response);
    }

    @RequestMapping(value = "/services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices12(HttpServletResponse response) throws Exception {
        return getResource("services", response);
    }

    @RequestMapping(value = "/foo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InputStreamResource> getServices(HttpServletResponse response) throws Exception {
        return getResource("foo", response);
    }

}
