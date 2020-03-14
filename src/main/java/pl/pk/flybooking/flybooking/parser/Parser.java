package pl.pk.flybooking.flybooking.parser;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface Parser<T> {
    public List<T> parse(com.fasterxml.jackson.databind.JsonNode jsonNodeData) throws JsonProcessingException;
}
