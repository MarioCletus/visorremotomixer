package com.basculasmagris.visorremotomixer.model.entities

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.lang.reflect.Constructor

@Parcelize
data class RoundRunDetail (
    val userId: Long,
    val userDisplayName: String,
    val round: RoundDetail,
    var mixer: MixerDetail?,
    var mixerBluetoothDevice: BluetoothDevice?,
    var startDate: String,
    val updatedDate: String,
    val endDate: String,
    val remoteId: Long,
    var customPercentage: Double,
    var customTara: Double,
    var addedBlend: Double,
    var state: Int,
    var id: Long

)  : Parcelable

@Parcelize
data class MixerDetail (
    val name: String,
    val description: String,
    val mac: String,
    val btBox: String,
    var tara: Double,
    val calibration: Float,
    val rfid: Long,
    var remoteId: Long,
    var updatedDate: String,
    val archiveDate: String?,
    var id: Long
) : Parcelable

@Parcelize
data class RoundDetail (
    val name: String,
    val description: String,
    var remoteId: Long,
    var updatedDate: String,
    val archiveDate: String?,
    var weight: Double,
    var customRoundRunWeight: Double,
    val usePercentage: Boolean,
    var customPercentage: Double,
    var customRoundRunPercentage: Double,
    var diet: DietDetail,
    var corrals: MutableList<CorralDetail>,
    var id: Long
) : Parcelable

@Parcelize
data class CorralDetail (
    val establishment: EstablishmentDetail,
    val name: String,
    val description: String,
    var remoteId: Long,
    var updatedDate: String,
    val archiveDate: String?,
    var animalQuantity: Int,
    val rfid: Long,
    val order: Int,
    var weight: Double,
    var actualTargetWeight: Double,
    var customTargetWeight: Double,
    var initialWeight: Double,
    var currentWeight: Double,
    var finalWeight: Double,
    val percentage: Double,
    var id: Long
) : Parcelable

@Parcelize
data class EstablishmentDetail (
    val name: String,
    val description: String,
    var remoteId: Long,
    var updatedDate: String,
    val archiveDate: String?,
    var id: Long
) : Parcelable

@Parcelize
data class DietDetail (
    val name: String,
    val description: String,
    val remoteId: Long,
    val updatedDate: String,
    val archiveDate: String?,
    val usePercentage: Boolean,
    var products: MutableList<ProductDetail>,
    val id: Long


) : Parcelable {
    fun getTotalKg() : Double{
        var totalKg = 0.0

        products.forEach {
            totalKg += it.targetWeight
        }

        return totalKg
    }
}


@Parcelize
data class ProductDetail (
    val name: String,
    val description: String,
    val specificWeight: Double,
    val rfid: Long,
    val image: String,
    val imageSource: String,
    val remoteId: Long,
    val updatedDate: String,
    val archiveDate: String?,
    val order: Int,
    var initialWeight: Double,
    var currentWeight: Double,
    var finalWeight: Double,
    var targetWeight: Double,
    val percentage: Double,
    val id: Long
) : Parcelable

//    private val columnsLoad = arrayOf("Ronda", "Planificado (Kg)", "Real (Kg)", "Dieta", "Producto", "Esperado (Kg)","Real (Kg)")
//    private val columnsDownload = arrayOf("Ronda", "Planificado (Kg)", "Real (Kg)", "Dieta", "Corral", "Esperado (Kg)", "Real (Kg)")
@Parcelize
data class LoadReport (
    val userId: Long,
    val userName: String,
    val roundName: String,
    val roundStartDate: String,
    val roundDuration: Double,
    val roundPlaningWeight: Double,
    val roundRealWeight: Double,
    val roundDiffWeight: Double,
    val dietName: String,
    val productName: String,
    val productPlaningWeight: Double,
    val productRealWeight: Double,
    val productDiffWeight: Double,
) : Parcelable

@Parcelize
data class DownloadReport (
    val userId: Long,
    val userName: String,
    val roundName: String,
    val roundStartDate: String,
    val roundDuration: Double,
    val roundPlaningWeight: Double,
    val roundRealWeight: Double,
    val roundDiffWeight: Double,
    val dietName: String,
    val corralName: String,
    val corralPlaningWeight: Double,
    val corralRealWeight: Double,
    val corralDiffWeight: Double,
) : Parcelable