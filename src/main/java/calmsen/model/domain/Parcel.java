package calmsen.model.domain;

import calmsen.model.domain.enums.ParcelDimensionsType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public class Parcel {
    private final List<List<Character>> content;
    private final int height;
    private final int dimensions;
    private final ParcelDimensionsType dimensionsType;

    public Parcel(List<List<Character>> content) {
        this(content, false);

    }

    public Parcel(List<List<Character>> content, boolean isExtra) {
        this.content = content;
        this.height = content.size();
        this.dimensions = (int)content.stream()
                .flatMap(Collection::stream)
                .count();

        if (!isExtra) {
            this.dimensionsType = ParcelDimensionsType.findDimensionsType(this.dimensions);

        } else {
            this.dimensionsType = null;
        }

    }

    public int getWidth(int rowNumber) {
        return content.get(rowNumber).size();
    }

    @Override
    public String toString(){
        var sb = new StringBuilder();
        for (List<Character> row : content) {
            for (Character character : row) {
                sb.append(character);
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
