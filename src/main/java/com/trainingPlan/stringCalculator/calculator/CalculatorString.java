package com.trainingPlan.stringCalculator.calculator;

import java.util.ArrayList;
import java.util.List;

public class CalculatorString {

    public String add(String input) {
        if (input.isEmpty()) {
            return "0";
        }

        String delimiter = ",";
        int startIndex = 0;

        // Handle custom delimiter
        if (input.startsWith("//")) {
            int delimiterEndIndex = input.indexOf('\n');
            if (delimiterEndIndex == -1) {
                return "Invalid input: newline missing after custom delimiter";
            }
            delimiter = input.substring(2, delimiterEndIndex);
            startIndex = delimiterEndIndex + 1;
        }

        StringBuilder number = new StringBuilder();
        List<Double> numbers = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<String> negatives = new ArrayList<>();

        char[] chars = input.substring(startIndex).toCharArray();
        int position = 0;
        int lastDelimiterOrNewlinePos = -1;
        char lastDelimiterOrNewline = 0;

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            // Start of number
            if (Character.isDigit(c) || c == '.' || c == '-' || (number.length() == 0 && c == '+')) {
                number.append(c);
            }
            // Match delimiter
            else if (input.substring(startIndex + i).startsWith(delimiter)) {
                if (number.length() == 0) {
                    errors.add("Number expected but '" + delimiter + "' found at position " + position + ".");
                } else {
                    String numStr = number.toString();
                    try {
                        double num = Double.parseDouble(numStr);
                        if (num < 0) negatives.add(String.valueOf((int) num));
                        numbers.add(num);
                    } catch (NumberFormatException e) {
                        errors.add("Invalid number: " + numStr);
                    }
                }
                number.setLength(0);
                i += delimiter.length() - 1;
                position += delimiter.length() - 1;
                lastDelimiterOrNewlinePos = position;
                lastDelimiterOrNewline = delimiter.charAt(0);
            }
            // Handle newline
            else if (c == '\n') {
                if (number.length() == 0) {
                    errors.add("Number expected but '\\n' found at position " + position + ".");
                } else {
                    try {
                        double num = Double.parseDouble(number.toString());
                        if (num < 0) negatives.add(String.valueOf((int) num));
                        numbers.add(num);
                    } catch (NumberFormatException e) {
                        errors.add("Invalid number: " + number.toString());
                    }
                }
                number.setLength(0);
                lastDelimiterOrNewlinePos = position;
                lastDelimiterOrNewline = '\n';
            }
            // Unexpected character
            else {
                errors.add("'" + delimiter + "' expected but '" + c + "' found at position " + position + ".");
            }

            position++;
        }

        // Handle trailing delimiter or newline
        if (number.length() == 0 && lastDelimiterOrNewlinePos == chars.length - 1) {
            if (lastDelimiterOrNewline == '\n' || (delimiter.length() == 1 && lastDelimiterOrNewline == delimiter.charAt(0))) {
                errors.add("Number expected but EOF found.");
            }
        } else if (number.length() > 0) {
            try {
                double num = Double.parseDouble(number.toString());
                if (num < 0) negatives.add(String.valueOf((int) num));
                numbers.add(num);
            } catch (NumberFormatException e) {
                errors.add("Invalid number: " + number.toString());
            }
        }

        // Handle negatives
        if (!negatives.isEmpty()) {
            errors.add(0, "Negative not allowed : " + String.join(", ", negatives));
        }

        // If any errors exist
        if (!errors.isEmpty()) {
            return String.join("\n", errors);
        }

        // Return sum
        double sum = numbers.stream().mapToDouble(Double::doubleValue).sum();
        return (sum == (long) sum) ? String.valueOf((long) sum) : String.format("%.1f", sum);
    }
}