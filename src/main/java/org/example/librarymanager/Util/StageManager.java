package org.example.librarymanager.Util;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class StageManager {

    private static final Map<String, Stage> stages = new HashMap<>();

    // Add a stage with a unique name
    public static void addStage(String name, Stage stage) {
        stages.put(name, stage);
    }

    // Retrieve a stage by name
    public static Stage getStage(String name) {
        return stages.get(name);
    }

    // Remove a stage when it is closed
    public static void removeStage(String name) {
        stages.remove(name);
    }

    // Get all available stages
    public static Map<String, Stage> getAllStages() {
        return stages;
    }
}
