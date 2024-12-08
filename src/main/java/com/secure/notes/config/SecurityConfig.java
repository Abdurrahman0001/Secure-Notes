package com.secure.notes.config;

import com.secure.notes.jwt.AuthEntryPointJwt;
import com.secure.notes.jwt.AuthTokenFilter;
import com.secure.notes.models.AppRole;
import com.secure.notes.models.Role;
import com.secure.notes.models.User;
import com.secure.notes.repository.RoleRepository;
import com.secure.notes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
/*@EnableMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) --> this annotation has been deprecated*/

public class SecurityConfig {
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/notes/**","/api/users/**").hasAnyRole("USER","ADMIN")
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated());

       // http.formLogin(withDefaults());
        http.sessionManagement(
                session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(exception-> exception.authenticationEntryPoint(unauthorizedHandler));

       // http.httpBasic(withDefaults());
        http.headers(header-> header
                .frameOptions(frameOptions -> frameOptions.sameOrigin()));
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception{
        return builder.getAuthenticationManager();
    }

















    /*Creating in memory user*/

   /* @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        if (!manager.userExists("user1")) {
            manager.createUser(
                    User.withUsername("user1")
                            .password("{noop}password")
                            .roles("ROLES")
                            .build()
            );
        }
        if (!manager.userExists("admin")) {
            manager.createUser(
                    User.withUsername("admin")
                            .password("{noop}adminpassword")
                            .roles("ADMIN")
                            .build()
            );
        }
        return manager;
    }*/


    /*this is for the database persistent users data like username ,password ,authorities etc*/

   /* @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
        if (!manager.userExists("user1")) {
            manager.createUser(
                    User.withUsername("user1")
                            .password("{noop}password")
                            .roles("USER")
                            .build()
            );
        }
        if (!manager.userExists("admin")) {
            manager.createUser(
                    User.withUsername("admin")
                            .password("{noop}adminpassword")
                            .roles("ADMIN")
                            .build()
            );
        }
        return manager;
    }*/

    /*Custom data insert into the database*/

/*

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository){
        return args -> {
          Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                  .orElseGet(()-> roleRepository.save(new Role(AppRole.ROLE_USER)));

          Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                  .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

          if (!userRepository.existsByUserName("Sam")){
              User sam = new User("sam","sam@gmail.com","{noop}sam");
              sam.setAccountNonExpired(true);
              sam.setAccountNonLocked(true);
              sam.setEnabled(true);
              sam.setRole(userRole);
              sam.setCreatedDate(LocalDateTime.now());
              sam.setAccountExpiryDate(LocalDate.now().plusYears(1));
              sam.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
              sam.setSignUpMethod("email");
              sam.setTwoFactorEnabled(false);
              userRepository.save(sam);
          }

            if (!userRepository.existsByUserName("sahil")){
                User sahil = new User("sahil","sahil@gmail.com","{noop}sahil");
                sahil.setAccountNonExpired(true);
                sahil.setAccountNonLocked(false);
                sahil.setEnabled(true);
                sahil.setRole(adminRole);
                sahil.setCreatedDate(LocalDateTime.now());
                sahil.setAccountExpiryDate(LocalDate.now().plusYears(1));
                sahil.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                sahil.setSignUpMethod("email");
                sahil.setTwoFactorEnabled(false);
                userRepository.save(sahil);
            }

            if (!userRepository.existsByUserName("shahid")){
                User sahil = new User("shahid","shahid@gmail.com","{noop}shahid");
*/
/*                sahil.setAccountNonExpired(true);
                sahil.setAccountNonLocked(false);
                sahil.setEnabled(true);
                sahil.setRole(adminRole);
                sahil.setCreatedDate(LocalDateTime.now());
                sahil.setAccountExpiryDate(LocalDate.now().plusYears(1));
                sahil.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                sahil.setSignUpMethod("email");
                sahil.setTwoFactorEnabled(false);*//*

                userRepository.save(sahil);
            }




        };
    }
*/

}
