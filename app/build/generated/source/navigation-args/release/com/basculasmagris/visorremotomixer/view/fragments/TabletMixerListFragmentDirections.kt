package com.basculasmagris.visorremotomixer.view.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import java.io.Serializable
import kotlin.Int
import kotlin.Suppress

public class TabletMixerListFragmentDirections private constructor() {
  private data class ActionTabletMixerListFragmentToRemoteMixerFragment(
    public val tabletMixer: TabletMixer? = null,
    public val tipoRonda: Int = 0
  ) : NavDirections {
    public override val actionId: Int =
        R.id.action_tablet_mixer_list_fragment_to_remote_mixer_fragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        if (Parcelable::class.java.isAssignableFrom(TabletMixer::class.java)) {
          result.putParcelable("tabletMixer", this.tabletMixer as Parcelable?)
        } else if (Serializable::class.java.isAssignableFrom(TabletMixer::class.java)) {
          result.putSerializable("tabletMixer", this.tabletMixer as Serializable?)
        }
        result.putInt("tipoRonda", this.tipoRonda)
        return result
      }
  }

  public companion object {
    public fun actionTabletMixerListFragmentToAddUpdateTabletMixerActivity(): NavDirections =
        ActionOnlyNavDirections(R.id.action_tablet_mixerListFragment_to_addUpdateTabletMixerActivity)

    public fun actionTabletMixerListFragmentToRemoteMixerFragment(tabletMixer: TabletMixer? = null,
        tipoRonda: Int = 0): NavDirections =
        ActionTabletMixerListFragmentToRemoteMixerFragment(tabletMixer, tipoRonda)
  }
}
