package com.practice.socialclient.ui.arch

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.practice.socialclient.model.logger.ILog
import com.practice.socialclient.model.logger.Logger
import com.practice.socialclient.ui.arch.Contract
import java.lang.reflect.ParameterizedType

open class MvvmFragment<Host : Contract.Host?> : Fragment() {
    private val logger: ILog = Logger.withTag("MyLog")
    /**
     * get the current fragment call back
     *
     * @return the current fragment call back
     */
    /**
     * the fragment callBack
     */
    var callBack: Host? = null
        private set

    //@Override
    fun hasCallBack(): Boolean {
        return callBack != null
    }

    fun noHost(): Boolean {
        return callBack == null
    }

    fun showToast(message: String){
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    fun showToast(resId: Int){
        Toast.makeText(activity, resId, Toast.LENGTH_LONG).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logger.log("onAttach " + this.javaClass.canonicalName)
        // keep the call back
        try {
            callBack = context as Host
        } catch (e: Throwable) {
            val hostClassName = ((javaClass.genericSuperclass as ParameterizedType?)
                ?.getActualTypeArguments()?.get(1) as Class<*>).canonicalName
            throw RuntimeException(
                "Activity must implement " + hostClassName
                        + " to attach " + this.javaClass.simpleName, e
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        // release the call back
        callBack = null
    }
}