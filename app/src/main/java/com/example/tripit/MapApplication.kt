package com.example.tripit

import android.app.Application
import com.mappls.sdk.maps.Mappls
import com.mappls.sdk.services.account.MapplsAccountManager

class MapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapplsAccountManager.getInstance().setRestAPIKey(restAPIKey)
        MapplsAccountManager.getInstance().setMapSDKKey(mapSDKKey)
        MapplsAccountManager.getInstance().setAtlasClientId(atlasClientId)
        MapplsAccountManager.getInstance().setAtlasClientSecret(atlasClientSecret)
        Mappls.getInstance(this)
    }

    private val atlasClientId: String
        private get() = "33OkryzDZsLALlzrrV7urjMNezZlPPX_m8hVIf7BjWGnPNo80opSLaBTTYyR15dKXApf7wco0fARYlrJ6NjrnA=="
    private val atlasClientSecret: String
        private get() = "lrFxI-iSEg_QxP7kjBWw2z9uUkCoSub3N3R6sTkRba2YMxolnTZxT_JC2OHd0_Yt1-D7wufEu2EHdxnAD8_MWAtS9DHVcBKZ"
    private val mapSDKKey: String
        private get() = "ced1b2f5797366ff5dd8f36048e116ad"
    private val restAPIKey: String
        private get() = "ced1b2f5797366ff5dd8f36048e116ad"
}