package com.basculasmagris.visorremotomixer.view.fragments
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.basculasmagris.visorremotomixer.databinding.FragmentConfirmDialogBinding

class ConfirmDialogFragment(
    var title: String,
    var message: String,
    private var iconWarning: Drawable,
    var source: Int
) : DialogFragment() {
    private lateinit var confirmListener: OnConfirmListener
    private var _binding: FragmentConfirmDialogBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val ACCEPT = 0x00
        const val CANCEL = 0x01
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentConfirmDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()


        binding.tvConfirmTitle.text = title
        binding.tvConfirmMsg.text = message
        binding.ivIconConfirm.background = iconWarning

        binding.btnConfirmAcept.setOnClickListener {
            confirmListener.onConfirm(ACCEPT,source)
            dismiss()
        }
        binding.btnConfirmCancel.setOnClickListener {
            confirmListener.onConfirm(CANCEL,source)
            dismiss()
        }

        return dialog

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        } else {
            throw RuntimeException(context.toString() + "Debe implementar OnSiNoListener")
        }
        if (context is OnConfirmListener) {
            confirmListener = context
        } else {
            throw RuntimeException(context.toString() + "Debe implementar OnSiNoListener ")
        }
    }

    //Interface para crear el evento on complete dialog
    interface OnConfirmListener {
        fun onConfirm(yesNo: Int, source: Int)
    }

}