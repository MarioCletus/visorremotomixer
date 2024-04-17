package com.basculasmagris.visorremotomixer.view.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0012\u0010\u0012\u001a\u00020\u00112\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0014J\u0006\u0010\u0015\u001a\u00020\u0011J\b\u0010\u0016\u001a\u00020\u0011H\u0002J\u0006\u0010\u0017\u001a\u00020\u0011J\n\u0010\u0018\u001a\u00020\u0011*\u00020\u0019J\n\u0010\u001a\u001a\u00020\u0011*\u00020\u0019R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0007\u001a\u00020\b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\nR\u0010\u0010\r\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/activities/AddUpdateMixerActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/basculasmagris/visorremotomixer/databinding/ActivityAddUpdateMixerBinding;", "mMixerDetails", "Lcom/basculasmagris/visorremotomixer/model/entities/Mixer;", "mMixerViewModel", "Lcom/basculasmagris/visorremotomixer/viewmodel/MixerViewModel;", "getMMixerViewModel", "()Lcom/basculasmagris/visorremotomixer/viewmodel/MixerViewModel;", "mMixerViewModel$delegate", "Lkotlin/Lazy;", "mNewMixerDetails", "isKeyBoardShowing", "", "onBackPressed", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "saveMmixer", "setupActionBar", "toggleKeyboard", "hideSoftKeyboard", "Landroid/app/Activity;", "showKeyboard", "app_release"})
public final class AddUpdateMixerActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateMixerBinding binding;
    private com.basculasmagris.visorremotomixer.model.entities.Mixer mMixerDetails;
    private com.basculasmagris.visorremotomixer.model.entities.Mixer mNewMixerDetails;
    private final kotlin.Lazy mMixerViewModel$delegate = null;
    
    public AddUpdateMixerActivity() {
        super();
    }
    
    private final com.basculasmagris.visorremotomixer.viewmodel.MixerViewModel getMMixerViewModel() {
        return null;
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
    
    public final void saveMmixer() {
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