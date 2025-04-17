package app.hakai.backend.agents;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AIKeySource implements KeySource {
    private final String[] keys = {};
    private int currentKey = -1;

    public String getKey() {
        return keys[currentKey];
    };

    public void changeCurrentKey() {
        currentKey = (currentKey + 1) % keys.length;
    };
};
