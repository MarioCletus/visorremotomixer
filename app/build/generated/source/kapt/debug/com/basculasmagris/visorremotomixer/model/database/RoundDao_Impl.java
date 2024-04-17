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
import com.basculasmagris.visorremotomixer.model.entities.Round;
import com.basculasmagris.visorremotomixer.model.entities.RoundCorral;
import com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail;
import com.basculasmagris.visorremotomixer.model.entities.RoundRun;
import com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload;
import com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad;
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
public final class RoundDao_Impl implements RoundDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Round> __insertionAdapterOfRound;

  private final EntityInsertionAdapter<RoundCorral> __insertionAdapterOfRoundCorral;

  private final EntityDeletionOrUpdateAdapter<Round> __deletionAdapterOfRound;

  private final EntityDeletionOrUpdateAdapter<RoundRun> __deletionAdapterOfRoundRun;

  private final EntityDeletionOrUpdateAdapter<Round> __updateAdapterOfRound;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedDate;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedRemoteId;

  private final SharedSQLiteStatement __preparedStmtOfSetUpdatedRemoteRoundRunId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRound;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRoundById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRoundRunById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCorralByRound;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRoundCorral;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRoundProgressLoad;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRoundProgressDownload;

  private final SharedSQLiteStatement __preparedStmtOfAddRoundProgressLoad;

  private final SharedSQLiteStatement __preparedStmtOfAddRoundRun;

  private final SharedSQLiteStatement __preparedStmtOfUpdateRoundRun;

  private final SharedSQLiteStatement __preparedStmtOfFinishRoundRun;

  private final SharedSQLiteStatement __preparedStmtOfFinishRoundRunWState;

  private final SharedSQLiteStatement __preparedStmtOfAddRoundProgressDownload;

  public RoundDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRound = new EntityInsertionAdapter<Round>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `round` (`name`,`description`,`remote_id`,`updated_date`,`archive_date`,`weight`,`use_percentage`,`custom_percentage`,`diet_id`,`remote_diet_id`,`id`) VALUES (?,?,?,?,?,?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Round value) {
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
        stmt.bindDouble(6, value.getWeight());
        final int _tmp = value.getUsePercentage() ? 1 : 0;
        stmt.bindLong(7, _tmp);
        stmt.bindDouble(8, value.getCustomPercentage());
        stmt.bindLong(9, value.getDietId());
        stmt.bindLong(10, value.getRemoteDietId());
        stmt.bindLong(11, value.getId());
      }
    };
    this.__insertionAdapterOfRoundCorral = new EntityInsertionAdapter<RoundCorral>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `round_corral` (`round_id`,`corral_id`,`remote_round_id`,`remote_corral_id`,`order`,`weight`,`percentage`,`remote_id`,`updated_date`,`archive_date`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RoundCorral value) {
        stmt.bindLong(1, value.getRoundId());
        stmt.bindLong(2, value.getCorralId());
        stmt.bindLong(3, value.getRemoteRoundId());
        stmt.bindLong(4, value.getRemoteCorralId());
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
    this.__deletionAdapterOfRound = new EntityDeletionOrUpdateAdapter<Round>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `round` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Round value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__deletionAdapterOfRoundRun = new EntityDeletionOrUpdateAdapter<RoundRun>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `round_run` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RoundRun value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfRound = new EntityDeletionOrUpdateAdapter<Round>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `round` SET `name` = ?,`description` = ?,`remote_id` = ?,`updated_date` = ?,`archive_date` = ?,`weight` = ?,`use_percentage` = ?,`custom_percentage` = ?,`diet_id` = ?,`remote_diet_id` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Round value) {
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
        stmt.bindDouble(6, value.getWeight());
        final int _tmp = value.getUsePercentage() ? 1 : 0;
        stmt.bindLong(7, _tmp);
        stmt.bindDouble(8, value.getCustomPercentage());
        stmt.bindLong(9, value.getDietId());
        stmt.bindLong(10, value.getRemoteDietId());
        stmt.bindLong(11, value.getId());
        stmt.bindLong(12, value.getId());
      }
    };
    this.__preparedStmtOfSetUpdatedDate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE round SET updated_date = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetUpdatedRemoteId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE round SET remote_id = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetUpdatedRemoteRoundRunId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE round_run SET remote_id = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteRound = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM round_corral WHERE round_id = ? AND corral_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteRoundById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ROUND WHERE ROUND.ID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteRoundRunById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ROUND_RUN WHERE ROUND_RUN.ID = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCorralByRound = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM round_corral WHERE round_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateRoundCorral = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE round_corral SET `order` = ?, weight = ?, percentage = ? WHERE round_id = ? AND corral_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateRoundProgressLoad = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE round_run_progress_load SET  initial_weight = ?,current_weight = ?,final_weight = ?, target_weight = ? WHERE round_run_id = ? AND diet_id = ? AND  product_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateRoundProgressDownload = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE round_run_progress_download SET  initial_weight = ?,current_weight = ?,final_weight = ?, custom_target_weight = ?, actual_target_weight = ? WHERE round_run_id = ? AND corral_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfAddRoundProgressLoad = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "INSERT INTO round_run_progress_load (round_run_id, diet_id, product_id, initial_weight, current_weight, final_weight, target_weight, remote_round_run_id, remote_diet_id, remote_product_id) VALUES (?, ?, ?, ?, ?, ?, ?, 0, 0, 0)";
        return _query;
      }
    };
    this.__preparedStmtOfAddRoundRun = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "INSERT INTO round_run (round_id, remote_round_id, start_date, updated_date, end_date, remote_id, mixer_id, remote_mixer_id, custom_percentage, custom_tara, added_blend, state, remote_user_id, user_display_name) VALUES (?, 0, datetime('now','localtime'), datetime('now','localtime'), '', 0, ?, 0, ?, ?, ?, ?, ?, ?)";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateRoundRun = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE round_run SET mixer_id = ?, updated_date = datetime('now','localtime'), custom_percentage = ?, custom_tara = ?, added_blend = ?, state = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfFinishRoundRun = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE round_run SET updated_date = datetime('now','localtime'), end_date = datetime('now','localtime')  WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfFinishRoundRunWState = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE round_run SET updated_date = datetime('now','localtime'), end_date = datetime('now','localtime'), state = ?  WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfAddRoundProgressDownload = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "INSERT INTO round_run_progress_download (round_run_id, corral_id, initial_weight, current_weight, final_weight, custom_target_weight, actual_target_weight, remote_round_run_id, remote_corral_id) VALUES (?, ?, ?, ?, ?, ?, ?, 0, 0)";
        return _query;
      }
    };
  }

  @Override
  public Object insertRound(final Round round, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfRound.insertAndReturnId(round);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object insertRoundCorral(final RoundCorral roundCorral,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRoundCorral.insert(roundCorral);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteRound(final Round round, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRound.handle(round);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteRoundRun(final RoundRun round_run,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRoundRun.handle(round_run);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateRound(final Round round, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRound.handle(round);
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
  public Object setUpdatedRemoteRoundRunId(final long id, final long remoteId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetUpdatedRemoteRoundRunId.acquire();
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
          __preparedStmtOfSetUpdatedRemoteRoundRunId.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteRound(final long roundId, final long corralId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRound.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, roundId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, corralId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteRound.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteRoundById(final long round_id,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRoundById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, round_id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteRoundById.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteRoundRunById(final long round_run_id,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRoundRunById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, round_run_id);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteRoundRunById.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteCorralByRound(final long roundId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCorralByRound.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, roundId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteCorralByRound.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateRoundCorral(final long roundId, final long corralId, final int order,
      final double weight, final double percentage, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRoundCorral.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, order);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, weight);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, percentage);
        _argIndex = 4;
        _stmt.bindLong(_argIndex, roundId);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, corralId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateRoundCorral.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateRoundProgressLoad(final long roundRunId, final long dietId,
      final long productId, final double initialWeight, final double currentWeight,
      final double finalWeight, final double targetWeight,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRoundProgressLoad.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, initialWeight);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, currentWeight);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, finalWeight);
        _argIndex = 4;
        _stmt.bindDouble(_argIndex, targetWeight);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, roundRunId);
        _argIndex = 6;
        _stmt.bindLong(_argIndex, dietId);
        _argIndex = 7;
        _stmt.bindLong(_argIndex, productId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateRoundProgressLoad.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateRoundProgressDownload(final long roundRunId, final long corralId,
      final double initialWeight, final double currentWeight, final double finalWeight,
      final double customTargetWeight, final double actualTargetWeight,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRoundProgressDownload.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, initialWeight);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, currentWeight);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, finalWeight);
        _argIndex = 4;
        _stmt.bindDouble(_argIndex, customTargetWeight);
        _argIndex = 5;
        _stmt.bindDouble(_argIndex, actualTargetWeight);
        _argIndex = 6;
        _stmt.bindLong(_argIndex, roundRunId);
        _argIndex = 7;
        _stmt.bindLong(_argIndex, corralId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateRoundProgressDownload.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object addRoundProgressLoad(final long roundRunId, final long dietId, final long productId,
      final double initialWeight, final double currentWeight, final double finalWeight,
      final double customTargetWeight, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddRoundProgressLoad.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, roundRunId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, dietId);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, productId);
        _argIndex = 4;
        _stmt.bindDouble(_argIndex, initialWeight);
        _argIndex = 5;
        _stmt.bindDouble(_argIndex, currentWeight);
        _argIndex = 6;
        _stmt.bindDouble(_argIndex, finalWeight);
        _argIndex = 7;
        _stmt.bindDouble(_argIndex, customTargetWeight);
        __db.beginTransaction();
        try {
          _stmt.executeInsert();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfAddRoundProgressLoad.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object addRoundRun(final long roundId, final long mixerId, final double customPercentage,
      final double customTara, final double addedBlend, final int state, final long userId,
      final String userDisplayName, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddRoundRun.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, roundId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, mixerId);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, customPercentage);
        _argIndex = 4;
        _stmt.bindDouble(_argIndex, customTara);
        _argIndex = 5;
        _stmt.bindDouble(_argIndex, addedBlend);
        _argIndex = 6;
        _stmt.bindLong(_argIndex, state);
        _argIndex = 7;
        _stmt.bindLong(_argIndex, userId);
        _argIndex = 8;
        if (userDisplayName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, userDisplayName);
        }
        __db.beginTransaction();
        try {
          final java.lang.Long _result = _stmt.executeInsert();
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
          __preparedStmtOfAddRoundRun.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object updateRoundRun(final long roundRunId, final long mixerId,
      final double customPercentage, final double customTara, final double addedBlend,
      final int state, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateRoundRun.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, mixerId);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, customPercentage);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, customTara);
        _argIndex = 4;
        _stmt.bindDouble(_argIndex, addedBlend);
        _argIndex = 5;
        _stmt.bindLong(_argIndex, state);
        _argIndex = 6;
        _stmt.bindLong(_argIndex, roundRunId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfUpdateRoundRun.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object finishRoundRun(final long roundRunId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfFinishRoundRun.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, roundRunId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfFinishRoundRun.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object finishRoundRunWState(final long roundRunId, final int state,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfFinishRoundRunWState.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, state);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, roundRunId);
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfFinishRoundRunWState.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Object addRoundProgressDownload(final long roundRunId, final long corralId,
      final double initialWeight, final double currentWeight, final double finalWeight,
      final double customTargetWeight, final double actualTargetWeight,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfAddRoundProgressDownload.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, roundRunId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, corralId);
        _argIndex = 3;
        _stmt.bindDouble(_argIndex, initialWeight);
        _argIndex = 4;
        _stmt.bindDouble(_argIndex, currentWeight);
        _argIndex = 5;
        _stmt.bindDouble(_argIndex, finalWeight);
        _argIndex = 6;
        _stmt.bindDouble(_argIndex, customTargetWeight);
        _argIndex = 7;
        _stmt.bindDouble(_argIndex, actualTargetWeight);
        __db.beginTransaction();
        try {
          _stmt.executeInsert();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfAddRoundProgressDownload.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<Round>> getActiveRoundList() {
    final String _sql = "SELECT * FROM round WHERE archive_date IS NULL OR archive_date = '' ORDER BY name";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"round"}, new Callable<List<Round>>() {
      @Override
      public List<Round> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfUsePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "use_percentage");
          final int _cursorIndexOfCustomPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "custom_percentage");
          final int _cursorIndexOfDietId = CursorUtil.getColumnIndexOrThrow(_cursor, "diet_id");
          final int _cursorIndexOfRemoteDietId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_diet_id");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Round> _result = new ArrayList<Round>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Round _item;
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
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final boolean _tmpUsePercentage;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUsePercentage);
            _tmpUsePercentage = _tmp != 0;
            final double _tmpCustomPercentage;
            _tmpCustomPercentage = _cursor.getDouble(_cursorIndexOfCustomPercentage);
            final long _tmpDietId;
            _tmpDietId = _cursor.getLong(_cursorIndexOfDietId);
            final long _tmpRemoteDietId;
            _tmpRemoteDietId = _cursor.getLong(_cursorIndexOfRemoteDietId);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Round(_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpWeight,_tmpUsePercentage,_tmpCustomPercentage,_tmpDietId,_tmpRemoteDietId,_tmpId);
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
  public Flow<List<Round>> getAllRoundList() {
    final String _sql = "SELECT * FROM round ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"round"}, new Callable<List<Round>>() {
      @Override
      public List<Round> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfUsePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "use_percentage");
          final int _cursorIndexOfCustomPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "custom_percentage");
          final int _cursorIndexOfDietId = CursorUtil.getColumnIndexOrThrow(_cursor, "diet_id");
          final int _cursorIndexOfRemoteDietId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_diet_id");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Round> _result = new ArrayList<Round>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Round _item;
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
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final boolean _tmpUsePercentage;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUsePercentage);
            _tmpUsePercentage = _tmp != 0;
            final double _tmpCustomPercentage;
            _tmpCustomPercentage = _cursor.getDouble(_cursorIndexOfCustomPercentage);
            final long _tmpDietId;
            _tmpDietId = _cursor.getLong(_cursorIndexOfDietId);
            final long _tmpRemoteDietId;
            _tmpRemoteDietId = _cursor.getLong(_cursorIndexOfRemoteDietId);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Round(_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpWeight,_tmpUsePercentage,_tmpCustomPercentage,_tmpDietId,_tmpRemoteDietId,_tmpId);
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
  public Flow<List<Round>> getFilteredRoundList(final String name) {
    final String _sql = "SELECT * FROM round WHERE archive_date IS NULL OR archive_date = '' ORDER BY name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, name);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"round"}, new Callable<List<Round>>() {
      @Override
      public List<Round> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfUsePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "use_percentage");
          final int _cursorIndexOfCustomPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "custom_percentage");
          final int _cursorIndexOfDietId = CursorUtil.getColumnIndexOrThrow(_cursor, "diet_id");
          final int _cursorIndexOfRemoteDietId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_diet_id");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<Round> _result = new ArrayList<Round>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Round _item;
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
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final boolean _tmpUsePercentage;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUsePercentage);
            _tmpUsePercentage = _tmp != 0;
            final double _tmpCustomPercentage;
            _tmpCustomPercentage = _cursor.getDouble(_cursorIndexOfCustomPercentage);
            final long _tmpDietId;
            _tmpDietId = _cursor.getLong(_cursorIndexOfDietId);
            final long _tmpRemoteDietId;
            _tmpRemoteDietId = _cursor.getLong(_cursorIndexOfRemoteDietId);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _item = new Round(_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpWeight,_tmpUsePercentage,_tmpCustomPercentage,_tmpDietId,_tmpRemoteDietId,_tmpId);
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
  public Flow<Round> getRoundById(final long id) {
    final String _sql = "SELECT * FROM round WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"round"}, new Callable<Round>() {
      @Override
      public Round call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfRemoteId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_id");
          final int _cursorIndexOfUpdatedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_date");
          final int _cursorIndexOfArchiveDate = CursorUtil.getColumnIndexOrThrow(_cursor, "archive_date");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfUsePercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "use_percentage");
          final int _cursorIndexOfCustomPercentage = CursorUtil.getColumnIndexOrThrow(_cursor, "custom_percentage");
          final int _cursorIndexOfDietId = CursorUtil.getColumnIndexOrThrow(_cursor, "diet_id");
          final int _cursorIndexOfRemoteDietId = CursorUtil.getColumnIndexOrThrow(_cursor, "remote_diet_id");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final Round _result;
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
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final boolean _tmpUsePercentage;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfUsePercentage);
            _tmpUsePercentage = _tmp != 0;
            final double _tmpCustomPercentage;
            _tmpCustomPercentage = _cursor.getDouble(_cursorIndexOfCustomPercentage);
            final long _tmpDietId;
            _tmpDietId = _cursor.getLong(_cursorIndexOfDietId);
            final long _tmpRemoteDietId;
            _tmpRemoteDietId = _cursor.getLong(_cursorIndexOfRemoteDietId);
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            _result = new Round(_tmpName,_tmpDescription,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate,_tmpWeight,_tmpUsePercentage,_tmpCustomPercentage,_tmpDietId,_tmpRemoteDietId,_tmpId);
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
  public Flow<List<RoundCorralDetail>> getCorralsBy(final long idRound) {
    final String _sql = "SELECT dp.remote_round_id as remoteRoundId, dp.remote_corral_id as remoteCorralId, dp.corral_id as corralId, p.name as corralName, p.description as corralDescription, dp.round_id as roundId,dp.percentage, dp.weight, dp.`order`, p.animal_quantity as animalQuantity FROM round_corral as dp JOIN corral as p ON p.id = dp.corral_id WHERE round_id = ? ORDER BY dp.`order`";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, idRound);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"round_corral","corral"}, new Callable<List<RoundCorralDetail>>() {
      @Override
      public List<RoundCorralDetail> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfRemoteRoundId = 0;
          final int _cursorIndexOfRemoteCorralId = 1;
          final int _cursorIndexOfCorralId = 2;
          final int _cursorIndexOfCorralName = 3;
          final int _cursorIndexOfCorralDescription = 4;
          final int _cursorIndexOfRoundId = 5;
          final int _cursorIndexOfPercentage = 6;
          final int _cursorIndexOfWeight = 7;
          final int _cursorIndexOfOrder = 8;
          final int _cursorIndexOfAnimalQuantity = 9;
          final List<RoundCorralDetail> _result = new ArrayList<RoundCorralDetail>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RoundCorralDetail _item;
            final long _tmpRemoteRoundId;
            _tmpRemoteRoundId = _cursor.getLong(_cursorIndexOfRemoteRoundId);
            final long _tmpRemoteCorralId;
            _tmpRemoteCorralId = _cursor.getLong(_cursorIndexOfRemoteCorralId);
            final long _tmpCorralId;
            _tmpCorralId = _cursor.getLong(_cursorIndexOfCorralId);
            final String _tmpCorralName;
            if (_cursor.isNull(_cursorIndexOfCorralName)) {
              _tmpCorralName = null;
            } else {
              _tmpCorralName = _cursor.getString(_cursorIndexOfCorralName);
            }
            final String _tmpCorralDescription;
            if (_cursor.isNull(_cursorIndexOfCorralDescription)) {
              _tmpCorralDescription = null;
            } else {
              _tmpCorralDescription = _cursor.getString(_cursorIndexOfCorralDescription);
            }
            final long _tmpRoundId;
            _tmpRoundId = _cursor.getLong(_cursorIndexOfRoundId);
            final double _tmpPercentage;
            _tmpPercentage = _cursor.getDouble(_cursorIndexOfPercentage);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final int _tmpAnimalQuantity;
            _tmpAnimalQuantity = _cursor.getInt(_cursorIndexOfAnimalQuantity);
            _item = new RoundCorralDetail(_tmpCorralId,_tmpRemoteCorralId,_tmpCorralName,_tmpCorralDescription,_tmpRoundId,_tmpRemoteRoundId,_tmpPercentage,_tmpWeight,_tmpOrder,_tmpAnimalQuantity);
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
  public Flow<List<RoundCorral>> getAllRoundCorralList() {
    final String _sql = "SELECT rc.`order`, rc.weight, rc.percentage, rc.remote_id, rc.archive_date, rc.corral_id, rc.remote_corral_id, rc.remote_round_id, rc.round_id, rc.updated_date, c.animal_quantity as animalQuantity FROM round_corral rc JOIN corral c ON c.id == rc.corral_id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"round_corral","corral"}, new Callable<List<RoundCorral>>() {
      @Override
      public List<RoundCorral> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfOrder = 0;
          final int _cursorIndexOfWeight = 1;
          final int _cursorIndexOfPercentage = 2;
          final int _cursorIndexOfRemoteId = 3;
          final int _cursorIndexOfArchiveDate = 4;
          final int _cursorIndexOfCorralId = 5;
          final int _cursorIndexOfRemoteCorralId = 6;
          final int _cursorIndexOfRemoteRoundId = 7;
          final int _cursorIndexOfRoundId = 8;
          final int _cursorIndexOfUpdatedDate = 9;
          final List<RoundCorral> _result = new ArrayList<RoundCorral>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RoundCorral _item;
            final int _tmpOrder;
            _tmpOrder = _cursor.getInt(_cursorIndexOfOrder);
            final double _tmpWeight;
            _tmpWeight = _cursor.getDouble(_cursorIndexOfWeight);
            final double _tmpPercentage;
            _tmpPercentage = _cursor.getDouble(_cursorIndexOfPercentage);
            final long _tmpRemoteId;
            _tmpRemoteId = _cursor.getLong(_cursorIndexOfRemoteId);
            final String _tmpArchiveDate;
            if (_cursor.isNull(_cursorIndexOfArchiveDate)) {
              _tmpArchiveDate = null;
            } else {
              _tmpArchiveDate = _cursor.getString(_cursorIndexOfArchiveDate);
            }
            final long _tmpCorralId;
            _tmpCorralId = _cursor.getLong(_cursorIndexOfCorralId);
            final long _tmpRemoteCorralId;
            _tmpRemoteCorralId = _cursor.getLong(_cursorIndexOfRemoteCorralId);
            final long _tmpRemoteRoundId;
            _tmpRemoteRoundId = _cursor.getLong(_cursorIndexOfRemoteRoundId);
            final long _tmpRoundId;
            _tmpRoundId = _cursor.getLong(_cursorIndexOfRoundId);
            final String _tmpUpdatedDate;
            if (_cursor.isNull(_cursorIndexOfUpdatedDate)) {
              _tmpUpdatedDate = null;
            } else {
              _tmpUpdatedDate = _cursor.getString(_cursorIndexOfUpdatedDate);
            }
            _item = new RoundCorral(_tmpRoundId,_tmpCorralId,_tmpRemoteRoundId,_tmpRemoteCorralId,_tmpOrder,_tmpWeight,_tmpPercentage,_tmpRemoteId,_tmpUpdatedDate,_tmpArchiveDate);
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
  public Flow<List<RoundRun>> getAllRoundRunList() {
    final String _sql = "SELECT rr.id, rr.updated_date , rr.round_id , rr.remote_round_id ,rr.remote_id , rr.end_date , rr.start_date, rr.mixer_id, rr.remote_mixer_id, rr.custom_percentage, rr.custom_tara, rr.added_blend, rr.state, rr.remote_user_id, rr.user_display_name FROM round_run rr";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"round_run"}, new Callable<List<RoundRun>>() {
      @Override
      public List<RoundRun> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfUpdatedDate = 1;
          final int _cursorIndexOfRoundId = 2;
          final int _cursorIndexOfRemoteRoundId = 3;
          final int _cursorIndexOfRemoteId = 4;
          final int _cursorIndexOfEndDate = 5;
          final int _cursorIndexOfStartDate = 6;
          final int _cursorIndexOfMixerId = 7;
          final int _cursorIndexOfRemoteMixerId = 8;
          final int _cursorIndexOfCustomPercentage = 9;
          final int _cursorIndexOfCustomTara = 10;
          final int _cursorIndexOfAddedBlend = 11;
          final int _cursorIndexOfState = 12;
          final int _cursorIndexOfRemoteUserId = 13;
          final int _cursorIndexOfUserDisplayName = 14;
          final List<RoundRun> _result = new ArrayList<RoundRun>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RoundRun _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpUpdatedDate;
            if (_cursor.isNull(_cursorIndexOfUpdatedDate)) {
              _tmpUpdatedDate = null;
            } else {
              _tmpUpdatedDate = _cursor.getString(_cursorIndexOfUpdatedDate);
            }
            final long _tmpRoundId;
            _tmpRoundId = _cursor.getLong(_cursorIndexOfRoundId);
            final long _tmpRemoteRoundId;
            _tmpRemoteRoundId = _cursor.getLong(_cursorIndexOfRemoteRoundId);
            final long _tmpRemoteId;
            _tmpRemoteId = _cursor.getLong(_cursorIndexOfRemoteId);
            final String _tmpEndDate;
            if (_cursor.isNull(_cursorIndexOfEndDate)) {
              _tmpEndDate = null;
            } else {
              _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
            }
            final String _tmpStartDate;
            if (_cursor.isNull(_cursorIndexOfStartDate)) {
              _tmpStartDate = null;
            } else {
              _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
            }
            final long _tmpMixerId;
            _tmpMixerId = _cursor.getLong(_cursorIndexOfMixerId);
            final long _tmpRemoteMixerId;
            _tmpRemoteMixerId = _cursor.getLong(_cursorIndexOfRemoteMixerId);
            final double _tmpCustomPercentage;
            _tmpCustomPercentage = _cursor.getDouble(_cursorIndexOfCustomPercentage);
            final double _tmpCustomTara;
            _tmpCustomTara = _cursor.getDouble(_cursorIndexOfCustomTara);
            final double _tmpAddedBlend;
            _tmpAddedBlend = _cursor.getDouble(_cursorIndexOfAddedBlend);
            final int _tmpState;
            _tmpState = _cursor.getInt(_cursorIndexOfState);
            final long _tmpRemoteUserId;
            _tmpRemoteUserId = _cursor.getLong(_cursorIndexOfRemoteUserId);
            final String _tmpUserDisplayName;
            if (_cursor.isNull(_cursorIndexOfUserDisplayName)) {
              _tmpUserDisplayName = null;
            } else {
              _tmpUserDisplayName = _cursor.getString(_cursorIndexOfUserDisplayName);
            }
            _item = new RoundRun(_tmpRemoteUserId,_tmpUserDisplayName,_tmpRoundId,_tmpRemoteRoundId,_tmpMixerId,_tmpRemoteMixerId,_tmpStartDate,_tmpUpdatedDate,_tmpEndDate,_tmpRemoteId,_tmpCustomPercentage,_tmpCustomTara,_tmpAddedBlend,_tmpState,_tmpId);
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
  public Flow<List<RoundRunProgressLoad>> getAllRoundRunProgressLoadList() {
    final String _sql = "SELECT rrpl.product_id , rrpl.diet_id , rrpl.initial_weight , rrpl.current_weight, rrpl.final_weight , rrpl.remote_diet_id , rrpl.remote_product_id ,rrpl.remote_round_run_id , rrpl.round_run_id ,rrpl.target_weight  FROM round_run_progress_load rrpl";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"round_run_progress_load"}, new Callable<List<RoundRunProgressLoad>>() {
      @Override
      public List<RoundRunProgressLoad> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProductId = 0;
          final int _cursorIndexOfDietId = 1;
          final int _cursorIndexOfInitialWeight = 2;
          final int _cursorIndexOfCurrentWeight = 3;
          final int _cursorIndexOfFinalWeight = 4;
          final int _cursorIndexOfRemoteDietId = 5;
          final int _cursorIndexOfRemoteProductId = 6;
          final int _cursorIndexOfRemoteRoundRunId = 7;
          final int _cursorIndexOfRoundRunId = 8;
          final int _cursorIndexOfTargetWeight = 9;
          final List<RoundRunProgressLoad> _result = new ArrayList<RoundRunProgressLoad>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RoundRunProgressLoad _item;
            final long _tmpProductId;
            _tmpProductId = _cursor.getLong(_cursorIndexOfProductId);
            final long _tmpDietId;
            _tmpDietId = _cursor.getLong(_cursorIndexOfDietId);
            final double _tmpInitialWeight;
            _tmpInitialWeight = _cursor.getDouble(_cursorIndexOfInitialWeight);
            final double _tmpCurrentWeight;
            _tmpCurrentWeight = _cursor.getDouble(_cursorIndexOfCurrentWeight);
            final double _tmpFinalWeight;
            _tmpFinalWeight = _cursor.getDouble(_cursorIndexOfFinalWeight);
            final long _tmpRemoteDietId;
            _tmpRemoteDietId = _cursor.getLong(_cursorIndexOfRemoteDietId);
            final long _tmpRemoteProductId;
            _tmpRemoteProductId = _cursor.getLong(_cursorIndexOfRemoteProductId);
            final long _tmpRemoteRoundRunId;
            _tmpRemoteRoundRunId = _cursor.getLong(_cursorIndexOfRemoteRoundRunId);
            final long _tmpRoundRunId;
            _tmpRoundRunId = _cursor.getLong(_cursorIndexOfRoundRunId);
            final double _tmpTargetWeight;
            _tmpTargetWeight = _cursor.getDouble(_cursorIndexOfTargetWeight);
            _item = new RoundRunProgressLoad(_tmpRoundRunId,_tmpRemoteRoundRunId,_tmpDietId,_tmpRemoteDietId,_tmpProductId,_tmpRemoteProductId,_tmpInitialWeight,_tmpCurrentWeight,_tmpFinalWeight,_tmpTargetWeight);
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
  public Flow<List<RoundRunProgressDownload>> getAllRoundRunProgressDownloadlist() {
    final String _sql = "SELECT rrpd.initial_weight ,rrpd.current_weight ,rrpd.final_weight ,rrpd.remote_round_run_id , rrpd.round_run_id ,rrpd.actual_target_weight , rrpd.corral_id , rrpd.remote_corral_id, rrpd.custom_target_weight  FROM round_run_progress_download rrpd";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"round_run_progress_download"}, new Callable<List<RoundRunProgressDownload>>() {
      @Override
      public List<RoundRunProgressDownload> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfInitialWeight = 0;
          final int _cursorIndexOfCurrentWeight = 1;
          final int _cursorIndexOfFinalWeight = 2;
          final int _cursorIndexOfRemoteRoundRunId = 3;
          final int _cursorIndexOfRoundRunId = 4;
          final int _cursorIndexOfActualTargetWeight = 5;
          final int _cursorIndexOfCorralId = 6;
          final int _cursorIndexOfRemoteCorralId = 7;
          final int _cursorIndexOfCustomTargetWeight = 8;
          final List<RoundRunProgressDownload> _result = new ArrayList<RoundRunProgressDownload>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RoundRunProgressDownload _item;
            final double _tmpInitialWeight;
            _tmpInitialWeight = _cursor.getDouble(_cursorIndexOfInitialWeight);
            final double _tmpCurrentWeight;
            _tmpCurrentWeight = _cursor.getDouble(_cursorIndexOfCurrentWeight);
            final double _tmpFinalWeight;
            _tmpFinalWeight = _cursor.getDouble(_cursorIndexOfFinalWeight);
            final long _tmpRemoteRoundRunId;
            _tmpRemoteRoundRunId = _cursor.getLong(_cursorIndexOfRemoteRoundRunId);
            final long _tmpRoundRunId;
            _tmpRoundRunId = _cursor.getLong(_cursorIndexOfRoundRunId);
            final double _tmpActualTargetWeight;
            _tmpActualTargetWeight = _cursor.getDouble(_cursorIndexOfActualTargetWeight);
            final long _tmpCorralId;
            _tmpCorralId = _cursor.getLong(_cursorIndexOfCorralId);
            final long _tmpRemoteCorralId;
            _tmpRemoteCorralId = _cursor.getLong(_cursorIndexOfRemoteCorralId);
            final double _tmpCustomTargetWeight;
            _tmpCustomTargetWeight = _cursor.getDouble(_cursorIndexOfCustomTargetWeight);
            _item = new RoundRunProgressDownload(_tmpRoundRunId,_tmpRemoteRoundRunId,_tmpCorralId,_tmpRemoteCorralId,_tmpInitialWeight,_tmpCurrentWeight,_tmpFinalWeight,_tmpCustomTargetWeight,_tmpActualTargetWeight);
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
