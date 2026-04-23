/*
 * Copyright (c) 2020 Forio
 *
 * This file is part of the Forio Epicenter project.
 *
 * These files from the Forio Epicenter project are free software, you can
 * redistribute and/or modify them under the terms of the Apache License, Version 2.0.
 *
 * These files from the Forio Epicenter project are distributed in the hope that
 * they will be useful, but are WITHOUT ANY WARRANTY; without even an implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Apache License
 * for more details.
 *
 * You should have received a copy of the Apache License along with the these Forio
 * Epicenter project files. If not, see <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package com.forio.epicenter.model.api;

/**
 * Normalizes free-form text to the uppercase underscore-separated form used by Java enum constant
 * names, enabling flexible string-to-enum lookup.
 *
 * <p>{@link #toEnumName(String)} is the single entry point. It applies these rules
 * character-by-character:</p>
 * <ol>
 *   <li>Whitespace is treated as a word boundary and replaced by {@code _}.</li>
 *   <li>Transitions between character categories (digit, uppercase letter, lowercase letter)
 *       also produce a {@code _} boundary, splitting camelCase and mixed-type tokens.</li>
 *   <li>Consecutive underscores are collapsed; the output never contains {@code __}.</li>
 *   <li>Non-alphanumeric characters other than underscores are dropped.</li>
 *   <li>All output characters are uppercased.</li>
 * </ol>
 *
 * <p>Transformation examples:</p>
 * <pre>
 *   "debug"      →  "DEBUG"
 *   "Info Level" →  "INFO_LEVEL"
 *   "fatalError" →  "FATAL_ERROR"
 *   "iPhone"     →  "I_PHONE"
 *   "my_enum"    →  "MY_ENUM"
 * </pre>
 *
 * <p>This class is used by {@link LogLevelEnumXmlAdapter} to normalize XML log-level strings
 * before passing them to {@link LogLevel#valueOf(String)}. It is public and may be used wherever
 * free-form text must be mapped to an enum constant name.</p>
 */
public class EnumUtility {

  /**
   * Classifies a character for boundary-detection purposes.
   */
  private enum LetterState {NONE, DIGIT, UPPER_LETTER, LOWER_LETTER, WHITESPACE, OTHER}

  /**
   * No-arg constructor. This class provides only static methods; instances serve no purpose.
   */
  public EnumUtility () {

  }

  /**
   * Converts a mixed-case or whitespace-delimited string to the uppercase underscore-separated
   * form expected by Java enum constants.
   *
   * <p>The transformation is stateful and character-by-character. Whitespace and category
   * transitions (e.g., lowercase to uppercase, letter to digit) become underscore word boundaries;
   * consecutive underscores in the output are suppressed; non-alphanumeric characters other than
   * underscores are discarded; all output is uppercased.</p>
   *
   * <p>The result is not validated against any enum type. If no constant matches the output, a
   * subsequent {@code Enum.valueOf} call will throw {@link IllegalArgumentException}; the caller
   * must handle that case.</p>
   *
   * <p><strong>Input constraints:</strong> {@code anyCase} must begin with a letter ({@code a-z},
   * {@code A-Z}) or digit ({@code 0-9}). Inputs that begin with whitespace, underscores, or any
   * other non-alphanumeric character will cause {@link StringIndexOutOfBoundsException} when the
   * first letter or digit is encountered. An empty string returns an empty string.</p>
   *
   * @param anyCase the text to normalize; must not be {@code null} and must begin with a letter
   *                or digit.
   * @return the normalized constant name in uppercase; {@code ""} if {@code anyCase} is empty;
   * never {@code null}.
   * @throws NullPointerException            if {@code anyCase} is {@code null}.
   * @throws StringIndexOutOfBoundsException if {@code anyCase} is non-empty and does not begin
   *                                         with a letter or digit.
   */
  public static String toEnumName (String anyCase) {

    StringBuilder fieldBuilder = new StringBuilder();
    LetterState prevState = LetterState.NONE;
    int stateCount = 0;

    for (int count = 0; count < anyCase.length(); count++) {

      LetterState state;

      if (Character.isWhitespace(anyCase.charAt(count))) {
        state = LetterState.WHITESPACE;
      } else if (Character.isDigit(anyCase.charAt(count))) {
        state = LetterState.DIGIT;
      } else if (Character.isLetter(anyCase.charAt(count))) {
        state = Character.isUpperCase(anyCase.charAt(count)) ? LetterState.UPPER_LETTER : LetterState.LOWER_LETTER;
      } else {
        state = LetterState.OTHER;
      }

      if (!(state.equals(LetterState.WHITESPACE) || ((count > 0) && (anyCase.charAt(count) == '_') && (fieldBuilder.charAt(fieldBuilder.length() - 1) == '_')))) {
        if ((count > 0) && (!state.equals(prevState)) && (anyCase.charAt(count) != '_') && (fieldBuilder.charAt(fieldBuilder.length() - 1) != '_') && (!(prevState.equals(LetterState.UPPER_LETTER) && state.equals(LetterState.LOWER_LETTER)))) {
          fieldBuilder.append('_');
        }
        if (!state.equals(LetterState.OTHER)) {
          if (prevState.equals(LetterState.UPPER_LETTER) && state.equals(LetterState.LOWER_LETTER) && stateCount > 0) {
            fieldBuilder.insert(fieldBuilder.length() - 1, '_');
          }

          fieldBuilder.append(state.equals(LetterState.LOWER_LETTER) ? Character.toUpperCase(anyCase.charAt(count)) : anyCase.charAt(count));
        }
      }

      stateCount = prevState.equals(state) ? stateCount + 1 : 0;
      prevState = state;
    }

    return fieldBuilder.toString();
  }
}
