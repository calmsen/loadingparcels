package calmsen.model.domain;

import calmsen.model.domain.enums.BoxDimensionsType;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class Parcel implements Box {
    private final List<List<Character>> content;
    private final int dimensions;
    private final BoxDimensionsType dimensionsType;

    public Parcel(List<List<Character>> content) {
        this.content = content;
        this.dimensions = (int)content.stream()
                .flatMap(Collection::stream)
                .count();

        this.dimensionsType = BoxDimensionsType.findDimensionsType(this.dimensions);

    }

    public int getHeight() {
        return content.size();
    }

    public int getWidth(int rowNumber) {
        return content.get(rowNumber).size();
    }
}
