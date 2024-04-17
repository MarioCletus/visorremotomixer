package com.basculasmagris.visorremotomixer.tableview.popup;

import java.lang.System;

/**
 * Created by evrencoskun on 21.01.2018.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00112\u00020\u00012\u00020\u0002:\u0001\u0011B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\b\u0010\n\u001a\u00020\u000bH\u0002J\b\u0010\f\u001a\u00020\u000bH\u0002J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/basculasmagris/visorremotomixer/tableview/popup/RowHeaderLongPressPopup;", "Landroid/widget/PopupMenu;", "Landroid/widget/PopupMenu$OnMenuItemClickListener;", "viewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "mTableView", "Lcom/evrencoskun/tableview/TableView;", "(Landroidx/recyclerview/widget/RecyclerView$ViewHolder;Lcom/evrencoskun/tableview/TableView;)V", "mRowPosition", "", "createMenuItem", "", "initialize", "onMenuItemClick", "", "menuItem", "Landroid/view/MenuItem;", "Companion", "app_release"})
public final class RowHeaderLongPressPopup extends android.widget.PopupMenu implements android.widget.PopupMenu.OnMenuItemClickListener {
    private final com.evrencoskun.tableview.TableView mTableView = null;
    private final int mRowPosition = 0;
    @org.jetbrains.annotations.NotNull
    public static final com.basculasmagris.visorremotomixer.tableview.popup.RowHeaderLongPressPopup.Companion Companion = null;
    private static final int SCROLL_COLUMN = 1;
    private static final int SHOWHIDE_COLUMN = 2;
    private static final int REMOVE_ROW = 3;
    
    public RowHeaderLongPressPopup(@org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, @org.jetbrains.annotations.NotNull
    com.evrencoskun.tableview.TableView mTableView) {
        super(null, null);
    }
    
    private final void initialize() {
    }
    
    private final void createMenuItem() {
    }
    
    @java.lang.Override
    public boolean onMenuItemClick(@org.jetbrains.annotations.NotNull
    android.view.MenuItem menuItem) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/basculasmagris/visorremotomixer/tableview/popup/RowHeaderLongPressPopup$Companion;", "", "()V", "REMOVE_ROW", "", "SCROLL_COLUMN", "SHOWHIDE_COLUMN", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}