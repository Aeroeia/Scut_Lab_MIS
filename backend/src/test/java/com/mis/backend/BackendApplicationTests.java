package com.mis.backend;

import com.mis.backend.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        UserRole userRole = UserRole.valueOf("Admin");
        System.out.println(userRole);
    }

}
