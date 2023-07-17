package com.basculasmagris.visorremotomixer.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.basculasmagris.visorremotomixer.model.entities.*

@Database(
    entities = [
        User::class,
        Product::class,
        Mixer::class,
        Establishment::class,
        Corral::class,
        DietProduct::class,
        Diet::class,
        Round::class,
        RoundCorral::class,
        RoundRunProgressLoad::class,
        RoundRunProgressDownload::class,
        RoundRun::class,
        TabletMixer::class],
    version = 12)

abstract class SpiMixerRoomDatabase: RoomDatabase() {

    abstract fun userDao() : UserDao
    abstract fun productDao() : ProductDao
    abstract fun mixerDao() : MixerDao
    abstract fun establishmentDao() : EstablishmentDao
    abstract fun corralDao() : CorralDao
    abstract fun dietDao() : DietDao
    abstract fun roundDao() : RoundDao
    abstract fun remoteViewerDao() : TabletMixerDao
    //abstract fun roundRunReportDao() : RoundRunReportDao

    companion object {
        @Volatile
        private  var INSTANCE: SpiMixerRoomDatabase? = null

        fun getDatabase(context: Context): SpiMixerRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpiMixerRoomDatabase::class.java,
                    "spi_mixer_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}