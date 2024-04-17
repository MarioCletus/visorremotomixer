package com.basculasmagris.visorremotomixer.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u009e\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0010\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u001a\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J>\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020+2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020.2\u0006\u00100\u001a\u00020.2\u0006\u00101\u001a\u00020.2\u0006\u00102\u001a\u00020.J>\u00103\u001a\u00020)2\u0006\u0010*\u001a\u00020+2\u0006\u00104\u001a\u00020+2\u0006\u00105\u001a\u00020+2\u0006\u0010-\u001a\u00020.2\u0006\u00106\u001a\u00020.2\u0006\u00100\u001a\u00020.2\u0006\u00107\u001a\u00020.JQ\u00108\u001a\u00020+2\u0006\u00109\u001a\u00020+2\u0006\u0010:\u001a\u00020+2\u0006\u0010;\u001a\u00020.2\u0006\u0010<\u001a\u00020.2\u0006\u0010=\u001a\u00020.2\u0006\u0010>\u001a\u00020?2\u0006\u0010@\u001a\u00020+2\u0006\u0010A\u001a\u00020BH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010CJ\u000e\u0010D\u001a\u00020)2\u0006\u0010E\u001a\u00020\bJ\u0016\u0010F\u001a\u00020)2\u0006\u00109\u001a\u00020+2\u0006\u0010,\u001a\u00020+J\u0019\u0010G\u001a\u00020H2\u0006\u00109\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010IJ\u000e\u0010J\u001a\u00020)2\u0006\u0010K\u001a\u00020+J\u000e\u0010L\u001a\u00020)2\u0006\u0010M\u001a\u00020\u0011J\u000e\u0010N\u001a\u00020)2\u0006\u0010K\u001a\u00020+J\u0019\u0010O\u001a\u00020H2\u0006\u0010*\u001a\u00020+H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010IJ!\u0010P\u001a\u00020H2\u0006\u0010*\u001a\u00020+2\u0006\u0010>\u001a\u00020?H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010QJ\u001a\u0010R\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020S0\u00070\u00062\u0006\u0010T\u001a\u00020+J\u001a\u0010U\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0%0\u00062\u0006\u0010V\u001a\u00020BJ\u0014\u0010W\u001a\b\u0012\u0004\u0012\u00020\b0\u00062\u0006\u0010X\u001a\u00020+J\u000e\u0010Y\u001a\u00020)2\u0006\u0010E\u001a\u00020\bJ\u000e\u0010Z\u001a\u00020)2\u0006\u0010[\u001a\u00020\fJ\u0019\u0010\\\u001a\u00020+2\u0006\u0010E\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010]J\u0016\u0010^\u001a\u00020)2\u0006\u0010X\u001a\u00020+2\u0006\u0010_\u001a\u00020BJ\u0016\u0010`\u001a\u00020)2\u0006\u0010X\u001a\u00020+2\u0006\u0010a\u001a\u00020+J\u0016\u0010b\u001a\u00020)2\u0006\u0010X\u001a\u00020+2\u0006\u0010a\u001a\u00020+J\u000e\u0010c\u001a\u00020)2\u0006\u0010E\u001a\u00020\bJ.\u0010d\u001a\u00020)2\u0006\u00109\u001a\u00020+2\u0006\u0010,\u001a\u00020+2\u0006\u0010e\u001a\u00020?2\u0006\u0010f\u001a\u00020.2\u0006\u0010g\u001a\u00020.J>\u0010h\u001a\u00020)2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020+2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020.2\u0006\u00100\u001a\u00020.2\u0006\u00101\u001a\u00020.2\u0006\u00102\u001a\u00020.J>\u0010i\u001a\u00020)2\u0006\u0010*\u001a\u00020+2\u0006\u00104\u001a\u00020+2\u0006\u00105\u001a\u00020+2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020.2\u0006\u00100\u001a\u00020.2\u0006\u00107\u001a\u00020.JA\u0010j\u001a\u00020H2\u0006\u0010*\u001a\u00020+2\u0006\u0010:\u001a\u00020+2\u0006\u0010;\u001a\u00020.2\u0006\u0010<\u001a\u00020.2\u0006\u0010=\u001a\u00020.2\u0006\u0010>\u001a\u00020?H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010kJ\u0019\u0010l\u001a\u00020H2\u0006\u0010E\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010]R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\nR\u001d\u0010\u0010\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\nR\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\nR\u001d\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\nR\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u001d0\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010\u001fR\u001f\u0010$\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020&\u0018\u00010%0\u001c\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001f\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006m"}, d2 = {"Lcom/basculasmagris/visorremotomixer/viewmodel/RoundViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/basculasmagris/visorremotomixer/model/database/RoundRepository;", "(Lcom/basculasmagris/visorremotomixer/model/database/RoundRepository;)V", "activeRoundList", "Landroidx/lifecycle/LiveData;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/Round;", "getActiveRoundList", "()Landroidx/lifecycle/LiveData;", "allRoundCorralList", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundCorral;", "getAllRoundCorralList", "allRoundList", "getAllRoundList", "allRoundRunList", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRun;", "getAllRoundRunList", "allRoundRunProgressDownloadList", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunProgressDownload;", "getAllRoundRunProgressDownloadList", "allRoundRunProgressLoadList", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunProgressLoad;", "getAllRoundRunProgressLoadList", "compositeDisposable", "Lio/reactivex/rxjava3/disposables/CompositeDisposable;", "loadRound", "Landroidx/lifecycle/MutableLiveData;", "", "getLoadRound", "()Landroidx/lifecycle/MutableLiveData;", "roundApiService", "Lcom/basculasmagris/visorremotomixer/model/network/RoundApiService;", "roundsLoadingError", "getRoundsLoadingError", "roundsResponse", "", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRemote;", "getRoundsResponse", "addProgressDownload", "Lkotlinx/coroutines/Job;", "roundRunId", "", "corralId", "initialWeight", "", "currentWeight", "finalWeight", "customTargetWeight", "actualTargetWeight", "addProgressLoad", "dietId", "productId", "actualWeight", "targetWeight", "addRoundRunSync", "roundId", "mixerId", "customPercentage", "customTara", "addedBlend", "state", "", "userId", "userDisplayName", "", "(JJDDDIJLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "round", "deleteCorralBy", "deleteCorralByRound", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteRounById", "round_id", "deleteRoundRun", "roundRun", "deleteRoundRunById", "finishRoundRunSync", "finishRoundRunSyncWState", "(JILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCorralsBy", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundCorralDetail;", "idRound", "getFilteredRoundList", "value", "getRoundById", "id", "insert", "insertRoundCorral", "roundCorral", "insertSync", "(Lcom/basculasmagris/visorremotomixer/model/entities/Round;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setUpdatedDate", "date", "setUpdatedRemoteId", "remoteId", "setUpdatedRemoteRoundRunId", "update", "updateCorralBy", "order", "weight", "percentage", "updateProgressDownload", "updateProgressLoad", "updateRoundRunSync", "(JJDDDILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateSync", "app_release"})
public final class RoundViewModel extends androidx.lifecycle.ViewModel {
    private final com.basculasmagris.visorremotomixer.model.database.RoundRepository repository = null;
    private final com.basculasmagris.visorremotomixer.model.network.RoundApiService roundApiService = null;
    private final io.reactivex.rxjava3.disposables.CompositeDisposable compositeDisposable = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> loadRound = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRemote>> roundsResponse = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> roundsLoadingError = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> allRoundList = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> activeRoundList = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundCorral>> allRoundCorralList = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRun>> allRoundRunList = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad>> allRoundRunProgressLoadList = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload>> allRoundRunProgressDownloadList = null;
    
    public RoundViewModel(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.RoundRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getLoadRound() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRemote>> getRoundsResponse() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getRoundsLoadingError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object insertSync(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insertRoundCorral(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundCorral roundCorral) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> getAllRoundList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> getActiveRoundList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundCorral>> getAllRoundCorralList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRun>> getAllRoundRunList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad>> getAllRoundRunProgressLoadList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload>> getAllRoundRunProgressDownloadList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundCorralDetail>> getCorralsBy(long idRound) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.basculasmagris.visorremotomixer.model.entities.Round> getRoundById(long id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.Round>> getFilteredRoundList(@org.jetbrains.annotations.NotNull
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job update(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job updateProgressLoad(long roundRunId, long dietId, long productId, double initialWeight, double currentWeight, double finalWeight, double targetWeight) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job updateProgressDownload(long roundRunId, long corralId, double initialWeight, double currentWeight, double finalWeight, double customTargetWeight, double actualTargetWeight) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job addProgressLoad(long roundRunId, long dietId, long productId, double initialWeight, double actualWeight, double finalWeight, double targetWeight) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job addProgressDownload(long roundRunId, long corralId, double initialWeight, double currentWeight, double finalWeight, double customTargetWeight, double actualTargetWeight) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object addRoundRunSync(long roundId, long mixerId, double customPercentage, double customTara, double addedBlend, int state, long userId, @org.jetbrains.annotations.NotNull
    java.lang.String userDisplayName, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateRoundRunSync(long roundRunId, long mixerId, double customPercentage, double customTara, double addedBlend, int state, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object finishRoundRunSync(long roundRunId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object finishRoundRunSyncWState(long roundRunId, int state, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateSync(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job updateCorralBy(long roundId, long corralId, int order, double weight, double percentage) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setUpdatedDate(long id, @org.jetbrains.annotations.NotNull
    java.lang.String date) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setUpdatedRemoteId(long id, long remoteId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job setUpdatedRemoteRoundRunId(long id, long remoteId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job delete(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.Round round) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job deleteRounById(long round_id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job deleteRoundRun(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundRun roundRun) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job deleteRoundRunById(long round_id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job deleteCorralBy(long roundId, long corralId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object deleteCorralByRound(long roundId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return null;
    }
}