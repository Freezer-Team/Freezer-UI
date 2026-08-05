package nep.timeline.freezer.provide

import android.os.RemoteException
import nep.timeline.freezer.binders.UIComponentData
import nep.timeline.freezer.binders.UIServiceInterface
import nep.timeline.freezerUI.binder.BinderService
import rikka.parcelablelist.ParcelableListSlice
import rikka.parcelablelist.StringListSlice

class UIServiceBinder : UIServiceInterface.Stub() {
    @Throws(RemoteException::class)
    override fun getScripts(): StringListSlice {
        throw UnsupportedOperationException()
    }

    @Throws(RemoteException::class)
    override fun getPackages(script: String): StringListSlice {
        throw UnsupportedOperationException()
    }

    @Throws(RemoteException::class)
    override fun getPages(script: String, packageName: String): StringListSlice {
        throw UnsupportedOperationException()
    }

    @Throws(RemoteException::class)
    override fun getComponents(script: String, packageName: String, page: String): ParcelableListSlice<UIComponentData?> {
        throw UnsupportedOperationException()
    }

    companion object {
        val instance: UIServiceInterface?
            get() {
                return asInterface(BinderService.getBinder("UIService"))
            }
    }
}