package com.basculasmagris.visorremotomixer.view.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001+B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0018\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u001aH\u0016J\u0018\u0010\u001e\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u001aH\u0016J\u0010\u0010\"\u001a\u00020\u00142\u0006\u0010#\u001a\u00020\tH\u0002J\u000e\u0010$\u001a\u00020\u00142\u0006\u0010\u0011\u001a\u00020\u0012J\u0016\u0010%\u001a\u00020\u00142\u0006\u0010&\u001a\u00020\t2\u0006\u0010\'\u001a\u00020(J\u0014\u0010)\u001a\u00020\u00142\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\t0\bR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\tX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006,"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/TabletMixerAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/basculasmagris/visorremotomixer/view/adapter/TabletMixerAdapter$ViewHolder;", "Landroid/widget/Filterable;", "fragment", "Landroidx/fragment/app/Fragment;", "(Landroidx/fragment/app/Fragment;)V", "filteredTabletMixers", "", "Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixer;", "previousTabletMixer", "selectedTabletMixer", "getSelectedTabletMixer", "()Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixer;", "setSelectedTabletMixer", "(Lcom/basculasmagris/visorremotomixer/model/entities/TabletMixer;)V", "tabletMixers", "weight", "", "alertTabletMixerNull", "", "msg", "", "getFilter", "Landroid/widget/Filter;", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "selectTabletMixer", "mixer", "setWeight", "statusConnexion", "tabletMixer", "connected", "", "tabletMixerList", "list", "ViewHolder", "app_debug"})
public final class TabletMixerAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.basculasmagris.visorremotomixer.view.adapter.TabletMixerAdapter.ViewHolder> implements android.widget.Filterable {
    private final androidx.fragment.app.Fragment fragment = null;
    private java.util.List<com.basculasmagris.visorremotomixer.model.entities.TabletMixer> tabletMixers;
    private java.util.List<com.basculasmagris.visorremotomixer.model.entities.TabletMixer> filteredTabletMixers;
    private long weight = 0L;
    @org.jetbrains.annotations.Nullable
    private com.basculasmagris.visorremotomixer.model.entities.TabletMixer selectedTabletMixer;
    private com.basculasmagris.visorremotomixer.model.entities.TabletMixer previousTabletMixer;
    
    public TabletMixerAdapter(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.Fragment fragment) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.basculasmagris.visorremotomixer.model.entities.TabletMixer getSelectedTabletMixer() {
        return null;
    }
    
    public final void setSelectedTabletMixer(@org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.basculasmagris.visorremotomixer.view.adapter.TabletMixerAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.view.adapter.TabletMixerAdapter.ViewHolder holder, int position) {
    }
    
    private final void selectTabletMixer(com.basculasmagris.visorremotomixer.model.entities.TabletMixer mixer) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    public final void tabletMixerList(@org.jetbrains.annotations.NotNull
    java.util.List<com.basculasmagris.visorremotomixer.model.entities.TabletMixer> list) {
    }
    
    public final void statusConnexion(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.TabletMixer tabletMixer, boolean connected) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.widget.Filter getFilter() {
        return null;
    }
    
    public final void setWeight(long weight) {
    }
    
    private final void alertTabletMixerNull(java.lang.String msg) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0012\u00a8\u0006\u0019"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/TabletMixerAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Lcom/basculasmagris/visorremotomixer/databinding/ItemTabletMixerLayoutBinding;", "(Lcom/basculasmagris/visorremotomixer/databinding/ItemTabletMixerLayoutBinding;)V", "btnDeleteTabletMixer", "Landroid/widget/Button;", "getBtnDeleteTabletMixer", "()Landroid/widget/Button;", "btnEditTabletMixer", "getBtnEditTabletMixer", "btnSelect", "Landroid/widget/ImageButton;", "getBtnSelect", "()Landroid/widget/ImageButton;", "etTabletMixerDescription", "Landroid/widget/TextView;", "getEtTabletMixerDescription", "()Landroid/widget/TextView;", "mixerCard", "Lcom/google/android/material/card/MaterialCardView;", "getMixerCard", "()Lcom/google/android/material/card/MaterialCardView;", "tvTabletMixerTitle", "getTvTabletMixerTitle", "app_debug"})
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvTabletMixerTitle = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.Button btnEditTabletMixer = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.Button btnDeleteTabletMixer = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView etTabletMixerDescription = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.ImageButton btnSelect = null;
        @org.jetbrains.annotations.NotNull
        private final com.google.android.material.card.MaterialCardView mixerCard = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        com.basculasmagris.visorremotomixer.databinding.ItemTabletMixerLayoutBinding view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvTabletMixerTitle() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.Button getBtnEditTabletMixer() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.Button getBtnDeleteTabletMixer() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getEtTabletMixerDescription() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.ImageButton getBtnSelect() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.google.android.material.card.MaterialCardView getMixerCard() {
            return null;
        }
    }
}