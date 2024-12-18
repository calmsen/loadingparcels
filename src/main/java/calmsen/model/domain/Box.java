package calmsen.model.domain;

import calmsen.model.domain.enums.BoxDimensionsType;

import java.util.List;

public interface Box {
    List<List<Character>> getContent();
    int getHeight();
    int getWidth(int rowNumber);
    int getDimensions();
    BoxDimensionsType getDimensionsType();
}
