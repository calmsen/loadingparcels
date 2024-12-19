package ru.calmsen.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.calmsen.model.domain.Box;
import ru.calmsen.model.domain.Truck;
import ru.calmsen.util.ConsoleLinesWriter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrucksViewTest {
    private TrucksView view;
    private MockConsoleLinesWriter mockConsoleLinesWriter;

    @BeforeEach
    void setUp() {
        mockConsoleLinesWriter = new MockConsoleLinesWriter();
        view = new TrucksView(mockConsoleLinesWriter);
    }

    @Test
    void shouldShowEmptyTruck() {
        List<Truck> trucks = List.of(new Truck(6, 6, new ArrayList<>()));
        view.showTrucks(trucks);

        List<String> expectedOutput = List.of(
                "\n",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "++++++++"
        );

        assertEquals(expectedOutput, mockConsoleLinesWriter.getWrittenLines());
    }

    @Test
    void shouldShowLoadedTruck() {
        List<List<Character>> boxContent = List.of(
                List.of('2', '2'),
                List.of('2', '2')
        );
        Box box = new Box(boxContent);
        List<Truck> trucks = List.of(new Truck(6, 6, List.of(box)));
        view.showTrucks(trucks);

        List<String> expectedOutput = List.of(
                "\n",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "+22    +",
                "+22    +",
                "++++++++"
        );

        assertEquals(expectedOutput, mockConsoleLinesWriter.getWrittenLines());
    }

    @Test
    void shouldShowMultipleTrucks() {
        List<List<Character>> boxContent1 = List.of(
                List.of('2', '2'),
                List.of('2', '2')
        );
        Box box1 = new Box(boxContent1);

        List<List<Character>> boxContent2 = List.of(
                List.of('6', '6', '6'),
                List.of('6', '6', '6')
        );
        Box box2 = new Box(boxContent2);

        List<Truck> trucks = List.of(
                new Truck(6, 6, List.of(box1)),
                new Truck(6, 6, List.of(box2))
        );
        view.showTrucks(trucks);

        List<String> expectedOutput = List.of(
                "\n",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "+22    +",
                "+22    +",
                "++++++++",
                "\n",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "+666   +",
                "+666   +",
                "++++++++"
        );

        assertEquals(expectedOutput, mockConsoleLinesWriter.getWrittenLines());
    }

    public class MockConsoleLinesWriter extends ConsoleLinesWriter {
        private List<String> writtenLines;

        @Override
        public void writeAllLines(List<String> lines) {
            writtenLines = new ArrayList<>(lines);
        }

        public List<String> getWrittenLines() {
            return writtenLines;
        }
    }
}