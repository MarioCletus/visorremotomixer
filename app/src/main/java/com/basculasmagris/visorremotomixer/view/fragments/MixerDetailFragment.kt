package com.basculasmagris.visorremotomixer.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentMixerDetailBinding

class MixerDetailFragment : Fragment() {

    private var mBinding: FragmentMixerDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentMixerDetailBinding.inflate(inflater, container, false)
        return mBinding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: MixerDetailFragmentArgs by navArgs()
        Log.i("Mixer name", args.mixerDetail.name)

        args.mixerDetail.let { mixer ->
            mBinding?.let {
                it.tvMixerTitle.text = mixer.name
                it.etMixerDescription.text = mixer.description
                it.tvNoAdditionalData.text = resources.getString(R.string.tv_no_additional_data)
                it.tvBtBox.text = mixer.btBox
                it.tvCalibration.text = mixer.calibration.toString()
                it.tvMac.text = mixer.mac
                it.tvTara.text = mixer.tara.toString()
                it.tvMixerRfid.text = mixer.rfid.toString()

                if (mixer.remoteId > 0) {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.green_500_primary), android.graphics.PorterDuff.Mode.SRC_IN)
                    it.tvSyncInfo.text = resources.getString(R.string.sync)
                } else {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.red_500_primary), android.graphics.PorterDuff.Mode.SRC_IN)
                    it.tvSyncInfo.text = resources.getString(R.string.pending_sync)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}