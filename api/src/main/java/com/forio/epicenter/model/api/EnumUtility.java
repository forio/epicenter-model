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

public class EnumUtility {

  private enum LetterState {NONE, DIGIT, UPPER_LETTER, LOWER_LETTER, WHITESPACE, OTHER}

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