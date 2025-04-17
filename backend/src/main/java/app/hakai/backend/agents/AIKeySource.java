package app.hakai.backend.agents;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class AIKeySource implements KeySource {
    @Getter
    private String[] keys = {};
    private int currentKey = -1;

    public AIKeySource(Environment environment) {
        String rawKeys = environment.getProperty("ai.keys");
        if(rawKeys != null) 
            this.keys = rawKeys.split(",");
        else {
            String[] newKeys = {"abc"};
            this.keys = newKeys;
        };
    };

    public synchronized String getKey() {
        this.changeCurrentKey();
        if(currentKey < 0) return null;
        return keys[currentKey];
    };

    private void changeCurrentKey() {
        if(keys.length > 0) 
            currentKey = (currentKey + 1) % keys.length;
    };
};
