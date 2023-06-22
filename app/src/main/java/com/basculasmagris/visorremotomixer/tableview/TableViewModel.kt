/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.basculasmagris.visorremotomixer.tableview

import android.util.Log
import androidx.annotation.DrawableRes
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.model.entities.DownloadReport
import com.basculasmagris.visorremotomixer.model.entities.LoadReport
import com.basculasmagris.visorremotomixer.model.entities.ProductDetail
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.tableview.TableViewModel
import com.basculasmagris.visorremotomixer.tableview.model.Cell
import com.basculasmagris.visorremotomixer.tableview.model.ColumnHeader
import com.basculasmagris.visorremotomixer.tableview.model.RowHeader
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Constants.COLUMNS_DOWNLOAD
import com.basculasmagris.visorremotomixer.utils.Constants.COLUMNS_LOAD
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.Helper.Companion.getCurrentUser
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong

/**
 * Created by evrencoskun on 4.02.2018.
 */
class TableViewModel {
    private val columnsLoad = COLUMNS_LOAD
    private val columnsDownload = COLUMNS_DOWNLOAD

    // Drawables
    @DrawableRes
    private val mBoyDrawable: Int

    @DrawableRes
    private val mGirlDrawable: Int

    @DrawableRes
    private val mHappyDrawable: Int

    @DrawableRes
    private val mSadDrawable: Int

    init {
        // initialize drawables
        mBoyDrawable = R.drawable.ic_access_time
        mGirlDrawable = R.drawable.ic_access_time
        mHappyDrawable = R.drawable.ic_access_time
        mSadDrawable = R.drawable.ic_access_time
    }

    fun simpleRowHeaderList (type: Int, data: MutableList<RoundRunDetail>): List<RowHeader> {
        val list: MutableList<RowHeader> = ArrayList()



        when (type) {
            R.id.rb_opt_carga -> {
                var productsCount = 0
                data.forEach { roundRunDetail ->
                    roundRunDetail.round.diet.products.forEach { _ ->
                        productsCount += 1
                    }
                }
                for (i in 0 until productsCount) {
                    val header = RowHeader(i.toString(), "${i+1}")
                    list.add(header)
                }
            }
            R.id.rb_opt_descarga -> {
                var corralsCount = 0
                data.forEach { roundRunDetail ->
                    roundRunDetail.round.corrals.forEach { _ ->
                        corralsCount += 1
                    }
                }
                for (i in 0 until corralsCount) {
                    val header = RowHeader(i.toString(), "${i+1}")
                    list.add(header)
                }
            }
        }

        return list
    }

    /**
     * This is a dummy model list test some cases.
     */
    fun randomColumnHeaderList (type: Int) : List<ColumnHeader> {
        val list: MutableList<ColumnHeader> = ArrayList()

        when (type) {
            R.id.rb_opt_carga -> {

                for (i in columnsLoad.indices) {
                    var title = "${columnsLoad.get(i)}"
                    val header = ColumnHeader(i.toString(), title)
                    list.add(header)
                }
            }
            R.id.rb_opt_descarga -> {
                for (i in columnsDownload.indices) {
                    var title = "${columnsDownload.get(i)}"
                    val header = ColumnHeader(i.toString(), title)
                    list.add(header)
                }
            }
        }


        return list
    }

