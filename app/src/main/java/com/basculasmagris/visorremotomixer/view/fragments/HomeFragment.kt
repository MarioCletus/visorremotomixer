package com.basculasmagris.visorremotomixer.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerVRApplication
import com.basculasmagris.visorremotomixer.databinding.FragmentHomeBinding
import com.basculasmagris.visorremotomixer.model.entities.TabletMixer
import com.basculasmagris.visorremotomixer.utils.Constants
import com.basculasmagris.visorremotomixer.utils.CustomAlertDialogBuilder
import com.basculasmagris.visorremotomixer.utils.Helper
import com.basculasmagris.visorremotomixer.view.activities.MainActivity
import com.basculasmagris.visorremotomixer.view.adapter.TabletMixerHomeAdapter
import com.basculasmagris.visorremotomixer.viewmodel.TabletMixerViewModelFactory
import com.basculasmagris.visorremotomixer.viewmodel.TabletViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla de inicio: lista de tablets principales. El visor no está conectado acá (la tablet
 * principal atiende un solo visor a la vez); se conecta al elegir una tablet y pasar a la lista
 * de rondas. Por eso este fragment no escucha Bluetooth.
 */
class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding
    private val TAG = "DEBHome"

    private val mTabletMixerViewModel: TabletViewModel by viewModels {
        TabletMixerViewModelFactory((requireActivity().application as SpiMixerVRApplication).tabletMixerRepository)
    }

    private var tabletMixerHomeAdapter: TabletMixerHomeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_remote_mixer_fragment, menu)
                val activity = requireActivity() as MainActivity
                if(isAdded){
                    activity.clearbShowDevice()
                }
                activity.supportActionBar?.let {
                    val user = Helper.getCurrentUser(activity)
                    val title = "${getString(R.string.inicio)} - ${user.name} ${user.lastname}"
                    it.title = title

                }
                menu.findItem(R.id.cancel_round).isVisible = false
                menu.findItem(R.id.privacy_policy).isVisible = true
                // En el inicio el visor no está conectado (se conecta al elegir una tablet), así que
                // los íconos de conexión y de tablet seleccionada no aportan. En pantallas grandes
                // además se encimaban con el logo.
                menu.findItem(R.id.menu_selected_remote_tablet)?.isVisible = false
                menu.findItem(R.id.bluetooth_remote_status)?.isVisible = false
                menu.findItem(R.id.bluetooth_balance)?.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.privacy_policy -> {
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(Constants.URL_POLITICA_DE_PRIVACIDAD)
                        )
                        startActivity(browserIntent)
                        return true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        mBinding = FragmentHomeBinding.inflate(inflater, container, false)

        mBinding.btnAdmin.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToAdmin())
        }

        mBinding.btnToTablet.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToMixer())
        }

        mBinding.btnSync.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeToSync())
        }

        // TEMPORAL: prueba de Crashlytics. El reporte llega a la consola al volver a abrir la app.
        mBinding.btnTestCrash.setOnLongClickListener {
            throw RuntimeException("Crash de prueba de Crashlytics (visor remoto)")
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeTabletMixers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.rvTabletMixerHomeAdapter.adapter = null
    }

    private fun observeTabletMixers() {
        mTabletMixerViewModel.allTabletMixerList.observe(viewLifecycleOwner) { tabletMixers ->
            tabletMixerHomeAdapter?.updateList(tabletMixers)
            updateGridLayout(tabletMixers.size)
        }
    }

    /**
     * Ajusta columnas según la cantidad de tablets:
     *  - ≤4 → 1 columna, altura fija grande
     *  - >4 → 2 columnas, altura fija compacta
     * Siempre altura fija — sin cálculos proporcionales al RV.
     */
    private fun updateGridLayout(count: Int) {
        val rv      = mBinding.rvTabletMixerHomeAdapter
        val density = resources.displayMetrics.density

        val columns      = if (count <= 4) 1 else 2
        val itemHeightDp = 90 //if (count <= 3) 120 else 90
        val itemHeightPx = (itemHeightDp * density).toInt()

        val current = rv.layoutManager as? GridLayoutManager
        if (current == null || current.spanCount != columns) {
            rv.layoutManager = GridLayoutManager(requireContext(), columns)
        }

        tabletMixerHomeAdapter?.setItemHeight(itemHeightPx)
        tabletMixerHomeAdapter?.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        tabletMixerHomeAdapter = TabletMixerHomeAdapter(
            initialList    = emptyList(),
            onItemPressed  = { onTabletMixerPressed(it) },
            onOrderChanged = { reordered ->
                mTabletMixerViewModel.updateAll(reordered)
            }
        )

        mBinding.rvTabletMixerHomeAdapter.apply {
            // 2 columnas para mostrar hasta 8 tablets cómodamente
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = tabletMixerHomeAdapter
        }

        val dragCallback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) = makeMovementFlags(
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,  // drag en grid
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT   // swipe para eliminar
            )

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                tabletMixerHomeAdapter?.onItemMoved(
                    viewHolder.adapterPosition,
                    target.adapterPosition
                )
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // Restaurar visualmente primero para que el item vuelva a su lugar
                // mientras el diálogo está abierto.
                tabletMixerHomeAdapter?.notifyItemChanged(position)
                val tablet = tabletMixerHomeAdapter?.getItemAt(position) ?: return
                showDeleteConfirmation(tablet)
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                tabletMixerHomeAdapter?.onDragEnded()
            }

            // Long-press inicia el drag
            override fun isLongPressDragEnabled() = true
        }

        ItemTouchHelper(dragCallback).attachToRecyclerView(mBinding.rvTabletMixerHomeAdapter)
    }

    private fun showDeleteConfirmation(tabletMixer: TabletMixer) {
        if (!isAdded) return
        val builder = CustomAlertDialogBuilder(requireActivity())
        builder.setTitle(getString(R.string.eliminar))
        builder.setMessage(getString(R.string.msg_delete_tablet_dialog, tabletMixer.name))
        builder.setPositiveButton(getString(R.string.lbl_yes)) { dialog, _ ->
            mTabletMixerViewModel.delete(tabletMixer)
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.lbl_no)) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun onTabletMixerPressed(tabletMixer: TabletMixer) {
        Log.i(TAG, "TabletMixer presionado: ${tabletMixer.id} - ${tabletMixer.name} - ${tabletMixer.btName}")

        val activity = requireActivity() as MainActivity
        // Solo por la MAC elegida en este visor (ver MainActivity.resolveBluetoothDevice).
        val device = activity.resolveBluetoothDevice(tabletMixer)
        val resolvedTablet = tabletMixer

        viewLifecycleOwner.lifecycleScope.launch {
            if (device == null) {
                // Todavía no se eligió el bluetooth de esta tablet en este visor: se elige en la
                // configuración de la tablet. No se intenta conectar: no hay a qué.
                Log.w(TAG, "⚠️ '${tabletMixer.name}' sin bluetooth elegido — navegando a TabletConfigActivity para asignarlo")
                Toast.makeText(
                    requireContext(),
                    getString(R.string.seleccione_el_bluetooth_de_latablet, tabletMixer.name),
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToTabletConfig(tabletMixer)
                )
                return@launch
            }

            activity.changeTablet_SUSPEND(resolvedTablet)
            activity.saveTabletMixerAndWait(resolvedTablet)

            // Limpiar la lista de rondas al cambiar de tablet para que
            // RoundListFragment no muestre rondas de una sesión anterior.
            // Se poblarán de nuevo al conectar y recibir CMD_ROUNDS.
            activity.listOfMedRoundsRun.clear()

            Log.i(TAG, "TabletMixer '${resolvedTablet.name}' mac='${resolvedTablet.mac}' btName='${resolvedTablet.btName}' → navegando a RoundList")
            findNavController().navigate(
                HomeFragmentDirections.actionHomeToRound()
            )
        }
    }

    override fun onResume() {
        Log.i(TAG,"onResume")
        // La tablet principal atiende un solo visor a la vez: en el inicio este visor se
        // desconecta para dejarla libre. Se vuelve a conectar al elegir una tablet.
        (requireActivity() as MainActivity).disconnectFromTablet()
        super.onResume()
    }
}
