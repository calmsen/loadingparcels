package ru.calmsen.loadingparcels.view;

import ru.calmsen.loadingparcels.model.domain.Truck;

import java.util.List;

/**
 * Текстовое представление для списка машин
 */
public class TxtTrucksView implements TrucksView {
    /**
     * Возвращает данные для представления списка машин в txt.
     *
     * @param trucks список машин
     * @return данные в виде txt
     */
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
            for (var parcel : truck.getParcels()) {
                var y = truck.getHeight() - 1 - parcel.getPositionY();//переворачиваем
                var x = parcel.getPositionX();
                for (int i = parcel.getParcel().getHeight() - 1, currenY = y; i >= 0; i--, currenY--) {
                    for (int j = 0, currentX = x; j < parcel.getParcel().getWidth(i); j++, currentX++) {
                        if (parcel.getParcel().getContent().get(i).get(j) == ' '){
                            continue;
                        }
                        content[currenY][currentX] = parcel.getParcel().getContent().get(i).get(j);
                    }
                }
            }

            // передадим содержимое на вывод
            if (!output.isEmpty()) {
                output.append("\n\n");
            }

            for (var row : content) {
                output.append("+").append(new String(row)).append("+").append("\n");
            }
            output.append("+".repeat(truck.getWidth() + 2));
        }

        return output.toString();
    }
}
