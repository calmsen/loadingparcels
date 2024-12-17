package calmsen.model.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
public enum ParcelDimensionsType {
    DimensionsNone(0, null, null, null, false),
    Dimensions1X1(1, null, null, null, false),
    Dimensions2X1(2, ParcelDimensionsType.Dimensions1X1, ParcelDimensionsType.Dimensions1X1, ParcelJoinType.HORIZONTAL, false),
    Dimensions3X1(3, ParcelDimensionsType.Dimensions1X1, ParcelDimensionsType.Dimensions2X1, ParcelJoinType.HORIZONTAL, false),
    Dimensions3X2(6, ParcelDimensionsType.Dimensions3X1, ParcelDimensionsType.Dimensions3X1, ParcelJoinType.VERTICAL, false),
    Dimensions3X3(9, ParcelDimensionsType.Dimensions3X1, ParcelDimensionsType.Dimensions3X2, ParcelJoinType.VERTICAL, false),
    Dimensions4X1(4, ParcelDimensionsType.Dimensions3X1, ParcelDimensionsType.Dimensions1X1, ParcelJoinType.HORIZONTAL, false),
    Dimensions4X2(8, null, null, null, false),
    Dimensions5X1(5, ParcelDimensionsType.Dimensions3X1, ParcelDimensionsType.Dimensions2X1, ParcelJoinType.HORIZONTAL, false),
    Dimensions7(7, null, null, null, false),

    Dimensions2X2(4, ParcelDimensionsType.Dimensions2X1, ParcelDimensionsType.Dimensions2X1, ParcelJoinType.VERTICAL, true),
    Dimensions3X1And2X1(5, ParcelDimensionsType.Dimensions3X1, ParcelDimensionsType.Dimensions2X1, ParcelJoinType.VERTICAL, true),

    Dimensions6X1( 6, null, null, null, true),
    Dimensions6X1From5X1( 6, ParcelDimensionsType.Dimensions5X1, ParcelDimensionsType.Dimensions1X1, ParcelJoinType.HORIZONTAL, true),
    Dimensions6X1From4X1( 6, ParcelDimensionsType.Dimensions4X1, ParcelDimensionsType.Dimensions2X1, ParcelJoinType.HORIZONTAL, true),
    Dimensions6X1From3X1( 6, ParcelDimensionsType.Dimensions3X1, ParcelDimensionsType.Dimensions3X1, ParcelJoinType.HORIZONTAL, true),

    Dimensions6X2(12,ParcelDimensionsType.Dimensions6X1, ParcelDimensionsType.Dimensions6X1,ParcelJoinType.VERTICAL,true),
    Dimensions6X2From3X2(12, ParcelDimensionsType.Dimensions3X2, ParcelDimensionsType.Dimensions3X2,ParcelJoinType.HORIZONTAL,true),
    Dimensions6X2From7(12, ParcelDimensionsType.Dimensions7, ParcelDimensionsType.Dimensions3X1And2X1, ParcelJoinType.HORIZONTAL, true),
    Dimensions6X2From4X2( 12, ParcelDimensionsType.Dimensions4X2, ParcelDimensionsType.Dimensions2X2, ParcelJoinType.HORIZONTAL, true),

    Dimensions6X3(18,ParcelDimensionsType.Dimensions6X1, ParcelDimensionsType.Dimensions6X2,ParcelJoinType.VERTICAL,true),
    Dimensions6X3From3X3(18, ParcelDimensionsType.Dimensions3X3, ParcelDimensionsType.Dimensions3X3, ParcelJoinType.HORIZONTAL, true),

    Dimensions6X4( 24, ParcelDimensionsType.Dimensions6X2, ParcelDimensionsType.Dimensions6X2, ParcelJoinType.VERTICAL, true),

    Dimensions6X6From6X3( 36, ParcelDimensionsType.Dimensions6X3, ParcelDimensionsType.Dimensions6X3, ParcelJoinType.VERTICAL, true),
    Dimensions6X6From6X4( 36, ParcelDimensionsType.Dimensions6X2, ParcelDimensionsType.Dimensions6X4, ParcelJoinType.VERTICAL, true),

    // не стандартные посылки
    Dimensions15From9( 15, ParcelDimensionsType.Dimensions3X3, ParcelDimensionsType.Dimensions3X2, ParcelJoinType.HORIZONTAL, true),
    Dimensions10From8( 10, ParcelDimensionsType.Dimensions4X2, ParcelDimensionsType.Dimensions2X1, ParcelJoinType.HORIZONTAL, true),
    Dimensions9From7( 9, ParcelDimensionsType.Dimensions7, ParcelDimensionsType.Dimensions2X1, ParcelJoinType.HORIZONTAL, true),
    ;

    private final int dimensions;

    private final JoinPair joinPair;
    private final ParcelJoinType joinType;
    private final boolean isExtra;

    ParcelDimensionsType(
            int dimensions,
            ParcelDimensionsType firstDimensionsForJoin,
            ParcelDimensionsType secondDimensionsForJoin,
            ParcelJoinType joinType,
            boolean isExtra) {
        this.dimensions = dimensions;
        this.isExtra = isExtra;
        this.joinPair = new JoinPair(firstDimensionsForJoin, secondDimensionsForJoin);
        this.joinType = joinType;
    }

    public static ParcelDimensionsType findDimensionsType(int dimensions) {
        return Arrays.stream(ParcelDimensionsType.values())
                .filter(x -> !x.isExtra() && x.getDimensions() == dimensions)
                .findFirst()
                .orElse(null);
    }

    @Getter
    @RequiredArgsConstructor
    public static class JoinPair{
        private final ParcelDimensionsType firstDimensionsForJoin;
        private final ParcelDimensionsType secondDimensionsForJoin;
    }
}
