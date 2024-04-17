package com.basculasmagris.visorremotomixer.model.database;

import java.lang.System;

@androidx.room.Database(entities = {com.basculasmagris.visorremotomixer.model.entities.User.class, com.basculasmagris.visorremotomixer.model.entities.Product.class, com.basculasmagris.visorremotomixer.model.entities.Mixer.class, com.basculasmagris.visorremotomixer.model.entities.Establishment.class, com.basculasmagris.visorremotomixer.model.entities.Corral.class, com.basculasmagris.visorremotomixer.model.entities.DietProduct.class, com.basculasmagris.visorremotomixer.model.entities.Diet.class, com.basculasmagris.visorremotomixer.model.entities.Round.class, com.basculasmagris.visorremotomixer.model.entities.RoundCorral.class, com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressLoad.class, com.basculasmagris.visorremotomixer.model.entities.RoundRunProgressDownload.class, com.basculasmagris.visorremotomixer.model.entities.RoundRun.class, com.basculasmagris.visorremotomixer.model.entities.RoundLocal.class, com.basculasmagris.visorremotomixer.model.entities.TabletMixer.class}, version = 13)
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&J\b\u0010\u000f\u001a\u00020\u0010H&J\b\u0010\u0011\u001a\u00020\u0012H&J\b\u0010\u0013\u001a\u00020\u0014H&\u00a8\u0006\u0016"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/database/SpiMixerRoomDatabase;", "Landroidx/room/RoomDatabase;", "()V", "corralDao", "Lcom/basculasmagris/visorremotomixer/model/database/CorralDao;", "dietDao", "Lcom/basculasmagris/visorremotomixer/model/database/DietDao;", "establishmentDao", "Lcom/basculasmagris/visorremotomixer/model/database/EstablishmentDao;", "mixerDao", "Lcom/basculasmagris/visorremotomixer/model/database/MixerDao;", "productDao", "Lcom/basculasmagris/visorremotomixer/model/database/ProductDao;", "remoteViewerDao", "Lcom/basculasmagris/visorremotomixer/model/database/TabletMixerDao;", "roundDao", "Lcom/basculasmagris/visorremotomixer/model/database/RoundDao;", "roundLocalDao", "Lcom/basculasmagris/visorremotomixer/model/database/RoundLocalDao;", "userDao", "Lcom/basculasmagris/visorremotomixer/model/database/UserDao;", "Companion", "app_debug"})
public abstract class SpiMixerRoomDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull
    public static final com.basculasmagris.visorremotomixer.model.database.SpiMixerRoomDatabase.Companion Companion = null;
    @kotlin.jvm.Volatile
    private static volatile com.basculasmagris.visorremotomixer.model.database.SpiMixerRoomDatabase INSTANCE;
    
    public SpiMixerRoomDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract com.basculasmagris.visorremotomixer.model.database.UserDao userDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.basculasmagris.visorremotomixer.model.database.ProductDao productDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.basculasmagris.visorremotomixer.model.database.MixerDao mixerDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.basculasmagris.visorremotomixer.model.database.EstablishmentDao establishmentDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.basculasmagris.visorremotomixer.model.database.CorralDao corralDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.basculasmagris.visorremotomixer.model.database.DietDao dietDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.basculasmagris.visorremotomixer.model.database.RoundDao roundDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.basculasmagris.visorremotomixer.model.database.RoundLocalDao roundLocalDao();
    
    @org.jetbrains.annotations.NotNull
    public abstract com.basculasmagris.visorremotomixer.model.database.TabletMixerDao remoteViewerDao();
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/basculasmagris/visorremotomixer/model/database/SpiMixerRoomDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/basculasmagris/visorremotomixer/model/database/SpiMixerRoomDatabase;", "getDatabase", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.basculasmagris.visorremotomixer.model.database.SpiMixerRoomDatabase getDatabase(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
    }
}