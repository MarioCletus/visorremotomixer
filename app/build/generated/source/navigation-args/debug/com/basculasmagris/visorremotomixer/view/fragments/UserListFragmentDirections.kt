package com.basculasmagris.visorremotomixer.view.fragments

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.basculasmagris.visorremotomixer.R

public class UserListFragmentDirections private constructor() {
  public companion object {
    public fun actionUserListFragmentToAddUpdateUserActivity(): NavDirections =
        ActionOnlyNavDirections(R.id.action_userListFragment_to_addUpdateUserActivity)
  }
}
