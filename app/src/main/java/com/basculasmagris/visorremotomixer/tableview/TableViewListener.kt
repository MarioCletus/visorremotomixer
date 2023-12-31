/*
 * MIT License
 *
 * Copyright (c) 2021 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.basculasmagris.visorremotomixer.tableview

import android.content.Context
import com.evrencoskun.tableview.listener.ITableViewListener
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.tableview.holder.ColumnHeaderViewHolder
import com.basculasmagris.visorremotomixer.tableview.popup.ColumnHeaderLongPressPopup
import com.basculasmagris.visorremotomixer.tableview.popup.RowHeaderLongPressPopup
import android.widget.Toast
import com.evrencoskun.tableview.TableView

/**
 * Created by evrencoskun on 21/09/2017.
 */
class TableViewListener(tableView: TableView) : ITableViewListener {
    private val mContext: Context
    private val mTableView: TableView

    init {
        mContext = tableView.context
        mTableView = tableView
    }

    /**
     * Called when user click any cell item.
     *
     * @param cellView : Clicked Cell ViewHolder.
     * @param column   : X (Column) position of Clicked Cell item.
     * @param row      : Y (Row) position of Clicked Cell item.
     */
    override fun onCellClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {

        // Do what you want.
        //showToast("Cell $column $row has been clicked.")
    }

    /**
     * Called when user double click any cell item.
     *
     * @param cellView : Clicked Cell ViewHolder.
     * @param column   : X (Column) position of Clicked Cell item.
     * @param row      : Y (Row) position of Clicked Cell item.
     */
    override fun onCellDoubleClicked(cellView: RecyclerView.ViewHolder, column: Int, row: Int) {
        // Do what you want.
        //showToast("Cell $column $row has been double clicked.")
    }

    /**
     * Called when user long press any cell item.
     *
     * @param cellView : Long Pressed Cell ViewHolder.
     * @param column   : X (Column) position of Long Pressed Cell item.
     * @param row      : Y (Row) position of Long Pressed Cell item.
     */
    override fun onCellLongPressed(
        cellView: RecyclerView.ViewHolder, column: Int,
        row: Int
    ) {

        // Do What you want
        //showToast("Cell $column $row has been long pressed.")
    }

    /**
     * Called when user click any column header item.
     *
     * @param columnHeaderView : Clicked Column Header ViewHolder.
     * @param column           : X (Column) position of Clicked Column Header item.
     */
    override fun onColumnHeaderClicked(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
        // Do what you want.
        //showToast("Column header  $column has been clicked.")
    }

    /**
     * Called when user double click any column header item.
     *
     * @param columnHeaderView : Clicked Column Header ViewHolder.
     * @param column           : X (Column) position of Clicked Column Header item.
     */
    override fun onColumnHeaderDoubleClicked(
        columnHeaderView: RecyclerView.ViewHolder,
        column: Int
    ) {
        // Do what you want.
        //showToast("Column header  $column has been double clicked.")
    }

    /**
     * Called when user long press any column header item.
     *
     * @param columnHeaderView : Long Pressed Column Header ViewHolder.
     * @param column           : X (Column) position of Long Pressed Column Header item.
     */
    override fun onColumnHeaderLongPressed(columnHeaderView: RecyclerView.ViewHolder, column: Int) {
        if (columnHeaderView is ColumnHeaderViewHolder) {
            // Create Long Press Popup
            val popup = ColumnHeaderLongPressPopup(
                columnHeaderView, mTableView
            )
            // Show
            popup.show()
        }
    }

    /**
     * Called when user click any Row Header item.
     *
     * @param rowHeaderView : Clicked Row Header ViewHolder.
     * @param row           : Y (Row) position of Clicked Row Header item.
     */
    override fun onRowHeaderClicked(rowHeaderView: RecyclerView.ViewHolder, row: Int) {
        // Do whatever you want.
        //showToast("Row header $row has been clicked.")
    }

    /**
     * Called when user double click any Row Header item.
     *
     * @param rowHeaderView : Clicked Row Header ViewHolder.
     * @param row           : Y (Row) position of Clicked Row Header item.
     */
    override fun onRowHeaderDoubleClicked(rowHeaderView: RecyclerView.ViewHolder, row: Int) {
        // Do whatever you want.
        //showToast("Row header $row has been double clicked.")
    }

    /**
     * Called when user long press any row header item.
     *
     * @param rowHeaderView : Long Pressed Row Header ViewHolder.
     * @param row           : Y (Row) position of Long Pressed Row Header item.
     */
    override fun onRowHeaderLongPressed(rowHeaderView: RecyclerView.ViewHolder, row: Int) {

        // Create Long Press Popup
        val popup = RowHeaderLongPressPopup(rowHeaderView, mTableView)
        // Show
        //popup.show()
    }

    private fun showToast(p_strMessage: String) {
        //Toast.makeText(mContext, p_strMessage, Toast.LENGTH_SHORT).show()
    }
}