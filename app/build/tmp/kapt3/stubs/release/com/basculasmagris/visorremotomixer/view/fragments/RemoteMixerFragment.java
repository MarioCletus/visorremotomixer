package com.basculasmagris.visorremotomixer.view.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u00bc\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0012\n\u0002\b\n\n\u0002\u0010\u0007\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010V\u001a\u00020WH\u0002J\b\u0010X\u001a\u00020WH\u0002J\u001e\u0010Y\u001a\u0004\u0018\u00010%2\b\b\u0002\u0010Z\u001a\u00020\u00072\b\b\u0002\u0010[\u001a\u00020\u0007H\u0002J\u0006\u0010\\\u001a\u00020\u000bJ\b\u0010]\u001a\u00020WH\u0002J\u0012\u0010^\u001a\u00020W2\b\u0010_\u001a\u0004\u0018\u00010`H\u0016J$\u0010a\u001a\u00020b2\u0006\u0010c\u001a\u00020d2\b\u0010e\u001a\u0004\u0018\u00010f2\b\u0010_\u001a\u0004\u0018\u00010`H\u0016J\b\u0010g\u001a\u00020WH\u0016J\b\u0010h\u001a\u00020WH\u0016J\u001a\u0010i\u001a\u00020W2\u0006\u0010j\u001a\u00020b2\b\u0010_\u001a\u0004\u0018\u00010`H\u0016J\b\u0010k\u001a\u00020WH\u0002J\b\u0010l\u001a\u00020WH\u0002J\b\u0010m\u001a\u00020WH\u0002J\u0010\u0010n\u001a\u00020W2\u0006\u0010o\u001a\u00020pH\u0002J\n\u0010q\u001a\u0004\u0018\u00010%H\u0002J\u0006\u0010r\u001a\u00020WJ\u0006\u0010s\u001a\u00020WJ\u0010\u0010t\u001a\u00020W2\b\u0010u\u001a\u0004\u0018\u00010NJ\u000e\u0010v\u001a\u00020W2\u0006\u0010w\u001a\u00020PJ.\u0010x\u001a\u0004\u0018\u00010%2\u0006\u0010y\u001a\u00020\u00072\u0006\u0010o\u001a\u00020\u00072\b\b\u0002\u0010z\u001a\u00020{2\b\b\u0002\u0010|\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0017\"\u0004\b\u001c\u0010\u0019R\u000e\u0010\u001d\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\"\u001a\u0004\u0018\u00010#X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010&\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\'\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010(\u001a\u0004\u0018\u00010)X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010*\u001a\u00020+X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R\u000e\u00100\u001a\u000201X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u00102\u001a\u0004\u0018\u000103X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b4\u00105\"\u0004\b6\u00107R\u0010\u00108\u001a\u0004\u0018\u000109X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010;\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b<\u0010=\"\u0004\b>\u0010?R\u000e\u0010@\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010A\u001a\u0004\u0018\u00010BX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bC\u0010D\"\u0004\bE\u0010FR\u001c\u0010G\u001a\u0004\u0018\u00010HX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\bI\u0010J\"\u0004\bK\u0010LR\u0010\u0010M\u001a\u0004\u0018\u00010NX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010O\u001a\u0004\u0018\u00010PX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010Q\u001a\u0004\u0018\u00010%X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010R\u001a\u0004\u0018\u00010SX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010T\u001a\u00020UX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006}"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/fragments/RemoteMixerFragment;", "Lcom/google/android/material/bottomsheet/BottomSheetDialogFragment;", "()V", "REFRESH_DATA_TIME", "", "REFRESH_VIEW_TIME", "TAG", "", "activity", "Lcom/basculasmagris/visorremotomixer/view/activities/MainActivity;", "bInCfg", "", "bInDownload", "bInFree", "bInLoad", "bInRes", "bSyncroRounds", "bSyncroUsers", "contPressTara", "countDataMsg", "countGreaterThanTarget", "countInResume", "getCountInResume", "()I", "setCountInResume", "(I)V", "countMessage", "getCountMessage", "setCountMessage", "countMsg", "count_resume", "count_weight", "currentCorralDetail", "Lcom/basculasmagris/visorremotomixer/model/entities/MinCorralDetail;", "currentProductDetail", "Lcom/basculasmagris/visorremotomixer/model/entities/MinProductDetail;", "dialogCountDown", "Landroid/app/AlertDialog;", "dialogResto", "dialogTara", "estableTimer", "Ljava/util/Timer;", "mBinding", "Lcom/basculasmagris/visorremotomixer/databinding/FragmentRemoteMixerBinding;", "getMBinding", "()Lcom/basculasmagris/visorremotomixer/databinding/FragmentRemoteMixerBinding;", "setMBinding", "(Lcom/basculasmagris/visorremotomixer/databinding/FragmentRemoteMixerBinding;)V", "mBluetoothListener", "Lcom/basculasmagris/visorremotomixer/view/interfaces/IBluetoothSDKListener;", "menu", "Landroid/view/Menu;", "getMenu", "()Landroid/view/Menu;", "setMenu", "(Landroid/view/Menu;)V", "mixerDetail", "Lcom/basculasmagris/visorremotomixer/model/entities/MixerDetail;", "noPrevAlert", "oneBack", "getOneBack", "()Z", "setOneBack", "(Z)V", "rest", "roundRunCorralAdapter", "Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunCorralDownloadAdapter;", "getRoundRunCorralAdapter", "()Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunCorralDownloadAdapter;", "setRoundRunCorralAdapter", "(Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunCorralDownloadAdapter;)V", "roundRunProductAdapter", "Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunProductAdapter;", "getRoundRunProductAdapter", "()Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunProductAdapter;", "setRoundRunProductAdapter", "(Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunProductAdapter;)V", "selectedMixerInFragment", "Lcom/basculasmagris/visorremotomixer/model/entities/Mixer;", "selectedTabletMixerInFragment", "Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixer;", "targetReachedDialog", "timerTask", "Ljava/util/TimerTask;", "weight", "", "alertFinalDialog", "", "cleanAll", "dialogAlertTargetWeight", "nextItem", "strKg", "isInFree", "loadRoundDetail", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroyView", "onResume", "onViewCreated", "view", "permission", "refreshDataRound", "refreshRound", "refreshWeight", "message", "", "restDialog", "sendNextCorral", "sendNextProduct", "setMixer", "mixerIn", "setTabletMixer", "tabletMixerIn", "tareDialog", "title", "fontSize", "", "gravity", "app_release"})
public final class RemoteMixerFragment extends com.google.android.material.bottomsheet.BottomSheetDialogFragment {
    public com.basculasmagris.visorremotomixer.databinding.FragmentRemoteMixerBinding mBinding;
    private final java.lang.String TAG = "DEBMixerVR";
    @org.jetbrains.annotations.Nullable
    private android.view.Menu menu;
    private android.app.AlertDialog dialogResto;
    private android.app.AlertDialog dialogTara;
    private int rest = 0;
    private java.util.TimerTask timerTask;
    private java.util.Timer estableTimer;
    private com.basculasmagris.visorremotomixer.view.activities.MainActivity activity;
    private boolean noPrevAlert = true;
    private int countGreaterThanTarget = 0;
    private com.basculasmagris.visorremotomixer.model.entities.MinProductDetail currentProductDetail;
    private com.basculasmagris.visorremotomixer.model.entities.MixerDetail mixerDetail;
    @org.jetbrains.annotations.Nullable
    private com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter roundRunProductAdapter;
    private com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail currentCorralDetail;
    @org.jetbrains.annotations.Nullable
    private com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter roundRunCorralAdapter;
    private boolean bInFree = true;
    private boolean bInCfg = false;
    private boolean bInRes = false;
    private boolean bInLoad = false;
    private boolean bInDownload = false;
    private boolean bSyncroUsers = false;
    private boolean bSyncroRounds = false;
    private com.basculasmagris.visorremotomixer.model.entities.Mixer selectedMixerInFragment;
    private com.basculasmagris.visorremotomixer.model.entities.TabletMixer selectedTabletMixerInFragment;
    private android.app.AlertDialog targetReachedDialog;
    private android.app.AlertDialog dialogCountDown;
    private final int REFRESH_VIEW_TIME = 20;
    private int countMsg;
    private final int REFRESH_DATA_TIME = 4;
    private int countDataMsg;
    private int count_resume = 0;
    private int count_weight = 0;
    private int contPressTara = 0;
    private long weight = 0L;
    private int countInResume = 0;
    private int countMessage = 0;
    private boolean oneBack = true;
    private final com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener mBluetoothListener = null;
    
    public RemoteMixerFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.basculasmagris.visorremotomixer.databinding.FragmentRemoteMixerBinding getMBinding() {
        return null;
    }
    
    public final void setMBinding(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.databinding.FragmentRemoteMixerBinding p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final android.view.Menu getMenu() {
        return null;
    }
    
    public final void setMenu(@org.jetbrains.annotations.Nullable
    android.view.Menu p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter getRoundRunProductAdapter() {
        return null;
    }
    
    public final void setRoundRunProductAdapter(@org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter getRoundRunCorralAdapter() {
        return null;
    }
    
    public final void setRoundRunCorralAdapter(@org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter p0) {
    }
    
    @java.lang.Override
    public void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadRoundDetail() {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    private final void permission() {
    }
    
    public final int getCountInResume() {
        return 0;
    }
    
    public final void setCountInResume(int p0) {
    }
    
    @java.lang.Override
    public void onResume() {
    }
    
    public final int getCountMessage() {
        return 0;
    }
    
    public final void setCountMessage(int p0) {
    }
    
    public final boolean getOneBack() {
        return false;
    }
    
    public final void setOneBack(boolean p0) {
    }
    
    private final void refreshWeight(byte[] message) {
    }
    
    private final void refreshRound() {
    }
    
    private final void refreshDataRound() {
    }
    
    @java.lang.Override
    public void onDestroyView() {
    }
    
    private final void cleanAll() {
    }
    
    private final void alertFinalDialog() {
    }
    
    private final android.app.AlertDialog restDialog() {
        return null;
    }
    
    public final void setMixer(@org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.model.entities.Mixer mixerIn) {
    }
    
    private final android.app.AlertDialog dialogAlertTargetWeight(java.lang.String nextItem, java.lang.String strKg) {
        return null;
    }
    
    public final void sendNextProduct() {
    }
    
    public final void sendNextCorral() {
    }
    
    public final void setTabletMixer(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixerIn) {
    }
    
    private final android.app.AlertDialog tareDialog(java.lang.String title, java.lang.String message, float fontSize, int gravity) {
        return null;
    }
    
    public final boolean isInFree() {
        return false;
    }
}