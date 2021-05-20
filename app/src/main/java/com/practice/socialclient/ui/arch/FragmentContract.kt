package com.practice.socialclient.ui.arch

import androidx.lifecycle.LifecycleObserver

interface FragmentContract {
    interface Host
    interface ViewModel: LifecycleObserver
}
