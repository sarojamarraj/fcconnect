package com.freightcom.api;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "freightcom")
public class SessionListener implements HttpSessionListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ApplicationEventPublisher publisher;

    @Value("${freightcom.session_timeout}")
    private int sessionTimeout = 60000;

    @Autowired
    public SessionListener(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }


  @Override
  public void sessionCreated(HttpSessionEvent arg0) {
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent event) {
      log.debug("SESSION EVENT " + event);
      long lastAccessed = event.getSession().getLastAccessedTime();

      if (System.currentTimeMillis() - lastAccessed > SuccessfulAuthentication.sessiontTimeOutMinutes * sessionTimeout) {
          log.debug("SESSION EXPIRED " + lastAccessed + " "  + System.currentTimeMillis() );
          publisher.publishEvent(new SessionExpiredEvent());

      }

  }
}
