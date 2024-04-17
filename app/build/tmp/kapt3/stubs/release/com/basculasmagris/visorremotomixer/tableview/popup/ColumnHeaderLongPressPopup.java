package com.basculasmagris.visorremotomixer.tableview.popup;

import java.lang.System;

/**
 * Created by evrencoskun on 24.12.2017.
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00122\u00020\u00012\u00020\u0002:\u0001\u0012B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\b\u0010\n\u001a\u00020\u000bH\u0002J\b\u0010\f\u001a\u00020\u000bH\u0002J\b\u0010\r\u001a\u00020\u000bH\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/basculasmagris/visorremotomixer/tableview/popup/ColumnHeaderLongPressPopup;", "Landroid/widget/PopupMenu;", "Landroid/widget/PopupMenu$OnMenuItemClickListener;", "viewHolder", "Lcom/basculasmagris/visorremotomixer/tableview/holder/ColumnHeaderViewHolder;", "mTableView", "Lcom/evrencoskun/tableview/TableView;", "(Lcom/basculasmagris/visorremotomixer/tableview/holder/ColumnHeaderViewHolder;Lcom/evrencoskun/tableview/TableView;)V", "mXPosition", "", "changeMenuItemVisibility", "", "createMenuItem", "initialize", "onMenuItemClick", "", "menuItem", "Landroid/view/MenuItem;", "Companion", "app_release"})
public final class ColumnHeaderLongPressPopup extends android.widget.PopupMenu implements android.widget.PopupMenu.OnMenuItemClickListener {
    private final com.evrencoskun.tableview.TableView mTableView = null;
    private final int mXPosition = 0;
    @org.jetbrains.annotations.NotNull
    public static final com.basculasmagris.visorremotomixer.tableview.popup.ColumnHeaderLongPressPopup.Companion Companion = null;
    private static final int ASCENDING = 1;
    private static final int DESCENDING = 2;
    private static final int HIDE_ROW = 3;
    private static final int SHOW_ROW = 4;
    private static final int SCROLL_ROW = 5;
    
    public ColumnHeaderLongPressPopup(@org.jetbrains.annotations.NotNull
    com.basculasmagris.visorremotomixer.tableview.holder.ColumnHeaderViewHolder viewHolder, @org.jetbrains.annotations.NotNull
    com.evrencoskun.tableview.TableView mTableView) {
        super(null, null);
    }
    
    private final void initialize() {
    }
    
    private final void createMenuItem() {
    }
    
    private final void changeMenuItemVisibility() {
    }
    
    @java.lang.Override
    public boolean onMenuItemClick(@org.jetbrains.annotations.NotNull
    android.view.MenuItem menuItem) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/basculasmagris/visorremotomixer/tableview/popup/ColumnHeaderLongPressPopup$Companion;", "", "()V", "ASCENDING", "", "DESCENDING", "HIDE_ROW", "SCROLL_ROW", "SHOW_ROW", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}