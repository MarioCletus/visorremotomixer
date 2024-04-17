package com.basculasmagris.visorremotomixer.view.fragments

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.basculasmagris.visorremotomixer.R

public class RemoteMixerFragmentDirections private constructor() {
  public companion object {
    public fun actionRemoteMixerFragmentToTableMixerListFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_remote_mixer_fragment_to_table_mixer_list_fragment)

    public fun actionRemoteMixerFragmentToResumeFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_remote_mixer_fragment_to_resume_fragment)

    public fun actionRemoteMixerFragmentToRoundListFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_remote_mixer_fragment_to_round_list_fragment)
  }
}
