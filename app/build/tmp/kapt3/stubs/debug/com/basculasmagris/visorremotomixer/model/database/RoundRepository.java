package com.basculasmagris.visorremotomixer.model.database;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0011\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0018\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004JI\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020\u001f2\u0006\u0010#\u001a\u00020\u001fH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$JI\u0010%\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010&\u001a\u00020\u001c2\u0006\u0010\'\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u001fH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)JQ\u0010*\u001a\u00020\u001c2\u0006\u0010+\u001a\u00020\u001c2\u0006\u0010,\u001a\u00020\u001c2\u0006\u0010-\u001a\u00020\u001f2\u0006\u0010.\u001a\u00020\u001f2\u0006\u0010/\u001a\u00020\u001f2\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u00020\u001c2\u0006\u00103\u001a\u000204H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00105J\u0019\u00106\u001a\u00020\u001a2\u0006\u0010+\u001a\u00020\u001cH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00107J!\u00108\u001a\u00020\u001a2\u0006\u0010+\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001cH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00109J\u0019\u0010:\u001a\u00020\u001a2\u0006\u0010;\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010<J\u0019\u0010=\u001a\u00020\u001a2\u0006\u0010>\u001a\u00020\u001cH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00107J\u0019\u0010?\u001a\u00020\u001a2\u0006\u0010@\u001a\u00020\u000eH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010AJ\u0019\u0010B\u001a\u00020\u001a2\u0006\u0010C\u001a\u00020\u001cH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00107J\u0019\u0010D\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00107J!\u0010E\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u00100\u001a\u000201H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010FJ\u001a\u0010G\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020H0\u00070\u00062\u0006\u0010I\u001a\u00020\u001cJ\u001a\u0010J\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0K0\u00062\u0006\u0010L\u001a\u000204J\u0014\u0010M\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010N\u001a\u00020\u001cJ\u0019\u0010O\u001a\u00020\u001a2\u0006\u0010P\u001a\u00020\u0017H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010QJ\u0019\u0010R\u001a\u00020\u001c2\u0006\u0010;\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010<J!\u0010S\u001a\u00020\u001a2\u0006\u0010N\u001a\u00020\u001c2\u0006\u0010T\u001a\u000204H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010UJ!\u0010V\u001a\u00020\u001a2\u0006\u0010N\u001a\u00020\u001c2\u0006\u0010W\u001a\u00020\u001cH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00109J!\u0010X\u001a\u00020\u001a2\u0006\u0010N\u001a\u00020\u001c2\u0006\u0010W\u001a\u00020\u001cH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00109J9\u0010Y\u001a\u00020\u001a2\u0006\u0010+\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010Z\u001a\u0002012\u0006\u0010[\u001a\u00020\u001f2\u0006\u0010\\\u001a\u00020\u001fH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010]J\u0019\u0010^\u001a\u00020\u001a2\u0006\u0010;\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010<JI\u0010_\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020\u001f2\u0006\u0010#\u001a\u00020\u001fH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010$JI\u0010`\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010&\u001a\u00020\u001c2\u0006\u0010\'\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u001fH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)JA\u0010a\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010,\u001a\u00020\u001c2\u0006\u0010-\u001a\u00020\u001f2\u0006\u0010.\u001a\u00020\u001f2\u0006\u0010/\u001a\u00020\u001f2\u0006\u00100\u001a\u000201H\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010bR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u001d\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\nR\u001d\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\nR\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\nR\u001d\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006c"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/database/RoundRepository;", "", "roundDao", "Lcom/basculasmagris/visorremotomixer/model/database/RoundDao;", "(Lcom/basculasmagris/visorremotomixer/model/database/RoundDao;)V", "activeRoundList", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/Round;", "getActiveRoundList", "()Lkotlinx/coroutines/flow/Flow;", "allRoundList", "getAllRoundList", "allRoundRunList", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRun;", "getAllRoundRunList", "allRoundRunProgressDownloadList", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunProgressDownload;", "getAllRoundRunProgressDownloadList", "allRoundRunProgressLoadList", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunProgressLoad;", "getAllRoundRunProgressLoadList", "getAllRoundCorralList", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundCorral;", "getGetAllRoundCorralList", "addRoundProgressDownloadData", "", "roundRunId", "", "corralId", "initialWeight", "", "currentWeight", "finalWeight", "customTargetWeight", "actualTargetWeight", "(JJDDDDDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addRoundProgressLoadData", "dietId", "productId", "targetWeight", "(JJJDDDDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addRoundRunData", "roundId", "mixerId", "customPercentage", "customTara", "addedBlend", "state", "", "userId", "userDisplayName", "", "(JJDDDIJLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteCorralByRoundData", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRoundCorralData", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRoundData", "round", "(Lcom/basculasmagris/visorremotomixer/model/entities/Round;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRoundDataById", "round_id", "deleteRoundRunData", "round_run", "(Lcom/basculasmagris/visorremotomixer/model/entities/RoundRun;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRoundRunDataById", "round_run_id", "finishRoundRunData", "finishRoundRunDataWState", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCorralsBy", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundCorralDetail;", "idRound", "getFilteredRoundList", "", "value", "getRoundById", "id", "insertRoundCorralData", "roundCorral", "(Lcom/basculasmagris/visorremotomixer/model/entities/RoundCorral;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertRoundData", "setUpdatedDate", "date", "(JLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedRemoteId", "remoteId", "setUpdatedRemoteRoundRunId", "updateRoundCorralData", "order", "weight", "percentage", "(JJIDDLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateRoundData", "updateRoundProgressDownloadData", "updateRoundProgressLoadData", "updateRoundRunData", "(JJDDDILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class RoundRepository {
    private final com.basculasmagris.visorremotomixer.model.database.RoundDao roundDao = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> allRoundList = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> activeRoundList = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundCorral>> getAllRoundCorralList = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRun>> allRoundRunList = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad>> allRoundRunProgressLoadList = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload>> allRoundRunProgressDownloadList = null;
    
    public RoundRepository(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.RoundDao roundDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object insertRoundData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object insertRoundCorralData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundCorral roundCorral, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> getAllRoundList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> getActiveRoundList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.basculasmagris.visorremotomixer.model.entities.Round> getRoundById(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> getFilteredRoundList(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object updateRoundData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object updateRoundProgressLoadData(long roundRunId, long dietId, long productId, double initialWeight, double currentWeight, double finalWeight, double targetWeight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object updateRoundProgressDownloadData(long roundRunId, long corralId, double initialWeight, double currentWeight, double finalWeight, double customTargetWeight, double actualTargetWeight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object addRoundProgressLoadData(long roundRunId, long dietId, long productId, double initialWeight, double currentWeight, double finalWeight, double targetWeight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object addRoundProgressDownloadData(long roundRunId, long corralId, double initialWeight, double currentWeight, double finalWeight, double customTargetWeight, double actualTargetWeight, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object updateRoundCorralData(long roundId, long corralId, int order, double weight, double percentage, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object addRoundRunData(long roundId, long mixerId, double customPercentage, double customTara, double addedBlend, int state, long userId, @org.jetbrains.annotations.NotNull
    java.lang.String userDisplayName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object updateRoundRunData(long roundRunId, long mixerId, double customPercentage, double customTara, double addedBlend, int state, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object finishRoundRunData(long roundRunId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object finishRoundRunDataWState(long roundRunId, int state, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object setUpdatedDate(long id, @org.jetbrains.annotations.NotNull
    java.lang.String date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object setUpdatedRemoteId(long id, long remoteId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object setUpdatedRemoteRoundRunId(long id, long remoteId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteRoundData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteRoundDataById(long round_id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteRoundRunData(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundRun round_run, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteRoundRunDataById(long round_run_id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteRoundCorralData(long roundId, long corralId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object deleteCorralByRoundData(long roundId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail>> getCorralsBy(long idRound) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundCorral>> getGetAllRoundCorralList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRun>> getAllRoundRunList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad>> getAllRoundRunProgressLoadList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload>> getAllRoundRunProgressDownloadList() {
        return null;
    }
}