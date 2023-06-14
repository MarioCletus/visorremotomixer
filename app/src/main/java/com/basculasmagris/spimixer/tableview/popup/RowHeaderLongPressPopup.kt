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
package com.basculasmagris.visorremotomixer.tableview.popup

import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.tableview.popup.RowHeaderLongPressPopup
import com.basculasmagris.visorremotomixer.R
import com.evrencoskun.tableview.TableView

/**
 * Created by evrencoskun on 21.01.2018.
 */
class RowHeaderLongPressPopup(
    viewHolder: RecyclerView.ViewHolder,
    private val mTableView: TableView
) : PopupMenu(viewHolder.itemView.context, viewHolder.itemView), PopupMenu.OnMenuItemClickListener {
    private val mRowPosition: Int

    init {
        mRowPosition = viewHolder.adapterPosition
        initialize()
    }

    private fun initialize() {
        createMenuItem()
        setOnMenuItemClickListener(this)
    }

    private fun createMenuItem() {
        val context = mTableView.context
        this.menu.add(
            Menu.NONE,
            SCROLL_COLUMN,
            0,
            context.getString(R.string.scroll_to_column_position)
        )
        this.menu.add(
            Menu.NONE,
            SHOWHIDE_COLUMN,
            1,
            context.getString(R.string.show_hide_the_column)
        )
        this.menu.add(Menu.NONE, REMOVE_ROW, 2, "Remove $mRowPosition position")
        // add new one ...
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        // Note: item id is index of menu item..
        when (menuItem.itemId) {
            SCROLL_COLUMN -> mTableView.scrollToColumnPosition(15)
            SHOWHIDE_COLUMN -> {
                val column = 1
                if (mTableView.isColumnVisible(column)) {
                    mTableView.hideColumn(column)
                } else {
                    mTableView.showColumn(column)
                }
            }
            REMOVE_ROW -> mTableView.adapter!!.removeRow(mRowPosition)
        }
        return true
    }

    companion object {
        // Menu Item constants
        private const val SCROLL_COLUMN = 1
        private const val SHOWHIDE_COLUMN = 2
        private const val REMOVE_ROW = 3
    }
}