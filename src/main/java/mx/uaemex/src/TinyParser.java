package mx.uaemex.src;

import mx.uaemex.src.util.Token;
import mx.uaemex.src.parser.Parser;
import mx.uaemex.src.lexer.Lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TinyParser {
    public static void main(String[] args) {
        String filename = "/home/luismolina/ProgramaTiny.txt";
        String content;

        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("RECHAZA: No se encontró el archivo 'ProgramaTiny.txt'");
            return;
        }

        try {
            Lexer lexer = new Lexer(content);
            List<Token> tokens = lexer.getTokens();
            if (tokens.isEmpty()) {
                System.out.println("RECHAZA: Archivo vacío o sin tokens válidos");
                return;
            }
            Parser parser = new Parser(tokens);
            parser.parse();
            System.out.println("ACEPTA");
        } catch (Exception e) {
            System.out.println("RECHAZA");
            System.err.println(e.getMessage());
        }
    }
}