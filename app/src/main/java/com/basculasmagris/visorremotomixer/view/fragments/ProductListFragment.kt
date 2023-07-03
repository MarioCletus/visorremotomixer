package com.basculasmagris.visorremotomixer.view.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.application.SpiMixerApplication
import com.basculasmagris.visorremotomixer.databinding.FragmentProductListBinding
import com.basculasmagris.visorremotomixer.model.entities.Product
import com.basculasmagris.visorremotomixer.view.activities.MergedLocalData
import com.basculasmagris.visorremotomixer.view.activities.ProductData
import com.basculasmagris.visorremotomixer.view.adapter.ProductAdapter
import com.basculasmagris.visorremotomixer.viewmodel.ProductViewModel
import com.basculasmagris.visorremotomixer.viewmodel.ProductViewModelFactory

class ProductListFragment : Fragment() {

    private lateinit var mBinding: FragmentProductListBinding

    private val mProductViewModel: ProductViewModel by viewModels {
        ProductViewModelFactory((requireActivity().application as SpiMixerApplication).productRepository)
    }
    private var mLocalProducts: List<Product>? = null

    private fun fetchLocalData(): MediatorLiveData<MergedLocalData> {
        val liveDataMerger = MediatorLiveData<MergedLocalData>()
        liveDataMerger.addSource(mProductViewModel.allProductList) {
            if (it != null) {
                liveDataMerger.value = ProductData(it)
            }
        }
        return liveDataMerger
    }
    private fun getLocalData(){
        // Sync local data
        val liveData = fetchLocalData()
        liveData.observe(viewLifecycleOwner, object : Observer<MergedLocalData> {
            override fun onChanged(it: MergedLocalData?) {
                when (it) {
                    is ProductData -> mLocalProducts = it.products
                    else -> {}
                }

                if (mLocalProducts != null) {
                    liveData.removeObserver(this)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentProductListBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Start Sync
        getLocalData()
        getLocalProduct()

        // Navigation Menu
//        val menuHost: MenuHost = requireActivity()
//        menuHost.addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                // Add menu items here
//                menuInflater.inflate(R.menu.menu_product_list, menu)
//
//                // Associate searchable configuration with the SearchView
//                val search = menu.findItem(R.id.search_product)
//                val searchView = search.actionView as SearchView
//                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                        (mBinding.rvProductsList.adapter as ProductAdapter).filter.filter(query)
//                        return false
//                    }
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                        (mBinding.rvProductsList.adapter as ProductAdapter).filter.filter(newText)
//                        return true
//                    }
//                })
//            }
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                // Handle the menu selection
//                return when (menuItem.itemId) {
//                    R.id.action_add_product -> {
//                        // clearCompletedTasks()
//                        //startActivity(Intent(requireActivity(), AddUpdateProductActivity::class.java))
////                        goToAddUpdateProduct()
//                        return true
//                    }
//                    else -> false
//                }
//            }
//        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    private fun getLocalProduct(){
        mBinding.rvProductsList.layoutManager = GridLayoutManager(requireActivity(), 3)
        val productAdapter =  ProductAdapter(this@ProductListFragment)
        mBinding.rvProductsList.adapter = productAdapter
        mProductViewModel.allProductList.observe(viewLifecycleOwner) {
                products ->
            products.let{
                if (it.isNotEmpty()){
                    mBinding.rvProductsList.visibility = View.VISIBLE
                    mBinding.tvNoData.visibility = View.GONE
                    productAdapter.productList(it.filter { product ->
                        product.archiveDate.isNullOrEmpty()
                    })
                } else {
                    mBinding.rvProductsList.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        }
    }

    fun goToProductDetails(product: Product){
        findNavController().navigate(ProductListFragmentDirections.actionProductFragmentToProductDetailFragment(
            product
        ))
    }

    fun goToAddUpdateProduct(){
        findNavController().navigate(ProductListFragmentDirections.actionNavProductToAddUpdateProductActivity())
    }

    fun deleteProduct(product: Product){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.title_delete_product))
        builder.setMessage(resources.getString(R.string.msg_delete_product_dialog, product.name))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)){ dialogInterface, _ ->
            mProductViewModel.delete(product)
            dialogInterface.dismiss()
        }

        builder.setNegativeButton(resources.getString(R.string.lbl_no)){ dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

    }
}