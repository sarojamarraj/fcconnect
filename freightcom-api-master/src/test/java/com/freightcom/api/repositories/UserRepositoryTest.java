package com.freightcom.api.repositories;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.freightcom.api.model.Admin;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.repositories.custom.UserRoleRepository;


@RunWith(SpringRunner.class)
@DataJpaTest
@RestClientTest
public class UserRepositoryTest
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private Long testId;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserRoleRepository userRoleRepository;

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
    public void test()
    {        
        List<User> foundUsers = repository.findByLogin("foobar");
        
        assertTrue(foundUsers.size() == 1);
    }


    @Test
    public void test2()
    {      
        List<User> foundUsers = repository.findByLogin("foobar");
        
        assertTrue(foundUsers.get(0).getAuthorities().size() == 1);
        
        log.debug("HERE HERE " + foundUsers.get(0).getAuthorities().toArray().length);
        
        for (UserRole role: foundUsers.get(0).getAuthorities()) {
            log.debug("USER ROLE " + role);
        }
    }


    @Test
    public void test3()
    {  
        Iterable<UserRole> roles = userRoleRepository.findAll();
        int count = 0;
        
        for (UserRole role: roles) {
            count += 1;
            log.debug("QQQQ " + role.toString());
        }
        
        assertTrue(count == 3);
    }


    @Test
    public void test4()
    {        
        List<User> foundUsers = repository.findByLogin("john.smith");
        
        log.debug("eee " + foundUsers.size());
        
        assertTrue(foundUsers.size() == 1);
        
        Collection<UserRole> roles = foundUsers.get(0).getAuthorities();
        
        int count = 0;
        UserRole foundRole = null;
        
        for (UserRole role: roles) {
            count += 1;
            foundRole = role;
        }
        
        assertTrue(count == 1);
        assertTrue(foundRole.getRoleName().equals(UserRole.ROLE_CUSTOMER_ADMIN));
        assertTrue("Found role id should equal " + testId, foundRole.getId().equals(testId));
    }




}
