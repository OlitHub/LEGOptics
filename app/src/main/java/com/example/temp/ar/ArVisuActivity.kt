package com.example.temp.ar


import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.temp.R
import com.google.ar.core.Anchor
import com.google.ar.core.AugmentedImage
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.core.Session
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Scene
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.TransformableNode


class ArVisuActivity : AppCompatActivity(), Scene.OnUpdateListener  {
    private var pouletIndex: Int = 0
    private lateinit var imageDatabase: AugmentedImageDatabase
    private var anchorNode: AnchorNode? = null
    private lateinit var rotatingNode: TransformableNode
    private var modelMap = mutableMapOf<String, ModelRenderable>()
    private var modelCreated : Boolean = false


    private lateinit var arFragment : VisuArFragment
    //private lateinit var arFragment: ArFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.visu_activity)

        //On recup le fragment et ajoute la détection de tape sur le plan
        arFragment = supportFragmentManager.findFragmentById(R.id.arFrag) as VisuArFragment
        arFragment.setOnTapArPlaneListener { hitResult: HitResult, _: Plane?, _: MotionEvent? ->
            placeModel(hitResult.createAnchor())
        }

        initRenderable("poulet")
        arFragment.arSceneView.scene.addOnUpdateListener (this)

    }

    private fun initRenderable(filename: String) {
        ModelRenderable.builder().setSource(
            this, RenderableSource.builder().setSource(
                this,
                Uri.parse("https://github.com/OlitHub/LEGOptics/raw/main/app/src/main/assets/models/poulet.glb"),
                RenderableSource.SourceType.GLB
            ).setScale(0.01f).setRecenterMode(RenderableSource.RecenterMode.ROOT).build()
        ).setRegistryId(filename).build().thenAccept { renderable ->
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
        val modelRenderable: ModelRenderable? = modelMap["poulet"]
        if (anchorNode != null) {
            arFragment.arSceneView.scene.removeChild(anchorNode)
        }
        anchorNode = AnchorNode(anchor)

        rotatingNode = TransformableNode(arFragment.transformationSystem)
        rotatingNode.setParent(anchorNode)
        rotatingNode.renderable = modelRenderable
        startRotationAnimation()
        arFragment.arSceneView.scene.addChild(anchorNode)

        return
    }

    private fun startRotationAnimation() {
        var totalDeltaAngleDegrees = 0f // Track the total rotation angle

        arFragment.arSceneView.scene.addOnUpdateListener { frameTime ->
            val deltaAngleDegrees = frameTime.deltaSeconds * 50f // Adjust the rotation speed here

            // Increment the total rotation angle
            totalDeltaAngleDegrees += deltaAngleDegrees

            // Create a new Quaternion with the updated rotation angle
            val rotation = Quaternion.axisAngle(Vector3.up(), totalDeltaAngleDegrees)

            // Set the new rotation on the TransformableNode
            rotatingNode.localRotation = rotation
        }
    }

    override fun onUpdate(p0: FrameTime?) {
        val frame : Frame? = arFragment.arSceneView.arFrame
        val images : Collection<AugmentedImage> = frame!!.getUpdatedTrackables(AugmentedImage::class.java)

        for(img in images){
            if(img.trackingState == TrackingState.TRACKING){
                if(img.name.equals("img_poulet")){
                    Log.d("DBG_MSG","POULET !!")
                    if(modelCreated == false) {
                        val anchor: Anchor = img.createAnchor(img.centerPose)
                        placeModel(anchor)
                        modelCreated = true
                    }
                    else{
                        anchorNode!!.localPosition = Vector3(img.centerPose.tx(), img.centerPose.ty(),img.centerPose.tz())
                    }
                }
            }
        }
    }

    fun setupImgDatabase(config : Config, sess:Session) {
        imageDatabase = AugmentedImageDatabase(sess)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.legoptics_aug)
        pouletIndex = imageDatabase.addImage("img_poulet", bitmap)
        config.augmentedImageDatabase = imageDatabase
        sess.configure(config)

    }
}