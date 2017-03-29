package com.schibsted.spain.barista.custom;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class HelperMatchers {

  public static <T> Matcher<T> firstOf(final Matcher<T> matcher) {
    return new BaseMatcher<T>() {
      private boolean isFirst = true;

      @Override
      public boolean matches(final Object item) {
        if (isFirst && matcher.matches(item)) {
          isFirst = false;
          return true;
        }
        return false;
      }

      @Override
      public void describeTo(final Description description) {
        description.appendText("should return first matching item");
      }
    };
  }
}
