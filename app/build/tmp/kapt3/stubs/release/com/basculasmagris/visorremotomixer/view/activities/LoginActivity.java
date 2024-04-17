package com.basculasmagris.visorremotomixer.view.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010 \u001a\u00020!H\u0002J&\u0010\"\u001a\u00020!2\u0006\u0010#\u001a\u00020\u00042\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\r0%2\u0006\u0010&\u001a\u00020\u0004H\u0002J\b\u0010\'\u001a\u00020!H\u0002J\u0012\u0010(\u001a\u00020!2\b\u0010)\u001a\u0004\u0018\u00010*H\u0014J\b\u0010+\u001a\u00020!H\u0014J\u0016\u0010,\u001a\u00020!2\u0006\u0010-\u001a\u00020\r2\u0006\u0010&\u001a\u00020\u0004J\b\u0010.\u001a\u00020!H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u001a\u001a\u0010\u0012\f\u0012\n \u001d*\u0004\u0018\u00010\u001c0\u001c0\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006/"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/activities/LoginActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "TAG", "", "alertDialog", "Landroid/app/AlertDialog;", "binding", "Lcom/basculasmagris/visorremotomixer/databinding/ActivityLoginBinding;", "mCustomListDialog", "Landroid/app/Dialog;", "mUserList", "Ljava/util/ArrayList;", "Lcom/basculasmagris/visorremotomixer/view/adapter/CustomListItem;", "mUserViewModel", "Lcom/basculasmagris/visorremotomixer/viewmodel/UserViewModel;", "getMUserViewModel", "()Lcom/basculasmagris/visorremotomixer/viewmodel/UserViewModel;", "mUserViewModel$delegate", "Lkotlin/Lazy;", "mUsersList", "originUserList", "Lcom/basculasmagris/visorremotomixer/model/entities/User;", "selectedUser", "sharedpreferences", "Landroid/content/SharedPreferences;", "someActivityResultLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "kotlin.jvm.PlatformType", "userApiService", "Lcom/basculasmagris/visorremotomixer/model/network/UserApiService;", "connectionOnline", "", "customItemsLDialog", "title", "itemsList", "", "selection", "hideUserList", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "selectedListItem", "item", "showUserList", "app_release"})
public final class LoginActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.basculasmagris.visorremotomixer.databinding.ActivityLoginBinding binding;
    private android.app.AlertDialog alertDialog;
    private final com.basculasmagris.visorremotomixer.model.network.UserApiService userApiService = null;
    private final java.lang.String TAG = "DEBLogin";
    private android.content.SharedPreferences sharedpreferences;
    private android.app.Dialog mCustomListDialog;
    private final java.util.ArrayList<com.basculasmagris.visorremotomixer.view.adapter.CustomListItem> mUsersList = null;
    private com.basculasmagris.visorremotomixer.view.adapter.CustomListItem selectedUser;
    private final kotlin.Lazy mUserViewModel$delegate = null;
    private final java.util.ArrayList<com.basculasmagris.visorremotomixer.view.adapter.CustomListItem> mUserList = null;
    private final java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.User> originUserList = null;
    private final androidx.activity.result.ActivityResultLauncher<android.content.Intent> someActivityResultLauncher = null;
    
    public LoginActivity() {
        super();
    }
    
    private final com.basculasmagris.visorremotomixer.viewmodel.UserViewModel getMUserViewModel() {
        return null;
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void hideUserList() {
    }
    
    private final void showUserList() {
    }
    
    private final void connectionOnline() {
    }
    
    private final void customItemsLDialog(java.lang.String title, java.util.List<com.basculasmagris.visorremotomixer.view.adapter.CustomListItem> itemsList, java.lang.String selection) {
    }
    
    public final void selectedListItem(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.view.adapter.CustomListItem item, @org.jetbrains.annotations.NotNull
    java.lang.String selection) {
    }
    
    @java.lang.Override
    protected void onDestroy() {
    }
}