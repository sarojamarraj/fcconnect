package com.freightcom.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SlackServiceTest
{
     
    private SlackService slackService = new SlackService("https://hooks.slack.com/services/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");

    @Test
    public void test()
    {
        try {
            assertNotNull(slackService.send("Test Message"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

}
