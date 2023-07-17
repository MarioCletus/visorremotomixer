package com.basculasmagris.visorremotomixer.application

import android.app.Application
import com.basculasmagris.visorremotomixer.model.database.*
import com.basculasmagris.visorremotomixer.model.database.TabletMixerRepository

class SpiMixerApplication: Application() {
    private  val database by lazy { SpiMixerRoomDatabase.getDatabase((this@SpiMixerApplication))}
    val corralRepository by lazy { CorralRepository(database.corralDao()) }
    val dietRepository by lazy { DietRepository(database.dietDao()) }
    val establishmentRepository by lazy { EstablishmentRepository(database.establishmentDao()) }
    val mixerRepository by lazy { MixerRepository(database.mixerDao()) }
    val productRepository by lazy { ProductRepository(database.productDao()) }
    val userRepository by lazy { UserRepository(database.userDao())}
    val roundRepository by lazy { RoundRepository(database.roundDao())}
    val tabletMixerRepository by lazy { TabletMixerRepository(database.remoteViewerDao()) }

    //val roundRunReportRepository by lazy { RoundRunReportRepository(database.roundRunReportDao())}
}