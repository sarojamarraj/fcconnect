package com.freightcom.api.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@RestClientTest
@TestPropertySource(locations="classpath:application-test.properties")
public class UserTest
{

    @Test
    public void test()
    {
        User user = new User();
        
        assertThat(user != null);
    }

}
