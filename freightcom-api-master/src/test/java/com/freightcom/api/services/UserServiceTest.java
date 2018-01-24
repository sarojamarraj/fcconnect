package com.freightcom.api.services;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.Admin;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.User;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.UserRepository;
import com.freightcom.api.repositories.custom.CustomerRepository;
import com.freightcom.api.repositories.custom.UserRoleRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@RestClientTest(components = { UserRepository.class })
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(classes = { ApiSession.class })
public class UserServiceTest
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Long testId;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    @Qualifier(value = "userRepository")
    private UserRepository userRepository;

    @Autowired
    @Qualifier(value = "customerRepository")
    private CustomerRepository customerRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ApiSession apiSession;

    @Autowired
    private ObjectBase objectBase;

    @Autowired
    private PagedResourcesAssembler<User> pagedAssembler;

    private UserService userService = new UserService(publisher, userRepository, userRoleRepository, passwordEncoder,
            userRoleService, customerRepository, objectBase, null, apiSession, pagedAssembler);

    @Before
    public void setup() throws Exception {
        User user = new User();
        user.setLogin("foobar");

        Admin admin = new Admin();

        entityManager.persist(user);
        admin.setUser(user);

        if (user.getId() == null) {
            throw new Exception("NO ID FROM PERSIST");
        }

        log.debug("CREATE USER " + user.getId());
        log.debug("CREATE USER " + admin);

        Customer customer = new Customer();
        customer.setName("Gorus Inc");
        entityManager.persist(customer);

        User user2 = new User();
        user.setLogin("barfoo");
        entityManager.persist(user2);

        CustomerStaff customerStaff = new CustomerStaff();
        customerStaff.setCustomerId(customer.getId());
        customerStaff.setUser(user2);

        entityManager.persist(customerStaff);
        entityManager.detach(customerStaff);
        entityManager.detach(user2);

        User user3 = new User();
        user3.setLogin("john.smith");
        entityManager.persist(user3);

        CustomerAdmin customerAdmin2 = new CustomerAdmin();
        customerAdmin2.setCustomerId(customer.getId());
        customerAdmin2.setUser(user3);

        entityManager.persist(customerAdmin2);
        testId = customerAdmin2.getId();
        entityManager.detach(customerAdmin2);
        entityManager.detach(user3);
        entityManager.detach(customer);

        entityManager.persist(admin);
        entityManager.detach(user);
        entityManager.detach(admin);
    }

    @Test
    public void test() {
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("customer_id", testId);

        PagedResources<Resource<User>> foundUsers = userService.getUsers(criteria, new PageRequest(0, 100));

        assertTrue(foundUsers.getContent().size() == 1);
    }
}
