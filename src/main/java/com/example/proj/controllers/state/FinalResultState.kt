package com.example.proj.controllers.state

import com.example.proj.HelloApplication
import com.example.proj.data.models.FileResult
import javafx.event.ActionEvent
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox

class FinalResultState(mainWindowStateMachine: MainWindowStateMachine) : State(mainWindowStateMachine) {
    override fun toMenu(event: ActionEvent?) {
        setNewContent("start", event)
        mainWindowStateMachine.currentState = mainWindowStateMachine.menu
    }

    override fun toSemiResult(event: ActionEvent?) {}

    override fun toQuest(event: ActionEvent?) {}
    override fun toChooseResult(event: ActionEvent?) {
        TODO("Not yet implemented")
    }

    override fun toFinalResult(event: ActionEvent?) {}

    override fun <T : Any?> setContent(scene: Scene, obj: T) {
        val scroll = scene.lookup("#finalScroll") as ScrollPane
        val vbox = scroll.content as VBox

        try {
            val fileRes = (obj as FileResult)
            val results = fileRes.results

            (scene.lookup("#result") as Label).text = "${results.count { it.correct == it.current }} из ${results.size}"
            (scene.lookup("#resTime") as Label).text = fileRes.time

            results.forEach {
                val hBox = HBox()

                val name = Label(it.question)
                name.style = "-fx-font-size: 18pt"

                val imageView = ImageView().apply {
                    image = if (it.current != null && it.current == it.correct)
                        Image(HelloApplication::class.java.getResourceAsStream("tick.png"))
                    else
                        Image(HelloApplication::class.java.getResourceAsStream("cross.png"))

                    fitHeight = 20.0
                    fitWidth = 20.0
                }
                hBox.children.addAll(imageView, name)
                HBox.setMargin(hBox, Insets(30.0, 0.0, 0.0, 30.0))
                HBox.setMargin(imageView, Insets(10.0, 20.0, 0.0, 20.0))

                vbox.children.add(hBox)
                HBox.setHgrow(hBox, Priority.ALWAYS)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}