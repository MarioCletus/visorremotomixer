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
import com.basculasmagris.visorremotomixer.model.entities.Product;
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
public final class ProductDao_Impl implements ProductDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Product> __insertionAdapterOfProduct;

  private final EntityDeletionOrUpdateAdapter<Product> __deletionAdapterOfProduct;

  private final EntityDeletionOrUpdateAdapter<Product> __updateAdapterOfProduct;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedDate;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedRemoteId;

  public ProductDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProduct = new EntityInsertionAdapter<Product>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `product` (`name`,`description`,`specific_weight`,`rfid`,`image`,`imageSource`,`remote_id`,`updated_date`,`archive_date`,`id`) VALUES (?,?,?,?,?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Product value) {
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
        stmt.bindDouble(3, value.getSpecificWeight());
        stmt.bindLong(4, value.getRfid());
        if (value.getImage() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getImage());
        }
        if (value.getImageSource() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getImageSource());
        }
        stmt.bindLong(7, value.getRemoteId());
        if (value.getUpdatedDate() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getUpdatedDate());
        }
        if (value.getArchiveDate() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getArchiveDate());
        }
        stmt.bindLong(10, value.getId());
      }
    };
    this.__deletionAdapterOfProduct = new EntityDeletionOrUpdateAdapter<Product>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `product` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Product value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfProduct = new EntityDeletionOrUpdateAdapter<Product>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `product` SET `name` = ?,`description` = ?,`specific_weight` = ?,`rfid` = ?,`image` = ?,`imageSource` = ?,`remote_id` = ?,`updated_date` = ?,`archive_date` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Product value) {
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
        stmt.bindDouble(3, value.getSpecificWeight());
        stmt.bindLong(4, value.getRfid());
        if (value.getImage() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getImage());
        }
        if (value.getImageSource() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getImageSource());
        }
        stmt.bindLong(7, value.getRemoteId());
        if (value.getUpdatedDate() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getUpdatedDate());
        }
        if (value.getArchiveDate() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getArchiveDate());
        }
        stmt.bindLong(10, value.getId());
        stmt.bindLong(11, value.getId());
      }
    };
    this.__preparedStmtOfSetUpdatedDate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE product SET updated_date = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetUpdatedRemoteId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE product SET remote_id = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertProduct(final Product product,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfProduct.insertAndReturnId(product);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteProduct(final Product product,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfProduct.handle(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateProduct(final Product product,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfProduct.handle(product);
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
  public Flow<List<Product>> getAllProductList() {
    final String _sql = "SELECT * FROM product WHERE archive_date IS NULL OR archive_date = '' ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"product"}, new Callable<List<Product>>() {
      @Override
      public List<Product> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSpecificWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "specific_weight");
          final int _cursorIndexOfRfid = CursorUtil.getColumnIndexOrThrow(_cursor, "rfid");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfImageSource = CursorUtil.getColumnIndexOrThrow(_cursor, "imageSource");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Product> _result = new ArrayList<Product>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Product _item;
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
            final double _tmpSpecificWeight;
            _tmpSpecificWeight = _cursor.getDouble(_cursorIndexOfSpecificWeight);
            final long _tmpRfid;
            _tmpRfid = _cursor.getLong(_cursorIndexOfRfid);
            final String _tmpImage;
            if (_cursor.isNull(_cursorIndexOfImage)) {
              _tmpImage = null;
            } else {
              _tmpImage = _cursor.getString(_cursorIndexOfImage);
            }
            final String _tmpImageSource;
            if (_cursor.isNull(_cursorIndexOfImageSource)) {
              _tmpImageSource = null;
            } else {
              _tmpImageSource = _cursor.getString(_cursorIndexOfImageSource);
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
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Product(_tmpName,_tmpDescription,_tmpSpecificWeight,_tmpRfid,_tmpImage,_tmpImageSource,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpId);
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
  public Flow<List<Product>> getActiveProductList() {
    final String _sql = "SELECT * FROM product ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"product"}, new Callable<List<Product>>() {
      @Override
      public List<Product> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSpecificWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "specific_weight");
          final int _cursorIndexOfRfid = CursorUtil.getColumnIndexOrThrow(_cursor, "rfid");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfImageSource = CursorUtil.getColumnIndexOrThrow(_cursor, "imageSource");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Product> _result = new ArrayList<Product>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Product _item;
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
            final double _tmpSpecificWeight;
            _tmpSpecificWeight = _cursor.getDouble(_cursorIndexOfSpecificWeight);
            final long _tmpRfid;
            _tmpRfid = _cursor.getLong(_cursorIndexOfRfid);
            final String _tmpImage;
            if (_cursor.isNull(_cursorIndexOfImage)) {
              _tmpImage = null;
            } else {
              _tmpImage = _cursor.getString(_cursorIndexOfImage);
            }
            final String _tmpImageSource;
            if (_cursor.isNull(_cursorIndexOfImageSource)) {
              _tmpImageSource = null;
            } else {
              _tmpImageSource = _cursor.getString(_cursorIndexOfImageSource);
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
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Product(_tmpName,_tmpDescription,_tmpSpecificWeight,_tmpRfid,_tmpImage,_tmpImageSource,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpId);
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
  public Flow<List<Product>> getFilteredProductList(final String name) {
    final String _sql = "SELECT * FROM product WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"product"}, new Callable<List<Product>>() {
      @Override
      public List<Product> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSpecificWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "specific_weight");
          final int _cursorIndexOfRfid = CursorUtil.getColumnIndexOrThrow(_cursor, "rfid");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfImageSource = CursorUtil.getColumnIndexOrThrow(_cursor, "imageSource");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Product> _result = new ArrayList<Product>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Product _item;
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
            final double _tmpSpecificWeight;
            _tmpSpecificWeight = _cursor.getDouble(_cursorIndexOfSpecificWeight);
            final long _tmpRfid;
            _tmpRfid = _cursor.getLong(_cursorIndexOfRfid);
            final String _tmpImage;
            if (_cursor.isNull(_cursorIndexOfImage)) {
              _tmpImage = null;
            } else {
              _tmpImage = _cursor.getString(_cursorIndexOfImage);
            }
            final String _tmpImageSource;
            if (_cursor.isNull(_cursorIndexOfImageSource)) {
              _tmpImageSource = null;
            } else {
              _tmpImageSource = _cursor.getString(_cursorIndexOfImageSource);
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
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Product(_tmpName,_tmpDescription,_tmpSpecificWeight,_tmpRfid,_tmpImage,_tmpImageSource,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpId);
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
  public Flow<Product> getProductById(final long id) {
    final String _sql = "SELECT * FROM product WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"product"}, new Callable<Product>() {
      @Override
      public Product call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfSpecificWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "specific_weight");
          final int _cursorIndexOfRfid = CursorUtil.getColumnIndexOrThrow(_cursor, "rfid");
          final int _cursorIndexOfImage = CursorUtil.getColumnIndexOrThrow(_cursor, "image");
          final int _cursorIndexOfImageSource = CursorUtil.getColumnIndexOrThrow(_cursor, "imageSource");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final Product _result;
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
            final double _tmpSpecificWeight;
            _tmpSpecificWeight = _cursor.getDouble(_cursorIndexOfSpecificWeight);
            final long _tmpRfid;
            _tmpRfid = _cursor.getLong(_cursorIndexOfRfid);
            final String _tmpImage;
            if (_cursor.isNull(_cursorIndexOfImage)) {
              _tmpImage = null;
            } else {
              _tmpImage = _cursor.getString(_cursorIndexOfImage);
            }
            final String _tmpImageSource;
            if (_cursor.isNull(_cursorIndexOfImageSource)) {
              _tmpImageSource = null;
            } else {
              _tmpImageSource = _cursor.getString(_cursorIndexOfImageSource);
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
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _result = new Product(_tmpName,_tmpDescription,_tmpSpecificWeight,_tmpRfid,_tmpImage,_tmpImageSource,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpId);
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
