package com.freightcom.api;

import java.io.IOException;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import com.github.seratch.jslack.api.webhook.WebhookResponse;

@Component
public class SlackService
{

    private final Slack slack;
    private final String slackUrl;

    public SlackService(String url)
    {
        slack = Slack.getInstance();
        slackUrl = url;
    }

    @Async
    public WebhookResponse send(String message) throws IOException
    {
        Payload payload = Payload.builder()
                .username("jSlack Bot")
                .iconEmoji(":smile_cat:")
                .text(message)
                .build();

        System.out.println("URL " + slackUrl);

        return slack.send(slackUrl, payload);
    }
}
