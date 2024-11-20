package pl.kurs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import pl.kurs.model.Role;
import pl.kurs.repository.UserRepository;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Slf4j
@Configuration
public class SecurityConfig {


    //    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeRequests(authorize -> authorize
//                        .requestMatchers(HttpMethod.POST, "/api/v1/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasAnyRole("ADMIN", "USER")
//                        .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/api/v1/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }

    //        @Bean
//    public UserDetailsService users(PasswordEncoder encoder) {
//        UserDetails user = User.builder()
//                .username("user")
//                .password(encoder.encode("user"))
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(encoder.encode("admin"))
//                .roles("USER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);

        userDetailsManager.setUsersByUsernameQuery(
                "SELECT email AS username, password, true AS enabled FROM app_user WHERE email = ?");
        userDetailsManager.setAuthoritiesByUsernameQuery(
                "SELECT u.email AS username, r.name AS authority " +
                        "FROM app_user u " +
                        "JOIN user_roles ur ON u.id = ur.user_id " +
                        "JOIN role r ON ur.role_id = r.id " +
                        "WHERE u.email = ?");

        return userDetailsManager;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
