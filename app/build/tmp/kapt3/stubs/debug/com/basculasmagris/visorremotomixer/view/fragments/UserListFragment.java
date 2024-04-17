package com.basculasmagris.visorremotomixer.view.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\u000e\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0007J\u000e\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015H\u0002J\b\u0010\u0017\u001a\u00020\u0011H\u0002J\b\u0010\u0018\u001a\u00020\u0011H\u0002J\u0006\u0010\u0019\u001a\u00020\u0011J$\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010\"\u001a\u00020\u0011H\u0016J\u001a\u0010#\u001a\u00020\u00112\u0006\u0010$\u001a\u00020\u001b2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\b\u001a\u00020\t8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\f\u0010\r\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/fragments/UserListFragment;", "Landroidx/fragment/app/Fragment;", "()V", "mBinding", "Lcom/basculasmagris/visorremotomixer/databinding/FragmentUserListBinding;", "mLocalUsers", "", "Lcom/basculasmagris/visorremotomixer/model/entities/User;", "mUserViewModel", "Lcom/basculasmagris/visorremotomixer/viewmodel/UserViewModel;", "getMUserViewModel", "()Lcom/basculasmagris/visorremotomixer/viewmodel/UserViewModel;", "mUserViewModel$delegate", "Lkotlin/Lazy;", "mUserViewModelRemote", "Lcom/basculasmagris/visorremotomixer/viewmodel/UserRemoteViewModel;", "cleanObservers", "", "deleteUser", "user", "fetchLocalData", "Landroidx/lifecycle/MediatorLiveData;", "Lcom/basculasmagris/visorremotomixer/view/activities/MergedLocalData;", "getLocalData", "getLocalUser", "goToAddUpdateUser", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "app_debug"})
public final class UserListFragment extends androidx.fragment.app.Fragment {
    private com.basculasmagris.visorremotomixer.databinding.FragmentUserListBinding mBinding;
    private final kotlin.Lazy mUserViewModel$delegate = null;
    private java.util.List<com.basculasmagris.visorremotomixer.model.entities.User> mLocalUsers;
    private com.basculasmagris.visorremotomixer.viewmodel.UserRemoteViewModel mUserViewModelRemote;
    
    public UserListFragment() {
        super();
    }
    
    private final com.basculasmagris.visorremotomixer.viewmodel.UserViewModel getMUserViewModel() {
        return null;
    }
    
    private final androidx.lifecycle.MediatorLiveData<com.basculasmagris.visorremotomixer.view.activities.MergedLocalData> fetchLocalData() {
        return null;
    }
    
    private final void getLocalData() {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void getLocalUser() {
    }
    
    public final void goToAddUpdateUser() {
    }
    
    public final void deleteUser(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.User user) {
    }
    
    @java.lang.Override
    public void onDestroyView() {
    }
    
    private final void cleanObservers() {
    }
}