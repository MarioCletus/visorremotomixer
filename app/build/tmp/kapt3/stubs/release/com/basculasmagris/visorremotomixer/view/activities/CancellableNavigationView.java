package com.basculasmagris.visorremotomixer.view.activities;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0012\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\bH\u0016J\u0010\u0010\f\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\bR\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/activities/CancellableNavigationView;", "Lcom/google/android/material/navigation/NavigationView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "cancellableListener", "Lcom/google/android/material/navigation/NavigationView$OnNavigationItemSelectedListener;", "setNavigationItemSelectedListener", "", "listener", "setNavigationItemSelectedPreviewListener", "app_release"})
public final class CancellableNavigationView extends com.google.android.material.navigation.NavigationView {
    private com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener cancellableListener;
    
    public CancellableNavigationView(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    @java.lang.Override
    public void setNavigationItemSelectedListener(@org.jetbrains.annotations.Nullable
    com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener listener) {
    }
    
    public final void setNavigationItemSelectedPreviewListener(@org.jetbrains.annotations.Nullable
    com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener listener) {
    }
}