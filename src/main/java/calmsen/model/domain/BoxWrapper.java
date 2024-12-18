package calmsen.model.domain;

import calmsen.model.domain.enums.BoxDimensionsType;
import lombok.Getter;

import java.util.Collection;
import java.util.List;

@Getter
public class BoxWrapper implements Box {
    public BoxWrapper(List<List<Character>> content) {
        this.content = content;
        this.dimensions = (int)content.stream()
                .flatMap(Collection::stream)
                .count();
        this.dimensionsType = null;
    }

    private final List<List<Character>> content;
    private final int dimensions;
    private final BoxDimensionsType dimensionsType;

    @Override
    public int getHeight() {
        return content.size();
    }

    @Override
    public int getWidth(int rowNumber) {
        return content.get(rowNumber).size();
    }
}
