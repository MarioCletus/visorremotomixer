package com.basculasmagris.visorremotomixer.tableview.holder;

import java.lang.System;

/**
 * Created by evrencoskun on 23/10/2017.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0010\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0014\u001a\u00020\u00102\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/basculasmagris/visorremotomixer/tableview/holder/ColumnHeaderViewHolder;", "Lcom/evrencoskun/tableview/adapter/recyclerview/holder/AbstractSorterViewHolder;", "itemView", "Landroid/view/View;", "tableView", "Lcom/evrencoskun/tableview/ITableView;", "(Landroid/view/View;Lcom/evrencoskun/tableview/ITableView;)V", "column_header_container", "Landroid/widget/LinearLayout;", "column_header_sortButton", "Landroid/widget/ImageButton;", "column_header_textview", "Landroid/widget/TextView;", "mSortButtonClickListener", "Landroid/view/View$OnClickListener;", "controlSortState", "", "sortState", "Lcom/evrencoskun/tableview/sort/SortState;", "onSortingStatusChanged", "setColumnHeader", "columnHeader", "Lcom/basculasmagris/visorremotomixer/tableview/model/ColumnHeader;", "Companion", "app_release"})
public final class ColumnHeaderViewHolder extends com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder {
    private final com.evrencoskun.tableview.ITableView tableView = null;
    private final android.widget.LinearLayout column_header_container = null;
    private final android.widget.TextView column_header_textview = null;
    private final android.widget.ImageButton column_header_sortButton = null;
    private final android.view.View.OnClickListener mSortButtonClickListener = null;
    @org.jetbrains.annotations.NotNull
    public static final com.basculasmagris.visorremotomixer.tableview.holder.ColumnHeaderViewHolder.Companion Companion = null;
    private static final java.lang.String LOG_TAG = null;
    
    public ColumnHeaderViewHolder(@org.jetbrains.annotations.NotNull
    android.view.View itemView, @org.jetbrains.annotations.Nullable
    com.evrencoskun.tableview.ITableView tableView) {
        super(null);
    }
    
    /**
     * This method is calling from onBindColumnHeaderHolder on TableViewAdapter
     */
    public final void setColumnHeader(@org.jetbrains.annotations.Nullable
    com.basculasmagris.visorremotomixer.tableview.model.ColumnHeader columnHeader) {
    }
    
    @java.lang.Override
    public void onSortingStatusChanged(@org.jetbrains.annotations.NotNull
    com.evrencoskun.tableview.sort.SortState sortState) {
    }
    
    private final void controlSortState(com.evrencoskun.tableview.sort.SortState sortState) {
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/basculasmagris/visorremotomixer/tableview/holder/ColumnHeaderViewHolder$Companion;", "", "()V", "LOG_TAG", "", "kotlin.jvm.PlatformType", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}