package com.basculasmagris.visorremotomixer.view.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010!\n\u0002\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0018B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\b\u0010\u000b\u001a\u00020\fH\u0016J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00022\u0006\u0010\u0010\u001a\u00020\fH\u0016J\u0018\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\fH\u0016J\u0014\u0010\u0015\u001a\u00020\u000e2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\n0\u0017R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunProductReportAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunProductReportAdapter$ViewHolder;", "fragment", "Landroidx/fragment/app/Fragment;", "roundRunDetail", "Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunDetail;", "(Landroidx/fragment/app/Fragment;Lcom/basculasmagris/visorremotomixer/model/entities/RoundRunDetail;)V", "filteredProducts", "", "Lcom/basculasmagris/visorremotomixer/model/entities/ProductDetail;", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "productList", "list", "", "ViewHolder", "app_debug"})
public final class RoundRunProductReportAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductReportAdapter.ViewHolder> {
    private final androidx.fragment.app.Fragment fragment = null;
    private final com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail roundRunDetail = null;
    private java.util.List<com.basculasmagris.visorremotomixer.model.entities.ProductDetail> filteredProducts;
    
    public RoundRunProductReportAdapter(@org.jetbrains.annotations.NotNull
    androidx.fragment.app.Fragment fragment, @org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.model.entities.RoundRunDetail roundRunDetail) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductReportAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.view.adapter.RoundRunProductReportAdapter.ViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    public final void productList(@org.jetbrains.annotations.NotNull
    java.util.List<com.basculasmagris.visorremotomixer.model.entities.ProductDetail> list) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u000f\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\fR\u0011\u0010\u0011\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\f\u00a8\u0006\u0013"}, d2 = {"Lcom/basculasmagris/visorremotomixer/view/adapter/RoundRunProductReportAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Lcom/basculasmagris/visorremotomixer/databinding/ItemLineRoundRunProductReportBinding;", "(Lcom/basculasmagris/visorremotomixer/databinding/ItemLineRoundRunProductReportBinding;)V", "pbCurrentProduct", "Landroid/widget/ProgressBar;", "getPbCurrentProduct", "()Landroid/widget/ProgressBar;", "tvCurrentProductPercentage", "Landroid/widget/TextView;", "getTvCurrentProductPercentage", "()Landroid/widget/TextView;", "tvCurrentProductWeightOf", "getTvCurrentProductWeightOf", "tvProductDifference", "getTvProductDifference", "tvProductName", "getTvProductName", "app_debug"})
    public static final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvProductName = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvProductDifference = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvCurrentProductWeightOf = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView tvCurrentProductPercentage = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.ProgressBar pbCurrentProduct = null;
        
        public ViewHolder(@org.jetbrains.annotations.NotNull
        com.basculasmagris.visorremotomixer.databinding.ItemLineRoundRunProductReportBinding view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvProductName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvProductDifference() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvCurrentProductWeightOf() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTvCurrentProductPercentage() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.ProgressBar getPbCurrentProduct() {
            return null;
        }
    }
}