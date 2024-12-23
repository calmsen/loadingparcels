package ru.calmsen.loadingparcels.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TxtTrucksViewTest {
//    private TxtTrucksView view;
//
//    @BeforeEach
//    void setUp() {
//        view = new TxtTrucksView();
//    }
//
//    @Test
//    void shouldShowEmptyTruck() {
//        List<Truck> trucks = List.of(new Truck(6, 6, new ArrayList<>()));
//        var result = view.getOutputData(trucks);
//
//        var expectedOutput = Stream.of(
//                "\n",
//                "+      +",
//                "+      +",
//                "+      +",
//                "+      +",
//                "+      +",
//                "+      +",
//                "++++++++"
//        ).reduce("", (x, y) -> x + y);
//
//        assertEquals(expectedOutput, result);
//    }
//
//    @Test
//    void shouldShowLoadedTruck() {
//        List<List<Character>> boxContent = List.of(
//                List.of('2', '2'),
//                List.of('2', '2')
//        );
//        Box box = new Box(boxContent);
//        List<Truck> trucks = List.of(new Truck(6, 6, List.of(box)));
//        var result = view.getOutputData(trucks);
//
//        var expectedOutput = Stream.of(
//                "\n",
//                "+      +",
//                "+      +",
//                "+      +",
//                "+      +",
//                "+22    +",
//                "+22    +",
//                "++++++++"
//        ).reduce("", (x, y) -> x + y);
//
//        assertEquals(expectedOutput, result);
//    }
//
//    @Test
//    void shouldShowMultipleTrucks() {
//        List<List<Character>> boxContent1 = List.of(
//                List.of('2', '2'),
//                List.of('2', '2')
//        );
//        Box box1 = new Box(boxContent1);
//
//        List<List<Character>> boxContent2 = List.of(
//                List.of('6', '6', '6'),
//                List.of('6', '6', '6')
//        );
//        Box box2 = new Box(boxContent2);
//
//        List<Truck> trucks = List.of(
//                new Truck(6, 6, List.of(box1)),
//                new Truck(6, 6, List.of(box2))
//        );
//        var result = view.getOutputData(trucks);
//
//        var expectedOutput = Stream.of(
//                "\n",
//                "+      +",
//                "+      +",
//                "+      +",
//                "+      +",
//                "+22    +",
//                "+22    +",
//                "++++++++",
//                "\n",
//                "+      +",
//                "+      +",
//                "+      +",
//                "+      +",
//                "+666   +",
//                "+666   +",
//                "++++++++"
//        ).reduce("", (x, y) -> x + y);
//
//        assertEquals(expectedOutput, result);
//    }
}