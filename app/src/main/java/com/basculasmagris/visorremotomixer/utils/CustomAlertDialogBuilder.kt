package com.basculasmagris.visorremotomixer.utils


import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import com.basculasmagris.visorremotomixer.R


class CustomAlertDialogBuilder(context: Context) : AlertDialog.Builder(context) {

    private var TAG:String = "DEBTest"
    var dialog :CustomAlertDialog? = null
    private var positiveButtonText: CharSequence? = null
    private var positiveButtonListener: DialogInterface.OnClickListener? = null

    private var negativeButtonText: CharSequence? = null
    private var negativeButtonListener: DialogInterface.OnClickListener? = null

    private var neutralButtonText: CharSequence? = null
    private var neutralButtonListener: DialogInterface.OnClickListener? = null

    private var isCancelableDialog: Boolean = true
    private var onCancelListenerDialog: DialogInterface.OnCancelListener? = null
    private var onDismissListenerDialog: DialogInterface.OnDismissListener? = null

    private var titleText: CharSequence? = null
    private var messageText: CharSequence? = null


    override fun setPositiveButton(
        text: CharSequence?,
        listener: DialogInterface.OnClickListener?
    ): CustomAlertDialogBuilder {
        Log.i(TAG,"setPositiveButton $text dialog $dialog")
        positiveButtonText = text
        positiveButtonListener = listener
        return this
    }

    override fun setNegativeButton(
        text: CharSequence?,
        listener: DialogInterface.OnClickListener?
    ): CustomAlertDialogBuilder {
        Log.i(TAG,"setNegativeButton $text dialog $dialog")
        negativeButtonText = text
        negativeButtonListener = listener
        return this
    }

    override fun setNeutralButton(
        text: CharSequence?,
        listener: DialogInterface.OnClickListener?
    ): CustomAlertDialogBuilder {
        Log.i(TAG,"setNeutralButton $text dialog $dialog")
        neutralButtonText = text
        neutralButtonListener = listener
        return this
    }

    override fun setItems(
        itemsId: Int,
        listener: DialogInterface.OnClickListener?
    ): AlertDialog.Builder {
        return super.setItems(itemsId, listener)
    }

    override fun setCancelable(cancelable: Boolean): CustomAlertDialogBuilder {
        isCancelableDialog = cancelable
        return this
    }

    override fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener?): CustomAlertDialogBuilder {
        onCancelListenerDialog = onCancelListener
        return this
    }

    override fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?): CustomAlertDialogBuilder {
        onDismissListenerDialog = onDismissListener
        return this
    }

    override fun setMessage(message: CharSequence?): AlertDialog.Builder {
        Log.i(TAG,"setTitle $message  dialog $dialog")
        dialog?.setMessage(message)
        messageText = message
        return super.setMessage(message)
    }

    override fun setTitle(title: CharSequence?): AlertDialog.Builder {
        Log.i(TAG,"setTitle $title dialog $dialog")
        titleText = title
        dialog?.setTitle(title)
        return super.setTitle(title)
    }

    override fun create(): CustomAlertDialog? {
        dialog = CustomAlertDialog(context)
        dialog?.setCancelable(isCancelableDialog)
        dialog?.setOnCancelListener(onCancelListenerDialog)
        dialog?.setOnDismissListener(onDismissListenerDialog)

        Log.i(TAG,"create \npositiveButtonText $positiveButtonText" +
                "\nnegativeButtonText $negativeButtonText" +
                "\nneutralButtonText $neutralButtonText" +
                "\ndialog $dialog")

        // Configurar botones del Builder
        positiveButtonText?.let {
            dialog?.setButton(DialogInterface.BUTTON_POSITIVE, it, positiveButtonListener)
        }
        negativeButtonText?.let {
            dialog?.setButton(DialogInterface.BUTTON_NEGATIVE, it, negativeButtonListener)
        }
        neutralButtonText?.let {
            dialog?.setButton(DialogInterface.BUTTON_NEUTRAL, it, neutralButtonListener)
        }

        titleText?.let { dialog?.setTitle(it) }
        messageText?.let { dialog?.setMessage(it) }
        dialog?.window?.decorView?.setBackgroundResource(R.drawable.custom_dialog_background)
        return dialog
    }


}
