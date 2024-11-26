package org.event.gestionevenement.Config;


import lombok.AllArgsConstructor;
import org.event.gestionevenement.service.UserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private PasswordEncoder passwordEncoder;
    private UserDetailServiceImpl userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {  // la creation d'un filtre pour l'autentification
        httpSecurity.formLogin().loginPage("/login").defaultSuccessUrl("/",true).permitAll(); // pour affichier une page login dans le cas quant je en veux acceder a notre appilcation
        httpSecurity.authorizeHttpRequests().requestMatchers("/webjars/","/Inscrire","/register").permitAll();
        httpSecurity.logout().logoutSuccessUrl("/").permitAll();
        httpSecurity.rememberMe();
        httpSecurity.authorizeHttpRequests().requestMatchers("/user/").hasRole("USER"); // autorizer les user qunat un role USER
        httpSecurity.authorizeHttpRequests().requestMatchers("/admin/").hasRole("ADMIN"); // autorizer les user qunat un role ADMIN
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();  // tous les requetes nisisite une autentification
        httpSecurity.exceptionHandling().accessDeniedPage("/notAuthenticated");
        httpSecurity.userDetailsService(userDetailService);
        return httpSecurity.build();
    }
}