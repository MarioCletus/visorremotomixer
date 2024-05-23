package com.basculasmagris.visorremotomixer.view.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemRoundToRunLayoutBinding
import com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.fragments.RoundListFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class RoundRunAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<RoundRunAdapter.ViewHolder>(),
    Filterable {

    private val CANT_HORAS_MUESTRA_RONDA_FINALIZADA : Int = 4
    private var roundsRun: List<MedRoundRunDetail> = ArrayList()
    private var filteredRoundsRun: List<MedRoundRunDetail> = ArrayList()
    private var bRoundRunning: Boolean = false

    class ViewHolder (view: ItemRoundToRunLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvRoundName = view.tvRoundName
        val tvRoundRunDescription = view.tvRoundRunDescription
        val tvRoundRunPercentage = view.tvRoundRunPercentage
        val tvRoundStartDate = view.tvRoundStartDate
        val btnStartRound = view.btnStartRound
        val btnStopRound = view.btnStopRound
        val pbRoundRun = view.pbRoundRun
        val llProgressBar = view.llProgressBar
        val roundCard = view.roundCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRoundToRunLayoutBinding = ItemRoundToRunLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }



    private fun getCurrentStatus(roundToRun: MedRoundRunDetail) : String {

        val status = ""

        if (roundToRun.startDate.isEmpty()) {
            return  Constants.STATUS_NOT_RUN
        }

        Log.i("RUN", "Fecha de fin: ${roundToRun.endDate} de ${roundToRun.round.name}")
        if (roundToRun.endDate.isNotEmpty()) {
            val state = if(roundToRun.state == Constants.STATE_INTERRUPT) Constants.STATUS_INCOMPLETED else Constants.STATUS_FINISHED
            return  "$state   ${Helper.formattedDate(roundToRun.endDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)}"
        }

        when(roundToRun.state){
            Constants.STATE_LOAD -> return Constants.STATUS_LOAD_IN_PROGRESS
            Constants.STATE_DOWNLOAD -> return Constants.STATUS_DOWNLOAD_IN_PROGRESS
            Constants.STATE_CONFIG -> return  Constants.STATUS_CONFIG
            Constants.STATE_RESUME -> return  Constants.STATUS_RESUME
            else-> return ""
        }

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(filteredRoundsRun.isEmpty() || position>= filteredRoundsRun.size){
            return
        }
        val roundToRun = filteredRoundsRun[position]
        if(roundToRun == null || roundToRun.round == null){
            if(roundToRun == null){
                Log.i("DEBUG","Esto no debería pasar!! roundToRun null")
            }else{
                Log.i("DEBUG","Esto no debería pasar!! rounToRun.round = null")
            }
            return
        }

        holder.tvRoundName.text = roundToRun.round.name
        holder.pbRoundRun.progress = roundToRun.progress
        holder.tvRoundRunPercentage.text = "${holder.pbRoundRun.progress}%"

        if (roundToRun.endDate.isEmpty() && roundToRun.startDate.isEmpty()){
            // Nunca iniciada
            holder.llProgressBar.visibility = GONE
            holder.btnStopRound.visibility = INVISIBLE
            if(bRoundRunning){
                if((fragment.requireActivity() as MainActivity).minRoundRunDetail?.round?.id == roundToRun.round.id){
                    holder.btnStartRound.text = holder.btnStartRound.context.getString(R.string.ver_ronda)
                    holder.btnStartRound.isEnabled = true
                }
                else{
                    holder.btnStartRound.text = holder.btnStartRound.context.getString(R.string.iniciar)
                    holder.btnStartRound.isEnabled = false
                }
            }else {
                holder.btnStartRound.text = holder.btnStartRound.context.getString(R.string.iniciar)
                holder.btnStartRound.isEnabled = true
            }
            holder.tvRoundRunDescription.text = getCurrentStatus(roundToRun)

        } else if (roundToRun.endDate.isEmpty()) {
            // En curso
            if(bRoundRunning){
                if((fragment.requireActivity() as MainActivity).minRoundRunDetail?.round?.id == roundToRun.round.id){
                    holder.btnStartRound.text = holder.btnStartRound.context.getString(R.string.ver_ronda)
                    holder.btnStartRound.isEnabled = true
                }
                else{
                    holder.btnStartRound.text = holder.btnStartRound.context.getString(R.string.continuar)
                    holder.btnStartRound.isEnabled = false
                }
            }else {
                holder.btnStartRound.text = holder.btnStartRound.context.getString(R.string.continuar)
                holder.btnStartRound.isEnabled = true
            }
            holder.tvRoundStartDate.text = "Iniciada el ${Helper.formattedDate(roundToRun.startDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)}"
            holder.tvRoundRunDescription.text = getCurrentStatus(roundToRun)

            holder.btnStopRound.text = fragment.resources.getString(R.string.detener)
            holder.btnStopRound.background = fragment.context?.let { getDrawable(it,R.drawable.btn_round_to_run_red) }
            holder.llProgressBar.visibility = VISIBLE
        } else {
            // Con ejecuciones previas
            holder.llProgressBar.visibility = GONE
            holder.btnStopRound.visibility = INVISIBLE
            if(bRoundRunning){
                if((fragment.requireActivity() as MainActivity).minRoundRunDetail?.round?.id == roundToRun.round.id){
                    holder.btnStartRound.text = holder.btnStartRound.context.getString(R.string.ver_ronda)
                    holder.btnStartRound.isEnabled = true
                }
                else{
                    holder.btnStartRound.text = holder.btnStartRound.context.getString(R.string.iniciar)
                    holder.btnStartRound.isEnabled = false
                }
            }else {
                holder.btnStartRound.text = holder.btnStartRound.context.getString(R.string.iniciar)
                holder.btnStartRound.isEnabled = true
            }
            holder.tvRoundRunDescription.text = getCurrentStatus(roundToRun)
            holder.tvRoundStartDate.text = "Iniciada el ${Helper.formattedDate(roundToRun.startDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)}"
            val strDate = roundToRun.endDate
            val currentDate = Date()
            val endDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(strDate)
            val diff = currentDate.time - (endDate?.time ?: currentDate.time)
            if(diff<(60*60*1000 * CANT_HORAS_MUESTRA_RONDA_FINALIZADA)){
                holder.roundCard.setCardBackgroundColor(Color.LTGRAY)
                holder.tvRoundStartDate.setTextColor(Color.BLACK)
                holder.tvRoundName.setTextColor(Color.BLACK)

                holder.btnStopRound.text = fragment.resources.getString(R.string.resumen)
                holder.btnStopRound.background = fragment.context?.let { getDrawable(it,R.drawable.btn_round_rounded_blue) }
            }
        }

        holder.btnStartRound.setOnClickListener{
            if(fragment is RoundListFragment)
                fragment.sendGoToRound(roundToRun.round.id)
        }

        holder.btnStopRound.setOnClickListener{
            if(holder.btnStopRound.text.equals(fragment.resources.getString(R.string.detener))){
                if (roundToRun.endDate.isEmpty() && roundToRun.startDate.isNotEmpty()){
                    roundToRun.let { roundRunDetail ->
                        if (roundRunDetail.round.id != 0L){
                            (fragment.requireActivity() as MainActivity).sendEndToMixer()
                        }
                    }
                }
            }else if(holder.btnStopRound.text.equals(fragment.resources.getString(R.string.resumen))){
                (fragment.requireActivity() as MainActivity).sendGoToResume(roundToRun.round.id)
            }
        }

    }

    override fun getItemCount(): Int {
        return filteredRoundsRun.size
    }

    fun roundList(list: List<MedRoundRunDetail>){
        roundsRun = list
        filteredRoundsRun = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredRoundsRun = if (charString.isEmpty()) roundsRun else {
                    val filteredList = ArrayList<MedRoundRunDetail>()
                    roundsRun
                        .filter {
                            (it.round.name.lowercase().contains(charString.lowercase())) or
                                    (it.round.description.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = filteredRoundsRun }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredRoundsRun = if (results?.values == null){
                    Log.i("DEBUG","results.value = null")
                    ArrayList()
                }
                else{
                    Log.i("DEBUG","results.value ${results.values}")
                    results.values as List<MedRoundRunDetail>
                }
                notifyDataSetChanged()
            }
        }
    }

    fun sincroRound(roundRunning:Boolean) {
        if(!(this.bRoundRunning && roundRunning)){
            this.bRoundRunning = roundRunning
            notifyDataSetChanged()
        }
    }


}