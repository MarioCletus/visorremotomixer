package com.basculasmagris.visorremotomixer.viewmodel;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u0002*\u0004\b\u0002\u0010\u00032 \u0012\u001c\u0012\u001a\u0012\u0006\u0012\u0004\u0018\u0001H\u0001\u0012\u0006\u0012\u0004\u0018\u0001H\u0002\u0012\u0006\u0012\u0004\u0018\u0001H\u00030\u00050\u0004B/\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00000\u0007\u0012\f\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00010\u0007\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00020\u0007\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lcom/basculasmagris/visorremotomixer/viewmodel/SyncMediatorLiveData;", "F", "S", "T", "Landroidx/lifecycle/MediatorLiveData;", "Lkotlin/Triple;", "EstablishmentLiveData", "Landroidx/lifecycle/LiveData;", "CorralLiveData", "ProductLiveData", "(Landroidx/lifecycle/LiveData;Landroidx/lifecycle/LiveData;Landroidx/lifecycle/LiveData;)V", "app_release"})
public final class SyncMediatorLiveData<F extends java.lang.Object, S extends java.lang.Object, T extends java.lang.Object> extends androidx.lifecycle.MediatorLiveData<kotlin.Triple<? extends F, ? extends S, ? extends T>> {
    
    public SyncMediatorLiveData(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.LiveData<F> EstablishmentLiveData, @org.jetbrains.annotations.NotNull
    androidx.lifecycle.LiveData<S> CorralLiveData, @org.jetbrains.annotations.NotNull
    androidx.lifecycle.LiveData<T> ProductLiveData) {
        super();
    }
}