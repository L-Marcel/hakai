package app.hakai.backend.agents;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AIKeyController {
    private final String[] keys = {};
    private int currentKey = 0;

    public String getKey() {
        return keys[currentKey];
    };

    public void changeCurrentKey() {
        currentKey = (currentKey + 1) % keys.length;
    };

    public String[] getEnvironmentKeys() {
        return keys;
    };
};
