# Analizador Sintáctico para el Lenguaje Tiny

Este proyecto implementa un **analizador sintáctico descendente recursivo** para el lenguaje educativo **Tiny**, conforme a los requisitos del proyecto *"Reconocimiento de Patrones Sintáctico-Estructural"*.

El programa lee un archivo de texto con código en Tiny y determina si es **sintácticamente válido** según la gramática especificada.

---

## ✅ Funcionalidades

- **Lexer**: Tokeniza el código fuente reconociendo:
  - Identificadores (`[a-zA-Z]+`)
  - Números enteros (`\d+`)
  - Palabras clave: `if`, `then`, `else`, `end`
  - Operadores: `:=`, `=`, `>`, `+`, `-`, `*`, `/`
  - Símbolos: `(`, `)`, `;`
  - Ignora espacios, tabuladores y saltos de línea.

- **Parser**: Implementa una gramática que permite:
  - Asignaciones: `x := expresión;`
  - Expresiones aritméticas con paréntesis y precedencia correcta (`*`, `/` antes que `+`, `-`)
  - Comparaciones: `expresión > expresión` o `expresión = expresión`
  - Sentencias condicionales:
    ```tiny
    if condición then
      sentencia(s)
    [else
      sentencia(s)]
    end
    ```
  - Múltiples sentencias separadas por punto y coma (`;`), **opcional al final**.

- **Salida clara**:
  - `ACEPTA` si el programa es válido.
  - `RECHAZA` + mensaje de error detallado (línea y token problemático) si no lo es.

---

## 📁 Estructura del Proyecto
