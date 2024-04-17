package com.basculasmagris.visorremotomixer.view.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import java.io.Serializable
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException
import kotlin.Int
import kotlin.Suppress
import kotlin.jvm.JvmStatic

public data class RemoteMixerFragmentArgs(
  public val tabletMixer: TabletMixer? = null,
  public val tipoRonda: Int = 0
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(TabletMixer::class.java)) {
      result.putParcelable("tabletMixer", this.tabletMixer as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(TabletMixer::class.java)) {
      result.putSerializable("tabletMixer", this.tabletMixer as Serializable?)
    }
    result.putInt("tipoRonda", this.tipoRonda)
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    if (Parcelable::class.java.isAssignableFrom(TabletMixer::class.java)) {
      result.set("tabletMixer", this.tabletMixer as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(TabletMixer::class.java)) {
      result.set("tabletMixer", this.tabletMixer as Serializable?)
    }
    result.set("tipoRonda", this.tipoRonda)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): RemoteMixerFragmentArgs {
      bundle.setClassLoader(RemoteMixerFragmentArgs::class.java.classLoader)
      val __tabletMixer : TabletMixer?
      if (bundle.containsKey("tabletMixer")) {
        if (Parcelable::class.java.isAssignableFrom(TabletMixer::class.java) ||
            Serializable::class.java.isAssignableFrom(TabletMixer::class.java)) {
          __tabletMixer = bundle.get("tabletMixer") as TabletMixer?
        } else {
          throw UnsupportedOperationException(TabletMixer::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        __tabletMixer = null
      }
      val __tipoRonda : Int
      if (bundle.containsKey("tipoRonda")) {
        __tipoRonda = bundle.getInt("tipoRonda")
      } else {
        __tipoRonda = 0
      }
      return RemoteMixerFragmentArgs(__tabletMixer, __tipoRonda)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): RemoteMixerFragmentArgs {
      val __tabletMixer : TabletMixer?
      if (savedStateHandle.contains("tabletMixer")) {
        if (Parcelable::class.java.isAssignableFrom(TabletMixer::class.java) ||
            Serializable::class.java.isAssignableFrom(TabletMixer::class.java)) {
          __tabletMixer = savedStateHandle.get<TabletMixer?>("tabletMixer")
        } else {
          throw UnsupportedOperationException(TabletMixer::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        __tabletMixer = null
      }
      val __tipoRonda : Int?
      if (savedStateHandle.contains("tipoRonda")) {
        __tipoRonda = savedStateHandle["tipoRonda"]
        if (__tipoRonda == null) {
          throw IllegalArgumentException("Argument \"tipoRonda\" of type integer does not support null values")
        }
      } else {
        __tipoRonda = 0
      }
      return RemoteMixerFragmentArgs(__tabletMixer, __tipoRonda)
    }
  }
}
