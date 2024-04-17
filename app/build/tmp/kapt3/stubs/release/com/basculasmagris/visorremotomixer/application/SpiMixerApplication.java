package com.basculasmagris.visorremotomixer.application;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002R\u001b\u0010\u0003\u001a\u00020\u00048FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0007\u0010\b\u001a\u0004\b\u0005\u0010\u0006R\u001b\u0010\t\u001a\u00020\n8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\b\u001a\u0004\b\u000b\u0010\fR\u001b\u0010\u000e\u001a\u00020\u000f8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\b\u001a\u0004\b\u0010\u0010\u0011R\u001b\u0010\u0013\u001a\u00020\u00148FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0017\u0010\b\u001a\u0004\b\u0015\u0010\u0016R\u001b\u0010\u0018\u001a\u00020\u00198FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001c\u0010\b\u001a\u0004\b\u001a\u0010\u001bR\u001b\u0010\u001d\u001a\u00020\u001e8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b!\u0010\b\u001a\u0004\b\u001f\u0010 R\u001b\u0010\"\u001a\u00020#8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b&\u0010\b\u001a\u0004\b$\u0010%R\u001b\u0010\'\u001a\u00020(8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b+\u0010\b\u001a\u0004\b)\u0010*\u00a8\u0006,"}, d2 = {"Lcom/basculasmagris/visorremotomixer/application/SpiMixerApplication;", "Landroid/app/Application;", "()V", "corralRepository", "Lcom/basculasmagris/visorremotomixer/model/database/CorralRepository;", "getCorralRepository", "()Lcom/basculasmagris/visorremotomixer/model/database/CorralRepository;", "corralRepository$delegate", "Lkotlin/Lazy;", "database", "Lcom/basculasmagris/visorremotomixer/model/database/SpiMixerRoomDatabase;", "getDatabase", "()Lcom/basculasmagris/visorremotomixer/model/database/SpiMixerRoomDatabase;", "database$delegate", "dietRepository", "Lcom/basculasmagris/visorremotomixer/model/database/DietRepository;", "getDietRepository", "()Lcom/basculasmagris/visorremotomixer/model/database/DietRepository;", "dietRepository$delegate", "mixerRepository", "Lcom/basculasmagris/visorremotomixer/model/database/MixerRepository;", "getMixerRepository", "()Lcom/basculasmagris/visorremotomixer/model/database/MixerRepository;", "mixerRepository$delegate", "roundLocalRepository", "Lcom/basculasmagris/visorremotomixer/model/database/RoundLocalRepository;", "getRoundLocalRepository", "()Lcom/basculasmagris/visorremotomixer/model/database/RoundLocalRepository;", "roundLocalRepository$delegate", "roundRepository", "Lcom/basculasmagris/visorremotomixer/model/database/RoundRepository;", "getRoundRepository", "()Lcom/basculasmagris/visorremotomixer/model/database/RoundRepository;", "roundRepository$delegate", "tabletMixerRepository", "Lcom/basculasmagris/visorremotomixer/model/database/TabletMixerRepository;", "getTabletMixerRepository", "()Lcom/basculasmagris/visorremotomixer/model/database/TabletMixerRepository;", "tabletMixerRepository$delegate", "userRepository", "Lcom/basculasmagris/visorremotomixer/model/database/UserRepository;", "getUserRepository", "()Lcom/basculasmagris/visorremotomixer/model/database/UserRepository;", "userRepository$delegate", "app_release"})
public final class SpiMixerApplication extends android.app.Application {
    private final kotlin.Lazy database$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy corralRepository$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy dietRepository$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy mixerRepository$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy userRepository$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy roundRepository$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy roundLocalRepository$delegate = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy tabletMixerRepository$delegate = null;
    
    public SpiMixerApplication() {
        super();
    }
    
    private final com.basculasmagris.visorremotomixer.model.database.SpiMixerRoomDatabase getDatabase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.database.CorralRepository getCorralRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.database.DietRepository getDietRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.database.MixerRepository getMixerRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.database.UserRepository getUserRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.database.RoundRepository getRoundRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.database.RoundLocalRepository getRoundLocalRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.model.database.TabletMixerRepository getTabletMixerRepository() {
        return null;
    }
}