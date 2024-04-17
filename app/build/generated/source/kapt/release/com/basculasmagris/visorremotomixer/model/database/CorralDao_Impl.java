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
import com.basculasmagris.visorremotomixer.model.entities.Corral;
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
public final class CorralDao_Impl implements CorralDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Corral> __insertionAdapterOfCorral;

  private final EntityDeletionOrUpdateAdapter<Corral> __deletionAdapterOfCorral;

  private final EntityDeletionOrUpdateAdapter<Corral> __updateAdapterOfCorral;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedDate;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedRemoteId;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedEstablishmentRemoteId;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCorralAnimals;

  public CorralDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCorral = new EntityInsertionAdapter<Corral>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `corral` (`establishment_id`,`establishment_remote_id`,`name`,`description`,`remote_id`,`updated_date`,`archive_date`,`animal_quantity`,`rfid`,`id`) VALUES (?,?,?,?,?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Corral value) {
        stmt.bindLong(1, value.getEstablishmentId());
        stmt.bindLong(2, value.getEstablishmentRemoteId());
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
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
        stmt.bindLong(8, value.getAnimalQuantity());
        stmt.bindLong(9, value.getRfid());
        stmt.bindLong(10, value.getId());
      }
    };
    this.__deletionAdapterOfCorral = new EntityDeletionOrUpdateAdapter<Corral>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `corral` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Corral value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfCorral = new EntityDeletionOrUpdateAdapter<Corral>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `corral` SET `establishment_id` = ?,`establishment_remote_id` = ?,`name` = ?,`description` = ?,`remote_id` = ?,`updated_date` = ?,`archive_date` = ?,`animal_quantity` = ?,`rfid` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Corral value) {
        stmt.bindLong(1, value.getEstablishmentId());
        stmt.bindLong(2, value.getEstablishmentRemoteId());
        if (value.getName() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription());
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
        stmt.bindLong(8, value.getAnimalQuantity());
        stmt.bindLong(9, value.getRfid());
        stmt.bindLong(10, value.getId());
        stmt.bindLong(11, value.getId());
      }
    };
    this.__preparedStmtOfSetUpdatedDate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE corral SET updated_date = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetUpdatedRemoteId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE corral SET remote_id = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetUpdatedEstablishmentRemoteId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE corral SET establishment_remote_id = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateCorralAnimals = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE corral SET animal_quantity = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertCorral(final Corral corral, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfCorral.insertAndReturnId(corral);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteCorral(final Corral corral, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCorral.handle(corral);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateCorral(final Corral corral, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCorral.handle(corral);
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
  public Object setUpdatedEstablishmentRemoteId(final long id, final long establishmentRemoteId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetUpdatedEstablishmentRemoteId.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, establishmentRemoteId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfSetUpdatedEstablishmentRemoteId.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateCorralAnimals(final int id, final int animalQuantity,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCorralAnimals.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, animalQuantity);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateCorralAnimals.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Corral>> getAllCorralList() {
    final String _sql = "SELECT * FROM corral ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"corral"}, new Callable<List<Corral>>() {
      @Override
      public List<Corral> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEstablishmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "establishment_id");
          final int _cursorIndexOfEstablishmentRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "establishment_remote_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfAnimalQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "animal_quantity");
          final int _cursorIndexOfRfid = CursorUtil.getColumnIndexOrThrow(_cursor, "rfid");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Corral> _result = new ArrayList<Corral>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Corral _item;
            final long _tmpEstablishmentId;
            _tmpEstablishmentId = _cursor.getLong(_cursorIndexOfEstablishmentId);
            final long _tmpEstablishmentRemoteId;
            _tmpEstablishmentRemoteId = _cursor.getLong(_cursorIndexOfEstablishmentRemoteId);
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
            final int _tmpAnimalQuantity;
            _tmpAnimalQuantity = _cursor.getInt(_cursorIndexOfAnimalQuantity);
            final long _tmpRfid;
            _tmpRfid = _cursor.getLong(_cursorIndexOfRfid);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Corral(_tmpEstablishmentId,_tmpEstablishmentRemoteId,_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpAnimalQuantity,_tmpRfid,_tmpId);
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
  public Flow<List<Corral>> getActiveCorralList() {
    final String _sql = "SELECT * FROM corral WHERE archive_date IS NULL OR archive_date = '' ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"corral"}, new Callable<List<Corral>>() {
      @Override
      public List<Corral> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEstablishmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "establishment_id");
          final int _cursorIndexOfEstablishmentRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "establishment_remote_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfAnimalQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "animal_quantity");
          final int _cursorIndexOfRfid = CursorUtil.getColumnIndexOrThrow(_cursor, "rfid");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Corral> _result = new ArrayList<Corral>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Corral _item;
            final long _tmpEstablishmentId;
            _tmpEstablishmentId = _cursor.getLong(_cursorIndexOfEstablishmentId);
            final long _tmpEstablishmentRemoteId;
            _tmpEstablishmentRemoteId = _cursor.getLong(_cursorIndexOfEstablishmentRemoteId);
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
            final int _tmpAnimalQuantity;
            _tmpAnimalQuantity = _cursor.getInt(_cursorIndexOfAnimalQuantity);
            final long _tmpRfid;
            _tmpRfid = _cursor.getLong(_cursorIndexOfRfid);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Corral(_tmpEstablishmentId,_tmpEstablishmentRemoteId,_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpAnimalQuantity,_tmpRfid,_tmpId);
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
  public Flow<List<Corral>> getFilteredCorralList(final String name) {
    final String _sql = "SELECT * FROM corral WHERE archive_date IS NULL  OR archive_date = '' ORDER BY name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"corral"}, new Callable<List<Corral>>() {
      @Override
      public List<Corral> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEstablishmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "establishment_id");
          final int _cursorIndexOfEstablishmentRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "establishment_remote_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfAnimalQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "animal_quantity");
          final int _cursorIndexOfRfid = CursorUtil.getColumnIndexOrThrow(_cursor, "rfid");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Corral> _result = new ArrayList<Corral>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Corral _item;
            final long _tmpEstablishmentId;
            _tmpEstablishmentId = _cursor.getLong(_cursorIndexOfEstablishmentId);
            final long _tmpEstablishmentRemoteId;
            _tmpEstablishmentRemoteId = _cursor.getLong(_cursorIndexOfEstablishmentRemoteId);
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
            final int _tmpAnimalQuantity;
            _tmpAnimalQuantity = _cursor.getInt(_cursorIndexOfAnimalQuantity);
            final long _tmpRfid;
            _tmpRfid = _cursor.getLong(_cursorIndexOfRfid);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Corral(_tmpEstablishmentId,_tmpEstablishmentRemoteId,_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpAnimalQuantity,_tmpRfid,_tmpId);
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
  public Flow<Corral> getCorralById(final int id) {
    final String _sql = "SELECT * FROM corral WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"corral"}, new Callable<Corral>() {
      @Override
      public Corral call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEstablishmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "establishment_id");
          final int _cursorIndexOfEstablishmentRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "establishment_remote_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfAnimalQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "animal_quantity");
          final int _cursorIndexOfRfid = CursorUtil.getColumnIndexOrThrow(_cursor, "rfid");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final Corral _result;
          if(_cursor.moveToFirst()) {
            final long _tmpEstablishmentId;
            _tmpEstablishmentId = _cursor.getLong(_cursorIndexOfEstablishmentId);
            final long _tmpEstablishmentRemoteId;
            _tmpEstablishmentRemoteId = _cursor.getLong(_cursorIndexOfEstablishmentRemoteId);
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
            final int _tmpAnimalQuantity;
            _tmpAnimalQuantity = _cursor.getInt(_cursorIndexOfAnimalQuantity);
            final long _tmpRfid;
            _tmpRfid = _cursor.getLong(_cursorIndexOfRfid);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _result = new Corral(_tmpEstablishmentId,_tmpEstablishmentRemoteId,_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpAnimalQuantity,_tmpRfid,_tmpId);
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

  @Override
  public Flow<Corral> getCorralByEstablishment(final long id) {
    final String _sql = "SELECT * FROM corral WHERE establishment_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"corral"}, new Callable<Corral>() {
      @Override
      public Corral call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEstablishmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "establishment_id");
          final int _cursorIndexOfEstablishmentRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "establishment_remote_id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfAnimalQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "animal_quantity");
          final int _cursorIndexOfRfid = CursorUtil.getColumnIndexOrThrow(_cursor, "rfid");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final Corral _result;
          if(_cursor.moveToFirst()) {
            final long _tmpEstablishmentId;
            _tmpEstablishmentId = _cursor.getLong(_cursorIndexOfEstablishmentId);
            final long _tmpEstablishmentRemoteId;
            _tmpEstablishmentRemoteId = _cursor.getLong(_cursorIndexOfEstablishmentRemoteId);
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
            final int _tmpAnimalQuantity;
            _tmpAnimalQuantity = _cursor.getInt(_cursorIndexOfAnimalQuantity);
            final long _tmpRfid;
            _tmpRfid = _cursor.getLong(_cursorIndexOfRfid);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _result = new Corral(_tmpEstablishmentId,_tmpEstablishmentRemoteId,_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpAnimalQuantity,_tmpRfid,_tmpId);
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
