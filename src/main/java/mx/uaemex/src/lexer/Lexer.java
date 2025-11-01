package mx.uaemex.src.lexer;

import mx.uaemex.src.util.Token;

import java.util.*;
import java.util.regex.*;

public class Lexer {
    private String text;
    private int pos = 0;
    private int line = 1;
    private List<Token> tokens = new ArrayList<>();
    private static final Set<String> KEYWORDS = Set.of("if", "then", "else", "end");

    public Lexer(String text) {
        this.text = text;
        tokenize();
    }

    private void error(String msg) {
        throw new RuntimeException("Error léxico en línea " + line + ": " + msg);
    }

    private void tokenize() {
        String tokenRegex =
                "(?<NUMBER>\\d+)|" +
                        "(?<ID>[a-zA-Z]+)|" +
                        "(?<ASSIGN>:=)|" +
                        "(?<EQ>=)|" +
                        "(?<GT>>)|" +
                        "(?<PLUS>\\+)|" +
                        "(?<MINUS>-)|" +
                        "(?<TIMES>\\*)|" +
                        "(?<DIV>/)|" +
                        "(?<LPAREN>\\()|" +
                        "(?<RPAREN>\\))|" +
                        "(?<SEMI>;)|" +
                        "(?<IF>if)|" +
                        "(?<THEN>then)|" +
                        "(?<ELSE>else)|" +
                        "(?<END>end)|" +
                        "(?<SKIP>[ \\t]+)|" +
                        "(?<NEWLINE>\\n)|" +
                        "(?<MISMATCH>.)";

        Pattern pattern = Pattern.compile(tokenRegex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String match = matcher.group();
            String kind = null;
            for (String name : new String[]{"NUMBER","ID","ASSIGN","EQ","GT","PLUS","MINUS",
                    "TIMES","DIV","LPAREN","RPAREN","SEMI","IF","THEN","ELSE","END","SKIP","NEWLINE","MISMATCH"}) {
                if (matcher.group(name) != null) {
                    kind = name;
                    break;
                }
            }

            if ("NEWLINE".equals(kind)) {
                line++;
            } else if ("SKIP".equals(kind)) {
                continue;
            } else if ("MISMATCH".equals(kind)) {
                error("Carácter ilegal '" + match + "'");
            } else {
                if ("ID".equals(kind) && KEYWORDS.contains(match)) {
                    kind = match.toUpperCase();
                }
                tokens.add(new Token(kind, match, line));
            }
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }
}