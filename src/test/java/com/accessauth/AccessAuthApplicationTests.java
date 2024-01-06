package com.accessauth;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.accessauth.model.db.Login;
import com.accessauth.repository.LoginRepository;

@SpringBootTest
class AccessAuthApplicationTests {
	
	@Autowired
	private LoginRepository loginRepo;

	@Test
	void contextLoads() {
		Optional<Login> optionalLogin = loginRepo.findByUsernameAndPassword("roshan", "Rosh@9594");
		Login login = optionalLogin.get();
		System.out.println(login.getName());
	}

}
