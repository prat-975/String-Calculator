package com.trainingPlan.stringCalculator.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorStringTest {

    private CalculatorString calc;

    @BeforeEach
    void setup() {
        calc = new CalculatorString();
    }

    @Test
    void emptyInputShouldReturnZero() {
        assertEquals("0", calc.add(""));
    }

    @Test
    void singleNumberShouldReturnSameNumber() {
        assertEquals("3", calc.add("3"));
    }

    @Test
    void twoNumbersShouldReturnSum() {
        assertEquals("7", calc.add("3,4"));
    }

    @Test
    void numbersWithNewlineShouldReturnSum() {
        assertEquals("6", calc.add("1\n2,3"));
    }

    @Test
    void numbersWithDecimalShouldReturnSum() {
        assertEquals("3.3", calc.add("1.1,2.2"));
    }

    @Test
    void newlineAtInvalidPositionShouldThrowError() {
        assertEquals("Number expected but ',' found at position 2.", calc.add("1\n,2"));
    }

    @Test
    void customDelimiterShouldBeUsed() {
        assertEquals("6", calc.add("//;\n1;2;3"));
    }

    @Test
    void customDelimiterMissingNewlineShouldReturnError() {
        assertEquals("Invalid input: newline missing after custom delimiter", calc.add("//;1;2"));
    }

    @Test
    void negativeNumberShouldReturnError() {
        assertEquals("Negative not allowed : -3", calc.add("1,-3"));
    }

    @Test
    void multipleNegativesShouldReturnError() {
        assertEquals("Negative not allowed : -1, -4", calc.add("-1,2,-4"));
    }

    @Test
    void mixedSeparatorsWithCustomDelimiterShouldReturnError() {
        assertEquals("'|' expected but ',' found at position 3.", calc.add("//|\n1|2,3"));
    }

    @Test
    void multipleErrorsShouldBeReturnedTogether() {
        String input = "1,\n";
        String expected = "Number expected but '\\n' found at position 2.\nNumber expected but EOF found.";
        assertEquals(expected, calc.add(input));
    }
}
