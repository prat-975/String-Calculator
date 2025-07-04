package com.trainingPlan.stringCalculator.calculator;

import java.util.*;
import java.util.regex.*;

public class CalculatorString {

    public String add(String input) {
        if (input.isEmpty()) return "0";

        String delimiter = ",";
        String numbers = input;
        List<String> errors = new ArrayList<>();
        List<String> negatives = new ArrayList<>();

        // Custom delimiter check
        if (input.startsWith("//")) {
            int newlineIndex = input.indexOf("\n");
            if (newlineIndex == -1) return "Invalid input format";

            delimiter = Pattern.quote(input.substring(2, newlineIndex));
            numbers = input.substring(newlineIndex + 1);
        }

        String regex = delimiter + "|\n";
        String[] tokens = numbers.split(regex, -1);

        double sum = 0;
        int index = 0;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (token.isEmpty()) {
                if (i == tokens.length - 1) {
                    errors.add("Number expected but EOF found.");
                } else {
                    int errorPos = getErrorPosition(numbers, i, delimiter);
                    errors.add("Number expected but '" + getSeparatorAt(numbers, errorPos) + "' found at position " + errorPos + ".");
                }
                continue;
            }

            if (!token.matches("-?\\d+(\\.\\d+)?")) {
                errors.add("Invalid number: " + token);
                continue;
            }

            if (token.contains(",") || token.contains("\n")) {
                int wrongIndex = numbers.indexOf(token);
                errors.add("'" + delimiter.replace("\\", "") + "' expected but ',' found at position " + wrongIndex + ".");
                continue;
            }

            if (token.startsWith("-")) {
                negatives.add(token);
            }

            try {
                sum += Double.parseDouble(token);
            } catch (NumberFormatException e) {
                errors.add("Invalid number: " + token);
            }
        }

        if (!negatives.isEmpty()) {
            errors.add("Negative not allowed : " + String.join(", ", negatives));
        }

        return errors.isEmpty() ? format(sum) : String.join("\n", errors);
    }

    private int getErrorPosition(String input, int index, String delimiter) {
        String regex = delimiter.replace("\\", "") + "|\n";
        Matcher m = Pattern.compile(regex).matcher(input);
        int pos = 0;
        for (int i = 0; i <= index && m.find(); i++) {
            pos = m.start();
        }
        return pos;
    }

    private String getSeparatorAt(String input, int pos) {
        return String.valueOf(input.charAt(pos));
    }

    private String format(double sum) {
        if (sum == (long) sum) {
            return String.format("%d", (long) sum);
        } else {
            return String.format("%.1f", sum); // Rounds to one decimal
        }
    }
}
