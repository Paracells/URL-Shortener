package ru.paracells.urlshortener.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUrl {

    public static boolean checkString(String realUrl) {
        if (realUrl == null) return false;
        String regex = "((http?|https|ftp|file)://)?(([Ww]){3}.)?[a-zA-Z0-9]+\\.[a-zA-Z]{2,}";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(realUrl);
        boolean matches = matcher.matches();
        return matches;
    }
}
