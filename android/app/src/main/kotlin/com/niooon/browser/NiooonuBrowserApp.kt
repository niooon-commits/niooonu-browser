package com.niooon.browser

import android.app.Application
import com.niooon.browser.model.DomainBlockManager

class NiooonuBrowserApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DomainBlockManager.init(this)
    }
}
