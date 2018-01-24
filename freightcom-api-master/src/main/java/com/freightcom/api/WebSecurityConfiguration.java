package com.freightcom.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.freightcom.api.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationEntryPoint alwaysSendUnauthorized401AuthenticationEntryPoint;

    @Autowired
    private SuccessfulAuthentication successfulAuthentication;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FailedAuthentication failedAuthentication;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http
            .addFilterBefore(new ApiHeaderFilter(), UsernamePasswordAuthenticationFilter.class);

        http.headers()
                .frameOptions()
                .disable();
        http.authorizeRequests()
                .antMatchers("/login")
                .permitAll();
        http.authorizeRequests()
                .antMatchers("/logout")
                .permitAll();
        http.authorizeRequests()
                .antMatchers("/login")
                .permitAll();
        http.authorizeRequests()
                .antMatchers("/user/resetPasswordByEmail")
                .permitAll();

        http.authorizeRequests()
        .antMatchers("/logout")
        .permitAll();

        String[] adminRoutes =  { "/systemlog/**", "/salesagent/**", "/freightcomstaff/**",
                                  "/customeradmin/**" };

        for (String adminRoute: adminRoutes) {
            http.authorizeRequests()
                .antMatchers(adminRoute)
                .access("isAuthenticated() and hasAuthority('ADMIN')");
        }

        http.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/systemlog/**")
                .denyAll();

        http.authorizeRequests()
            .antMatchers(HttpMethod.PUT, "/systemlog/**")
                .denyAll();

        http.authorizeRequests()
            .antMatchers(HttpMethod.DELETE, "/systemlog/**")
                .denyAll();

        http.authorizeRequests()
                .antMatchers("/**")
                .authenticated();

        http.authorizeRequests()
        .antMatchers("/user/resetPasswordByEmail")
        .permitAll();

        http.csrf()
                .disable();
        http.exceptionHandling()
                .authenticationEntryPoint(alwaysSendUnauthorized401AuthenticationEntryPoint);
        http.formLogin();
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.ACCEPTED))
                .invalidateHttpSession(true)
                .permitAll();
        http.formLogin()
                .successHandler(successfulAuthentication);
        http.formLogin()
                .failureHandler(failedAuthentication);
        // http.csrf().disable();

    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring()
                .antMatchers("/guber");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        log.debug("AUTHENTICATINO PROVIDER " + userDetailsService + " " + passwordEncoder);

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return authenticationProvider;
    }

    @Bean
    public UserDetailsService userDetailsService()
    {
        return new UserDetailsServiceImpl();
    }
}
