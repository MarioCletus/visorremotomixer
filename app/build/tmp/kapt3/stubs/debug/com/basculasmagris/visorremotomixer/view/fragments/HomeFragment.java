package com.basculasmagris.visorremotomixer.view.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0018J\u0012\u0010\u001e\u001a\u00020\u001c2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J$\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\b\u0010\'\u001a\u00020\u001cH\u0016J\b\u0010(\u001a\u00020\u001cH\u0016J\u001a\u0010)\u001a\u00020\u001c2\u0006\u0010*\u001a\u00020\"2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u0016J\u000e\u0010+\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0018J\u000e\u0010,\u001a\u00020\u001c2\u0006\u0010-\u001a\u00020\u0018R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u00020\fX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0019\u001a\u0004\u0018\u00010\u001aX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/fragments/HomeFragment;", "Landroidx/fragment/app/Fragment;", "()V", "TAG", "", "bInCfg", "", "bInDownload", "bInFree", "bInLoad", "bInRes", "countMessage", "", "getCountMessage", "()I", "setCountMessage", "(I)V", "mBinding", "Lcom/basculasmagris/visorremotomixer/databinding/FragmentHomeBinding;", "mBluetoothListener", "Lcom/basculasmagris/visorremotomixer/view/interfaces/IBluetoothSDKListener;", "menu", "Landroid/view/Menu;", "selectedTabletMixerInFragment", "Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixer;", "tabletMixerBluetoothDevice", "Landroid/bluetooth/BluetoothDevice;", "connectTable", "", "tabletMixer", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onResume", "onStop", "onViewCreated", "view", "selectTablet", "setTabletMixer", "tabletMixerIn", "app_debug"})
public final class HomeFragment extends androidx.fragment.app.Fragment {
    private com.basculasmagris.visorremotomixer.databinding.FragmentHomeBinding mBinding;
    private final java.lang.String TAG = "DEBHome";
    private com.basculasmagris.visorremotomixer.model.entities.TabletMixer selectedTabletMixerInFragment;
    private android.bluetooth.BluetoothDevice tabletMixerBluetoothDevice;
    private android.view.Menu menu;
    private boolean bInFree = true;
    private boolean bInCfg = false;
    private boolean bInRes = false;
    private boolean bInLoad = false;
    private boolean bInDownload = false;
    private int countMessage = 0;
    private final com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener mBluetoothListener = null;
    
    public HomeFragment() {
        super();
    }
    
    @java.lang.Override
    public void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
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
    
    public final int getCountMessage() {
        return 0;
    }
    
    public final void setCountMessage(int p0) {
    }
    
    public final void selectTablet(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixer) {
    }
    
    public final void connectTable(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixer) {
    }
    
    public final void setTabletMixer(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixerIn) {
    }
    
    @java.lang.Override
    public void onResume() {
    }
    
    @java.lang.Override
    public void onStop() {
    }
}