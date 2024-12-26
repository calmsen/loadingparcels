package ru.calmsen.loadingparcels.view;

import ru.calmsen.loadingparcels.model.domain.Box;

import java.util.List;

public class TxtParcelsView implements ParcelsView {
    @Override
    public String getOutputData(List<Box> boxes) {
        var output = new StringBuilder();
        for (var box : boxes) {
            for (var row : box.getContent()){
                output.append(mapRowToString(row)).append("\n");
            }
            output.append("\n");
        }

        return output.toString().trim();
    }

    private String mapRowToString(List<Character> row) {
        var sb = new StringBuilder();
        for (Character ch : row) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
