package app.hakai.backend.agents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KeySourceTest {
    @Autowired
    private KeySource keySource;

    @Test
    void shouldReturnSomeKey(){
        assertNotEquals(keySource.getKeys().length, 0);
        for(int i = 0; i < keySource.getKeys().length * 2; i++) {
            String key = keySource.getKey();
            assertEquals(key, keySource.getKeys()[i % keySource.getKeys().length]);
        };

        if(keySource.getKeys().length > 1)
            assertNotEquals(keySource.getKey(), keySource.getKey());
    };
};