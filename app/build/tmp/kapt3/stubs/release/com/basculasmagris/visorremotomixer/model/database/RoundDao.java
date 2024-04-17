package com.basculasmagris.visorremotomixer.model.database;

import java.lang.System;

@androidx.room.Dao
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0010\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0019\bg\u0018\u00002\u00020\u0001JI\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJI\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011JQ\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00052\u0006\u0010\u001b\u001a\u00020\u001cH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001dJ\u0019\u0010\u001e\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fJ\u0019\u0010 \u001a\u00020\u00032\u0006\u0010!\u001a\u00020\"H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J!\u0010 \u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$J\u0019\u0010%\u001a\u00020\u00032\u0006\u0010&\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fJ\u0019\u0010\'\u001a\u00020\u00032\u0006\u0010(\u001a\u00020)H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010*J\u0019\u0010+\u001a\u00020\u00032\u0006\u0010,\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fJ\u0019\u0010-\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001fJ!\u0010.\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0018\u001a\u00020\u0019H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010/J\u0014\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0201H\'J\u0014\u00103\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002040201H\'J\u0014\u00105\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0201H\'J\u0014\u00106\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020)0201H\'J\u0014\u00107\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002080201H\'J\u0014\u00109\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020:0201H\'J\u001c\u0010;\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020<02012\u0006\u0010=\u001a\u00020\u0005H\'J\u001c\u0010>\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\"0?012\u0006\u0010@\u001a\u00020\u001cH\'J\u0016\u0010A\u001a\b\u0012\u0004\u0012\u00020\"012\u0006\u0010B\u001a\u00020\u0005H\'J\u0019\u0010C\u001a\u00020\u00052\u0006\u0010!\u001a\u00020\"H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u0019\u0010D\u001a\u00020\u00032\u0006\u0010E\u001a\u000204H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010FJ!\u0010G\u001a\u00020\u00032\u0006\u0010B\u001a\u00020\u00052\u0006\u0010H\u001a\u00020\u001cH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010IJ!\u0010J\u001a\u00020\u00032\u0006\u0010B\u001a\u00020\u00052\u0006\u0010K\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$J!\u0010L\u001a\u00020\u00032\u0006\u0010B\u001a\u00020\u00052\u0006\u0010K\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$J\u0019\u0010M\u001a\u00020\u00032\u0006\u0010!\u001a\u00020\"H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J9\u0010N\u001a\u00020\u00032\u0006\u0010\u0013\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010O\u001a\u00020\u00192\u0006\u0010P\u001a\u00020\b2\u0006\u0010Q\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010RJI\u0010S\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJI\u0010T\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010U\u001a\u00020\bH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011JA\u0010V\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u0019H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010W\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006X"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/database/RoundDao;", "", "addRoundProgressDownload", "", "roundRunId", "", "corralId", "initialWeight", "", "currentWeight", "finalWeight", "customTargetWeight", "actualTargetWeight", "(JJDDDDDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addRoundProgressLoad", "dietId", "productId", "(JJJDDDDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addRoundRun", "roundId", "mixerId", "customPercentage", "customTara", "addedBlend", "state", "", "userId", "userDisplayName", "", "(JJDDDIJLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCorralByRound", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRound", "round", "Lcom/basculasmagris/visorremotomixer/model/entities/Round;", "(Lcom/basculasmagris/visorremotomixer/model/entities/Round;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRoundById", "round_id", "deleteRoundRun", "round_run", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRun;", "(Lcom/basculasmagris/visorremotomixer/model/entities/RoundRun;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRoundRunById", "round_run_id", "finishRoundRun", "finishRoundRunWState", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getActiveRoundList", "Lkotlinx/coroutines/flow/Flow;", "", "getAllRoundCorralList", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundCorral;", "getAllRoundList", "getAllRoundRunList", "getAllRoundRunProgressDownloadlist", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunProgressDownload;", "getAllRoundRunProgressLoadList", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunProgressLoad;", "getCorralsBy", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundCorralDetail;", "idRound", "getFilteredRoundList", "", "name", "getRoundById", "id", "insertRound", "insertRoundCorral", "roundCorral", "(Lcom/basculasmagris/visorremotomixer/model/entities/RoundCorral;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedDate", "date", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedRemoteId", "remoteId", "setUpdatedRemoteRoundRunId", "updateRound", "updateRoundCorral", "order", "weight", "percentage", "(JJIDDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateRoundProgressDownload", "updateRoundProgressLoad", "targetWeight", "updateRoundRun", "(JJDDDILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public abstract interface RoundDao {
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert
    public abstract java.lang.Object insertRound(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM round WHERE archive_date IS NULL OR archive_date = \'\' ORDER BY name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> getActiveRoundList();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM round ORDER BY id DESC")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> getAllRoundList();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM round WHERE archive_date IS NULL OR archive_date = \'\' ORDER BY name LIKE :name")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> getFilteredRoundList(@org.jetbrains.annotations.NotNull
    java.lang.String name);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT * FROM round WHERE id = :id")
    public abstract kotlinx.coroutines.flow.Flow<com.basculasmagris.visorremotomixer.model.entities.Round> getRoundById(long id);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Update
    public abstract java.lang.Object updateRound(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE round SET updated_date = :date WHERE id = :id")
    public abstract java.lang.Object setUpdatedDate(long id, @org.jetbrains.annotations.NotNull
    java.lang.String date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE round SET remote_id = :remoteId WHERE id = :id")
    public abstract java.lang.Object setUpdatedRemoteId(long id, long remoteId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE round_run SET remote_id = :remoteId WHERE id = :id")
    public abstract java.lang.Object setUpdatedRemoteRoundRunId(long id, long remoteId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Delete
    public abstract java.lang.Object deleteRound(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "DELETE FROM ROUND WHERE ROUND.ID = :round_id")
    public abstract java.lang.Object deleteRoundById(long round_id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Delete
    public abstract java.lang.Object deleteRoundRun(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundRun round_run, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "DELETE FROM ROUND_RUN WHERE ROUND_RUN.ID = :round_run_id")
    public abstract java.lang.Object deleteRoundRunById(long round_run_id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "DELETE FROM round_corral WHERE round_id = :roundId AND corral_id = :corralId")
    public abstract java.lang.Object deleteRound(long roundId, long corralId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "DELETE FROM round_corral WHERE round_id = :roundId")
    public abstract java.lang.Object deleteCorralByRound(long roundId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT dp.remote_round_id as remoteRoundId, dp.remote_corral_id as remoteCorralId, dp.corral_id as corralId, p.name as corralName, p.description as corralDescription, dp.round_id as roundId,dp.percentage, dp.weight, dp.`order`, p.animal_quantity as animalQuantity FROM round_corral as dp JOIN corral as p ON p.id = dp.corral_id WHERE round_id = :idRound ORDER BY dp.`order`")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail>> getCorralsBy(long idRound);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT rc.`order`, rc.weight, rc.percentage, rc.remote_id, rc.archive_date, rc.corral_id, rc.remote_corral_id, rc.remote_round_id, rc.round_id, rc.updated_date, c.animal_quantity as animalQuantity FROM round_corral rc JOIN corral c ON c.id == rc.corral_id")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundCorral>> getAllRoundCorralList();
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Insert
    public abstract java.lang.Object insertRoundCorral(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundCorral roundCorral, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE round_corral SET `order` = :order, weight = :weight, percentage = :percentage WHERE round_id = :roundId AND corral_id = :corralId")
    public abstract java.lang.Object updateRoundCorral(long roundId, long corralId, int order, double weight, double percentage, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE round_run_progress_load SET  initial_weight = :initialWeight,current_weight = :currentWeight,final_weight = :finalWeight, target_weight = :targetWeight WHERE round_run_id = :roundRunId AND diet_id = :dietId AND  product_id = :productId")
    public abstract java.lang.Object updateRoundProgressLoad(long roundRunId, long dietId, long productId, double initialWeight, double currentWeight, double finalWeight, double targetWeight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE round_run_progress_download SET  initial_weight = :initialWeight,current_weight = :currentWeight,final_weight = :finalWeight, custom_target_weight = :customTargetWeight, actual_target_weight = :actualTargetWeight WHERE round_run_id = :roundRunId AND corral_id = :corralId")
    public abstract java.lang.Object updateRoundProgressDownload(long roundRunId, long corralId, double initialWeight, double currentWeight, double finalWeight, double customTargetWeight, double actualTargetWeight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "INSERT INTO round_run_progress_load (round_run_id, diet_id, product_id, initial_weight, current_weight, final_weight, target_weight, remote_round_run_id, remote_diet_id, remote_product_id) VALUES (:roundRunId, :dietId, :productId, :initialWeight, :currentWeight, :finalWeight, :customTargetWeight, 0, 0, 0)")
    public abstract java.lang.Object addRoundProgressLoad(long roundRunId, long dietId, long productId, double initialWeight, double currentWeight, double finalWeight, double customTargetWeight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "INSERT INTO round_run (round_id, remote_round_id, start_date, updated_date, end_date, remote_id, mixer_id, remote_mixer_id, custom_percentage, custom_tara, added_blend, state, remote_user_id, user_display_name) VALUES (:roundId, 0, datetime(\'now\',\'localtime\'), datetime(\'now\',\'localtime\'), \'\', 0, :mixerId, 0, :customPercentage, :customTara, :addedBlend, :state, :userId, :userDisplayName)")
    public abstract java.lang.Object addRoundRun(long roundId, long mixerId, double customPercentage, double customTara, double addedBlend, int state, long userId, @org.jetbrains.annotations.NotNull
    java.lang.String userDisplayName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE round_run SET mixer_id = :mixerId, updated_date = datetime(\'now\',\'localtime\'), custom_percentage = :customPercentage, custom_tara = :customTara, added_blend = :addedBlend, state = :state WHERE id = :roundRunId")
    public abstract java.lang.Object updateRoundRun(long roundRunId, long mixerId, double customPercentage, double customTara, double addedBlend, int state, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE round_run SET updated_date = datetime(\'now\',\'localtime\'), end_date = datetime(\'now\',\'localtime\')  WHERE id = :roundRunId")
    public abstract java.lang.Object finishRoundRun(long roundRunId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "UPDATE round_run SET updated_date = datetime(\'now\',\'localtime\'), end_date = datetime(\'now\',\'localtime\'), state = :state  WHERE id = :roundRunId")
    public abstract java.lang.Object finishRoundRunWState(long roundRunId, int state, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.Nullable
    @androidx.room.Query(value = "INSERT INTO round_run_progress_download (round_run_id, corral_id, initial_weight, current_weight, final_weight, custom_target_weight, actual_target_weight, remote_round_run_id, remote_corral_id) VALUES (:roundRunId, :corralId, :initialWeight, :currentWeight, :finalWeight, :customTargetWeight, :actualTargetWeight, 0, 0)")
    public abstract java.lang.Object addRoundProgressDownload(long roundRunId, long corralId, double initialWeight, double currentWeight, double finalWeight, double customTargetWeight, double actualTargetWeight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation);
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT rr.id, rr.updated_date , rr.round_id , rr.remote_round_id ,rr.remote_id , rr.end_date , rr.start_date, rr.mixer_id, rr.remote_mixer_id, rr.custom_percentage, rr.custom_tara, rr.added_blend, rr.state, rr.remote_user_id, rr.user_display_name FROM round_run rr")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRun>> getAllRoundRunList();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT rrpl.product_id , rrpl.diet_id , rrpl.initial_weight , rrpl.current_weight, rrpl.final_weight , rrpl.remote_diet_id , rrpl.remote_product_id ,rrpl.remote_round_run_id , rrpl.round_run_id ,rrpl.target_weight  FROM round_run_progress_load rrpl")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad>> getAllRoundRunProgressLoadList();
    
    @org.jetbrains.annotations.NotNull
    @androidx.room.Query(value = "SELECT rrpd.initial_weight ,rrpd.current_weight ,rrpd.final_weight ,rrpd.remote_round_run_id , rrpd.round_run_id ,rrpd.actual_target_weight , rrpd.corral_id , rrpd.remote_corral_id, rrpd.custom_target_weight  FROM round_run_progress_download rrpd")
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload>> getAllRoundRunProgressDownloadlist();
}