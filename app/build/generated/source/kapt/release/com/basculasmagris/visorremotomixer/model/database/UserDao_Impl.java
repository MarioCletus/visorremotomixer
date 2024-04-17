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
import com.basculasmagris.visorremotomixer.model.entities.User;
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
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __deletionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __updateAdapterOfUser;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedDate;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedRemoteId;

  public UserDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `user` (`username`,`name`,`lastname`,`mail`,`password`,`remote_id`,`updated_date`,`archive_date`,`code_role`,`code_client`,`id`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        if (value.getUsername() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getUsername());
        }
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getLastname() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLastname());
        }
        if (value.getMail() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMail());
        }
        if (value.getPassword() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPassword());
        }
        stmt.bindLong(6, value.getRemoteId());
        if (value.getUpdatedDate() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getUpdatedDate());
        }
        if (value.getArchiveDate() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getArchiveDate());
        }
        stmt.bindLong(9, value.getCodeRole());
        if (value.getCodeClient() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getCodeClient());
        }
        stmt.bindLong(11, value.getId());
      }
    };
    this.__deletionAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `user` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `user` SET `username` = ?,`name` = ?,`lastname` = ?,`mail` = ?,`password` = ?,`remote_id` = ?,`updated_date` = ?,`archive_date` = ?,`code_role` = ?,`code_client` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        if (value.getUsername() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getUsername());
        }
        if (value.getName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getName());
        }
        if (value.getLastname() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getLastname());
        }
        if (value.getMail() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getMail());
        }
        if (value.getPassword() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPassword());
        }
        stmt.bindLong(6, value.getRemoteId());
        if (value.getUpdatedDate() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getUpdatedDate());
        }
        if (value.getArchiveDate() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getArchiveDate());
        }
        stmt.bindLong(9, value.getCodeRole());
        if (value.getCodeClient() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getCodeClient());
        }
        stmt.bindLong(11, value.getId());
        stmt.bindLong(12, value.getId());
      }
    };
    this.__preparedStmtOfSetUpdatedDate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE user SET updated_date = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetUpdatedRemoteId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE user SET remote_id = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertUser(final User user, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfUser.insertAndReturnId(user);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteUser(final User user, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfUser.handle(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateUser(final User user, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUser.handle(user);
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
  public Flow<List<User>> getAllUserList() {
    final String _sql = "SELECT * FROM user ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"user"}, new Callable<List<User>>() {
      @Override
      public List<User> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfLastname = CursorUtil.getColumnIndexOrThrow(_cursor, "lastname");
          final int _cursorIndexOfMail = CursorUtil.getColumnIndexOrThrow(_cursor, "mail");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfCodeRole = CursorUtil.getColumnIndexOrThrow(_cursor, "code_role");
          final int _cursorIndexOfCodeClient = CursorUtil.getColumnIndexOrThrow(_cursor, "code_client");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<User> _result = new ArrayList<User>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final User _item;
            final String _tmpUsername;
            if (_cursor.isNull(_cursorIndexOfUsername)) {
              _tmpUsername = null;
            } else {
              _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpLastname;
            if (_cursor.isNull(_cursorIndexOfLastname)) {
              _tmpLastname = null;
            } else {
              _tmpLastname = _cursor.getString(_cursorIndexOfLastname);
            }
            final String _tmpMail;
            if (_cursor.isNull(_cursorIndexOfMail)) {
              _tmpMail = null;
            } else {
              _tmpMail = _cursor.getString(_cursorIndexOfMail);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
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
            final int _tmpCodeRole;
            _tmpCodeRole = _cursor.getInt(_cursorIndexOfCodeRole);
            final String _tmpCodeClient;
            if (_cursor.isNull(_cursorIndexOfCodeClient)) {
              _tmpCodeClient = null;
            } else {
              _tmpCodeClient = _cursor.getString(_cursorIndexOfCodeClient);
            }
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new User(_tmpUsername,_tmpName,_tmpLastname,_tmpMail,_tmpPassword,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpCodeRole,_tmpCodeClient,_tmpId);
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
