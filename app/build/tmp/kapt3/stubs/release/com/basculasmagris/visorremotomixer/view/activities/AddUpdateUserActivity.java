package com.basculasmagris.visorremotomixer.view.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0080\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 @2\u00020\u0001:\u0001@B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010%\u001a\u00020&H\u0002J&\u0010\'\u001a\u00020&2\u0006\u0010(\u001a\u00020\b2\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\r0*2\u0006\u0010+\u001a\u00020\bH\u0002J\u0010\u0010,\u001a\u00020\b2\u0006\u0010-\u001a\u00020\nH\u0002J\u0006\u0010.\u001a\u00020/J\b\u00100\u001a\u00020&H\u0016J\u0012\u00101\u001a\u00020&2\b\u00102\u001a\u0004\u0018\u000103H\u0014J\u0010\u00104\u001a\u00020\b2\u0006\u00105\u001a\u000206H\u0002J\u0006\u00107\u001a\u00020&J\u0016\u00108\u001a\u00020&2\u0006\u00109\u001a\u00020\r2\u0006\u0010+\u001a\u00020\bJ\b\u0010:\u001a\u00020&H\u0002J\b\u0010;\u001a\u00020&H\u0002J\u0006\u0010<\u001a\u00020&J\n\u0010=\u001a\u00020&*\u00020>J\n\u0010?\u001a\u00020&*\u00020>R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000f\u001a\u00020\u00108BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0015\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R(\u0010\u0016\u001a\u0010\u0012\f\u0012\n \u0019*\u0004\u0018\u00010\u00180\u00180\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001dR(\u0010\u001e\u001a\u0010\u0012\f\u0012\n \u0019*\u0004\u0018\u00010\u00180\u00180\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u001b\"\u0004\b \u0010\u001dR \u0010!\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020#\u0012\u0004\u0012\u00020\b0\"0\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006A"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/activities/AddUpdateUserActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/basculasmagris/visorremotomixer/databinding/ActivityAddUpdateUserBinding;", "mCustomListDialog", "Landroid/app/Dialog;", "mImagePath", "", "mNewUserDetails", "Lcom/basculasmagris/visorremotomixer/model/entities/User;", "mRolesList", "Ljava/util/ArrayList;", "Lcom/basculasmagris/visorremotomixer/view/adapter/CustomListItem;", "mUserDetails", "mUserViewModel", "Lcom/basculasmagris/visorremotomixer/viewmodel/UserViewModel;", "getMUserViewModel", "()Lcom/basculasmagris/visorremotomixer/viewmodel/UserViewModel;", "mUserViewModel$delegate", "Lkotlin/Lazy;", "maskPassword", "resultCameraLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "kotlin.jvm.PlatformType", "getResultCameraLauncher", "()Landroidx/activity/result/ActivityResultLauncher;", "setResultCameraLauncher", "(Landroidx/activity/result/ActivityResultLauncher;)V", "resultGalleryLauncher", "getResultGalleryLauncher", "setResultGalleryLauncher", "roles", "Lkotlin/Pair;", "", "selectedRole", "customImageSelectionDialog", "", "customItemsLDialog", "title", "itemsList", "", "selection", "getUserName", "user", "isKeyBoardShowing", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "saveImageToInternalStorage", "bitmap", "Landroid/graphics/Bitmap;", "saveUser", "selectedListItem", "item", "setupActionBar", "showRelationalDialogForPermissions", "toggleKeyboard", "hideSoftKeyboard", "Landroid/app/Activity;", "showKeyboard", "Companion", "app_release"})
public final class AddUpdateUserActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.basculasmagris.visorremotomixer.databinding.ActivityAddUpdateUserBinding binding;
    private java.lang.String mImagePath = "";
    private final java.lang.String maskPassword = "********";
    private android.app.Dialog mCustomListDialog;
    private com.basculasmagris.visorremotomixer.model.entities.User mUserDetails;
    private com.basculasmagris.visorremotomixer.model.entities.User mNewUserDetails;
    private final kotlin.Lazy mUserViewModel$delegate = null;
    private java.util.ArrayList<kotlin.Pair<java.lang.Long, java.lang.String>> roles;
    private final java.util.ArrayList<com.basculasmagris.visorremotomixer.view.adapter.CustomListItem> mRolesList = null;
    private com.basculasmagris.visorremotomixer.view.adapter.CustomListItem selectedRole;
    @org.jetbrains.annotations.NotNull
    private androidx.activity.result.ActivityResultLauncher<android.content.Intent> resultCameraLauncher;
    @org.jetbrains.annotations.NotNull
    private androidx.activity.result.ActivityResultLauncher<android.content.Intent> resultGalleryLauncher;
    @org.jetbrains.annotations.NotNull
    public static final com.basculasmagris.visorremotomixer.view.activities.AddUpdateUserActivity.Companion Companion = null;
    private static final java.lang.String IMAGE_DIRECTORY = "SpiMixerImages";
    
    public AddUpdateUserActivity() {
        super();
    }
    
    private final com.basculasmagris.visorremotomixer.viewmodel.UserViewModel getMUserViewModel() {
        return null;
    }
    
    private final void customImageSelectionDialog() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.activity.result.ActivityResultLauncher<android.content.Intent> getResultCameraLauncher() {
        return null;
    }
    
    public final void setResultCameraLauncher(@org.jetbrains.annotations.NotNull
    androidx.activity.result.ActivityResultLauncher<android.content.Intent> p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.activity.result.ActivityResultLauncher<android.content.Intent> getResultGalleryLauncher() {
        return null;
    }
    
    public final void setResultGalleryLauncher(@org.jetbrains.annotations.NotNull
    androidx.activity.result.ActivityResultLauncher<android.content.Intent> p0) {
    }
    
    private final void showRelationalDialogForPermissions() {
    }
    
    private final java.lang.String saveImageToInternalStorage(android.graphics.Bitmap bitmap) {
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
    
    private final java.lang.String getUserName(com.basculasmagris.visorremotomixer.model.entities.User user) {
        return null;
    }
    
    public final void saveUser() {
    }
    
    public final void selectedListItem(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.view.adapter.CustomListItem item, @org.jetbrains.annotations.NotNull
    java.lang.String selection) {
    }
    
    private final void customItemsLDialog(java.lang.String title, java.util.List<com.basculasmagris.visorremotomixer.view.adapter.CustomListItem> itemsList, java.lang.String selection) {
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
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/activities/AddUpdateUserActivity$Companion;", "", "()V", "IMAGE_DIRECTORY", "", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}