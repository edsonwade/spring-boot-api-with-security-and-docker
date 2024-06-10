//package code.with.vanilson.securityservice.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.annotation.PostConstruct;
//
///**
// * Security configuration class for Spring Security.
// * This class configures in-memory authentication and HTTP security settings.
// *
// * @author vanilson muhongo
// */
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    private final AuthenticationManagerBuilder auth;
//    private final PasswordEncoder passwordEncoder;
//    @Value("${security.user-password}")
//    private String userPassword;
//
//    @Value("${security.admin-password}")
//    private String adminPassword;
//
//    /**
//     * Constructs the security configuration with necessary dependencies.
//     *
//     * @param auth            the AuthenticationManagerBuilder for setting up in-memory authentication
//     * @param passwordEncoder the PasswordEncoder for encoding passwords
//     */
//
//    public SecurityConfig(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) {
//        this.auth = auth;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    /**
//     * Initializes in-memory authentication with user details.
//     * This method is called after dependency injection is done.
//     *
//     * @throws Exception if there is an error during authentication setup
//     */
//
//    @PostConstruct
//    void initAuth() throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password(passwordEncoder.encode(userPassword))
//                .roles("USER")
//                .and()
//                .withUser("admin")
//                .password(passwordEncoder.encode(adminPassword))
//                .roles("ADMIN");
//    }
//
//    /**
//     * Configures the HTTP security for the application.
//     * This method sets up authorization requirements and form-based and HTTP Basic authentication.
//     *
//     * @param http the HttpSecurity to configure
//     * @return the configured SecurityFilterChain
//     * @throws Exception if there is an error during configuration
//     */
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .csrf().disable()
//                .httpBasic(); // Optional: if you want to enable HTTP basic authentication.
//
//        return http.build();
//    }
//}