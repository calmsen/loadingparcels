package ru.calmsen.loadingparcels.view;

import ru.calmsen.loadingparcels.model.domain.Truck;

import java.util.List;

public class TxtTrucksView implements TrucksView {
    public String getOutputData(List<Truck> trucks) {
        var output = new StringBuilder();
        for (var truck : trucks) {
            // заполним все пространство пробелами
            var content = new char[truck.getHeight()][truck.getWidth()];
            for (int i = 0; i < truck.getHeight(); i++) {
                for (int j = 0; j < truck.getWidth(); j++) {
                    content[i][j] = ' ';
                }
            }

            // заполним пространство посылками
            for (var box : truck.getBoxes()) {
                var y = truck.getHeight() - 1 - box.getPositionY();//переворачиваем
                var x = box.getPositionX();
                for (int i = box.getBox().getHeight() - 1, currenY = y; i >= 0; i--, currenY--) {
                    for (int j = 0, currentX = x; j < box.getBox().getWidth(i); j++, currentX++) {
                        content[currenY][currentX] = box.getBox().getContent().get(i).get(j);
                    }
                }
            }

            // передадим содержимое на вывод
            output.append("\n");
            for (var row : content) {
                output.append("+").append(new String(row)).append("+").append("\n");
            }
            output.append("+".repeat(truck.getWidth() + 2)).append("\n");
        }

        return output.toString();
    }
}
