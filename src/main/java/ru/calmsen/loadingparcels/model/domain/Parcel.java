package ru.calmsen.loadingparcels.model.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.PostLoad;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Доменная модель посылки.
 */
@Entity
@Table(name = "parcel")
@Getter
@NoArgsConstructor
@FieldNameConstants
public class Parcel {
    @Transient
    private List<List<Character>> content;
    @Transient
    private int width;
    @Transient
    private int height;
    @Transient
    private int dimensions;

    @Id
    private String name;
    private String form;
    private char symbol;

    public Parcel(List<List<Character>> content) {
        this.content = content;
        this.dimensions = toDimensions(content);
        this.width = toWidth(content);
        this.height = toHeight(content);
        this.form = toForm(content);
        this.symbol = toSymbol(content);
        this.name = toName(symbol);
    }

    public Parcel(String name, String form, char symbol) {
        this.content = toContent(form, symbol);
        this.dimensions = toDimensions(content);
        this.width = toWidth(content);
        this.height = toHeight(content);
        this.form = form;
        this.symbol = symbol;
        this.name = name;
    }

    @PostLoad
    private void onLoad() {
        this.content = toContent(form, symbol);
        this.dimensions = toDimensions(content);
        this.width = toWidth(content);
        this.height = toHeight(content);
    }

    /**
     * Получает ширину посылки для указанного ряда
     * @param rowNumber номер ряда. Начинается с 0
     * @return ширина ряда
     */
    public int getWidth(int rowNumber) {
        return content.get(rowNumber).size();
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("name: ").append(name).append('\n');
        sb.append("content: ").append('\n');
        for (int i = 0; i < content.size(); i++) {
            var row = content.get(i);
            for (Character ch : row) {
                sb.append(ch);
            }
            if (i < content.size() - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    private List<List<Character>> toContent(String form, char symbol) {
        List<List<Character>> content = new ArrayList<>();
        for (String row : form.split(Pattern.quote("\n"))) {
            content.add(
                    row.chars()
                            .mapToObj(c -> noneSpace((char)c) ? symbol : ' ')
                            .collect(Collectors.toList())
            );
        }
        return content;
    }

    private int toDimensions(List<List<Character>> content) {
        var symbols = content.stream()
                .flatMap(Collection::stream)
                .filter(this::noneSpace)
                .toList();
        return symbols.size();
    }

    private boolean noneSpace(Character character) {
        return character != ' ';
    }

    private int toWidth(List<List<Character>> content) {
        var maxWidth = 0;
        for (List<Character> characters : content) {
            if (maxWidth < characters.size()) {
                maxWidth = characters.size();
            }
        }
        return maxWidth;
    }

    private int toHeight(List<List<Character>> content) {
        return content.size();
    }

    private String toForm(List<List<Character>> content) {
        var sb = new StringBuilder();
        for (int i = 0; i < content.size(); i++) {
            var row = content.get(i);
            for (Character ch : row) {
                sb.append(noneSpace(ch) ? 'x' : ' ');
            }

            if (i < content.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private char toSymbol(List<List<Character>> content) {
        return content.stream()
                .flatMap(Collection::stream)
                .filter(this::noneSpace)
                .findFirst().orElse('x');
    }

    private String toName(char symbol) {
        return String.format("Посылка тип %c", symbol);
    }
}
