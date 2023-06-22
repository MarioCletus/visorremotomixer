package com.basculasmagris.visorremotomixer.view.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.basculasmagris.visorremotomixer.R

class SeleccionCalibracionDlgFragment : DialogFragment() {
    private var btnAceptar: Button? = null
    private var btnCancelar: Button? = null
    private var radioButton1: RadioButton? = null
    private var radioButton2: RadioButton? = null
    private var seleccionCalibracionListener: OnSeleccionCalibracionListener? = null
    private var activity: Activity? = null



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
        val alertDialog = dialogSeleccionCalibracion()
        alertDialog.setCanceledOnTouchOutside(false)
        return alertDialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            activity = context
            activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        } else {
            throw RuntimeException(context.toString() + "Debe implementar OnSeleccionCalibracionListener")
        }
        seleccionCalibracionListener = if (context is OnSeleccionCalibracionListener) {
            context
        } else {
            throw RuntimeException(context.toString() + "Debe implementar OnSeleccionCalibracionListener ")
        }
    }

    private fun dialogSeleccionCalibracion(): AlertDialog {
        val builder = AlertDialog.Builder(getActivity())
        val inflater = getActivity()!!.layoutInflater
        val view = inflater.inflate(R.layout.fragment_seleccion_calibracion, null)
        builder.setView(view)
        btnAceptar = view.findViewById(R.id.btnSeleccionCalibracionAceptar)
        btnCancelar = view.findViewById(R.id.btnSeleccionCalibracionCancelar)
        radioButton1 = view.findViewById(R.id.rbPesoConocido)
        radioButton2 = view.findViewById(R.id.rbOtraBalanza)
        listeners()
        return builder.create()
    }

    private fun listeners() {
        btnAceptar!!.setOnClickListener {
            if (radioButton1!!.isChecked) {
                seleccionCalibracionListener!!.onSeleccionCalibracion(1)
            }
            if (radioButton2!!.isChecked) {
                seleccionCalibracionListener!!.onSeleccionCalibracion(2)
            }
            dismiss()
        }
        btnCancelar!!.setOnClickListener { dismiss() }
    }

    //Interface para crear el evento on complete dialog
    interface OnSeleccionCalibracionListener {
        fun onSeleccionCalibracion(modo: Int)
    }
}
