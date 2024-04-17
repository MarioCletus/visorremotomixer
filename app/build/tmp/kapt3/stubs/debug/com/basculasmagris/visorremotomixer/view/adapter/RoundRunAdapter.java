package com.basculasmagris.visorremotomixer.view.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001#B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\rH\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\bH\u0016J\u0010\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\rH\u0002J\u0018\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\bH\u0016J\u0018\u0010\u001b\u001a\u00020\u00022\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\bH\u0016J\u0014\u0010\u001f\u001a\u00020\u00182\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\r0\fJ\u000e\u0010!\u001a\u00020\u00182\u0006\u0010\"\u001a\u00020\nR\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunAdapter$ViewHolder;", "Landroid/widget/Filterable;", "fragment", "Landroidx/fragment/app/Fragment;", "(Landroidx/fragment/app/Fragment;)V", "CANT_HORAS_MUESTRA_RONDA_FINALIZADA", "", "bRoundRunning", "", "filteredRoundsRun", "", "Lcom/basculasmagris/visorremotomixer/model/entities/MedRoundRunDetail;", "roundsRun", "getCurrentStatus", "", "roundToRun", "getFilter", "Landroid/widget/Filter;", "getItemCount", "getProgress", "roundRunDetail", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "roundList", "list", "sincroRound", "roundRunning", "ViewHolder", "app_debug"})
public final class RoundRunAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.basculasmagris.visorremotomixer.view.adapter.RoundRunAdapter.ViewHolder> implements android.widget.Filterable {
    private final androidx.fragment.app.Fragment fragment = null;
    private final int CANT_HORAS_MUESTRA_RONDA_FINALIZADA = 4;
    private java.util.List<com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail> roundsRun;
    private java.util.List<com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail> filteredRoundsRun;
    private boolean bRoundRunning = false;
    
    public RoundRunAdapter(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.Fragment fragment) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.basculasmagris.visorremotomixer.view.adapter.RoundRunAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    private final int getProgress(com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail roundRunDetail) {
        return 0;
    }
    
    private final java.lang.String getCurrentStatus(com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail roundToRun) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.view.adapter.RoundRunAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    public final void roundList(@org.jetbrains.annotations.NotNull
    java.util.List<com.basculasmagris.visorremotomixer.model.entities.MedRoundRunDetail> list) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.widget.Filter getFilter() {
        return null;
    }
    
    public final void sincroRound(boolean roundRunning) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u0017\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\u001b\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001aR\u0011\u0010\u001d\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001aR\u0011\u0010\u001f\u001a\u00020\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u001a\u00a8\u0006!"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Lcom/basculasmagris/visorremotomixer/databinding/ItemRoundToRunLayoutBinding;", "(Lcom/basculasmagris/visorremotomixer/databinding/ItemRoundToRunLayoutBinding;)V", "btnStartRound", "Landroid/widget/Button;", "getBtnStartRound", "()Landroid/widget/Button;", "btnStopRound", "getBtnStopRound", "llProgressBar", "Landroid/widget/LinearLayout;", "getLlProgressBar", "()Landroid/widget/LinearLayout;", "pbRoundRun", "Landroid/widget/ProgressBar;", "getPbRoundRun", "()Landroid/widget/ProgressBar;", "roundCard", "Lcom/google/android/material/card/MaterialCardView;", "getRoundCard", "()Lcom/google/android/material/card/MaterialCardView;", "tvRoundName", "Landroid/widget/TextView;", "getTvRoundName", "()Landroid/widget/TextView;", "tvRoundRunDescription", "getTvRoundRunDescription", "tvRoundRunPercentage", "getTvRoundRunPercentage", "tvRoundStartDate", "getTvRoundStartDate", "app_debug"})
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvRoundName = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvRoundRunDescription = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvRoundRunPercentage = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvRoundStartDate = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.Button btnStartRound = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.Button btnStopRound = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.ProgressBar pbRoundRun = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.LinearLayout llProgressBar = null;
        @org.jetbrains.annotations.NotNull
        private final com.google.android.material.card.MaterialCardView roundCard = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        com.basculasmagris.visorremotomixer.databinding.ItemRoundToRunLayoutBinding view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvRoundName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvRoundRunDescription() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvRoundRunPercentage() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvRoundStartDate() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.Button getBtnStartRound() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.Button getBtnStopRound() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.ProgressBar getPbRoundRun() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.LinearLayout getLlProgressBar() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.google.android.material.card.MaterialCardView getRoundCard() {
            return null;
        }
    }
}