package com.bijoysingh.quicknote.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

@Entity(
    tableName = "widget",
    indices = {@Index("widgetId")}
)
public class Widget {

  @PrimaryKey
  public int widgetId;

  public String noteUUID;

  public Widget() {
  }

  public Widget(int widgetId, String noteId) {
    this.widgetId = widgetId;
    this.noteUUID = noteId;
  }

  public static WidgetDao db(Context context) {
    return AppDatabase.getDatabase(context).widgets();
  }
}
