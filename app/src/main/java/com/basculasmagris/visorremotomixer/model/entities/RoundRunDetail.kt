package com.basculasmagris.visorremotomixer.model.entities

import android.bluetooth.BluetoothDevice
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
    var status: Int,
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
    val order: Int,
    var weight: Double,
    var actualTargetWeight: Double,
    var customTargetWeight: Double,
    var initialWeight: Double,
    var currentWeight: Double,
    var finalWeight: Double,
    val percentage: Double,
    var id: Long,
    var startDate: String,
    var endDate: String,
    var status: Int,
    val rest: Long,
    var latitudeStart: Double?,
    var longitudeStart: Double?,
    var latitudeEnd: Double?,
    var longitudeEnd: Double?
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
    val mixingTime: Long,
    var products: MutableList<ProductDetail>,
    val id: Long
) : Parcelable

@Parcelize
data class ProductDetail (
    val name: String,
    val description: String,
    val specificWeight: Double,
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
    var percentage: Double,
    val id: Long,
    var startDate: String,
    var endDate: String,
    var status: Int,
    var rest: Long
) : Parcelable

//    private val columnsLoad = arrayOf("Ronda", "Planificado (Kg)", "Real (Kg)", "Dieta", "Producto", "Esperado (Kg)","Real (Kg)")
//    private val columnsDownload = arrayOf("Ronda", "Planificado (Kg)", "Real (Kg)", "Dieta", "Corral", "Esperado (Kg)", "Real (Kg)")
@Parcelize
data class LoadDownloadReport (
    val userId: Long,
    val userName: String,
    val roundName: String,
    val mixerName: String,
    val establishmentName: String,
    val roundStartDate: String,
    val dietName: String,
    val productName: String,
    val corralName: String,
    val planingWeight: Double,
    val realWeight: Double,
    val diffWeight: Double,
    val roundDuration: String,
    val latitudeStart: Double?,
    val longitudeStart: Double?,
    val latitudeEnd: Double?,
    val longitudeEnd: Double?
) : Parcelable

@Parcelize
data class LoadDownloadReportResumed (
    val userId: Long,
    val userName: String,
    val roundName: String,
    val roundStartDate: String,
    val dietName: String,
    val productName: String,
    val corralName: String,
    val productPlaningWeight: Double,
    val productRealWeight: Double,
    val productDiffWeight: Double,
) : Parcelable


@Parcelize
data class LoadReport (
    val userId: Long,
    val userName: String,
    val roundName: String,
    val mixerName: String,
    val establishmentName: String,
    val loadStartDate: String,
    val dietName: String,
    val productName: String,
    val productPlaningWeight: Double,
    val productRealWeight: Double,
    val productDiffWeight: Double,
    val loadDuration: String
) : Parcelable

@Parcelize
data class LoadReportResumed (
    val userId: Long,
    val userName: String,
    val roundName: String,
    val loadStartDate: String,
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
    val mixerName: String,
    val establishmentName: String,
    val downloadStartDate: String,
    val dietName: String,
    val corralName: String,
    val corralPlaningWeight: Double,
    val corralRealWeight: Double,
    val corralDiffWeight: Double,
    val downloadDuration: String,
    val latitudeStart: Double?,
    val longitudeStart: Double?,
    val latitudeEnd: Double?,
    val longitudeEnd: Double?
) : Parcelable

@Parcelize
data class DownloadReportResumed (
    val userId: Long,
    val userName: String,
    val roundName: String,
    val dietName: String,
    val corralName: String,
    val corralPlaningWeight: Double,
    val corralRealWeight: Double,
    val corralDiffWeight: Double,
) : Parcelable



/***************************************************************
 * Para transmitir a remoto
 ***************************************************************/
@Parcelize
data class MinUserDetail (
    val username: String,
    val name: String,
    val lastname: String,
    val password: String,
    var remoteId: Long,
    var id: Long
) : Parcelable

@Parcelize
data class MinRoundRunDetail (
    val userDisplayName: String,
    val round: MinRoundDetail,
    var mixer: MinMixerDetail?,
    var status: Int,
    var id: Long
)  : Parcelable
@Parcelize
data class MinMixerDetail (
    val name: String,
    val description: String,
    val mac: String,
    var id: Long
) : Parcelable

@Parcelize
data class MinRoundDetail (
    val name: String,
    val description: String,
    var diet: MinDietDetail,
    var corrals: ArrayList<MinCorralDetail>,
    var establishment: MinEstablishmentDetail,
    var id: Long
) : Parcelable

@Parcelize
data class MinCorralDetail (
    val name: String,
    val description: String,
    val establishmentId: Long,
    val order: Int,
    var initialWeight: Long,
    var currentWeight: Long,
    var finalWeight: Long,
    var customTargetWeight: Long,
    var actualTargetWeight: Long,
    var status: Int,
    var id: Long
) : Parcelable

@Parcelize
data class MinEstablishmentDetail (
    val name: String,
    val description: String,
    var id: Long
) : Parcelable

@Parcelize
data class MinDietDetail (
    val name: String,
    val description: String,
    var products: ArrayList<MinProductDetail>,
    val id: Long
) : Parcelable
@Parcelize
data class MinProductDetail (
    val name: String,
    val description: String,
    val order: Int,
    var initialWeight: Long,
    var currentWeight: Long,
    var finalWeight: Long,
    var targetWeight: Long,
    var status: Int,
    val id: Long
) : Parcelable


@Parcelize
data class MinUser (
    val username: String,
    val name: String,
    val lastname: String,
    val password: String,
    val codeRole: Int,
    val remoteId: Long,
    val id: Long
) : Parcelable

@Parcelize
data class MinRound (
    val name: String,
    val description: String,
    var remoteId: Long,
    val id: Long
) : Parcelable

@Parcelize
data class MinEstablishment (
    val name: String,
    val description: String,
    val remoteId: Long,
    val id: Long
) : Parcelable

@Parcelize
data class MinCorral (
    val name: String,
    val description: String,
    val establishmentId: Long,
    var remoteId: Long,
    val id: Long
) : Parcelable


@Parcelize
data class MinProduct (
    val name: String,
    val description: String,
    val remoteId: Long,
    val id: Long
) : Parcelable

/********************************************
 * MinData
 ********************************************/

@Parcelize
data class MinRoundRunData (
    var corralsData: ArrayList<MinCorralData>,
    var productsData: ArrayList<MinProductData>,
    var status: Int,
    var id: Long
)  : Parcelable

@Parcelize
data class MinCorralData (
    val order: Int,
    var initialWeight: Long,
    var currentWeight: Long,
    var finalWeight: Long,
    var customTargetWeight: Long,
    var actualTargetWeight: Long,
    var status: Int,
    var id: Long
) : Parcelable

@Parcelize
data class MinProductData (
    val order: Int,
    var initialWeight: Long,
    var currentWeight: Long,
    var finalWeight: Long,
    var targetWeight: Long,
    var status: Int,
    val id: Long
) : Parcelable

/****************************
 *
 */
@Parcelize
data class MedRoundRunDetail (
    val round: MinRound,
    var startDate: String,
    val endDate: String,
    val progress: Int,
    var status: Int
)  : Parcelable
