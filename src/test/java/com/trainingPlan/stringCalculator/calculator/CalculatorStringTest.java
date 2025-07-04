package com.trainingPlan.stringCalculator.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorStringTest {

    private CalculatorString calculator;

    @BeforeEach
    void setUp() {
        calculator = new CalculatorString();
    }

    @Test
    void emptyStringShouldReturnZero() {
        assertEquals("0", calculator.add(""));
    }

    @Test
    void singleNumberShouldReturnItself() {
        assertEquals("5", calculator.add("5"));
    }

    @Test
    void twoNumbersShouldReturnSum() {
        assertEquals("8", calculator.add("3,5"));
    }

    @Test
    void multipleNumbersShouldReturnSum() {
        assertEquals("15", calculator.add("1,2,3,4,5"));
    }

    @Test
    void numbersWithDecimalShouldReturnSum() {
        assertEquals("3.3", calculator.add("1.1,2.2"));
    }

    @Test
    void newlineAsSeparatorShouldWork() {
        assertEquals("6", calculator.add("1\n2,3"));
    }

    @Test
    void newlineAtInvalidPositionShouldThrowError() {
        String input = "175.2,\n35";
        String expected = "Number expected but '\\n' found at position 6.";
        assertEquals(expected, calculator.add(input));
    }

    @Test
    void missingNumberAtEndShouldReturnError() {
        String input = "1,3,";
        String expected = "Number expected but EOF found.";
        assertEquals(expected, calculator.add(input));
    }

    @Test
    void customDelimiterStringShouldWork() {
        assertEquals("3", calculator.add("//;\n1;2"));
        assertEquals("6", calculator.add("//|\n1|2|3"));
        assertEquals("5", calculator.add("//sep\n2sep3"));
    }

    @Test
    void mixedSeparatorsWithCustomDelimiterShouldReturnError() {
        String input = "//|\n1|2,3";
        String expected = "'|' expected but ',' found at position 7.";  // FIXED position
        assertEquals(expected, calculator.add(input));
    }

    @Test
    void negativeNumberShouldReturnError() {
        assertEquals("Negative not allowed : -1", calculator.add("-1,2"));
    }

    @Test
    void multipleNegativesShouldReturnError() {
        assertEquals("Negative not allowed : -4, -5", calculator.add("2,-4,-5"));
    }

    @Test
    void multipleErrorsShouldBeReturnedTogether() {
        String input1 = "-1,,2";
        String expected1 = "Negative not allowed : -1\nNumber expected but ',' found at position 3.";
        assertEquals(expected1, calculator.add(input1));

        String input2 = "-1,,-2";
        String expected2 = "Negative not allowed : -1\nNumber expected but ',' found at position 3.\nNegative not allowed : -2";
        assertEquals(expected2, calculator.add(input2));
    }
}
