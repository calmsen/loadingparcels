package calmsen.model.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
public enum BoxDimensionsType {
    DimensionsNone(0, null, null, null, false),
    Dimensions1X1(1, null, null, null, false),
    Dimensions2X1(2, BoxDimensionsType.Dimensions1X1, BoxDimensionsType.Dimensions1X1, BoxJoinType.HORIZONTAL, false),
    Dimensions3X1(3, BoxDimensionsType.Dimensions1X1, BoxDimensionsType.Dimensions2X1, BoxJoinType.HORIZONTAL, false),
    Dimensions3X2(6, BoxDimensionsType.Dimensions3X1, BoxDimensionsType.Dimensions3X1, BoxJoinType.VERTICAL, false),
    Dimensions3X3(9, BoxDimensionsType.Dimensions3X1, BoxDimensionsType.Dimensions3X2, BoxJoinType.VERTICAL, false),
    Dimensions4X1(4, BoxDimensionsType.Dimensions3X1, BoxDimensionsType.Dimensions1X1, BoxJoinType.HORIZONTAL, false),
    Dimensions4X2(8, null, null, null, false),
    Dimensions5X1(5, BoxDimensionsType.Dimensions3X1, BoxDimensionsType.Dimensions2X1, BoxJoinType.HORIZONTAL, false),
    Dimensions7(7, null, null, null, false),

    Dimensions2X2(4, BoxDimensionsType.Dimensions2X1, BoxDimensionsType.Dimensions2X1, BoxJoinType.VERTICAL, true),
    Dimensions3X1And2X1(5, BoxDimensionsType.Dimensions3X1, BoxDimensionsType.Dimensions2X1, BoxJoinType.VERTICAL, true),

    Dimensions6X1( 6, null, null, null, true),
    Dimensions6X1From5X1( 6, BoxDimensionsType.Dimensions5X1, BoxDimensionsType.Dimensions1X1, BoxJoinType.HORIZONTAL, true),
    Dimensions6X1From4X1( 6, BoxDimensionsType.Dimensions4X1, BoxDimensionsType.Dimensions2X1, BoxJoinType.HORIZONTAL, true),
    Dimensions6X1From3X1( 6, BoxDimensionsType.Dimensions3X1, BoxDimensionsType.Dimensions3X1, BoxJoinType.HORIZONTAL, true),

    Dimensions6X2(12, BoxDimensionsType.Dimensions6X1, BoxDimensionsType.Dimensions6X1, BoxJoinType.VERTICAL,true),
    Dimensions6X2From3X2(12, BoxDimensionsType.Dimensions3X2, BoxDimensionsType.Dimensions3X2, BoxJoinType.HORIZONTAL,true),
    Dimensions6X2From7(12, BoxDimensionsType.Dimensions7, BoxDimensionsType.Dimensions3X1And2X1, BoxJoinType.HORIZONTAL, true),
    Dimensions6X2From4X2( 12, BoxDimensionsType.Dimensions4X2, BoxDimensionsType.Dimensions2X2, BoxJoinType.HORIZONTAL, true),

    Dimensions6X3(18, BoxDimensionsType.Dimensions6X1, BoxDimensionsType.Dimensions6X2, BoxJoinType.VERTICAL,true),
    Dimensions6X3From3X3(18, BoxDimensionsType.Dimensions3X3, BoxDimensionsType.Dimensions3X3, BoxJoinType.HORIZONTAL, true),

    Dimensions6X4( 24, BoxDimensionsType.Dimensions6X2, BoxDimensionsType.Dimensions6X2, BoxJoinType.VERTICAL, true),

    Dimensions6X6From6X3( 36, BoxDimensionsType.Dimensions6X3, BoxDimensionsType.Dimensions6X3, BoxJoinType.VERTICAL, true),
    Dimensions6X6From6X4( 36, BoxDimensionsType.Dimensions6X2, BoxDimensionsType.Dimensions6X4, BoxJoinType.VERTICAL, true),
    ;

    private final int dimensions;

    private final JoinPair joinPair;
    private final BoxJoinType joinType;
    private final boolean isExtra;

    BoxDimensionsType(
            int dimensions,
            BoxDimensionsType firstDimensionsForJoin,
            BoxDimensionsType secondDimensionsForJoin,
            BoxJoinType joinType,
            boolean isExtra) {
        this.dimensions = dimensions;
        this.isExtra = isExtra;
        this.joinPair = new JoinPair(firstDimensionsForJoin, secondDimensionsForJoin);
        this.joinType = joinType;
    }

    public static BoxDimensionsType findDimensionsType(int dimensions) {
        return Arrays.stream(BoxDimensionsType.values())
                .filter(x -> !x.isExtra() && x.getDimensions() == dimensions)
                .findFirst()
                .orElse(null);
    }

    @Getter
    @RequiredArgsConstructor
    public static class JoinPair{
        private final BoxDimensionsType firstDimensionsForJoin;
        private final BoxDimensionsType secondDimensionsForJoin;
    }
}
