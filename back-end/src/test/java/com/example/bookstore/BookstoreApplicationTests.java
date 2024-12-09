package com.example.bookstore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookstoreApplicationTests {

	@Autowired
	private BookstoreApplication app;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(app);
	}

}
