package com.basculasmagris.visorremotomixer.view.adapter

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View.*
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.ItemRoundToRunLayoutBinding
import com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.Helper.Companion.getCurrentUser
import com.basculasmagris.visorremotomixer.view.activities.AddUpdateRoundActivity
import com.basculasmagris.visorremotomixer.view.fragments.HomeFragment
import com.basculasmagris.visorremotomixer.view.fragments.HomeFragmentDirections
import com.basculasmagris.visorremotomixer.viewmodel.RoundViewModel
import com.basculasmagris.visorremotomixer.viewmodel.RoundViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class RoundRunAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<RoundRunAdapter.ViewHolder>(),
    Filterable {

    private val CANT_HORAS_MUESTRA_RONDA_FINALIZADA : Int = 4
    private var roundsRun: List<RoundRunDetail> = ArrayList()
    private var filteredRoundsRun: List<RoundRunDetail> = ArrayList()

    private val mRoundViewModel: RoundViewModel by fragment.viewModels {
        RoundViewModelFactory((fragment.requireActivity().application as SpiMixerApplication).roundRepository)
    }

    class ViewHolder (view: ItemRoundToRunLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvRoundName = view.tvRoundName
        val tvRoundRunDescription = view.tvRoundRunDescription
        val ibMoreRoundRun = view.ibMoreRoundRun
        val cvRoundIcon = view.cvRoundIcon
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

    private fun getProgress(roundRunDetail: RoundRunDetail) : Int{
        var progressValue = 0
        var totalProgressLoadWeight = 0.0
        var totalWeightLoad = 0.0
        var totalProgressDownLoadWeight = 0.0
        var totalWeightDownload = 0.0
        val roundTargetWeight = roundRunDetail.round.customRoundRunWeight

        roundRunDetail.round.corrals.forEach { corralDetail ->
            totalProgressDownLoadWeight += corralDetail.initialWeight - corralDetail.currentWeight
            totalWeightDownload += corralDetail.weight
        }

        if (totalWeightDownload > 0 && totalProgressDownLoadWeight > 0){
            progressValue = 50 + (totalProgressDownLoadWeight * 50 / totalWeightDownload).roundToInt()
        } else {
            roundRunDetail.round.diet.products.forEach { productDetail ->
                val maxWeightForProducts = productDetail.percentage*roundTargetWeight/100
                totalWeightLoad += maxWeightForProducts
                if (roundTargetWeight > 0){
                    totalProgressLoadWeight += (if (productDetail.currentWeight-productDetail.initialWeight > maxWeightForProducts) maxWeightForProducts else productDetail.currentWeight-productDetail.initialWeight)
                }
            }

            if (totalWeightLoad > 0){
                progressValue = (totalProgressLoadWeight*50/totalWeightLoad).roundToInt()
            }
        }
        return progressValue
    }

    private fun getCurrentStatus(roundToRun: RoundRunDetail) : String {

        val status = ""

        if (roundToRun.startDate.isEmpty()) {
            return  Constants.STATUS_NOT_RUN
        }

        Log.i("RUN", "Fecha de fin: ${roundToRun.endDate} de ${roundToRun.round.name}")
        if (roundToRun.endDate.isNotEmpty()) {
            val state = if(roundToRun.state == Constants.STATE_INTERRUPT) Constants.STATUS_INCOMPLETED else Constants.STATUS_FINISHED
            return  "$state   ${Helper.formattedDate(roundToRun.endDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)}"
        }

        val currentProgress = getProgress(roundToRun)
        if (currentProgress < 50){
            return Constants.STATUS_LOAD_IN_PROGRESS
        }

        if (currentProgress == 50){
            return Constants.STATUS_LOAD_COMPLETED
        }

        if (currentProgress > 50){
            return Constants.STATUS_DOWNLOAD_IN_PROGRESS
        }

        if (currentProgress == 100){
            return Constants.STATUS_DOWNLOAD_COMPLETED
        }

        return status
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roundToRun = filteredRoundsRun[position]
        holder.tvRoundName.text = roundToRun.round.name

        if (roundToRun.endDate.isEmpty() && roundToRun.startDate.isEmpty()){
            // Nunca iniciada
            holder.llProgressBar.visibility = GONE
            holder.btnStopRound.visibility = INVISIBLE
            holder.btnStartRound.text = "INICIAR"
            holder.tvRoundRunDescription.text = getCurrentStatus(roundToRun)

        } else if (roundToRun.endDate.isEmpty()) {
            // En curso
            holder.btnStartRound.text = "CONTINUAR"
            holder.tvRoundStartDate.text = "Iniciada el ${Helper.formattedDate(roundToRun.startDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)}"
            holder.tvRoundRunDescription.text = getCurrentStatus(roundToRun)
            holder.pbRoundRun.progress = getProgress(roundToRun)
            holder.tvRoundRunPercentage.text = "${holder.pbRoundRun.progress}%"
            holder.btnStopRound.visibility = VISIBLE
            holder.btnStopRound.text = fragment.resources.getString(R.string.detener)
            holder.btnStopRound.background = fragment.context?.let { getDrawable(it,R.drawable.btn_round_to_run_red) }
            holder.llProgressBar.visibility = VISIBLE
        } else {
            // Con ejecuciones previas
            holder.llProgressBar.visibility = GONE
            holder.btnStopRound.visibility = INVISIBLE
            holder.btnStartRound.text = "INICIAR"
            holder.tvRoundRunDescription.text = getCurrentStatus(roundToRun)
            holder.tvRoundStartDate.text = "Iniciada el ${Helper.formattedDate(roundToRun.startDate, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)}"
            val strDate = roundToRun.endDate
            val currentDate = Date()
            val endDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate)
            val diff = currentDate.time - (endDate?.time ?: currentDate.time)
            if(diff<(60*60*1000 * CANT_HORAS_MUESTRA_RONDA_FINALIZADA)){
                holder.roundCard.setCardBackgroundColor(Color.LTGRAY)
                holder.tvRoundStartDate.setTextColor(Color.BLACK)
                holder.tvRoundName.setTextColor(Color.BLACK)
                holder.tvRoundRunDescription.setTextColor(R.color.color_deep_green)
                holder.btnStopRound.visibility = VISIBLE
                holder.btnStopRound.text = fragment.resources.getString(R.string.resumen)
                holder.btnStopRound.background = fragment.context?.let { getDrawable(it,R.drawable.btn_round_rounded_blue) }
            }
        }

        holder.itemView.setOnClickListener {
            if (fragment is HomeFragment){
                val localRow = mRoundViewModel.getRoundById(roundToRun.round.id)
                fragment.lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        localRow.observeOnce(fragment){round->
                            fragment.goToRoundDetails(round)
                        }
                    }
                }
            }

        }

        holder.ibMoreRoundRun.setOnClickListener{
            val popup =  PopupMenu(fragment.context, holder.ibMoreRoundRun)
            popup.menuInflater.inflate(R.menu.menu_adapter_round, popup.menu)

            // Si el roundo está sincronizado no se permite el borrado.
            if (roundToRun.remoteId != 0L) {
                popup.menu.getItem(1).isVisible = false
            }

            popup.setOnMenuItemClickListener {menuItem ->
                if (menuItem.itemId  == R.id.action_edit_round){
                    val ldRound = mRoundViewModel.getRoundById(roundToRun.round.id)
                    ldRound.observeOnce(fragment.requireActivity()){round->
                        val intent = Intent(fragment.requireActivity(), AddUpdateRoundActivity::class.java)
                        intent.putExtra(Constants.EXTRA_ROUND_DETAILS, round)
                        fragment.requireActivity().startActivity(intent)
                    }
                } else if (menuItem.itemId == R.id.action_delete_round){
                    if (fragment is HomeFragment) {
                        val localRow = mRoundViewModel.getRoundById(roundToRun.round.id)
                        fragment.lifecycleScope.launch {
                            withContext(Dispatchers.Main) {
                                localRow.observeOnce(fragment){round->
                                    fragment.deleteRound(round)
                                }
                            }
                        }

                    }
                }
                true
            }

            popup.show()
        }

        holder.btnStartRound.setOnClickListener{
            if (fragment is HomeFragment){
                if(roundToRun.startDate.isEmpty() && roundToRun.endDate.isEmpty()){
                    //Ronda nueva
                    fragment.goToRoundRun(roundToRun,1)
                }
                else if (roundToRun.startDate.isNotEmpty() && roundToRun.endDate.isNotEmpty()){
                    //Ronda finalizada
                    fragment.goToRoundRun(roundToRun,4)
                }else if (roundToRun.startDate.isNotEmpty() && roundToRun.endDate.isEmpty()){
                    //Ronda comenzada
                    roundToRun.round.diet.products.forEach {
                        if(it.finalWeight.isNaN() || it.finalWeight.roundToLong() == 0L){
                            fragment.goToRoundRun(roundToRun,2)
                            return@setOnClickListener
                        }
                    }
                    fragment.goToRoundRun(roundToRun,3)
                }else if(roundToRun.startDate.isEmpty() && roundToRun.endDate.isNotEmpty()){
                    Log.i("DEBUG","Este estado no debería existir")
                    fragment.goToRoundRun(roundToRun,1)
                }

            }
        }

        holder.btnStopRound.setOnClickListener{
            if (fragment is HomeFragment){
                if(holder.btnStopRound.text.equals(fragment.resources.getString(R.string.detener))){
                    if (roundToRun.endDate.isEmpty() && roundToRun.startDate.isNotEmpty()){
                        roundToRun.let { roundRunDetail ->
                            if (roundRunDetail.id != 0L){
                                fragment.activity?.lifecycleScope?.launch(Dispatchers.IO){
                                    mRoundViewModel.finishRoundRunSyncWState(roundRunDetail.id,Constants.STATE_INTERRUPT)
                                }
                            }
                        }
                    }
                }else if(holder.btnStopRound.text.equals(fragment.resources.getString(R.string.resumen))){
                    fragment.findNavController().navigate(HomeFragmentDirections.actionNavHomeToRoundRunActivity(roundToRun,4))
                }

            }
        }

        if (fragment is HomeFragment) {
            val currentUser = getCurrentUser(fragment.requireActivity())
            if(currentUser.role.code == 1)
                holder.ibMoreRoundRun.visibility = VISIBLE
            holder.cvRoundIcon.visibility = GONE
        } else {
            holder.ibMoreRoundRun.visibility = GONE
            holder.cvRoundIcon.visibility = GONE
        }
    }

    override fun getItemCount(): Int {
        return filteredRoundsRun.size
    }

    fun roundList(list: List<RoundRunDetail>){
        roundsRun = list
        filteredRoundsRun = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredRoundsRun = if (charString.isEmpty()) roundsRun else {
                    val filteredList = ArrayList<RoundRunDetail>()
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
                    results.values as List<RoundRunDetail>
                }
                notifyDataSetChanged()
            }
        }
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}