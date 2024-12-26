package ru.calmsen.loadingparcels.model.domain.enums;

import lombok.Getter;

/**
 * Данное перечисление необходимо для алгоритма: EfficientLoadingAlgorithm (Максимально плотная погрузка).
 */
@Getter
public enum DimensionsType {
    Dimensions1X1(1, 1),
    Dimensions2X1(2, 1),
    Dimensions3X1(3, 1),
    Dimensions4X1(4, 1),
    Dimensions5X1(5, 1),
    Dimensions3X2(3, 2),
    Dimensions3X1And4X1(4, 2, 7),
    Dimensions4X2(4, 2),
    Dimensions3X3(3, 3, DimensionsType.Dimensions3X1, DimensionsType.Dimensions3X2, JoinType.VERTICAL),

    Dimensions2X2(2, 2, DimensionsType.Dimensions2X1, DimensionsType.Dimensions2X1, JoinType.VERTICAL),
    Dimensions3X1And2X1(4, 2, 5, DimensionsType.Dimensions3X1, DimensionsType.Dimensions2X1, JoinType.VERTICAL),

    Dimensions6X1(6, 1, new JoinPair[]{
            new JoinPair(DimensionsType.Dimensions5X1, DimensionsType.Dimensions1X1, JoinType.HORIZONTAL),
            new JoinPair(DimensionsType.Dimensions4X1, DimensionsType.Dimensions2X1, JoinType.HORIZONTAL)
    }),
    Dimensions6X2(6, 2, new JoinPair[]{
            new JoinPair(DimensionsType.Dimensions3X1And4X1, DimensionsType.Dimensions3X1And2X1, JoinType.HORIZONTAL),
            new JoinPair(DimensionsType.Dimensions3X2, DimensionsType.Dimensions3X2, JoinType.HORIZONTAL),
            new JoinPair(DimensionsType.Dimensions4X2, DimensionsType.Dimensions2X2, JoinType.HORIZONTAL),
            new JoinPair(DimensionsType.Dimensions6X1, DimensionsType.Dimensions6X1, JoinType.VERTICAL),
    }),
    Dimensions6X3(6, 3, new JoinPair[]{
            new JoinPair(DimensionsType.Dimensions3X3, DimensionsType.Dimensions3X3, JoinType.HORIZONTAL),
            new JoinPair(DimensionsType.Dimensions6X1, DimensionsType.Dimensions6X2, JoinType.VERTICAL)
    }),
    Dimensions6X4(6, 4, new JoinPair[]{
            new JoinPair(DimensionsType.Dimensions6X1, DimensionsType.Dimensions6X3, JoinType.VERTICAL),
            new JoinPair(DimensionsType.Dimensions6X2, DimensionsType.Dimensions6X2, JoinType.VERTICAL)
    }),

    Dimensions2X1L2(2, 1, DimensionsType.Dimensions1X1, DimensionsType.Dimensions1X1, JoinType.HORIZONTAL),
    Dimensions3X1L2(3, 1, DimensionsType.Dimensions2X1L2, DimensionsType.Dimensions1X1, JoinType.HORIZONTAL),
    Dimensions3X2L2(3, 2, DimensionsType.Dimensions3X1L2, DimensionsType.Dimensions3X1L2, JoinType.VERTICAL),
    Dimensions3X3L2(3, 3, DimensionsType.Dimensions3X1L2, DimensionsType.Dimensions3X2L2, JoinType.VERTICAL),

    Dimensions2X2L2(2, 2, DimensionsType.Dimensions2X1L2, DimensionsType.Dimensions2X1L2, JoinType.VERTICAL),
    Dimensions3X1And2X1L2(4, 2, 5, DimensionsType.Dimensions3X1L2, DimensionsType.Dimensions2X1L2, JoinType.VERTICAL),

