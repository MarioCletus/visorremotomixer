package com.basculasmagris.visorremotomixer.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentHomeBinding



class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)

        mBinding.btnMixer.setOnClickListener{
            findNavController().navigate(R.id.nav_tablet_mixer)
        }

        mBinding.btnSincro.setOnClickListener{
            findNavController().navigate(R.id.nav_sync)
        }

        mBinding.btnRondas.setOnClickListener{
            findNavController().navigate(R.id.nav_round)
        }

        mBinding.btnRondaLibre.setOnClickListener{
            findNavController().navigate(R.id.nav_mixer_remoto)
        }


        return mBinding.root
    }


}