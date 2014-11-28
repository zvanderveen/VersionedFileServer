package DistributedVersionedFileServer.VectorFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

import com.google.common.base.Throwables;

public class VectorClock {
    Map<String, Integer> vectorClockMap;

    public VectorClock() {
        vectorClockMap = new HashMap<String, Integer>();
    }

    public VectorClock(String parseString) {
        vectorClockMap = parseString(parseString);
    }

    public VectorClock(Map<String, Integer> vectorMap) {
        this.vectorClockMap = vectorMap;
    }

    public boolean conflict(VectorClock newVectorClock) {
        return newVectorClock.getVersion() <= getVersion();
    }

    public void increment(String string) {
        vectorClockMap.put(string, getVersion() + 1);
    }

    public int getVersion() {
        if (vectorClockMap.size() == 0) {
            return 0;
        }

        return Collections.max(vectorClockMap.values());
    }

    public Set<String> getMaxKeys() {
        int version = getVersion();
        Set<String> ret = new HashSet<String>();

        for (Map.Entry<String, Integer> entry : vectorClockMap.entrySet()) {
            if (entry.getValue() == version) {
                ret.add(entry.getKey());
            }
        }

        return ret;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(vectorClockMap);
        } catch (IOException e) {
            e.printStackTrace();
            Throwables.propagate(e);
        }

        return "";
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
