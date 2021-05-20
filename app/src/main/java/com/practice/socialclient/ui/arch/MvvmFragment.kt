package com.practice.socialclient.ui.arch

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.practice.socialclient.model.logger.Log
import com.practice.socialclient.model.logger.Logger
import java.lang.reflect.ParameterizedType

abstract class MvvmFragment<
        Host : FragmentContract.Host?,
        VIEW_MODEL : FragmentContract.ViewModel
        >: Fragment() {
    private val logger: Log = Logger.withTag("MyLog")
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

    protected var model: VIEW_MODEL? = null
        private set

    //@Override
    fun hasCallBack(): Boolean {
        return callBack != null
    }

    fun noHost(): Boolean {
        return callBack == null
    }

    fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    fun showToast(resId: Int) {
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
                ?.actualTypeArguments?.get(1) as Class<*>).canonicalName
            throw RuntimeException(
                "Activity must implement " + hostClassName
                        + " to attach " + this.javaClass.simpleName, e
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setModel(createModel())
        if (model != null) {
            lifecycle.addObserver(model!!)

        }
    }

    override fun onDetach() {
        super.onDetach()
        // release the call back
        callBack = null
    }

    protected abstract fun createModel(): VIEW_MODEL

    protected fun setModel(model: VIEW_MODEL) {
        this.model = model
    }
}
