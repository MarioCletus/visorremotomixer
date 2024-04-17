package com.basculasmagris.visorremotomixer.view.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0002J\b\u0010\u0018\u001a\u00020\u0017H\u0002J\u0006\u0010\u0019\u001a\u00020\u001aJ\b\u0010\u001b\u001a\u00020\u0017H\u0016J\u0012\u0010\u001c\u001a\u00020\u00172\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0014J\b\u0010\u001f\u001a\u00020\u0017H\u0014J\u0006\u0010 \u001a\u00020\u0017J\b\u0010!\u001a\u00020\u0017H\u0002J\b\u0010\"\u001a\u00020\u0017H\u0002J\u0006\u0010#\u001a\u00020\u0017J\n\u0010$\u001a\u00020\u0017*\u00020%J\n\u0010&\u001a\u00020\u0017*\u00020%R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0010\u001a\u00020\u00118BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0014\u0010\u0015\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\'"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/activities/AddUpdateTabletMixerActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/basculasmagris/visorremotomixer/databinding/ActivityAddUpdateTabletMixerBinding;", "connection", "Landroid/content/ServiceConnection;", "mBluetoothListener", "Lcom/basculasmagris/visorremotomixer/view/interfaces/IBluetoothSDKListener;", "mNewTabletMixerDetails", "Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixer;", "mProgressDialog", "Landroid/app/Dialog;", "mService", "Lcom/basculasmagris/visorremotomixer/services/BluetoothSDKService;", "mTabletMixerDetails", "mTabletMixerViewModel", "Lcom/basculasmagris/visorremotomixer/viewmodel/TabletMixerViewModel;", "getMTabletMixerViewModel", "()Lcom/basculasmagris/visorremotomixer/viewmodel/TabletMixerViewModel;", "mTabletMixerViewModel$delegate", "Lkotlin/Lazy;", "bindBluetoothService", "", "hideCustomProgressDialog", "isKeyBoardShowing", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "saveMremoteViewer", "setupActionBar", "showCustomProgressDialog", "toggleKeyboard", "hideSoftKeyboard", "Landroid/app/Activity;", "showKeyboard", "app_debug"})
public final class AddUpdateTabletMixerActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateTabletMixerBinding binding;
    private com.basculasmagris.visorremotomixer.model.entities.TabletMixer mTabletMixerDetails;
    private com.basculasmagris.visorremotomixer.model.entities.TabletMixer mNewTabletMixerDetails;
    private com.basculasmagris.visorremotomixer.services.BluetoothSDKService mService;
    private final kotlin.Lazy mTabletMixerViewModel$delegate = null;
    private android.app.Dialog mProgressDialog;
    private final android.content.ServiceConnection connection = null;
    private final com.basculasmagris.visorremotomixer.view.interfaces.IBluetoothSDKListener mBluetoothListener = null;
    
    public AddUpdateTabletMixerActivity() {
        super();
    }
    
    private final com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModel getMTabletMixerViewModel() {
        return null;
    }
    
    private final void showCustomProgressDialog() {
    }
    
    private final void hideCustomProgressDialog() {
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupActionBar() {
    }
    
    @java.lang.Override
    public void onBackPressed() {
    }
    
    public final void saveMremoteViewer() {
    }
    
    @java.lang.Override
    protected void onDestroy() {
    }
    
    private final void bindBluetoothService() {
    }
    
    public final void toggleKeyboard() {
    }
    
    public final void hideSoftKeyboard(@org.jetbrains.annotations.NotNull
    android.app.Activity $this$hideSoftKeyboard) {
    }
    
    public final void showKeyboard(@org.jetbrains.annotations.NotNull
    android.app.Activity $this$showKeyboard) {
    }
    
    public final boolean isKeyBoardShowing() {
        return false;
    }
}