    /**
     * This is a dummy model list test some cases.
     */
    fun cellListForSortingTest(type: Int, data: MutableList<RoundRunDetail>): List<List<Cell>> {
        val list: MutableList<List<Cell>> = ArrayList()

        when (type) {
            R.id.rb_opt_carga -> {
                var loadReport: ArrayList<LoadReport> = ArrayList()
                data.forEach { roundRunDetail ->
                    roundRunDetail.round.diet.products.forEach { productDetail ->
                        Log.i("CARGA", "Nombre del producto: ${productDetail.name}")
                        val roundPlaningWeight = roundRunDetail.round.diet.products.sumOf { product -> product.targetWeight }.roundToLong().toDouble()
                        val roundRealWeight = roundRunDetail.round.diet.products.sumOf { product -> product.finalWeight - product.initialWeight}.roundToLong().toDouble()
                        val roundDiff = (roundRealWeight-roundPlaningWeight).roundToLong().toDouble()
                        val productDiff = (productDetail.currentWeight-productDetail.targetWeight).roundToLong().toDouble()

                        var loadLine = LoadReport(
                            userId = roundRunDetail.userId,
                            userName = roundRunDetail.userDisplayName,
                            roundName = roundRunDetail.round.name,
                            roundStartDate = "${Helper.formattedDate(roundRunDetail.startDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)}",
                            roundDuration = Helper.diffDate(roundRunDetail.startDate, roundRunDetail.endDate, Constants.APP_DB_FORMAT_DATE).toDouble(),
                            roundPlaningWeight = roundPlaningWeight,
                            roundRealWeight = roundRealWeight,
                            roundDiffWeight = roundDiff,//"${if (roundDiff > 0) "+" else ""}${roundRealWeight - roundPlaningWeight}",
                            dietName = roundRunDetail.round.diet.name,
                            productName = productDetail.name,
                            productPlaningWeight = productDetail.targetWeight.roundToLong().toDouble(),
                            productRealWeight = (productDetail.finalWeight-productDetail.initialWeight).roundToLong().toDouble(),
                            productDiffWeight = (productDetail.finalWeight-productDetail.initialWeight-productDetail.targetWeight).roundToLong().toDouble()//"${if (productDiff > 0) "+" else ""}${productDetail.currentWeight-productDetail.targetWeight}"
                        )
                        loadReport.add(loadLine)
                    }
                }

                for (i in 0 until loadReport.size) {
                    val cellList: MutableList<Cell> = ArrayList()
                    for (j in columnsLoad.indices) {
                        var text = ""
                        var number = 0.0

                        //    private val columnsDownload = arrayOf("Ronda", "Planificado (Kg)", "Real (Kg)", "Dieta", "Corral", "Esperado (Kg)", "Real (Kg)")
                        when (j) {
                            ROUND_INDEX -> {
                                text = loadReport[i].roundName
                            } //Nombre de ronda
                            USER_INDEX -> {
                                text = loadReport[i].userName
                            } //Nombre de ronda
                            START_DATE_INDEX -> {
                                text = loadReport[i].roundStartDate //Inicio
                            }
                            DURATION_INDEX -> {
                                number = loadReport[i].roundDuration  //Duración
                            }
                            ROUND_PLANING_WEIGHT_INDEX -> {
                                number = loadReport[i].roundPlaningWeight
                            } //Planificado (Kg)
                            ROUND_ACTUAL_WEIGHT_INDEX -> {
                                number = loadReport[i].roundRealWeight
                            } //Real (Kg)
                            ROUND_DIFF_WEIGHT_INDEX -> {
                                number = loadReport[i].roundDiffWeight
                            } //Diff (Kg)
                            DIET_INDEX -> {
                                text = loadReport[i].dietName
                            } //Dieta
                            PRODUCT_INDEX -> {
                                Log.i("CARGA", "loadReport: ${loadReport[i].productName}")
                                text = loadReport[i].productName
                            } //Producto
                            PRODUCT_PLANING_WEIGHT_INDEX -> {
                                number = loadReport[i].productPlaningWeight
                            } //Esperado (Kg)
                            PRODUCT_ACTUAL_WEIGHT_INDEX -> {
                                number =  loadReport[i].productRealWeight
                            } //Real (Kg)
                            PRODUCT_DIFF_WEIGHT_INDEX -> {
                                number =  loadReport[i].productDiffWeight
                            } //Diff (Kg)
                        }

                        val id = "$j-$i"
                        when (j) {
                            DURATION_INDEX,
                            ROUND_PLANING_WEIGHT_INDEX,
                            ROUND_ACTUAL_WEIGHT_INDEX, ROUND_DIFF_WEIGHT_INDEX,
                            PRODUCT_PLANING_WEIGHT_INDEX, PRODUCT_ACTUAL_WEIGHT_INDEX, PRODUCT_DIFF_WEIGHT_INDEX
                            -> {
                                var cell = Cell(id, number)
                                cellList.add(cell)
                            }
                            else -> {
                                var cell = Cell(id, text)
                                cellList.add(cell)
                            }
                        }

                    }
                    list.add(cellList)
                }
            }
            R.id.rb_opt_descarga -> {
                var downloadReport: ArrayList<DownloadReport> = ArrayList()
                data.forEach { roundRunDetail ->
                    roundRunDetail.round.corrals.forEach { corralDetail ->
                        val roundPlaningWeight = roundRunDetail.round.corrals.sumOf { corral -> corral.customTargetWeight }.roundToLong().toDouble()
                        val roundRealWeight = roundRunDetail.round.corrals.sumOf { corral -> corral.initialWeight - corral.finalWeight }.roundToLong().toDouble()
                        val roundDiff = (roundRealWeight-roundPlaningWeight).roundToLong().toDouble()
                        val corralDiff = (corralDetail.currentWeight-corralDetail.actualTargetWeight).roundToLong().toDouble()

                        var downloadLine = DownloadReport(
                            userId = roundRunDetail.userId,
                            userName = roundRunDetail.userDisplayName,
                            roundName = roundRunDetail.round.name,
                            roundStartDate = "${Helper.formattedDate(roundRunDetail.startDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)}",
                            roundDuration = Helper.diffDate(roundRunDetail.startDate, roundRunDetail.endDate, Constants.APP_DB_FORMAT_DATE).toDouble(),
                            roundPlaningWeight = roundPlaningWeight,
                            roundRealWeight = roundRealWeight,
                            roundDiffWeight = roundDiff,//"${if (roundDiff > 0) "+" else ""}${roundRealWeight-roundPlaningWeight}Kg",
                            dietName = roundRunDetail.round.diet.name,
                            corralName = corralDetail.name,
                            corralPlaningWeight = (corralDetail.actualTargetWeight).roundToLong().toDouble(),
                            corralRealWeight = (corralDetail.initialWeight - corralDetail.finalWeight).roundToLong().toDouble(),
                            corralDiffWeight = (corralDetail.initialWeight - corralDetail.finalWeight - corralDetail.actualTargetWeight).roundToLong().toDouble()//"${if (corralDiff > 0) "+" else ""}${corralDetail.currentWeight-corralDetail.actualTargetWeight}Kg"
                        )
                        downloadReport.add(downloadLine)
                    }
                }

                for (i in 0 until downloadReport.size) {
                    val cellList: MutableList<Cell> = ArrayList()
                    for (j in columnsLoad.indices) {
                        var text = ""
                        var number = 0.0

                        //    private val columnsDownload = arrayOf("Ronda", "Planificado (Kg)", "Real (Kg)", "Dieta", "Corral", "Esperado (Kg)", "Real (Kg)")
                        when (j) {
                            ROUND_INDEX -> {
                                text = downloadReport[i].roundName //Nombre de ronda
                            }
                            USER_INDEX -> {
                                text = downloadReport[i].userName
                            } //Nombre de ronda
                            START_DATE_INDEX -> {
                                text = downloadReport[i].roundStartDate //Inicio
                            }
                            DURATION_INDEX -> {
                                number = downloadReport[i].roundDuration  //Duración
                            }
                            ROUND_PLANING_WEIGHT_INDEX -> {
                                number = downloadReport[i].roundPlaningWeight
                            } //Planificado (Kg)
                            ROUND_ACTUAL_WEIGHT_INDEX-> {
                                number = downloadReport[i].roundRealWeight
                            } //Real (Kg),
                            ROUND_DIFF_WEIGHT_INDEX -> {
                                number = downloadReport[i].roundDiffWeight
                            } //Diff (Kg)
                            DIET_INDEX -> {
                                text = downloadReport[i].dietName
                            } //Dieta
                            CORRAL_INDEX -> {
                                text = downloadReport[i].corralName
                            } //Corral
                            CORRAL_PLANING_WEIGHT_INDEX-> {
                                number = downloadReport[i].corralPlaningWeight
                            } //Esperado (Kg)
                            CORRAL_ACTUAL_WEIGHT_INDEX -> {
                                number =  downloadReport[i].corralRealWeight
                            } //Real (Kg),
                            CORRAL_DIFF_WEIGHT_INDEX -> {
                                number =  downloadReport[i].corralDiffWeight
                            } //Diff (Kg)
                        }


                        val id = "$j-$i"
                        when (j) {
                            DURATION_INDEX,
                            ROUND_PLANING_WEIGHT_INDEX,
                            ROUND_ACTUAL_WEIGHT_INDEX, ROUND_DIFF_WEIGHT_INDEX,
                            CORRAL_PLANING_WEIGHT_INDEX, CORRAL_ACTUAL_WEIGHT_INDEX, CORRAL_DIFF_WEIGHT_INDEX
                            -> {
                                var cell = Cell(id, number)
                                cellList.add(cell)
                            }
                            else -> {
                                var cell = Cell(id, text)
                                cellList.add(cell)
                            }
                        }
                    }
                    list.add(cellList)
                }
            }
        }

        return list
    }


