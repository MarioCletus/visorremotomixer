package com.basculasmagris.visorremotomixer.view.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00180\u00172\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001cH\u0002J$\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010#2\b\u0010$\u001a\u0004\u0018\u00010%H\u0016J\b\u0010&\u001a\u00020\u001cH\u0016J\b\u0010\'\u001a\u00020\u001cH\u0016J\b\u0010(\u001a\u00020\u001cH\u0016J\u001a\u0010)\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\u001f2\b\u0010$\u001a\u0004\u0018\u00010%H\u0016J\b\u0010+\u001a\u00020\u001cH\u0002J\u000e\u0010,\u001a\u00020\u001c2\u0006\u0010-\u001a\u00020.J\u0010\u0010/\u001a\u00020\u001c2\u0006\u00100\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u000f\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0010\u001a\u00020\u00118BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0012\u0010\u0013\u00a8\u00061"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/fragments/RoundListFragment;", "Landroidx/fragment/app/Fragment;", "()V", "TAG", "", "bBlockButton", "", "bGoToRound", "fragmentRunning", "mBinding", "Lcom/basculasmagris/visorremotomixer/databinding/FragmentRoundListBinding;", "mBluetoothListener", "Lcom/basculasmagris/visorremotomixer/view/interfaces/IBluetoothSDKListener;", "mLocalRoundsLocal", "", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundLocal;", "mRoundLocalViewModel", "Lcom/basculasmagris/visorremotomixer/viewmodel/RoundLocalViewModel;", "getMRoundLocalViewModel", "()Lcom/basculasmagris/visorremotomixer/viewmodel/RoundLocalViewModel;", "mRoundLocalViewModel$delegate", "Lkotlin/Lazy;", "fetchLocalData", "Landroidx/lifecycle/MediatorLiveData;", "Lcom/basculasmagris/visorremotomixer/view/activities/MergedLocalData;", "tabletMixer", "Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixer;", "getLocalData", "", "getLocalRound", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onPause", "onResume", "onViewCreated", "view", "refreshData", "sendGoToRound", "id", "", "sincroRound", "b", "app_release"})
public final class RoundListFragment extends androidx.fragment.app.Fragment {
    private com.basculasmagris.visorremotomixer.databinding.FragmentRoundListBinding mBinding;
    private final java.lang.String TAG = "DEBRLF";
    private boolean bBlockButton = false;
    private final kotlin.Lazy mRoundLocalViewModel$delegate = null;
    private java.util.List<com.basculasmagris.visorremotomixer.model.entities.RoundLocal> mLocalRoundsLocal;
    private boolean bGoToRound = false;
    private boolean fragmentRunning = false;
    private final com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener mBluetoothListener = null;
    
    public RoundListFragment() {
        super();
    }
    
    private final com.basculasmagris.visorremotomixer.viewmodel.RoundLocalViewModel getMRoundLocalViewModel() {
        return null;
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
    
    private final void refreshData() {
    }
    
    private final androidx.lifecycle.MediatorLiveData<com.basculasmagris.visorremotomixer.view.activities.MergedLocalData> fetchLocalData(com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixer) {
        return null;
    }
    
    private final void getLocalData() {
    }
    
    private final void getLocalRound() {
    }
    
    private final void sincroRound(boolean b) {
    }
    
    @java.lang.Override
    public void onDestroyView() {
    }
    
    public final void sendGoToRound(long id) {
    }
    
    @java.lang.Override
    public void onResume() {
    }
    
    @java.lang.Override
    public void onPause() {
    }
}