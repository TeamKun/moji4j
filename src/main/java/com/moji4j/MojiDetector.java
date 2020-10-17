package com.moji4j;

public class MojiDetector {

    public static boolean hasKana(String string) {

        for (char c : string.toCharArray()) {

            if (isKana(c)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasKanji(String string) {

        for (char c : string.toCharArray()) {

            if (isKanji(c)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasRomaji(String string) {

        for (char c : string.toCharArray()) {

            if (isRomaji(c)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isKana(char c) {
        Character.UnicodeBlock b = Character.UnicodeBlock.of(c);

        return (b == Character.UnicodeBlock.HIRAGANA ||
                b == Character.UnicodeBlock.KATAKANA ||
                b == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS);
    }

    public static boolean isKanji(char c) {
        return (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
    }

    public static boolean isRomaji(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
}
