package calmsen.view;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.Truck;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TrucksView {
    private final List<Truck> trucks;

    public void showTrucks(){
        for (var truck : trucks){
            var loadingHeight = truck.getParcels().stream().mapToInt(Parcel::getHeight).sum();
            var emptySpaceHeight =  truck.getHeight() - loadingHeight;
            while(emptySpaceHeight > 0){
                System.out.println("+      +");
                emptySpaceHeight--;
            }

            for (var parcel : truck.getParcels()){
                for (var innerChars : getCharsForParcel(parcel)){
                    var extraSpacesForWidth = truck.getWidth() - innerChars.length;
                    System.out.println("+" + String.valueOf(innerChars) + " ".repeat(extraSpacesForWidth) + "+");
                }
            }
            System.out.println("++++++++");
            System.out.println();
        }
    }

    private char[][] getCharsForParcel(Parcel parcel) {
        var chars = new char[parcel.getHeight()][parcel.getWidth()];
        for (var i = 0; i < parcel.getHeight(); i++) {
            var innerCharsWidth = i == 0 ? parcel.getTopWidth() : parcel.getWidth();
            var innerChars = String.valueOf(parcel.getType()).repeat(innerCharsWidth)
                    + " ".repeat(parcel.getWidth() - innerCharsWidth);
            chars[i] = innerChars.toCharArray();
        }
        return chars;
    }
}
