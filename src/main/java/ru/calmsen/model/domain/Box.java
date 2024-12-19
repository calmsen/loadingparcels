package ru.calmsen.model.domain;

import lombok.Getter;
import ru.calmsen.model.domain.enums.BoxDimensionsType;

import java.util.Collection;
import java.util.List;

public class Box {
    @Getter
    private final List<List<Character>> content;

    /**
     * Коробка является "виртуальной". Она может содержать несколько посылок.
     */
    @Getter
    private final boolean isExtra;
    @Getter
    private final int dimensions;
    @Getter
    private final BoxDimensionsType dimensionsType;

    public Box(List<List<Character>> content) {
        this(content, false);
    }
    public Box(List<List<Character>> content, boolean isExtra) {
        this.content = content;
        this.isExtra = isExtra;
        this.dimensions = (int) content.stream()
                .flatMap(Collection::stream)
                .count();
        this.dimensionsType = BoxDimensionsType.fromDimensions(this.getDimensions());
    }

    public int getHeight() {
        return content.size();
    }

    public int getWidth(int rowNumber) {
        return content.get(rowNumber).size();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("\n");
        for (var row : content) {
            for (Character ch : row) {
                sb.append(ch);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
