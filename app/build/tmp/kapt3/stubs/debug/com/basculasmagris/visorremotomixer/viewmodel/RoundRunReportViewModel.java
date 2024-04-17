package com.basculasmagris.visorremotomixer.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u0019\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\r\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0011"}, d2 = {"Lcom/basculasmagris/visorremotomixer/viewmodel/RoundRunReportViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/basculasmagris/visorremotomixer/model/database/RoundRunReportRepository;", "(Lcom/basculasmagris/visorremotomixer/model/database/RoundRunReportRepository;)V", "activeReportList", "Landroidx/lifecycle/LiveData;", "", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunReport;", "getActiveReportList", "()Landroidx/lifecycle/LiveData;", "insert", "Lkotlinx/coroutines/Job;", "roundRunReport", "insertSync", "", "(Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunReport;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class RoundRunReportViewModel extends androidx.lifecycle.ViewModel {
    private final com.basculasmagris.visorremotomixer.model.database.RoundRunReportRepository repository = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunReport>> activeReportList = null;
    
    public RoundRunReportViewModel(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.database.RoundRunReportRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job insert(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundRunReport roundRunReport) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object insertSync(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundRunReport roundRunReport, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> continuation) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundRunReport>> getActiveReportList() {
        return null;
    }
}