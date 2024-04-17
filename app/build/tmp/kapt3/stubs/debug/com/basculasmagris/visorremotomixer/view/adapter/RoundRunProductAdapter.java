package com.basculasmagris.visorremotomixer.view.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001\'B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u000eH\u0016J\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u000b0\nJ\u0018\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00022\u0006\u0010\u001a\u001a\u00020\u000eH\u0016J\u0018\u0010\u001b\u001a\u00020\u00022\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u000eH\u0016J\u001e\u0010\u001f\u001a\u00020\u00182\u0016\u0010 \u001a\u0012\u0012\u0004\u0012\u00020\u000b0!j\b\u0012\u0004\u0012\u00020\u000b`\"J\u000e\u0010#\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u000eJ\u000e\u0010$\u001a\u00020\u00182\u0006\u0010%\u001a\u00020&R\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u00a8\u0006("}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunProductAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunProductAdapter$ViewHolder;", "Landroid/widget/Filterable;", "fragment", "Landroidx/fragment/app/Fragment;", "(Landroidx/fragment/app/Fragment;)V", "TAG", "", "filteredProducts", "", "Lcom/basculasmagris/visorremotomixer/model/entities/MinProductDetail;", "products", "selectedPosition", "", "getSelectedPosition", "()I", "setSelectedPosition", "(I)V", "getFilter", "Landroid/widget/Filter;", "getItemCount", "getItems", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "productList", "list", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "selectProduct", "updateRound", "roundRunDetail", "Lcom/basculasmagris/visorremotomixer/model/entities/MinRoundRunDetail;", "ViewHolder", "app_debug"})
public final class RoundRunProductAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter.ViewHolder> implements android.widget.Filterable {
    private final androidx.fragment.app.Fragment fragment = null;
    private final java.lang.String TAG = "DEBRRA";
    private java.util.List<com.basculasmagris.visorremotomixer.model.entities.MinProductDetail> products;
    private java.util.List<com.basculasmagris.visorremotomixer.model.entities.MinProductDetail> filteredProducts;
    private int selectedPosition = 0;
    
    public RoundRunProductAdapter(@org.jetbrains.annotations.NotNull
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
    public com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    public final void productList(@org.jetbrains.annotations.NotNull
    java.util.ArrayList<com.basculasmagris.visorremotomixer.model.entities.MinProductDetail> list) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.widget.Filter getFilter() {
        return null;
    }
    
    public final void selectProduct(int position) {
    }
    
    public final void updateRound(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.MinRoundRunDetail roundRunDetail) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.basculasmagris.visorremotomixer.model.entities.MinProductDetail> getItems() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\f\u00a8\u0006\u0011"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunProductAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Lcom/basculasmagris/visorremotomixer/databinding/ItemLineRoundRunProductStepLoadBinding;", "(Lcom/basculasmagris/visorremotomixer/databinding/ItemLineRoundRunProductStepLoadBinding;)V", "llBarra1", "Landroid/widget/LinearLayout;", "getLlBarra1", "()Landroid/widget/LinearLayout;", "tvCurrentWeight", "Landroid/widget/TextView;", "getTvCurrentWeight", "()Landroid/widget/TextView;", "tvDiffWeight", "getTvDiffWeight", "tvProductName", "getTvProductName", "app_debug"})
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvProductName = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvCurrentWeight = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvDiffWeight = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.LinearLayout llBarra1 = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunProductStepLoadBinding view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvProductName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvCurrentWeight() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvDiffWeight() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.LinearLayout getLlBarra1() {
            return null;
        }
    }
}