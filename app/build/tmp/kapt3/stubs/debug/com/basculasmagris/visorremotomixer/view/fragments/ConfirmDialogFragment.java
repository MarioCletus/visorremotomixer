package com.basculasmagris.visorremotomixer.view.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 \'2\u00020\u0001:\u0002\'(B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0012\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\b\u0010#\u001a\u00020\u001cH\u0016J\u001a\u0010$\u001a\u00020\u001c2\u0006\u0010%\u001a\u00020&2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\u000b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0012\"\u0004\b\u001a\u0010\u0014\u00a8\u0006)"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/fragments/ConfirmDialogFragment;", "Landroidx/fragment/app/DialogFragment;", "title", "", "message", "iconWarning", "Landroid/graphics/drawable/Drawable;", "source", "", "(Ljava/lang/String;Ljava/lang/String;Landroid/graphics/drawable/Drawable;I)V", "_binding", "Lcom/basculasmagris/visorremotomixer/databinding/FragmentConfirmDialogBinding;", "binding", "getBinding", "()Lcom/basculasmagris/visorremotomixer/databinding/FragmentConfirmDialogBinding;", "confirmListener", "Lcom/basculasmagris/visorremotomixer/view/fragments/ConfirmDialogFragment$OnConfirmListener;", "getMessage", "()Ljava/lang/String;", "setMessage", "(Ljava/lang/String;)V", "getSource", "()I", "setSource", "(I)V", "getTitle", "setTitle", "onAttach", "", "context", "Landroid/content/Context;", "onCreateDialog", "Landroid/app/Dialog;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "Landroid/view/View;", "Companion", "OnConfirmListener", "app_debug"})
public final class ConfirmDialogFragment extends androidx.fragment.app.DialogFragment {
    @org.jetbrains.annotations.NotNull
    private java.lang.String title;
    @org.jetbrains.annotations.NotNull
    private java.lang.String message;
    private android.graphics.drawable.Drawable iconWarning;
    private int source;
    private com.basculasmagris.visorremotomixer.view.fragments.ConfirmDialogFragment.OnConfirmListener confirmListener;
    private com.basculasmagris.visorremotomixer.databinding.FragmentConfirmDialogBinding _binding;
    @org.jetbrains.annotations.NotNull
    public static final com.basculasmagris.visorremotomixer.view.fragments.ConfirmDialogFragment.Companion Companion = null;
    public static final int ACCEPT = 0;
    public static final int CANCEL = 1;
    
    public ConfirmDialogFragment(@org.jetbrains.annotations.NotNull
    java.lang.String title, @org.jetbrains.annotations.NotNull
    java.lang.String message, @org.jetbrains.annotations.NotNull
    android.graphics.drawable.Drawable iconWarning, int source) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTitle() {
        return null;
    }
    
    public final void setTitle(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getMessage() {
        return null;
    }
    
    public final void setMessage(@org.jetbrains.annotations.NotNull
    java.lang.String p0) {
    }
    
    public final int getSource() {
        return 0;
    }
    
    public final void setSource(int p0) {
    }
    
    private final com.basculasmagris.visorremotomixer.databinding.FragmentConfirmDialogBinding getBinding() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.app.Dialog onCreateDialog(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onDestroyView() {
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    public void onAttach(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&\u00a8\u0006\u0007"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/fragments/ConfirmDialogFragment$OnConfirmListener;", "", "onConfirm", "", "yesNo", "", "source", "app_debug"})
    public static abstract interface OnConfirmListener {
        
        public abstract void onConfirm(int yesNo, int source);
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/fragments/ConfirmDialogFragment$Companion;", "", "()V", "ACCEPT", "", "CANCEL", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}