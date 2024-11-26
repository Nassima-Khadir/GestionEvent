package org.event.gestionevenement;

import org.event.gestionevenement.service.CompteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GestionEvenementApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionEvenementApplication.class, args);
    }
//    @Bean
//    CommandLineRunner commandLineRunnerUserDetails(CompteService compteService){
//        return args -> {
//            compteService.addNewRole("USER");
//            compteService.addNewRole("ADMIN");
//            compteService.addNewUser("user1","1234","user1@gmail.com","1234");
//            compteService.addNewUser("user2","1234","user2@gmail.com","1234");
//            compteService.addNewUser("admin","1234","org1@gmail.com","1234");
//            compteService.addNewUser("momo","1234","momo@gmail.com","1234");
//            compteService.addRoleToUser("admin","ADMIN");
//            compteService.addRoleToUser("admin","USER");
//            compteService.addRoleToUser("momo","ADMIN");
//            compteService.addRoleToUser("momo","USER");
//            compteService.addRoleToUser("user1","USER");
//            compteService.addRoleToUser("user2","USER");
//
//
//        };
//    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
