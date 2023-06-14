package com.basculasmagris.visorremotomixer.view.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentStepResumeBinding
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.utils.MarginItemDecoration
import com.basculasmagris.visorremotomixer.view.activities.RoundRunActivity
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralResumeAdapter
import com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductResumeAdapter
import com.kofigyan.stateprogressbar.StateProgressBar

class StepResumeFragment : Fragment() {
    private lateinit var mBinding: FragmentStepResumeBinding
    private var stepsDescription = arrayOf("Configuración","Carga", "Descarga", "Fin")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = (requireActivity() as RoundRunActivity)
        val currentUser = Helper.getCurrentUser(activity)

        activity.currentRoundRunDetail?.round?.diet?.let { dietDetail ->
            mBinding.rvRoundProductsToLoad.layoutManager = GridLayoutManager(requireActivity(), 1)
            val roundRunProductAdapter =  RoundRunProductResumeAdapter(
                this@StepResumeFragment)
            dietDetail.products.let { products ->
                roundRunProductAdapter.productList(products)
            }
            mBinding.rvRoundProductsToLoad.adapter = roundRunProductAdapter
        }
        mBinding.spRoundRunStep.setStateDescriptionData(stepsDescription)
        mBinding.spRoundRunStep.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR)
        mBinding.tvRoundUserRun.text = currentUser.displayName
        mBinding.rvRoundCorralsToLoad.layoutManager = GridLayoutManager(requireActivity(), 1)
        mBinding.rvRoundProductsToLoad.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_recycler)))
        mBinding.rvRoundCorralsToLoad.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.margin_recycler)))

        val roundRunCorralAdapter =  RoundRunCorralResumeAdapter(
            this@StepResumeFragment)
        activity.currentRoundRunDetail?.round?.corrals?.let { corrals ->
            roundRunCorralAdapter.corralList(corrals)
        }
        mBinding.rvRoundCorralsToLoad.adapter = roundRunCorralAdapter

        mBinding.tvRoundStartDate.text = Helper.formattedDate(activity.currentRoundRunDetail?.startDate!!, Constants.APP_DB_FORMAT_DATE, Constants.APP_SHOW_LARGE_FORMAT_DATE)

        val loadDiff = activity.getLoadDifference()
        if (loadDiff > -1 && loadDiff < 1){
            mBinding.tvTotalLoadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalLoad(), 0)}Kg     +${Helper.getNumberWithDecimals(loadDiff, 0)}Kg"
        } else if (loadDiff >= 1){
            mBinding.tvTotalLoadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalLoad(), 0)}Kg     +${Helper.getNumberWithDecimals(loadDiff, 0)}Kg"
        } else {
            mBinding.tvTotalLoadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalLoad(), 0)}Kg     ${Helper.getNumberWithDecimals(loadDiff, 0)}Kg"
        }

        val downloadDiff = activity.getDownloadDifference()
        if (downloadDiff >-1 && downloadDiff < 1){
            mBinding.tvTotalDownloadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalDownload(),0)}Kg     +${Helper.getNumberWithDecimals(downloadDiff,0)}Kg"
        } else if (downloadDiff >= 1){
            mBinding.tvTotalDownloadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalDownload(),0)}Kg     +${Helper.getNumberWithDecimals(downloadDiff,0)}Kg"
        } else {
            mBinding.tvTotalDownloadDiff.text = "${Helper.getNumberWithDecimals(activity.getFinalDownload(),0)}Kg     ${Helper.getNumberWithDecimals(downloadDiff,0)}Kg"
        }

        mBinding.tvRoundDietTotalWeight.text = "${Helper.getNumberWithDecimals(activity.getTargetWeight(), 0)}Kg"

        if(activity.currentRoundRunDetail?.endDate.isNullOrEmpty()){
            activity.finishRoundStatus(Constants.STATE_FINISH)
        }
        activity.stopAutomaticSave()
        activity.currentRoundRunDetail?.state = Constants.STATE_RESUME
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentStepResumeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    fun exitFragment(){
        (activity as RoundRunActivity).currentRoundRunDetail?.state = Constants.STATE_FINISH
        (requireActivity() as RoundRunActivity).changeStep(1)
    }
}