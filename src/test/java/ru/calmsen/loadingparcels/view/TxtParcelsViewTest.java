package ru.calmsen.loadingparcels.view;

import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.model.domain.Box;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class TxtParcelsViewTest {
    @Test
    void getOutputData_SingleBox_CorrectlyFormatsOutput() {
        // Arrange
        List<List<Character>> content = List.of(
                List.of('a', 'b'),
                List.of('c', 'd')
        );
        var singleBox = new Box(content);
        List<Box> boxes = List.of(singleBox);

        // Act
        String result = new TxtParcelsView().getOutputData(boxes);

        // Assert
        assertEquals("ab\ncd", result); // Проверяем вывод для одного бокса
    }

    @Test
    void getOutputData_MultipleBoxes_FormatsEachBoxSeparately() {
        // Arrange
        List<List<Character>> content1 = List.of(
                List.of('a', 'b'),
                List.of('c', 'd')
        );
        List<List<Character>> content2 = List.of(
                List.of('e', 'f'),
                List.of('g', 'h')
        );
        Box box1 = new Box(content1);
        Box box2 = new Box(content2);
        List<Box> boxes = List.of(box1, box2);

        // Act
        String result = new TxtParcelsView().getOutputData(boxes);

        // Assert
        assertEquals("ab\ncd\n\nef\ngh", result); // Проверяем вывод для нескольких боксов
    }
}