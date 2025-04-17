package app.hakai.backend.agents;

import java.util.Optional;
import java.util.function.Consumer;

public @FunctionalInterface
interface ChatbotCallback extends Consumer<Optional<String>> {};
