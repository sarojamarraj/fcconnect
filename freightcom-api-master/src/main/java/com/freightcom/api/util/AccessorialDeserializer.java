package com.freightcom.api.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.freightcom.api.model.AccessorialServices;
import com.freightcom.api.model.OrderAccessorials;
import com.freightcom.api.repositories.AccessorialServicesRepository;

public class AccessorialDeserializer extends JsonDeserializer<List<OrderAccessorials>>
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccessorialServicesRepository repository;

    @Override
    public List<OrderAccessorials> deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        ObjectCodec codec = parser.getCodec();
        JsonNode node = codec.readTree(parser);

        List<OrderAccessorials> accessorials = new ArrayList<OrderAccessorials>();

        log.debug("DESERIALIZE ACCESSORIAL " + node);
        log.debug("ACC PARENT " + node.findParent("x"));
        log.debug("REPO " + repository);

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();

            if (entry.getValue().asBoolean()) {
                log.debug("DESERIALIZE INT " + entry.getKey());

                repository.findByName(entry.getKey());

                for (AccessorialServices match: repository.findByName(entry.getKey())) {
                    OrderAccessorials item = new OrderAccessorials();
                    item.setService(match);
                    accessorials.add(item);
                }
            }
        }

        return accessorials;
    }

}
