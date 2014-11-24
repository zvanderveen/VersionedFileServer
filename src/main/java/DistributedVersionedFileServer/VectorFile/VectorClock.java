package DistributedVersionedFileServer.VectorFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import com.google.common.base.Throwables;

public class VectorClock {
    Map<String, Integer> vectorMap;

    public VectorClock(String string) {
        vectorMap = parseString(string);
    }

    public VectorClock(File file) {
        try (
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
        ) {
            String fileContents = bufferedReader.readLine();
            vectorMap = parseString(fileContents);
        } catch (IOException e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }
    }

    public VectorClock(Map<String, Integer> vectorMap) {
        this.vectorMap = vectorMap;
    }

    public boolean conflict(VectorClock newVector) {
        return newVector.getVersion() <= getVersion();
    }

    public void increment(String string) {
        vectorMap.put(string, getVersion() + 1);
    }

    public int getVersion() {
        int maxVersion = 0;

        for (Integer version : vectorMap.values()) {
            maxVersion = Math.max(maxVersion, version);
        }

        return maxVersion;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(vectorMap);
        } catch (IOException e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }

        return "";
    }

    public void writeToFile(File file) {
        try (
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        ) {
            String fileContents = toString();
            bufferedWriter.write(fileContents);
        } catch (IOException e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }
    }

    private Map<String, Integer> parseString(String string) {
        try {
            TypeReference<Map<String, Integer>> typeRef = new TypeReference<Map<String, Integer>>() {};
            return new ObjectMapper().readValue(string, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }

        return Collections.emptyMap();
    }
}
