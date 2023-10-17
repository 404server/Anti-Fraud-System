package com.example.antifraudsystem.controller.utility;

import java.util.List;

public class TransactionValidator {
    public static boolean isCardNumberValid(String cardNumber) {
        String panPattern = "^4[0-9]{12}(?:[0-9]{3})?$";
        // Check if the cardNumber matches the card number pattern
        if (!cardNumber.matches(panPattern)) {
            return false; //if not return false
        }
        int sum = 0; //Initialize variable sum to 0 to use Luhn algorithm
        boolean doubleDigit = false; // Initialize if the current digit should be doubled
        // Iterate through the cardNumber digits from right to left
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (doubleDigit) {
                digit *= 2; //if digit index is even and if its double it
                if (digit > 9) {
                    digit -= 9;//if doubled digit is more then 9 , substract from it 9
                }
            }
            sum += digit;// Add the current digit (possibly doubled) to the sum
            doubleDigit = !doubleDigit;
        }
        // Check if the sum is divisible by 10 to determine if the card number is valid
        return sum % 10 == 0;
    }

    public static boolean isRegionValid(String region) {
        List<String> regions = List.of("EAP", "ECA", "HIC", "LAC", "MENA", "SA", "SSA");
        return regions.contains(region);
    }

}
