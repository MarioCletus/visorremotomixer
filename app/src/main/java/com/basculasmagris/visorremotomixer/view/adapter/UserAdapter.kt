package com.basculasmagris.visorremotomixer.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.ItemUserLayoutBinding
import com.basculasmagris.visorremotomixer.model.entities.User
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.view.activities.AddUpdateUserActivity
import com.basculasmagris.visorremotomixer.view.fragments.UserListFragment

class UserAdapter (private  val fragment: Fragment) : RecyclerView.Adapter<UserAdapter.ViewHolder>(),
    Filterable {

    private var users: List<User> = listOf()
    private var filteredUsers: List<User> = listOf()

    private var roles: ArrayList<Pair<Int, String>> = arrayListOf(
        Pair(1, "Administrador"),
        Pair(2, "Operario"),
        Pair(3, "Supervisor"),
        Pair(4, "Super Admin")
    )


    class ViewHolder (view: ItemUserLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val tvUserTitle = view.tvUserTitle
        val tvUserDescription = view.tvUserDescription
        val ibMore = view.ibMore
        val btnDeleteUser = view.btnDeleteUser
        val btnEditUser = view.btnEditUser
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemUserLayoutBinding = ItemUserLayoutBinding.inflate(
            LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.tvUserTitle.text = user.name + ' ' + user.lastname
        holder.tvUserDescription.text = roles.first { role -> role.first == user.codeRole}.second + "\n" + user.mail

//        holder.ibMore.setOnClickListener{
//            val popup =  PopupMenu(fragment.context, holder.ibMore)
//            popup.menuInflater.inflate(R.menu.menu_adapter_user, popup.menu)
//
//            // Si el usero está sincronizado no se permite el borrado.
//            if (user.remoteId != 0L) {
//                popup.menu.getItem(1).isVisible = false
//            }
//
//            popup.setOnMenuItemClickListener {
//                if (it.itemId  == R.id.action_edit_user){
//                    val intent = Intent(fragment.requireActivity(), AddUpdateUserActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
//                    fragment.requireActivity().startActivity(intent)
//                } else if (it.itemId == R.id.action_delete_user){
//                    if (fragment is UserListFragment) {
//                        fragment.deleteUser(user)
//                    }
//                }
//                true
//            }
//            popup.show()
//        }

//        holder.btnEditUser.setOnClickListener {
//            val intent = Intent(fragment.requireActivity(), AddUpdateUserActivity::class.java)
//            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
//            fragment.requireActivity().startActivity(intent)
//        }

        holder.btnDeleteUser.setOnClickListener {
            if (fragment is UserListFragment) {
                fragment.deleteUser(user)
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun userList(list: List<User>){
        users = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                filteredUsers = if (charString.isEmpty()) users else {
                    val filteredList = ArrayList<User>()
                    users
                        .filter {
                            (it.name.lowercase().contains(charString.lowercase())) or
                                    (it.lastname.lowercase().contains(charString.lowercase()))

                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                return FilterResults().apply { values = filteredUsers }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredUsers = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }
}