package com.basculasmagris.visorremotomixer.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentProductDetailBinding
import com.basculasmagris.visorremotomixer.utils.Helper
import com.bumptech.glide.Glide

class ProductDetailFragment : Fragment() {

    private var mBinding: FragmentProductDetailBinding? = null
    private var mImagePath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return mBinding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: ProductDetailFragmentArgs by navArgs()
        Log.i("Product name", args.productDetail.name)

        args.productDetail.let { product ->
            mBinding?.let {
                mImagePath = product.image
                Glide.with(this@ProductDetailFragment)
                    .load(mImagePath)
                    .placeholder(R.mipmap.ic_launcher)
                    .centerCrop()
                    .into(it.ivProductAvatar)

                it.tvProductTitle.text = product.name
                it.tvProductDescription.text = product.description.ifEmpty { resources.getString(R.string.lbl_no_description) }
                it.tvProductSpecificWeight.text = Helper.getFormattedWeight(product.specificWeight, requireActivity())
                it.tvProductRefid.text = if (product.rfid > 0) product.rfid.toString() else resources.getString(R.string.lbl_empty)
                it.tvNoAdditionalData.text = resources.getString(R.string.tv_no_additional_data)

                if (product.remoteId > 0) {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.green_500_primary), android.graphics.PorterDuff.Mode.SRC_IN)
                    it.tvSyncInfo.text = resources.getString(R.string.sync)
                } else {
                    it.ivSync.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.red_500_primary), android.graphics.PorterDuff.Mode.SRC_IN)
                    it.tvSyncInfo.text = resources.getString(R.string.pending_sync)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}