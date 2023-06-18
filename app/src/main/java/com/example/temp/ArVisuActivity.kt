package com.example.temp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.Anchor
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.AnimationData
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment


class ArVisuActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment
    private var anchorNode: AnchorNode? = null
    private var isRotating = false
    private var modelMap = mutableMapOf<String, ModelRenderable>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.visu_activity)


        //On recup le fragment et ajoute la détection de tape sur le plan
        arFragment = supportFragmentManager.findFragmentById(R.id.arFrag) as ArFragment
        arFragment.setOnTapArPlaneListener { hitResult: HitResult, _: Plane?, _: MotionEvent? ->
            placeModel(hitResult.createAnchor())
        }
        initRenderable("poulet")



    }

    private fun initRenderable(filename: String) {
        ModelRenderable.builder().setSource(
            this, RenderableSource.builder().setSource(
                this, Uri.parse("models/$filename.glb"), RenderableSource.SourceType.GLB
            ).setScale(0.01f).setRecenterMode(RenderableSource.RecenterMode.ROOT).build()
        ).setRegistryId("poulet.glb").build().thenAccept { renderable ->
            //Si modele correctement chargé, on l'ajoute à la scene
            modelMap[filename] = renderable
        }.exceptionally { throwable ->
            //Si echec, on print l'erreur en console
            Log.d("DBG_MSG", "Error : " + throwable.message)
            //return@exceptionally null
            return@exceptionally null
        }
    }

    // Ajout de l'ancre à la scene et suppression des précédentes si besoin
    private fun placeModel(anchor: Anchor) {
        var modelRenderable: ModelRenderable? = modelMap["poulet"]
        if (anchorNode != null) {
            arFragment.arSceneView.scene.removeChild(anchorNode)
        }
        anchorNode = AnchorNode(anchor)

        anchorNode!!.renderable = modelRenderable
        arFragment.arSceneView.scene.addChild(anchorNode)

        return
    }


}