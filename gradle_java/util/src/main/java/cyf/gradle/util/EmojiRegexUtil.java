package cyf.gradle.util;

import java.util.regex.Pattern;

/**
 * Created by zhuleiyue on 2017/3/20.
 * <p>
 * emoji regex
 */
public class EmojiRegexUtil {

    private static final String MiscellaneousSymbolsAndPictographs = "[\\uD83C\\uDF00-\\uD83D\\uDDFF]";

    private static final String SupplementalSymbolsAndPictographs = "[\\uD83E\\uDD00-\\uD83E\\uDDFF]";

    private static final String Emoticons = "[\\uD83D\\uDE00-\\uD83D\\uDE4F]";

    private static final String TransportAndMapSymbols = "[\\uD83D\\uDE80-\\uD83D\\uDEFF]";

    private static final String MiscellaneousSymbols = "[\\u2600-\\u26FF]\\uFE0F?";

    private static final String Dingbats = "[\\u2700-\\u27BF]\\uFE0F?";

    private static final String EnclosedAlphanumerics = "\\u24C2\\uFE0F?";

    private static final String RegionalIndicatorSymbol = "[\\uD83C\\uDDE6-\\uD83C\\uDDFF]{1,2}";

    private static final String EnclosedAlphanumericSupplement = "[\\uD83C\\uDD70\\uD83C\\uDD71\\uD83C\\uDD7E\\uD83C\\uDD7F\\uD83C\\uDD8E\\uD83C\\uDD91-\\uD83C\\uDD9A]\\uFE0F?";

    private static final String BasicLatin = "[\\u0023\\u002A\\u0030-\\u0039]\\uFE0F?\\u20E3";

    private static final String Arrows = "[\\u2194-\\u2199\\u21A9-\\u21AA]\\uFE0F?";

    private static final String MiscellaneousSymbolsAndArrows = "[\\u2B05-\\u2B07\\u2B1B\\u2B1C\\u2B50\\u2B55]\\uFE0F?";

    private static final String SupplementalArrows = "[\\u2934\\u2935]\\uFE0F?";

    private static final String CJKSymbolsAndPunctuation = "[\\u3030\\u303D]\\uFE0F?";

    private static final String EnclosedCJKLettersAndMonths = "[\\u3297\\u3299]\\uFE0F?";

    private static final String EnclosedIdeographicSupplement = "[\\uD83C\\uDE01\\uD83C\\uDE02\\uD83C\\uDE1A\\uD83C\\uDE2F\\uD83C\\uDE32-\\uD83C\\uDE3A\\uD83C\\uDE50\\uD83C\\uDE51]\\uFE0F?";

    private static final String GeneralPunctuation = "[\\u203C\\u2049]\\uFE0F?";

    private static final String GeometricShapes = "[\\u25AA\\u25AB\\u25B6\\u25C0\\u25FB-\\u25FE]\\uFE0F?";

    private static final String LatinSupplement = "[\\u00A9\\u00AE]\\uFE0F?";

    private static final String LetterlikeSymbols = "[\\u2122\\u2139]\\uFE0F?";

    private static final String MahjongTiles = "\\uD83C\\uDC04\\uFE0F?";

    private static final String PlayingCards = "\\uD83C\\uDCCF\\uFE0F?";

    private static final String MiscellaneousTechnical = "[\\u231A\\u231B\\u2328\\u23CF\\u23E9-\\u23F3\\u23F8-\\u23FA]\\uFE0F?";

    private static String getFullEmojiRegex() {
        return "(?:"
                + MiscellaneousSymbolsAndPictographs + "|"
                + SupplementalSymbolsAndPictographs + "|"
                + Emoticons + "|"
                + TransportAndMapSymbols + "|"
                + MiscellaneousSymbols + "|"
                + Dingbats + "|"
                + EnclosedAlphanumerics + "|"
                + RegionalIndicatorSymbol + "|"
                + EnclosedAlphanumericSupplement + "|"
                + BasicLatin + "|"
                + Arrows + "|"
                + MiscellaneousSymbolsAndArrows + "|"
                + SupplementalArrows + "|"
                + CJKSymbolsAndPunctuation + "|"
                + EnclosedCJKLettersAndMonths + "|"
                + EnclosedIdeographicSupplement + "|"
                + GeneralPunctuation + "|"
                + GeometricShapes + "|"
                + LatinSupplement + "|"
                + LetterlikeSymbols + "|"
                + MahjongTiles + "|"
                + PlayingCards + "|"
                + MiscellaneousTechnical + ")";
    }


    /**
     * 校验EMOJI
     * @param emoji
     * @return
     */
    public static boolean isEmoji(String emoji){
        return Pattern.matches(getFullEmojiRegex(), emoji);

    }


    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i + 1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d
                        || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() - 1) {
                    char ls = source.charAt(i + 1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return isEmoji;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    public static void main(String[] args) {
        System.out.println(isEmoji("\uD83D\uDE1C\uD83D\uDE29\uD83D\uDE29"));
    }
}