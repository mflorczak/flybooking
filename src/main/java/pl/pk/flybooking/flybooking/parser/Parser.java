package pl.pk.flybooking.flybooking.parser;

import java.util.List;

public interface Parser<T> {
    public List<T> parse(com.fasterxml.jackson.databind.JsonNode jsonNode);
}
