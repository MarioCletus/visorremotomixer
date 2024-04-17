package com.basculasmagris.visorremotomixer.model.database;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TabletMixerDao_Impl implements TabletMixerDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TabletMixer> __insertionAdapterOfTabletMixer;

  private final EntityDeletionOrUpdateAdapter<TabletMixer> __deletionAdapterOfTabletMixer;

  private final EntityDeletionOrUpdateAdapter<TabletMixer> __updateAdapterOfTabletMixer;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedDate;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedMacAddress;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedRemoteId;

  public TabletMixerDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTabletMixer = new EntityInsertionAdapter<TabletMixer>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `tablet_mixer` (`name`,`description`,`mac`,`bt_box`,`remote_id`,`updated_date`,`archive_date`,`linked`,`id`) VALUES (?,?,?,?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TabletMixer value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDescription());
        }
        if (value.getMac() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getMac());
        }
        if (value.getBtBox() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getBtBox());
        }
        stmt.bindLong(5, value.getRemoteId());
        if (value.getUpdatedDate() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getUpdatedDate());
        }
        if (value.getArchiveDate() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getArchiveDate());
        }
        final Integer _tmp = value.getLinked() == null ? null : (value.getLinked() ? 1 : 0);
        if (_tmp == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, _tmp);
        }
        stmt.bindLong(9, value.getId());
      }
    };
    this.__deletionAdapterOfTabletMixer = new EntityDeletionOrUpdateAdapter<TabletMixer>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `tablet_mixer` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TabletMixer value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfTabletMixer = new EntityDeletionOrUpdateAdapter<TabletMixer>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `tablet_mixer` SET `name` = ?,`description` = ?,`mac` = ?,`bt_box` = ?,`remote_id` = ?,`updated_date` = ?,`archive_date` = ?,`linked` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, TabletMixer value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDescription());
        }
        if (value.getMac() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getMac());
        }
        if (value.getBtBox() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getBtBox());
        }
        stmt.bindLong(5, value.getRemoteId());
        if (value.getUpdatedDate() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getUpdatedDate());
        }
        if (value.getArchiveDate() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getArchiveDate());
        }
        final Integer _tmp = value.getLinked() == null ? null : (value.getLinked() ? 1 : 0);
        if (_tmp == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindLong(8, _tmp);
        }
        stmt.bindLong(9, value.getId());
        stmt.bindLong(10, value.getId());
      }
    };
    this.__preparedStmtOfSetUpdatedDate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE TABLET_MIXER SET updated_date = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetUpdatedMacAddress = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE TABLET_MIXER SET mac = ?, updated_date = datetime('now','localtime') WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetUpdatedRemoteId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE TABLET_MIXER SET remote_id = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTabletMixer(final TabletMixer tabletMixer,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfTabletMixer.insertAndReturnId(tabletMixer);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteTabletMixer(final TabletMixer tabletMixer,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTabletMixer.handle(tabletMixer);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateTabletMixer(final TabletMixer tabletMixer,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTabletMixer.handle(tabletMixer);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object setUpdatedDate(final long id, final String date,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetUpdatedDate.acquire();
        int _argIndex = 1;
        if (date == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, date);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfSetUpdatedDate.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object setUpdatedMacAddress(final long id, final String mac,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetUpdatedMacAddress.acquire();
        int _argIndex = 1;
        if (mac == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, mac);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfSetUpdatedMacAddress.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object setUpdatedRemoteId(final long id, final long remoteId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetUpdatedRemoteId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, remoteId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfSetUpdatedRemoteId.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<TabletMixer>> getAllTabletMixerList() {
    final String _sql = "SELECT * FROM TABLET_MIXER WHERE archive_date IS NULL OR archive_date = ''  ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"TABLET_MIXER"}, new Callable<List<TabletMixer>>() {
      @Override
      public List<TabletMixer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMac = CursorUtil.getColumnIndexOrThrow(_cursor, "mac");
          final int _cursorIndexOfBtBox = CursorUtil.getColumnIndexOrThrow(_cursor, "bt_box");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfLinked = CursorUtil.getColumnIndexOrThrow(_cursor, "linked");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<TabletMixer> _result = new ArrayList<TabletMixer>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TabletMixer _item;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpMac;
            if (_cursor.isNull(_cursorIndexOfMac)) {
              _tmpMac = null;
            } else {
              _tmpMac = _cursor.getString(_cursorIndexOfMac);
            }
            final String _tmpBtBox;
            if (_cursor.isNull(_cursorIndexOfBtBox)) {
              _tmpBtBox = null;
            } else {
              _tmpBtBox = _cursor.getString(_cursorIndexOfBtBox);
            }
            final long _tmpRemoteId;
            _tmpRemoteId = _cursor.getLong(_cursorIndexOfRemoteId);
            final String _tmpUpdatedDate;
            if (_cursor.isNull(_cursorIndexOfUpdatedDate)) {
              _tmpUpdatedDate = null;
            } else {
              _tmpUpdatedDate = _cursor.getString(_cursorIndexOfUpdatedDate);
            }
            final String _tmpArchiveDate;
            if (_cursor.isNull(_cursorIndexOfArchiveDate)) {
              _tmpArchiveDate = null;
            } else {
              _tmpArchiveDate = _cursor.getString(_cursorIndexOfArchiveDate);
            }
            final Boolean _tmpLinked;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfLinked)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfLinked);
            }
            _tmpLinked = _tmp == null ? null : _tmp != 0;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new TabletMixer(_tmpName,_tmpDescription,_tmpMac,_tmpBtBox,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpLinked,_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TabletMixer>> getActiveTabletMixerList() {
    final String _sql = "SELECT * FROM TABLET_MIXER ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"TABLET_MIXER"}, new Callable<List<TabletMixer>>() {
      @Override
      public List<TabletMixer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMac = CursorUtil.getColumnIndexOrThrow(_cursor, "mac");
          final int _cursorIndexOfBtBox = CursorUtil.getColumnIndexOrThrow(_cursor, "bt_box");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfLinked = CursorUtil.getColumnIndexOrThrow(_cursor, "linked");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<TabletMixer> _result = new ArrayList<TabletMixer>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TabletMixer _item;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpMac;
            if (_cursor.isNull(_cursorIndexOfMac)) {
              _tmpMac = null;
            } else {
              _tmpMac = _cursor.getString(_cursorIndexOfMac);
            }
            final String _tmpBtBox;
            if (_cursor.isNull(_cursorIndexOfBtBox)) {
              _tmpBtBox = null;
            } else {
              _tmpBtBox = _cursor.getString(_cursorIndexOfBtBox);
            }
            final long _tmpRemoteId;
            _tmpRemoteId = _cursor.getLong(_cursorIndexOfRemoteId);
            final String _tmpUpdatedDate;
            if (_cursor.isNull(_cursorIndexOfUpdatedDate)) {
              _tmpUpdatedDate = null;
            } else {
              _tmpUpdatedDate = _cursor.getString(_cursorIndexOfUpdatedDate);
            }
            final String _tmpArchiveDate;
            if (_cursor.isNull(_cursorIndexOfArchiveDate)) {
              _tmpArchiveDate = null;
            } else {
              _tmpArchiveDate = _cursor.getString(_cursorIndexOfArchiveDate);
            }
            final Boolean _tmpLinked;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfLinked)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfLinked);
            }
            _tmpLinked = _tmp == null ? null : _tmp != 0;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new TabletMixer(_tmpName,_tmpDescription,_tmpMac,_tmpBtBox,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpLinked,_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TabletMixer>> getFilteredTabletMixerList(final String name) {
    final String _sql = "SELECT * FROM TABLET_MIXER WHERE archive_date IS NULL OR archive_date = ''  ORDER BY name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"TABLET_MIXER"}, new Callable<List<TabletMixer>>() {
      @Override
      public List<TabletMixer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMac = CursorUtil.getColumnIndexOrThrow(_cursor, "mac");
          final int _cursorIndexOfBtBox = CursorUtil.getColumnIndexOrThrow(_cursor, "bt_box");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfLinked = CursorUtil.getColumnIndexOrThrow(_cursor, "linked");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<TabletMixer> _result = new ArrayList<TabletMixer>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final TabletMixer _item;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpMac;
            if (_cursor.isNull(_cursorIndexOfMac)) {
              _tmpMac = null;
            } else {
              _tmpMac = _cursor.getString(_cursorIndexOfMac);
            }
            final String _tmpBtBox;
            if (_cursor.isNull(_cursorIndexOfBtBox)) {
              _tmpBtBox = null;
            } else {
              _tmpBtBox = _cursor.getString(_cursorIndexOfBtBox);
            }
            final long _tmpRemoteId;
            _tmpRemoteId = _cursor.getLong(_cursorIndexOfRemoteId);
            final String _tmpUpdatedDate;
            if (_cursor.isNull(_cursorIndexOfUpdatedDate)) {
              _tmpUpdatedDate = null;
            } else {
              _tmpUpdatedDate = _cursor.getString(_cursorIndexOfUpdatedDate);
            }
            final String _tmpArchiveDate;
            if (_cursor.isNull(_cursorIndexOfArchiveDate)) {
              _tmpArchiveDate = null;
            } else {
              _tmpArchiveDate = _cursor.getString(_cursorIndexOfArchiveDate);
            }
            final Boolean _tmpLinked;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfLinked)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfLinked);
            }
            _tmpLinked = _tmp == null ? null : _tmp != 0;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new TabletMixer(_tmpName,_tmpDescription,_tmpMac,_tmpBtBox,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpLinked,_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<TabletMixer> getTabletMixerById(final long id) {
    final String _sql = "SELECT * FROM TABLET_MIXER WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"TABLET_MIXER"}, new Callable<TabletMixer>() {
      @Override
      public TabletMixer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfMac = CursorUtil.getColumnIndexOrThrow(_cursor, "mac");
          final int _cursorIndexOfBtBox = CursorUtil.getColumnIndexOrThrow(_cursor, "bt_box");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfLinked = CursorUtil.getColumnIndexOrThrow(_cursor, "linked");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final TabletMixer _result;
          if(_cursor.moveToFirst()) {
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpMac;
            if (_cursor.isNull(_cursorIndexOfMac)) {
              _tmpMac = null;
            } else {
              _tmpMac = _cursor.getString(_cursorIndexOfMac);
            }
            final String _tmpBtBox;
            if (_cursor.isNull(_cursorIndexOfBtBox)) {
              _tmpBtBox = null;
            } else {
              _tmpBtBox = _cursor.getString(_cursorIndexOfBtBox);
            }
            final long _tmpRemoteId;
            _tmpRemoteId = _cursor.getLong(_cursorIndexOfRemoteId);
            final String _tmpUpdatedDate;
            if (_cursor.isNull(_cursorIndexOfUpdatedDate)) {
              _tmpUpdatedDate = null;
            } else {
              _tmpUpdatedDate = _cursor.getString(_cursorIndexOfUpdatedDate);
            }
            final String _tmpArchiveDate;
            if (_cursor.isNull(_cursorIndexOfArchiveDate)) {
              _tmpArchiveDate = null;
            } else {
              _tmpArchiveDate = _cursor.getString(_cursorIndexOfArchiveDate);
            }
            final Boolean _tmpLinked;
            final Integer _tmp;
            if (_cursor.isNull(_cursorIndexOfLinked)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(_cursorIndexOfLinked);
            }
            _tmpLinked = _tmp == null ? null : _tmp != 0;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _result = new TabletMixer(_tmpName,_tmpDescription,_tmpMac,_tmpBtBox,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpLinked,_tmpId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
