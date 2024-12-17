package calmsen.view;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.Truck;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class TrucksView {
    private final List<Truck> trucks;

    public void showTrucks(){
        for (var truck : trucks){
            System.out.println();
            var loadingHeight = truck.getParcels().stream().mapToInt(Parcel::getHeight).sum();
            var emptySpaceHeight =  truck.getHeight() - loadingHeight;
            while(emptySpaceHeight > 0){
                System.out.println("+" + " ".repeat(truck.getWidth()) + "+");
                emptySpaceHeight--;
            }

            for (var parcel : truck.getParcels()){
                for (var row : parcel.getContent()){
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
