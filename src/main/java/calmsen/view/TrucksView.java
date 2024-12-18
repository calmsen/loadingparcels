package calmsen.view;

import calmsen.model.domain.Box;
import calmsen.model.domain.Truck;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class TrucksView {
    public void showTrucks(List<Truck> trucks){
        for (var truck : trucks){
            System.out.println();
            var loadingHeight = truck.getBoxes().stream().mapToInt(Box::getHeight).sum();
            var emptySpaceHeight =  truck.getHeight() - loadingHeight;
            while(emptySpaceHeight > 0){
                System.out.println("+" + " ".repeat(truck.getWidth()) + "+");
                emptySpaceHeight--;
            }

            for (var box : truck.getBoxes()){
                for (var row : box.getContent()){
                    var extraSpacesForWidth = truck.getWidth() - row.size();
                    System.out.println("+" + mapRowToString(row) + " ".repeat(extraSpacesForWidth) + "+");
                }
            }
            System.out.println("+" + "+".repeat(truck.getWidth()) + "+");
        }
    }

    private static String mapRowToString(List<Character> row){
        var sb = new StringBuilder();
        for (Character ch : row) {
            sb.append(ch);
        }
        return sb.toString();
    }
}
