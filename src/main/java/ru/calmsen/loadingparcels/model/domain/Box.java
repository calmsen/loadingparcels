package ru.calmsen.loadingparcels.model.domain;

import lombok.Getter;
import ru.calmsen.loadingparcels.model.domain.enums.JoinType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class Box {
    @Getter
    private final List<List<Character>> content;
    @Getter
    private final int dimensions;

    @Getter
    private final List<Box> inner;

    @Getter
    private JoinType joinType;

    public Box(List<List<Character>> content) {
        this.content = content;
        this.dimensions = (int) content.stream()
                .flatMap(Collection::stream)
                .count();
        this.inner = new ArrayList<>();

    }
    public Box(Box firstBox, Box secondBox, JoinType joinType) {
        this.joinType = joinType;
        if (joinType == JoinType.HORIZONTAL) {
            this.content = new ArrayList<>();
            for (int i = 0, j = 0; i < firstBox.getHeight(); i++) {
                this.content.add(Stream.concat(
                        firstBox.getContent().get(i).stream(),
                        secondBox.getContent().get(i).stream()
                ).toList());
            }
        }
        else {
            this.content = new ArrayList<>(firstBox.getContent());
            content.addAll(secondBox.getContent());
        }

        this.dimensions = (int) content.stream()
                .flatMap(Collection::stream)
                .count();
        this.inner = List.of(firstBox, secondBox);
    }

    public int getHeight() {
        return content.size();
    }

    public int getWidth(int rowNumber) {
        return content.get(rowNumber).size();
    }

    public int getWidth() {
        var maxWidth = 0;
        for (int row = 0; row < getHeight(); row++) {
            if (maxWidth < content.get(row).size()) {
                maxWidth = content.get(row).size();
            }
        }
        return maxWidth;
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
