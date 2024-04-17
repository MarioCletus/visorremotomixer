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
import com.basculasmagris.visorremotomixer.model.entities.Diet;
import com.basculasmagris.visorremotomixer.model.entities.DietProduct;
import com.basculasmagris.visorremotomixer.model.entities.DietProductDetail;
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
public final class DietDao_Impl implements DietDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Diet> __insertionAdapterOfDiet;

  private final EntityInsertionAdapter<DietProduct> __insertionAdapterOfDietProduct;

  private final EntityDeletionOrUpdateAdapter<Diet> __deletionAdapterOfDiet;

  private final EntityDeletionOrUpdateAdapter<Diet> __updateAdapterOfDiet;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedDate;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedRemoteId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteDiet;

  private final SharedSQLiteStatement __preparedStmtOfDeleteProductsByDiet;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDietProduct;

  public DietDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDiet = new EntityInsertionAdapter<Diet>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `diet` (`name`,`description`,`remote_id`,`updated_date`,`archive_date`,`user_percentage`,`id`) VALUES (?,?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Diet value) {
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
        stmt.bindLong(3, value.getRemoteId());
        if (value.getUpdatedDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getUpdatedDate());
        }
        if (value.getArchiveDate() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getArchiveDate());
        }
        final int _tmp = value.getUsePercentage() ? 1 : 0;
        stmt.bindLong(6, _tmp);
        stmt.bindLong(7, value.getId());
      }
    };
    this.__insertionAdapterOfDietProduct = new EntityInsertionAdapter<DietProduct>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `diet_product` (`diet_id`,`product_id`,`remote_diet_id`,`remote_product_id`,`order`,`weight`,`percentage`,`remote_id`,`updated_date`,`archive_date`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DietProduct value) {
        stmt.bindLong(1, value.getDietId());
        stmt.bindLong(2, value.getProductId());
        stmt.bindLong(3, value.getRemoteDietId());
        stmt.bindLong(4, value.getRemoteProductId());
        stmt.bindLong(5, value.getOrder());
        stmt.bindDouble(6, value.getWeight());
        stmt.bindDouble(7, value.getPercentage());
        stmt.bindLong(8, value.getRemoteId());
        if (value.getUpdatedDate() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getUpdatedDate());
        }
        if (value.getArchiveDate() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getArchiveDate());
        }
      }
    };
    this.__deletionAdapterOfDiet = new EntityDeletionOrUpdateAdapter<Diet>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `diet` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Diet value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfDiet = new EntityDeletionOrUpdateAdapter<Diet>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `diet` SET `name` = ?,`description` = ?,`remote_id` = ?,`updated_date` = ?,`archive_date` = ?,`user_percentage` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Diet value) {
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
        stmt.bindLong(3, value.getRemoteId());
        if (value.getUpdatedDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getUpdatedDate());
        }
        if (value.getArchiveDate() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getArchiveDate());
        }
        final int _tmp = value.getUsePercentage() ? 1 : 0;
        stmt.bindLong(6, _tmp);
        stmt.bindLong(7, value.getId());
        stmt.bindLong(8, value.getId());
      }
    };
    this.__preparedStmtOfSetUpdatedDate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE diet SET updated_date = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetUpdatedRemoteId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE diet SET remote_id = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteDiet = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM diet_product WHERE diet_id = ? AND product_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteProductsByDiet = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM diet_product WHERE diet_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateDietProduct = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE diet_product SET `order` = ?, weight = ?, percentage = ? WHERE diet_id = ? AND product_id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertDiet(final Diet diet, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfDiet.insertAndReturnId(diet);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertDietProduct(final DietProduct dietProduct,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDietProduct.insert(dietProduct);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteDiet(final Diet diet, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfDiet.handle(diet);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateDiet(final Diet diet, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfDiet.handle(diet);
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
  public Object deleteDiet(final long dietId, final long productId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteDiet.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, dietId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, productId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteDiet.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteProductsByDiet(final long dietId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteProductsByDiet.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, dietId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteProductsByDiet.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateDietProduct(final long dietId, final long productId, final int order,
      final double weight, final double percentage, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDietProduct.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, order);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, weight);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, percentage);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, dietId);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, productId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateDietProduct.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Diet>> getActiveDietList() {
    final String _sql = "SELECT * FROM diet WHERE archive_date IS NULL OR archive_date = '' ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"diet"}, new Callable<List<Diet>>() {
      @Override
      public List<Diet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfUsePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "user_percentage");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Diet> _result = new ArrayList<Diet>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Diet _item;
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
            final boolean _tmpUsePercentage;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUsePercentage);
            _tmpUsePercentage = _tmp != 0;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Diet(_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpUsePercentage,_tmpId);
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
  public Flow<List<Diet>> getAllDietList() {
    final String _sql = "SELECT * FROM diet ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"diet"}, new Callable<List<Diet>>() {
      @Override
      public List<Diet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfUsePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "user_percentage");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Diet> _result = new ArrayList<Diet>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Diet _item;
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
            final boolean _tmpUsePercentage;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUsePercentage);
            _tmpUsePercentage = _tmp != 0;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Diet(_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpUsePercentage,_tmpId);
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
  public Flow<List<Diet>> getFilteredDietList(final String name) {
    final String _sql = "SELECT * FROM diet WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"diet"}, new Callable<List<Diet>>() {
      @Override
      public List<Diet> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfUsePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "user_percentage");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Diet> _result = new ArrayList<Diet>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Diet _item;
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
            final boolean _tmpUsePercentage;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUsePercentage);
            _tmpUsePercentage = _tmp != 0;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Diet(_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpUsePercentage,_tmpId);
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
  public Flow<Diet> getDietById(final long id) {
    final String _sql = "SELECT * FROM diet WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"diet"}, new Callable<Diet>() {
      @Override
      public Diet call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfUsePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "user_percentage");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final Diet _result;
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
            final boolean _tmpUsePercentage;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUsePercentage);
            _tmpUsePercentage = _tmp != 0;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _result = new Diet(_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpUsePercentage,_tmpId);
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
  public Flow<List<DietProductDetail>> getProductsBy(final long idDiet) {
    final String _sql = "SELECT dp.remote_diet_id as remoteDietId, dp.remote_product_id as remoteProductId, dp.product_id as productId, p.name as productName, p.description as productDescription, dp.diet_id as dietId,dp.percentage, dp.weight, dp.`order` FROM diet_product as dp JOIN product as p ON p.id = dp.product_id WHERE diet_id = ? ORDER BY dp.`order`";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, idDiet);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"diet_product","product"}, new Callable<List<DietProductDetail>>() {
      @Override
      public List<DietProductDetail> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRemoteDietId = 0;
          final int _cursorIndexOfRemoteProductId = 1;
          final int _cursorIndexOfProductId = 2;
          final int _cursorIndexOfProductName = 3;
          final int _cursorIndexOfProductDescription = 4;
          final int _cursorIndexOfDietId = 5;
          final int _cursorIndexOfPercentage = 6;
          final int _cursorIndexOfWeight = 7;
          final int _cursorIndexOfOrder = 8;
          final List<DietProductDetail> _result = new ArrayList<DietProductDetail>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DietProductDetail _item;
            final long _tmpRemoteDietId;
            _tmpRemoteDietId = _cursor.getLong(_cursorIndexOfRemoteDietId);
            final long _tmpRemoteProductId;
            _tmpRemoteProductId = _cursor.getLong(_cursorIndexOfRemoteProductId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final String _tmpProductName;
            if (_cursor.isNull(_cursorIndexOfProductName)) {
              _tmpProductName = null;
            } else {
              _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            }
            final String _tmpProductDescription;
            if (_cursor.isNull(_cursorIndexOfProductDescription)) {
              _tmpProductDescription = null;
            } else {
              _tmpProductDescription = _cursor.getString(_cursorIndexOfProductDescription);
            }
            final long _tmpDietId;
            _tmpDietId = _cursor.getLong(_cursorIndexOfDietId);
            final double _tmpPercentage;
            _tmpPercentage = _cursor.getDouble(_cursorIndexOfPercentage);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            _item = new DietProductDetail(_tmpProductId,_tmpRemoteProductId,_tmpProductName,_tmpProductDescription,_tmpDietId,_tmpRemoteDietId,_tmpPercentage,_tmpWeight,_tmpOrder);
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
  public List<DietProductDetail> getSyncProductsBy(final long idDiet) {
    final String _sql = "SELECT  dp.remote_diet_id as remoteDietId, dp.remote_product_id as remoteProductId, dp.product_id as productId, p.name as productName, p.description as productDescription, dp.diet_id as dietId,dp.percentage, dp.weight, dp.`order` FROM diet_product as dp JOIN product as p ON p.id = dp.product_id WHERE diet_id = ? ORDER BY dp.`order`";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, idDiet);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfRemoteDietId = 0;
      final int _cursorIndexOfRemoteProductId = 1;
      final int _cursorIndexOfProductId = 2;
      final int _cursorIndexOfProductName = 3;
      final int _cursorIndexOfProductDescription = 4;
      final int _cursorIndexOfDietId = 5;
      final int _cursorIndexOfPercentage = 6;
      final int _cursorIndexOfWeight = 7;
      final int _cursorIndexOfOrder = 8;
      final List<DietProductDetail> _result = new ArrayList<DietProductDetail>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final DietProductDetail _item;
        final long _tmpRemoteDietId;
        _tmpRemoteDietId = _cursor.getLong(_cursorIndexOfRemoteDietId);
        final long _tmpRemoteProductId;
        _tmpRemoteProductId = _cursor.getLong(_cursorIndexOfRemoteProductId);
        final long _tmpProductId;
        _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
        final String _tmpProductName;
        if (_cursor.isNull(_cursorIndexOfProductName)) {
          _tmpProductName = null;
        } else {
          _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
        }
        final String _tmpProductDescription;
        if (_cursor.isNull(_cursorIndexOfProductDescription)) {
          _tmpProductDescription = null;
        } else {
          _tmpProductDescription = _cursor.getString(_cursorIndexOfProductDescription);
        }
        final long _tmpDietId;
        _tmpDietId = _cursor.getLong(_cursorIndexOfDietId);
        final double _tmpPercentage;
        _tmpPercentage = _cursor.getDouble(_cursorIndexOfPercentage);
        final double _tmpWeight;
        _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
        final int _tmpOrder;
        _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
        _item = new DietProductDetail(_tmpProductId,_tmpRemoteProductId,_tmpProductName,_tmpProductDescription,_tmpDietId,_tmpRemoteDietId,_tmpPercentage,_tmpWeight,_tmpOrder);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Flow<List<DietProduct>> getAllDietProductList() {
    final String _sql = "SELECT * FROM diet_product as dp ORDER BY dp.`order`";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"diet_product"}, new Callable<List<DietProduct>>() {
      @Override
      public List<DietProduct> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDietId = CursorUtil.getColumnIndexOrThrow(_cursor, "diet_id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "product_id");
          final int _cursorIndexOfRemoteDietId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_diet_id");
          final int _cursorIndexOfRemoteProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_product_id");
          final int _cursorIndexOfOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "order");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "percentage");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final List<DietProduct> _result = new ArrayList<DietProduct>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DietProduct _item;
            final long _tmpDietId;
            _tmpDietId = _cursor.getLong(_cursorIndexOfDietId);
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpRemoteDietId;
            _tmpRemoteDietId = _cursor.getLong(_cursorIndexOfRemoteDietId);
            final long _tmpRemoteProductId;
            _tmpRemoteProductId = _cursor.getLong(_cursorIndexOfRemoteProductId);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final double _tmpPercentage;
            _tmpPercentage = _cursor.getDouble(_cursorIndexOfPercentage);
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
            _item = new DietProduct(_tmpDietId,_tmpProductId,_tmpRemoteDietId,_tmpRemoteProductId,_tmpOrder,_tmpWeight,_tmpPercentage,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate);
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
