import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {


    String I = "([a-zA-Z]+\\d*)+";
    String N = "\\d+";
    String L = "(true)|(false)";
    String O = "(int|bool)";

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("file.txt")));
        String full;
        for (full = reader.readLine(); reader.ready(); full += " " + reader.readLine()) ;

        full = full.replace("\n", " ");
        Main main = new Main();
        System.out.println(main.P(full));
    }

    private String P(String s) {
        if (Pattern.compile("^\\s*programm\\s+").matcher(s).find()) {
            Matcher matcher = Pattern.compile("^\\s*programm\\s").matcher(s);
            if (matcher.find()) {
                String s2 = s.substring(matcher.end());
                s = D1(s2);
                matcher = Pattern.compile("\\s*;").matcher(s);
                if (matcher.find()) {
                    return B(s.substring(matcher.end()));
                }
            }
        }
        return s;
    }

    private String D1(String s) {
        String D = I + "\\s*(,\\s*" + I + "\\s*)*:\\s*" + O;
        String D1 = "\\s*var\\s+" + D + "(\\s*,\\s*" + D + ")*\\s*";
        Matcher matcher = Pattern.compile(D1).matcher(s);
        if (matcher.find()) {
            return s.substring(matcher.end());
        }
        return s;
    }

    private String B(String s) {
        Matcher matcher = Pattern.compile("^\\s*begin\\s").matcher(s);
        if (matcher.find()) {
            String s2 = S2(s.substring(matcher.end() - 1));
            matcher = Pattern.compile("^\\s*end").matcher(s2);
            if (matcher.find()) {
                return s2.substring(matcher.end());
            }
        }
        return s;
    }

    private String S2(String s) {
        String s2 = S(s);
        Matcher matcher = Pattern.compile("^\\s*\\;").matcher(s2);
        if (matcher.find()) {
            return S2(s2.substring(matcher.end()));
        }
        return s2;
    }

    private String S(String s) {
        Matcher matcher = Pattern.compile("^\\s*" + I + "\\s*:=").matcher(s);
        if (matcher.find()) {
            return E(s.substring(matcher.end()));
        }
        matcher = Pattern.compile("^\\s*if\\s").matcher(s);
        if (matcher.find()) {
            s = s.substring(matcher.end() - 1);
            String s2 = E(s);
            if (s.length() > s2.length()) {
                matcher = Pattern.compile("^\\s+then\\s").matcher(s2);
                if (matcher.find()) {
                    s = s2.substring(matcher.end());
                    s2 = S(s);
                    if (s.length() > s2.length()) {
                        matcher = Pattern.compile("^\\s+else\\s").matcher(s2);
                        if (matcher.find()) {
                            s = s2.substring(matcher.end());
                            return S(s);
                        }
                    }
                }
            }
        }
        matcher = Pattern.compile("^\\s*while\\s").matcher(s);
        if (matcher.find()) {
            String s2 = s.substring(matcher.end() - 1);
            s = E(s2);
            if (s2.length() > s.length()) {
                matcher = Pattern.compile("^\\s*do\\s").matcher(s);
                if (matcher.find()) {
                    return S(s.substring(matcher.end() - 1));
                }
            }
        }
        matcher = Pattern.compile("^\\s*read\\s*\\(\\s*" + I + "\\s*\\)").matcher(s);
        if (matcher.find()) {
            return s.substring(matcher.end());
        }
        matcher = Pattern.compile("^\\s*write\\s*\\(").matcher(s);
        if (matcher.find()) {
            String s2 = s.substring(matcher.end());
            s = E(s2);
            if (s2.length() > s.length()) {
                matcher = Pattern.compile("^\\s*\\)").matcher(s);
                if (matcher.find()) {
                    return s.substring(matcher.end());
                }
            }
        }
        matcher = Pattern.compile("^\\s*begin\\s").matcher(s);
        if (matcher.find())
            return B(s);
        return s;
    }

    private String F(String s) {
        Matcher matcher = Pattern.compile("^\\s*" + I).matcher(s);
        if (matcher.find()) {
            return s.substring(matcher.end());
        }
        matcher = Pattern.compile("^\\s*" + N).matcher(s);
        if (matcher.find()) {
            return s.substring(matcher.end());
        }
        matcher = Pattern.compile("^\\s*" + L).matcher(s);
        if (matcher.find()) {
            return s.substring(matcher.end());
        }
        matcher = Pattern.compile("^\\s*not\\s+").matcher(s);
        if (matcher.find()) {
            return s.substring(matcher.end());
        }

        matcher = Pattern.compile("^\\s*\\(").matcher(s);
        if (matcher.find()) {
            String s2 = E(s.substring(matcher.end()));
            matcher = Pattern.compile("^\\s*\\)").matcher(s2);
            if (matcher.find()) {
                return s2.substring(matcher.end());
            }
            return s2;
        }
        return s;
    }

    private String T(String s) {
        String s2 = F(s);
        if (s.length() > s2.length()) {
            Matcher matcher = Pattern.compile("(^\\s*\\*|/)|(^\\s*and\\s)").matcher(s2);
            if (matcher.find()) {
                s = s2.substring(matcher.end());
                return T(s);
            }
        }
        return s2;
    }

    private String E1(String s) {
        String s2 = T(s);
        if (s.length() > s2.length()) {
            Matcher matcher = Pattern.compile("(^\\s*\\+|-\\s*)|(^\\s*or\\s)").matcher(s2);
            if (matcher.find()) {
                s = s2.substring(matcher.end());
                return E1(s);
            }
        }
        return s2;
    }

    private String E(String s) {
        String s2 = E1(s);
        if (s.length() > s2.length()) {
            Matcher matcher = Pattern.compile("^\\s*(=|-|<|>|(!=))").matcher(s2);
            if (matcher.find()) {
                s = s2.substring(matcher.end());
                return E(s);
            }
        }
        return s2;
    }
}
