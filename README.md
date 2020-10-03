##URL Shortener

###How to use

1. You need db, I use **cloud MongoDB**, but <br/>
    1. you can install on your PC
    2. docker image
    3. use cloud MongoDB 
<br/>
After install, you should change ```spring.data.mongodb.uri``` in ```application.properties```

2. 
```
/addurl - /addurl html page, where you can shorten your URL
/decode - after shorten then it will to fianl page, where you can click and redirect to decode
```
After enter url it will check for URL matching
```java
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
```

I use [Hashids](https://hashids.org/java/) for shortening URL
<br/>
