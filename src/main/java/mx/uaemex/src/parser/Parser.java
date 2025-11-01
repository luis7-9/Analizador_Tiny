package mx.uaemex.src.parser;

import mx.uaemex.src.util.Token;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int pos = 0;
    private Token currentToken;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentToken = tokens.isEmpty() ? new Token("EOF", "", 0) : tokens.get(0);
    }

    private void error(String msg) {
        int line = currentToken != null ? currentToken.line : -1;
        throw new RuntimeException("Error sintáctico en línea " + line + ": " + msg);
    }

    private void eat(String tokenType) {
        if (currentToken.type.equals(tokenType)) {
            pos++;
            if (pos < tokens.size()) {
                currentToken = tokens.get(pos);
            } else {
                currentToken = new Token("EOF", "", currentToken.line);
            }
        } else {
            error("Se esperaba '" + tokenType + "', pero se encontró '" + currentToken.type +
                    "' ('" + currentToken.value + "')");
        }
    }

    private void factor() {
        String type = currentToken.type;
        if ("ID".equals(type)) {
            eat("ID");
        } else if ("NUMBER".equals(type)) {
            eat("NUMBER");
        } else if ("LPAREN".equals(type)) {
            eat("LPAREN");
            expr();
            eat("RPAREN");
        } else {
            error("Se esperaba ID, NUMBER o '('");
        }
    }

    private void term() {
        factor();
        while ("TIMES".equals(currentToken.type) || "DIV".equals(currentToken.type)) {
            String op = currentToken.type;
            if ("TIMES".equals(op)) eat("TIMES");
            else eat("DIV");
            factor();
        }
    }

    private void addExpr() {
        term();
        while ("PLUS".equals(currentToken.type) || "MINUS".equals(currentToken.type)) {
            String op = currentToken.type;
            if ("PLUS".equals(op)) eat("PLUS");
            else eat("MINUS");
            term();
        }
    }

    private void expr() {
        addExpr();
        if ("GT".equals(currentToken.type) || "EQ".equals(currentToken.type)) {
            String cmp = currentToken.type;
            eat(cmp);
            addExpr(); // permite expresiones completas tras > o =
        }
    }

    private void statement() {
        if ("ID".equals(currentToken.type)) {
            eat("ID");
            eat("ASSIGN");
            expr();
            // NO se consume SEMI aquí → se maneja en statementList como separador
        } else if ("IF".equals(currentToken.type)) {
            eat("IF");
            expr();
            eat("THEN");
            statementList();
            if ("ELSE".equals(currentToken.type)) {
                eat("ELSE");
                statementList();
            }
            eat("END");
        } else {
            error("Se esperaba una sentencia (asignación o if)");
        }
    }

    private void statementList() {
        statement();
        // Manejar ; como separador opcional entre sentencias
        while ("SEMI".equals(currentToken.type)) {
            eat("SEMI");
            // Solo continuar si hay otra sentencia válida
            if ("ID".equals(currentToken.type) || "IF".equals(currentToken.type)) {
                statement();
            } else {
                // ; al final (antes de END, ELSE o EOF) es aceptable → salir
                break;
            }
        }
    }

    public void parse() {
        if (tokens.isEmpty()) {
            error("Archivo vacío");
        }
        statementList();
        if (!"EOF".equals(currentToken.type)) {
            error("Token inesperado al final: '" + currentToken.value + "'");
        }
    }
}