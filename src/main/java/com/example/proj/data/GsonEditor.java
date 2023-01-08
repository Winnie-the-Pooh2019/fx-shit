package com.example.proj.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GsonEditor {
    public static <T> T read(String filename, Type type) throws IOException {
        File file = new File(filename);
        Path path = Paths.get(file.getAbsolutePath());

        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(path);

        return gson.fromJson(reader, type);
    }

    public static <T> T absoluteRead(String absolutePath, Type type) throws IOException {
        Path path = Paths.get(absolutePath);

        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(path);

        return gson.fromJson(reader, type);
    }

    public static <T> void write(String filename, T obj) throws IOException {
        File file = new File(filename);
        var path = Paths.get(file.getAbsolutePath());
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            JsonElement tree = gson.toJsonTree(obj);
            gson.toJson(tree, writer);
        }
    }
}
