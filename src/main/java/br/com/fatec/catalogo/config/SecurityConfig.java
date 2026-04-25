package br.com.fatec.catalogo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/dashboard", "/dashboard/**").hasRole("ADMIN")
                        .requestMatchers("/categorias/**").hasRole("ADMIN")
                        .requestMatchers("/cadastro-usuario", "/usuarios/salvar").hasRole("ADMIN")
                        .requestMatchers("/produtos/novo", "/produtos/editar/**", "/produtos/excluir/**").hasRole("ADMIN")
                        .requestMatchers("/produtos", "/produtos/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                    .loginPage("/login")
                        .defaultSuccessUrl("/produtos", true)
                        .permitAll()
                )
                .logout(logout -> logout.logoutSuccessUrl("/produtos"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
