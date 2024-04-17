package com.basculasmagris.visorremotomixer.view.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0016B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0014\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\fJ\b\u0010\r\u001a\u00020\u000eH\u0016J\u0018\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u000eH\u0016J\u0018\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u000eH\u0016R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunCorralResumeAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunCorralResumeAdapter$ViewHolder;", "fragment", "Landroidx/fragment/app/Fragment;", "(Landroidx/fragment/app/Fragment;)V", "filteredCorrals", "", "Lcom/basculasmagris/visorremotomixer/model/entities/MinCorralDetail;", "corralList", "", "list", "", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ViewHolder", "app_release"})
public final class RoundRunCorralResumeAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralResumeAdapter.ViewHolder> {
    private final androidx.fragment.app.Fragment fragment = null;
    private java.util.List<com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail> filteredCorrals;
    
    public RoundRunCorralResumeAdapter(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.Fragment fragment) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralResumeAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.view.adapter.RoundRunCorralResumeAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    public final void corralList(@org.jetbrains.annotations.NotNull
    java.util.List<com.basculasmagris.visorremotomixer.model.entities.MinCorralDetail> list) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\fR\u0011\u0010\u0011\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\f\u00a8\u0006\u0013"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunCorralResumeAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Lcom/basculasmagris/visorremotomixer/databinding/ItemLineRoundRunCorralResumeBinding;", "(Lcom/basculasmagris/visorremotomixer/databinding/ItemLineRoundRunCorralResumeBinding;)V", "pbCurrentCorral", "Landroid/widget/ProgressBar;", "getPbCurrentCorral", "()Landroid/widget/ProgressBar;", "tvCorralDifference", "Landroid/widget/TextView;", "getTvCorralDifference", "()Landroid/widget/TextView;", "tvCorralName", "getTvCorralName", "tvCurrentCorralPercentage", "getTvCurrentCorralPercentage", "tvCurrentCorralWeightOf", "getTvCurrentCorralWeightOf", "app_release"})
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvCorralName = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvCorralDifference = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvCurrentCorralWeightOf = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvCurrentCorralPercentage = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.ProgressBar pbCurrentCorral = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunCorralResumeBinding view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvCorralName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvCorralDifference() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvCurrentCorralWeightOf() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvCurrentCorralPercentage() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.ProgressBar getPbCurrentCorral() {
            return null;
        }
    }
}