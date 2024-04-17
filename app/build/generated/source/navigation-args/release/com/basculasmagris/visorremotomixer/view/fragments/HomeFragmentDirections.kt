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

public class HomeFragmentDirections private constructor() {
  private data class ActionHomeToFreeRound(
    public val tabletMixer: TabletMixer? = null,
    public val tipoRonda: Int = 0
  ) : NavDirections {
    public override val actionId: Int = R.id.action_home_to_free_round

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
    public fun actionHomeToMixer(): NavDirections =
        ActionOnlyNavDirections(R.id.action_home_to_mixer)

    public fun actionHomeToRound(): NavDirections =
        ActionOnlyNavDirections(R.id.action_home_to_round)

    public fun actionHomeToSync(): NavDirections = ActionOnlyNavDirections(R.id.action_home_to_sync)

    public fun actionHomeToFreeRound(tabletMixer: TabletMixer? = null, tipoRonda: Int = 0):
        NavDirections = ActionHomeToFreeRound(tabletMixer, tipoRonda)
  }
}
