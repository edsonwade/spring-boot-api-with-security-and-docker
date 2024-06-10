package code.with.vanilson.securityservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration {

    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfiguration(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManager.authenticationProvider(null);
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/accounts/**").permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .hasAnyRole("USER", "ADMIN", "MANAGER")
                .and()
                .httpBasic(Customizer.withDefaults())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

    /**
     * Its only use for testing
     */

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder.encode("password"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("password"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

}
