package com.schibsted.spain.barista.cleardata;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.test.InstrumentationRegistry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This rule clears all app's Databases before running each test
 */
public class ClearDatabaseRule implements TestRule {

  @Override
  public Statement apply(final Statement base, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        clearData();
        base.evaluate();
        clearData();
      }
    };
  }

  private void clearData() {
    for (String dbName : getDatabaseNames()) {
      Context context = InstrumentationRegistry.getTargetContext();
      SQLiteDatabase database = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
      for (String table : getTableNames(dbName)) {
        database.delete(table, null, null);
      }
      database.close();
    }
  }

  private List<String> getDatabaseNames() {
    Context context = InstrumentationRegistry.getTargetContext();
    return Arrays.asList(context.databaseList());
  }

  private List<String> getTableNames(String databaseName) throws SQLiteException {
    Context context = InstrumentationRegistry.getTargetContext();
    SQLiteDatabase database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
    try {
      Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type IN (?, ?)",
          new String[] { "table", "view" });
      try {
        List<String> tableNames = new ArrayList<>();
        while (cursor.moveToNext()) {
          tableNames.add(cursor.getString(0));
        }
        return tableNames;
      } finally {
        cursor.close();
      }
    } finally {
      database.close();
    }
  }
}