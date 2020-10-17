package com.moji4j;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.stream.Stream;

public class MojiExtractor {

    public static ImmutableList<String> getRomajiCandidate(String string) {
        if (string.isEmpty())
            return ImmutableList.of();

        ImmutableList<String> candidate = getCandidate(string, ConversionTable.getHiraganaToRomaji());

        if (string.length() >= 2) {
            char currentCharacter = string.charAt(0);

            if (currentCharacter == 'ん')
                return Stream.concat(
                        candidate.stream(),
                        getCandidate(string.substring(1), ConversionTable.getHiraganaToRomaji()).stream()
                                .filter(e -> e.charAt(0) != 'y' && isRomanConsonant(e.charAt(0)))
                                .map("n"::concat)
                ).collect(ImmutableList.toImmutableList());

            if (currentCharacter == 'っ') {
                return Stream.concat(
                        candidate.stream(),
                        getCandidate(string.substring(1), ConversionTable.getHiraganaToRomaji()).stream()
                                .filter(e -> isRomanConsonant(e.charAt(0)))
                                .map(e -> e.charAt(0) + e)
                ).collect(ImmutableList.toImmutableList());
            }
        }

        return candidate;
    }

    public static ImmutableList<String> getHiraganaCandidate(String string) {

        string = string.toLowerCase();

        if (hasDoubleConsonantWithSokuon(string))
            return getCandidate(string.substring(1), ConversionTable.getRomajiToHiragana()).stream()
                    .map("っ"::concat)
                    .collect(ImmutableList.toImmutableList());
        else if (hasConsonantNextToN(string))
            return getCandidate(string.substring(1), ConversionTable.getRomajiToHiragana()).stream()
                    .map("ん"::concat)
                    .collect(ImmutableList.toImmutableList());

        return getCandidate(string, ConversionTable.getRomajiToHiragana());
    }

    public static ImmutableList<String> getCandidate(String string, ConversionTable conversionTable) {

        ImmutableList.Builder<String> resultBuilder = ImmutableList.builder();

        int maxSubstringLength = Math.min(conversionTable.getMaxKeyLength(), string.length());

        // Look-up the longest substring match.
        for (int substringLength = maxSubstringLength; substringLength > 0; substringLength--) {

            String substring = string.substring(0, substringLength);
            List<String> replacementString = conversionTable.get(substring);

            // Replace substring if there's a match.
            resultBuilder.addAll(replacementString);

        }

        return resultBuilder.build();

    }

    public static boolean hasDoubleConsonantWithSokuon(String string) {

        if (string.length() <= 2)
            return false;

        char currentCharacter = string.charAt(0);
        char nextCharacter = string.charAt(1);

        boolean isDoubleConsonant = currentCharacter == nextCharacter;
        boolean isExceptionalCase = currentCharacter == 't' && nextCharacter == 'c';

        return (isRomanConsonant(currentCharacter) && (isDoubleConsonant || isExceptionalCase));

    }

    public static boolean hasConsonantNextToN(String string) {

        if (string.length() <= 2)
            return false;

        char currentCharacter = string.charAt(0);
        char nextCharacter = string.charAt(1);

        boolean isConsonantNextToN = currentCharacter == 'n' && isRomanConsonant(nextCharacter);
        boolean isExceptionalCase = currentCharacter == 'n' && nextCharacter == 'y';

        return (isConsonantNextToN && !isExceptionalCase);

    }

    public static boolean isRomanConsonant(char character) {
        return character >= 'a' && character <= 'z' && !isRomanVowel(character);
    }

    public static boolean isRomanVowel(char character) {
        return character == 'a' || character == 'i' || character == 'u' || character == 'e' || character == 'o';
    }

}
