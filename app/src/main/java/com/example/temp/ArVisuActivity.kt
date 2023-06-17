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
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment

class ArVisuActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.visu_activity)




        //On recup le fragment et ajoute la détection de tape sur le plan
        arFragment = supportFragmentManager.findFragmentById(R.id.arFrag) as ArFragment
        arFragment.setOnTapArPlaneListener { hitResult: HitResult, _: Plane?, _: MotionEvent? ->
            placeModel(hitResult.createAnchor())
        }
    }

    // Importation du model lors d'une tape sur le plan
    private fun placeModel(anchor: Anchor) {
        ModelRenderable.builder().setSource(
            this, RenderableSource.builder().setSource(
                this, Uri.parse("models/poulet.glb"), RenderableSource.SourceType.GLB
            ).setScale(0.01f).setRecenterMode(RenderableSource.RecenterMode.ROOT).build()
        ).setRegistryId("poulet.glb").build().thenAccept { renderable ->
            //Si modele correctement chargé, on l'ajoute à la scene
            addModelToScene(renderable, anchor)
            Log.d("DBG_MSG", "RENDERABLE !!")
        }.exceptionally { throwable ->
            //Si echec, on print l'erreur en console
            Log.d("DBG_MSG", "Error : " + throwable.message)
            return@exceptionally null
        }
    }

    // Ajout de l'ancre à la scene
    private fun addModelToScene(modelRenderable : ModelRenderable, anchor: Anchor){
        val anchorNode = AnchorNode(anchor)
        anchorNode.renderable = modelRenderable
        arFragment.arSceneView.scene.addChild(anchorNode)
        return
    }
}