package app.hakai.backend.agents;

import java.util.List;
import java.util.function.Consumer;

import app.hakai.backend.transients.QuestionVariant;

public @FunctionalInterface
interface PedagogicalAgentCallback extends Consumer<List<QuestionVariant>> {};