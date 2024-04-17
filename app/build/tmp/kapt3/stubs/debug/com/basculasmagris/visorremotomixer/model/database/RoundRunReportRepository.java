package com.basculasmagris.visorremotomixer.model.database;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bH\u0087@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000f"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/database/RoundRunReportRepository;", "", "roundRunReportDao", "Lcom/basculasmagris/visorremotomixer/model/database/RoundRunReportDao;", "(Lcom/basculasmagris/visorremotomixer/model/database/RoundRunReportDao;)V", "activeRoundRunReportList", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunReport;", "getActiveRoundRunReportList", "()Lkotlinx/coroutines/flow/Flow;", "insert", "", "roundRunReport", "(Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunReport;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class RoundRunReportRepository {
    private final com.basculasmagris.visorremotomixer.model.database.RoundRunReportDao roundRunReportDao = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunReport>> activeRoundRunReportList = null;
    
    public RoundRunReportRepository(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.RoundRunReportDao roundRunReportDao) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    @androidx.annotation.WorkerThread
    public final java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundRunReport roundRunReport, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunReport>> getActiveRoundRunReportList() {
        return null;
    }
}