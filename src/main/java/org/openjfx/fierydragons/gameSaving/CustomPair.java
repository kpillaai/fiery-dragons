package org.openjfx.fierydragons.gameSaving;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CustomPair<K, V> {
    private K key;
    private V value;

    /**
     * @author  Krishna Pillaai Manogaran
     * @desc    Using a custom pair class as Jackson (serializer) cannot serialize Java pairs
     */
    public CustomPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @JsonCreator
    public static <K, V> CustomPair<K, V> of(@JsonProperty("key") K key, @JsonProperty("value") V value) {
        return new CustomPair<>(key, value);
    }

}
