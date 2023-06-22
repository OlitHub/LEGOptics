package com.example.temp.ar

import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

class VisuArFragment : ArFragment(){
    override fun getSessionConfiguration(session: Session?): Config {
        val conf = Config(session)
        conf.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
        conf.focusMode = Config.FocusMode.AUTO
        session!!.configure(conf)
        this.arSceneView.setupSession(session)

        (activity as ArVisuActivity).setupImgDatabase(conf, session)
        return conf
    }


}