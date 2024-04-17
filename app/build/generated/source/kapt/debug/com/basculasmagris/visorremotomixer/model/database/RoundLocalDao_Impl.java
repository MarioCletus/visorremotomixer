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
import com.basculasmagris.visorremotomixer.model.entities.RoundLocal;
import java.lang.Class;
import java.lang.Exception;
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
public final class RoundLocalDao_Impl implements RoundLocalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RoundLocal> __insertionAdapterOfRoundLocal;

  private final EntityDeletionOrUpdateAdapter<RoundLocal> __deletionAdapterOfRoundLocal;

  private final EntityDeletionOrUpdateAdapter<RoundLocal> __updateAdapterOfRoundLocal;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRoundLocalById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllRoundLocal;

  public RoundLocalDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRoundLocal = new EntityInsertionAdapter<RoundLocal>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `RoundLocal` (`name`,`description`,`startDate`,`endDate`,`state`,`remote_id`,`tablet_mixer_id`,`tablet_mixer_mac`,`id`) VALUES (?,?,?,?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RoundLocal value) {
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
        if (value.getStartDate() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getStartDate());
        }
        if (value.getEndDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getEndDate());
        }
        stmt.bindLong(5, value.getState());
        stmt.bindLong(6, value.getRemoteId());
        stmt.bindLong(7, value.getTabletMixerId());
        if (value.getTabletMixerMac() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getTabletMixerMac());
        }
        stmt.bindLong(9, value.getId());
      }
    };
    this.__deletionAdapterOfRoundLocal = new EntityDeletionOrUpdateAdapter<RoundLocal>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `RoundLocal` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RoundLocal value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfRoundLocal = new EntityDeletionOrUpdateAdapter<RoundLocal>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `RoundLocal` SET `name` = ?,`description` = ?,`startDate` = ?,`endDate` = ?,`state` = ?,`remote_id` = ?,`tablet_mixer_id` = ?,`tablet_mixer_mac` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RoundLocal value) {
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
        if (value.getStartDate() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getStartDate());
        }
        if (value.getEndDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getEndDate());
        }
        stmt.bindLong(5, value.getState());
        stmt.bindLong(6, value.getRemoteId());
        stmt.bindLong(7, value.getTabletMixerId());
        if (value.getTabletMixerMac() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getTabletMixerMac());
        }
        stmt.bindLong(9, value.getId());
        stmt.bindLong(10, value.getId());
      }
    };
    this.__preparedStmtOfDeleteRoundLocalById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ROUNDLOCAL WHERE RoundLocal.ID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllRoundLocal = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ROUNDLOCAL";
        return _query;
      }
    };
  }

  @Override
  public Object insertRoundLocal(final RoundLocal roundLocal,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfRoundLocal.insertAndReturnId(roundLocal);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteRoundLocal(final RoundLocal roundLocal,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRoundLocal.handle(roundLocal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateRoundLocal(final RoundLocal roundLocal,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRoundLocal.handle(roundLocal);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteRoundLocalById(final long round_local_id,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRoundLocalById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, round_local_id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteRoundLocalById.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteAllRoundLocal(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllRoundLocal.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAllRoundLocal.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<RoundLocal>> getAllRoundLocalList() {
    final String _sql = "SELECT * FROM RoundLocal ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"RoundLocal"}, new Callable<List<RoundLocal>>() {
      @Override
      public List<RoundLocal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfTabletMixerId = CursorUtil.getColumnIndexOrThrow(_cursor, "tablet_mixer_id");
          final int _cursorIndexOfTabletMixerMac = CursorUtil.getColumnIndexOrThrow(_cursor, "tablet_mixer_mac");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<RoundLocal> _result = new ArrayList<RoundLocal>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RoundLocal _item;
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
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final int _tmpState;
            _tmpState = _cursor.getInt(_cursorIndexOfState);
            final long _tmpRemoteId;
            _tmpRemoteId = _cursor.getLong(_cursorIndexOfRemoteId);
            final long _tmpTabletMixerId;
            _tmpTabletMixerId = _cursor.getLong(_cursorIndexOfTabletMixerId);
            final String _tmpTabletMixerMac;
            if (_cursor.isNull(_cursorIndexOfTabletMixerMac)) {
              _tmpTabletMixerMac = null;
            } else {
              _tmpTabletMixerMac = _cursor.getString(_cursorIndexOfTabletMixerMac);
            }
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new RoundLocal(_tmpName,_tmpDescription,_tmpStartDate,_tmpEndDate,_tmpState,_tmpRemoteId,_tmpTabletMixerId,_tmpTabletMixerMac,_tmpId);
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
  public Flow<List<RoundLocal>> getAllRoundLocalListByMac(final String mac) {
    final String _sql = "SELECT * FROM RoundLocal WHERE ? == tablet_mixer_mac ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (mac == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, mac);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"RoundLocal"}, new Callable<List<RoundLocal>>() {
      @Override
      public List<RoundLocal> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "startDate");
          final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "endDate");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfTabletMixerId = CursorUtil.getColumnIndexOrThrow(_cursor, "tablet_mixer_id");
          final int _cursorIndexOfTabletMixerMac = CursorUtil.getColumnIndexOrThrow(_cursor, "tablet_mixer_mac");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<RoundLocal> _result = new ArrayList<RoundLocal>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RoundLocal _item;
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
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final int _tmpState;
            _tmpState = _cursor.getInt(_cursorIndexOfState);
            final long _tmpRemoteId;
            _tmpRemoteId = _cursor.getLong(_cursorIndexOfRemoteId);
            final long _tmpTabletMixerId;
            _tmpTabletMixerId = _cursor.getLong(_cursorIndexOfTabletMixerId);
            final String _tmpTabletMixerMac;
            if (_cursor.isNull(_cursorIndexOfTabletMixerMac)) {
              _tmpTabletMixerMac = null;
            } else {
              _tmpTabletMixerMac = _cursor.getString(_cursorIndexOfTabletMixerMac);
            }
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new RoundLocal(_tmpName,_tmpDescription,_tmpStartDate,_tmpEndDate,_tmpState,_tmpRemoteId,_tmpTabletMixerId,_tmpTabletMixerMac,_tmpId);
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

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
