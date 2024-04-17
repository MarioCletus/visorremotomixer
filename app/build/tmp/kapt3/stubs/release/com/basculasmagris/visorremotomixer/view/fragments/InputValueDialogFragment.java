package com.basculasmagris.visorremotomixer.view.fragments;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 32\u00020\u0001:\u000234B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\tJ\b\u0010\u001f\u001a\u00020 H\u0002J\b\u0010!\u001a\u00020\"H\u0002J\b\u0010#\u001a\u00020 H\u0002J\u0010\u0010$\u001a\u00020 2\u0006\u0010%\u001a\u00020&H\u0016J\u0012\u0010\'\u001a\u00020 2\b\u0010(\u001a\u0004\u0018\u00010)H\u0016J\u0012\u0010*\u001a\u00020+2\b\u0010(\u001a\u0004\u0018\u00010)H\u0016J&\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010.\u001a\u00020/2\b\u00100\u001a\u0004\u0018\u0001012\b\u0010(\u001a\u0004\u0018\u00010)H\u0016J\b\u00102\u001a\u00020 H\u0002R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u00020\u000b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u0012\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0016R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001e\u00a8\u00065"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/fragments/InputValueDialogFragment;", "Landroidx/fragment/app/DialogFragment;", "value", "", "source", "", "title", "", "paso", "(DILjava/lang/String;Ljava/lang/Double;)V", "_binding", "Lcom/basculasmagris/visorremotomixer/databinding/FragmentValueInputDialogBinding;", "action", "Ljava/lang/Runnable;", "binding", "getBinding", "()Lcom/basculasmagris/visorremotomixer/databinding/FragmentValueInputDialogBinding;", "handler", "Landroid/os/Handler;", "inputValueListener", "Lcom/basculasmagris/visorremotomixer/view/fragments/InputValueDialogFragment$ValueInputListener;", "interval", "Ljava/lang/Double;", "getSource", "()I", "getTitle", "()Ljava/lang/String;", "setTitle", "(Ljava/lang/String;)V", "getValue", "()D", "decrement", "", "dialogAjusteFactor", "Landroid/app/AlertDialog;", "increment", "onAttach", "context", "Landroid/content/Context;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateDialog", "Landroid/app/Dialog;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "setListeners", "Companion", "ValueInputListener", "app_release"})
public final class InputValueDialogFragment extends androidx.fragment.app.DialogFragment {
    private final double value = 0.0;
    private final int source = 0;
    @org.jetbrains.annotations.Nullable
    private java.lang.String title;
    private final java.lang.Double paso = null;
    private com.basculasmagris.visorremotomixer.databinding.FragmentValueInputDialogBinding _binding;
    private com.basculasmagris.visorremotomixer.view.fragments.InputValueDialogFragment.ValueInputListener inputValueListener;
    private final android.os.Handler handler = null;
    private final int interval = 10000;
    private final java.lang.Runnable action = null;
    @org.jetbrains.annotations.NotNull
    public static final com.basculasmagris.visorremotomixer.view.fragments.InputValueDialogFragment.Companion Companion = null;
    private static double step = 1.0;
    
    public InputValueDialogFragment(double value, int source, @org.jetbrains.annotations.Nullable
    java.lang.String title, @org.jetbrains.annotations.Nullable
    java.lang.Double paso) {
        super();
    }
    
    public final double getValue() {
        return 0.0;
    }
    
    public final int getSource() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getTitle() {
        return null;
    }
    
    public final void setTitle(@org.jetbrains.annotations.Nullable
    java.lang.String p0) {
    }
    
    private final com.basculasmagris.visorremotomixer.databinding.FragmentValueInputDialogBinding getBinding() {
        return null;
    }
    
    @java.lang.Override
    public void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.app.Dialog onCreateDialog(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onAttach(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    private final android.app.AlertDialog dialogAjusteFactor() {
        return null;
    }
    
    private final void setListeners() {
    }
    
    private final void decrement() {
    }
    
    private final void increment() {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u001f\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/fragments/InputValueDialogFragment$ValueInputListener;", "", "inputValue", "", "value", "", "source", "", "(Ljava/lang/Double;I)V", "app_release"})
    public static abstract interface ValueInputListener {
        
        public abstract void inputValue(@org.jetbrains.annotations.Nullable
        java.lang.Double value, int source);
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/fragments/InputValueDialogFragment$Companion;", "", "()V", "step", "", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}