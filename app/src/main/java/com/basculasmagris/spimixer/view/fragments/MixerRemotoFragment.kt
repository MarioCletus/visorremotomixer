package com.basculasmagris.visorremotomixer.view.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.basculasmagris.visorremotomixer.viewmodel.MixerRemotoViewModel
import com.basculasmagris.visorremotomixer.R

class MixerRemotoFragment : Fragment() {

    companion object {
        fun newInstance() = MixerRemotoFragment()
    }

    private lateinit var viewModel: MixerRemotoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mixer_remoto, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MixerRemotoViewModel::class.java)
        // TODO: Use the ViewModel
    }

}