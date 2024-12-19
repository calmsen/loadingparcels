package ru.calmsen.view;

import ru.calmsen.model.domain.Box;
import ru.calmsen.model.domain.Truck;
import ru.calmsen.util.ConsoleLinesWriter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TrucksView {
    private final ConsoleLinesWriter consoleLinesWriter;

    public void showTrucks(List<Truck> trucks) {
        var linesToOutput = new ArrayList<String>();
        for (var truck : trucks) {
            linesToOutput.add("\n");
            var loadingHeight = truck.getBoxes().stream().mapToInt(Box::getHeight).sum();
            var emptySpaceHeight = truck.getHeight() - loadingHeight;
            while (emptySpaceHeight > 0) {
                linesToOutput.add("+" + " ".repeat(truck.getWidth()) + "+");
                emptySpaceHeight--;
            }

            for (var box : truck.getBoxes()) {
                for (var row : box.getContent()) {
                    var extraSpacesForWidth = truck.getWidth() - row.size();
                    linesToOutput.add("+" + mapRowToString(row) + " ".repeat(extraSpacesForWidth) + "+");
                }
            }
            linesToOutput.add("+" + "+".repeat(truck.getWidth()) + "+");
            this.consoleLinesWriter.writeAllLines(linesToOutput);
        }
    }

    private String mapRowToString(List<Character> row) {
        var sb = new StringBuilder();
        for (Character ch : row) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
