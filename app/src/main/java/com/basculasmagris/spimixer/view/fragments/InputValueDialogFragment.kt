package com.basculasmagris.visorremotomixer.view.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.DialogFragment
import com.basculasmagris.visorremotomixer.R
import com.basculasmagris.visorremotomixer.databinding.FragmentValueInputDialogBinding


class InputValueDialogFragment(val value: Double, val source: Int = 0, var title: String?=null, private val paso: Double? =null) : DialogFragment() {

    private var _binding: FragmentValueInputDialogBinding? = null
    private val binding get() = _binding!!

    private var inputValueListener: ValueInputListener? = null
    private val handler = Handler()
    private val interval = 10000

    private val action: Runnable = object : Runnable {
        override fun run() {
            if (binding.btnMinus.isPressed) {
                decrement()
                handler.postDelayed(this, interval.toLong())
            } else if (binding.btnPlus.isPressed) {
                increment()
                handler.postDelayed(this, interval.toLong())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(paso!=null){
            step = paso
        }
        if(title==null){
            title = getString(R.string.selecione_valor)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val param = dialog!!.window!!.attributes
        param.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        param.gravity = Gravity.CENTER
        dialog!!.window!!.attributes = param
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialog = dialogAjusteFactor()
        alertDialog.setCanceledOnTouchOutside(false)
        return alertDialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        } else {
            throw RuntimeException(context.toString() + "Debe implementar SeleccionValorListener")
        }
        if (context is ValueInputListener) {
            inputValueListener =
                context
        } else {
            throw RuntimeException(context.toString() + "Debe implementar SeleccionValorListener ")
        }
    }

    private fun dialogAjusteFactor(): AlertDialog {
        _binding = FragmentValueInputDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .create()

        binding.etValue.setText(value.toString())
        setListeners()
        return dialog
    }

    private fun setListeners() {
        binding.btnPlus.setOnClickListener { increment() }
        binding.btnPlus.setOnLongClickListener {
            handler.postDelayed(action, 0)
            false
        }
        binding.btnMinus.setOnClickListener { decrement() }
        binding.btnMinus.setOnLongClickListener {
            handler.postDelayed(action, 0)
            false
        }
        binding.btnValorAcept.setOnClickListener {
            val value1 = binding.etValue.text.toString().toDouble()
            inputValueListener?.inputValue(value1, source)
            dismiss()
        }
        binding.btnValorCancel.setOnClickListener { dismiss() }

        binding.titDlgSelValor.text = title!!
    }

    private fun decrement() {
        var value1 = if (binding.etValue.text.toString().compareTo("") == 0) 0.0 else binding.etValue.text.toString().toDouble()
        if (value1 <= step) return
        value1 -= step
        binding.etValue.setText(value1.toString())
    }

    private fun increment() {
        var value1 = (if (binding.etValue.text.toString().compareTo("") == 0) 0.0 else binding.etValue.text.toString().toDouble())
        value1+=step
        binding.etValue.setText(value1.toString())
    }

    // Interface para crear el evento on complete dialog
    interface ValueInputListener {
        fun inputValue(value: Double?, source: Int)
    }

    companion object {
        private var step : Double = 1.0
    }
}