    @DrawableRes
    fun getDrawable(value: Int, isGender: Boolean): Int {
        return if (isGender) {
            if (value == BOY) mBoyDrawable else mGirlDrawable
        } else {
            if (value == SAD) mSadDrawable else mHappyDrawable
        }
    }

    /*val cellList: List<List<Cell>>
        get() = cellListForSortingTest*/
    /*val rowHeaderList: List<RowHeader>
        get() = simpleRowHeaderList*/
    /*val columnHeaderList: List<ColumnHeader>
        get() = randomColumnHeaderList()*/

    companion object {


        // Constant values for icons
        const val SAD = 1
        const val HAPPY = 2
        const val BOY = 1
        const val GIRL = 2

        // Constant size for dummy data sets
        private const val COLUMN_SIZE = 500
        private const val ROW_SIZE = 500

        // Columns indexes Download
        const val ROUND_INDEX = 0
        const val USER_INDEX = 1
        const val START_DATE_INDEX = 2
        const val DURATION_INDEX = 3
        const val ROUND_PLANING_WEIGHT_INDEX = 4
        const val ROUND_ACTUAL_WEIGHT_INDEX = 5
        const val ROUND_DIFF_WEIGHT_INDEX = 6
        const val DIET_INDEX = 7
        const val CORRAL_INDEX = 8
        const val CORRAL_PLANING_WEIGHT_INDEX = 9
        const val CORRAL_ACTUAL_WEIGHT_INDEX = 10
        const val CORRAL_DIFF_WEIGHT_INDEX = 11

        // Columns indexes Load
        const val PRODUCT_INDEX = 8
        const val PRODUCT_PLANING_WEIGHT_INDEX = 9
        const val PRODUCT_ACTUAL_WEIGHT_INDEX = 10
        const val PRODUCT_DIFF_WEIGHT_INDEX = 11

    }
}