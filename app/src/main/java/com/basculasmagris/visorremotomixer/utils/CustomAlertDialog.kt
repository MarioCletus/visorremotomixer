package com.basculasmagris.visorremotomixer.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.basculasmagris.visorremotomixer.R


class CustomAlertDialog(context: Context, themeResId: Int = 0) : AlertDialog(context, themeResId) {

    private var positiveButton: Button? = null
    private var negativeButton: Button? = null
    private var cancelButton: Button? = null
    private var tvMessage: TextView? = null
    private var tvTitle: TextView? = null
    private var messageText = ""
    private var titleText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el diseño personalizado
        val customView = LayoutInflater.from(context).inflate(R.layout.custom_default_dialog_layout, null)
        setContentView(customView)

        // Vincular vistas
        tvTitle = findViewById(R.id.title)
        tvMessage = findViewById(R.id.message)
        positiveButton = findViewById(R.id.positiveButton)
        negativeButton = findViewById(R.id.negativeButton)
        cancelButton = findViewById(R.id.cancelButton)

        // Configurar acciones de los botones automáticamente
        positiveButton?.setOnClickListener {
            getButton(BUTTON_POSITIVE)?.performClick()
        }

        negativeButton?.setOnClickListener {
            getButton(BUTTON_NEGATIVE)?.performClick()
        }

        cancelButton?.setOnClickListener {
            getButton(BUTTON_NEUTRAL)?.performClick()
        }

        // Configurar textos iniciales si ya existen
        tvTitle?.text = titleText // Verifica si `titleText` está siendo seteado
        tvMessage?.text = messageText

        setButtonTexts()
    }

    private fun setButtonTexts() {
        // Obtener los textos configurados en el Builder
        getButton(BUTTON_POSITIVE)?.let {
            positiveButton?.text = it.text
            if(it.text.isNotEmpty()){
                positiveButton?.visibility = View.VISIBLE
            }
        }
        getButton(BUTTON_NEGATIVE)?.let {
            negativeButton?.text = it.text
            if(it.text.isNotEmpty()){
                negativeButton?.visibility = View.VISIBLE
            }
        }
        getButton(BUTTON_NEUTRAL)?.let {
            cancelButton?.text = it.text
            if(it.text.isNotEmpty()){
                cancelButton?.visibility = View.VISIBLE
            }
        }
    }

    override fun setButton(whichButton: Int, text: CharSequence?, listener: DialogInterface.OnClickListener?) {
        super.setButton(whichButton, text, listener)

        when (whichButton) {
            BUTTON_POSITIVE -> positiveButton?.text = text
            BUTTON_NEGATIVE -> negativeButton?.text = text
            BUTTON_NEUTRAL -> cancelButton?.text = text
        }
    }

    override fun setMessage(message: CharSequence?) {
        if (tvMessage != null) {
            tvMessage?.text = message
        } else {
            messageText = message.toString() // Guarda el mensaje para usarlo después
        }
    }

    override fun setTitle(title: CharSequence?) {
        if (tvTitle != null) {
            tvTitle?.text = title
        } else {
            titleText = title.toString() // Guarda el título para usarlo después
        }
    }


}
