package com.basculasmagris.visorremotomixer.view.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001$B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u0013\u001a\u00020\u00142\u0016\u0010\u0015\u001a\u0012\u0012\u0004\u0012\u00020\t0\bj\b\u0012\u0004\u0012\u00020\t`\nJ\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u000eH\u0016J\u0018\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u00022\u0006\u0010\u001b\u001a\u00020\u000eH\u0016J\u0018\u0010\u001c\u001a\u00020\u00022\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u000eH\u0016J\u000e\u0010 \u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u000eJ\u000e\u0010!\u001a\u00020\u00142\u0006\u0010\"\u001a\u00020#R\u001e\u0010\u0007\u001a\u0012\u0012\u0004\u0012\u00020\t0\bj\b\u0012\u0004\u0012\u00020\t`\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u000b\u001a\u0012\u0012\u0004\u0012\u00020\t0\bj\b\u0012\u0004\u0012\u00020\t`\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\f\u001a\u0012\u0012\u0004\u0012\u00020\t0\bj\b\u0012\u0004\u0012\u00020\t`\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006%"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunCorralDownloadAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunCorralDownloadAdapter$ViewHolder;", "Landroid/widget/Filterable;", "fragment", "Landroidx/fragment/app/Fragment;", "(Landroidx/fragment/app/Fragment;)V", "filteredRoundCorrals", "Ljava/util/ArrayList;", "Lcom/basculasmagris/visorremotomixer/model/entities/MinCorralDetail;", "Lkotlin/collections/ArrayList;", "originalCorrals", "roundCorrals", "selectedPosition", "", "getSelectedPosition", "()I", "setSelectedPosition", "(I)V", "corralList", "", "list", "getFilter", "Landroid/widget/Filter;", "getItemCount", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "selectCorral", "updateRound", "roundRunDetail", "Lcom/basculasmagris/visorremotomixer/model/entities/MinRoundRunDetail;", "ViewHolder", "app_debug"})
public final class RoundRunCorralDownloadAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter.ViewHolder> implements android.widget.Filterable {
    private final androidx.fragment.app.Fragment fragment = null;
    private java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail> originalCorrals;
    private java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail> roundCorrals;
    private java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail> filteredRoundCorrals;
    private int selectedPosition = 0;
    
    public RoundRunCorralDownloadAdapter(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.Fragment fragment) {
        super();
    }
    
    public final int getSelectedPosition() {
        return 0;
    }
    
    public final void setSelectedPosition(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralDownloadAdapter.ViewHolder holder, int position) {
    }
    
    public final void selectCorral(int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    public final void corralList(@org.jetbrains.annotations.NotNull
    java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail> list) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.widget.Filter getFilter() {
        return null;
    }
    
    public final void updateRound(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail roundRunDetail) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\f\u00a8\u0006\u0011"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunCorralDownloadAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Lcom/basculasmagris/visorremotomixer/databinding/ItemLineRoundRunCorralDownloadBinding;", "(Lcom/basculasmagris/visorremotomixer/databinding/ItemLineRoundRunCorralDownloadBinding;)V", "llBarra1", "Landroid/widget/LinearLayout;", "getLlBarra1", "()Landroid/widget/LinearLayout;", "tvCorralName", "Landroid/widget/TextView;", "getTvCorralName", "()Landroid/widget/TextView;", "tvCurrentWeight", "getTvCurrentWeight", "tvDiffWeight", "getTvDiffWeight", "app_debug"})
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvCorralName = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvDiffWeight = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvCurrentWeight = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.LinearLayout llBarra1 = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunCorralDownloadBinding view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvCorralName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvDiffWeight() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvCurrentWeight() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.LinearLayout getLlBarra1() {
            return null;
        }
    }
}