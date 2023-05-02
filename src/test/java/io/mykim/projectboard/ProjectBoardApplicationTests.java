package io.mykim.projectboard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"JASYPT_SECRET_KEY=test"})
class ProjectBoardApplicationTests {

    @Test
    void contextLoads() {
    }

}
