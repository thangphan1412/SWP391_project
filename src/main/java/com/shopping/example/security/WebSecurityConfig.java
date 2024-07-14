package com.shopping.example.security;

import com.shopping.example.security.jwt.JwtConfig;
import com.shopping.example.security.jwt.JwtService;
import com.shopping.example.utility.Contant;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {
    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;


    private DataSource dataSource;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        http.getSharedObject(AuthenticationManagerBuilder.class).authenticationProvider(daoAuthenticationProvider());
        http.authorizeHttpRequests(authConfig -> {
                    authConfig.requestMatchers(HttpMethod.GET, "/admin/**").hasAuthority(Contant.ROLE_ADMIN.toString());
                    authConfig.requestMatchers(HttpMethod.GET, "/employee/**").hasAuthority(Contant.ROLE_EMPLOYEE.toString());
                    authConfig.requestMatchers(HttpMethod.GET, "/css/**", "/js/**", "/images/**").permitAll(); // Cấu hình cho phép truy cập các đường dẫn CSS, JS, và hình ảnh
                    authConfig.requestMatchers(HttpMethod.GET, "/ProductDetail/**", "/Category/**", "/ListProduct/**").permitAll(); // Cho phép truy cập vào các đường dẫn liên quan đến sản phẩm
                    authConfig.anyRequest().permitAll();
                }).rememberMe(rememberMe -> {
                    rememberMe.key("remember-me");
                    rememberMe.tokenValiditySeconds(7 * 24 * 60 * 60); // 7 ngày
                    rememberMe.tokenRepository(persistentTokenRepository());
                }).formLogin(login -> {
                    login.loginPage("/login");
                    login.failureUrl("/login?error=true");
                    login.defaultSuccessUrl("/");
                }).logout(logout -> {
//                    logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");
                    logout.deleteCookies("JSESSIONID");
                    logout.deleteCookies("JWT_TOKEN");
                    logout.deleteCookies("1234abc");
                    logout.invalidateHttpSession(true);
                })
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(this.userDetailsService);
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()

    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8088"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        var tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

}
