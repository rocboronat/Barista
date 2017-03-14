package com.schibsted.spain.barista.cleardata;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.test.InstrumentationRegistry;
import java.io.File;
import java.util.ArrayList;
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
    for (File dbFile : getDatabaseFiles()) {
      SQLiteDatabase database = performOpen(dbFile, 0);
      for (String table : getTableNames(dbFile)) {
        database.delete(table, null, null);
      }
      database.close();
    }
  }

  private List<File> getDatabaseFiles() {
    Context context = InstrumentationRegistry.getTargetContext();
    List<File> databaseFiles = new ArrayList<>();
    for (String databaseName : context.databaseList()) {
      databaseFiles.add(context.getDatabasePath(databaseName));
    }
    return databaseFiles;
  }

  private List<String> getTableNames(File databaseFile) throws SQLiteException {
    SQLiteDatabase database = performOpen(databaseFile, 0);
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

  private SQLiteDatabase performOpen(File databaseFile, int options) {
    int flags = SQLiteDatabase.OPEN_READWRITE;

    SQLiteDatabase db = SQLiteDatabase.openDatabase(
        databaseFile.getAbsolutePath(),
        null /* cursorFactory */,
        flags);
    return db;
  }
}