    Dimensions6X1L2(6, 1, new JoinPair[]{
            new JoinPair(DimensionsType.Dimensions5X1, DimensionsType.Dimensions1X1, JoinType.HORIZONTAL),
            new JoinPair(DimensionsType.Dimensions4X1, DimensionsType.Dimensions2X1L2, JoinType.HORIZONTAL)
    }),
    Dimensions6X2L2(6, 2, new JoinPair[]{
            new JoinPair(DimensionsType.Dimensions3X1And4X1, DimensionsType.Dimensions3X1And2X1L2, JoinType.HORIZONTAL),
            new JoinPair(DimensionsType.Dimensions3X2, DimensionsType.Dimensions3X2L2, JoinType.HORIZONTAL),
            new JoinPair(DimensionsType.Dimensions4X2, DimensionsType.Dimensions2X2L2, JoinType.HORIZONTAL),
            new JoinPair(DimensionsType.Dimensions6X1, DimensionsType.Dimensions6X1L2, JoinType.VERTICAL),
    }),
    Dimensions6X3L2(6, 3, new JoinPair[]{
            new JoinPair(DimensionsType.Dimensions3X3, DimensionsType.Dimensions3X3L2, JoinType.HORIZONTAL),
            new JoinPair(DimensionsType.Dimensions6X1, DimensionsType.Dimensions6X2L2, JoinType.VERTICAL)
    }),
    Dimensions6X4L2(6, 4, new JoinPair[]{
            new JoinPair(DimensionsType.Dimensions6X1, DimensionsType.Dimensions6X3L2, JoinType.VERTICAL),
            new JoinPair(DimensionsType.Dimensions6X2, DimensionsType.Dimensions6X2L2, JoinType.VERTICAL)
    }),

    Dimensions6X6(6, 6, new JoinPair[]{
            new JoinPair(DimensionsType.Dimensions6X3, DimensionsType.Dimensions6X3, JoinType.VERTICAL),
            new JoinPair(DimensionsType.Dimensions6X2, DimensionsType.Dimensions6X4, JoinType.VERTICAL),
            new JoinPair(DimensionsType.Dimensions6X3, DimensionsType.Dimensions6X3L2, JoinType.VERTICAL),
            new JoinPair(DimensionsType.Dimensions6X2, DimensionsType.Dimensions6X4L2, JoinType.VERTICAL)
    }),
    ;

    private final int width;
    private final int height;
    private final int dimensions;
    private final JoinPair[] joinPairs;

    DimensionsType(int width, int height) {
        this.width = width;
        this.height = height;
        this.dimensions = width * height;

        this.joinPairs = new JoinPair[0];
    }

    DimensionsType(int width, int height, int dimensions) {
        this.width = width;
        this.height = height;
        this.dimensions = dimensions;

        this.joinPairs = new JoinPair[0];
    }

    DimensionsType(int width, int height, DimensionsType firstDimensions, DimensionsType secondDimensions, JoinType joinType) {
        this.width = width;
        this.height = height;
        this.dimensions = width * height;
        var joinPair = new JoinPair(firstDimensions, secondDimensions, joinType);
        this.joinPairs = new JoinPair[]{joinPair};
    }

    DimensionsType(int width, int height, int dimensions, DimensionsType firstDimensions, DimensionsType secondDimensions, JoinType joinType) {
        this.width = width;
        this.height = height;
        this.dimensions = dimensions;
        var joinPair = new JoinPair(firstDimensions, secondDimensions, joinType);
        this.joinPairs = new JoinPair[]{joinPair};
    }

    DimensionsType(int width, int height, JoinPair[] joinPairs) {
        this.width = width;
        this.height = height;
        this.dimensions = width * height;
        this.joinPairs = joinPairs;
    }

    @Getter
    public static class JoinPair {
        private final DimensionsType firstDimensions;
        private final DimensionsType secondDimensions;
        private final JoinType joinType;

        public JoinPair(DimensionsType firstDimensions, DimensionsType secondDimensions, JoinType joinType) {
            this.firstDimensions = firstDimensions;
            this.secondDimensions = secondDimensions;
            this.joinType = joinType;
        }
    }
}
