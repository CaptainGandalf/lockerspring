package com.locker.locker;

import com.locker.locker.entities.User;
import com.locker.locker.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LockerApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {

	    User user = new User();
	    user.setEmail("skirmantas@email.com");
	    user.setName("Skirmantas");
	    user.setLastName("Stanaitis");
	    user.setIdNowPicture("dfakjsbfanfa");

	    SpringApplication.run(LockerApplication.class, args);
	}

}
