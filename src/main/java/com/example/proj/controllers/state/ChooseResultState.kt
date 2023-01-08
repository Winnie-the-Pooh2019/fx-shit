package com.example.proj.controllers.state

import com.example.proj.data.GsonEditor
import com.example.proj.data.models.FileResult
import com.google.gson.reflect.TypeToken
import javafx.event.ActionEvent
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ChooseResultState(mainWindowStateMachine: MainWindowStateMachine) : State(mainWindowStateMachine) {
    override fun toMenu(event: ActionEvent?) {
        setNewContent("start", event)
        mainWindowStateMachine.currentState = mainWindowStateMachine.menu
    }

    override fun toSemiResult(event: ActionEvent?) {
        TODO("Not yet implemented")
    }

    override fun toQuest(event: ActionEvent?) {
        TODO("Not yet implemented")
    }

    override fun toChooseResult(event: ActionEvent?) {
        TODO("Not yet implemented")
    }

    override fun toFinalResult(event: ActionEvent?) {
        setNewContent("final_results", event)
        mainWindowStateMachine.currentState = mainWindowStateMachine.finalRes
    }

    override fun <T : Any?> setContent(scene: Scene?, obj: T) {
        scene as Scene

        val scroll = scene.lookup("#scrollPane") as ScrollPane

        val res = (obj as List<String>)
        val fileNames = res.map {
            val index = it.lastIndexOf('-')
            it.split('.')[0].substring(index + 1)
        }
        val dates = ArrayList<FilesDates>().apply {
            fileNames.forEachIndexed { index, s ->
                try {
                    val date = LocalDateTime.ofInstant(Instant.ofEpochMilli(s.toLong()), ZoneId.systemDefault())
                    val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

                    this.add(FilesDates(res[index], dtf.format(date)))
                } catch (_: Exception) {
                }
            }
        }

        println(dates)

        val vbox = scroll.content as VBox
//        vbox.maxHeight = Double.MAX_VALUE
//        VBox.setVgrow(vbox, Priority.ALWAYS)

        dates.forEach {
            val hBox = HBox()

            val button = Button(it.timestamp)
            button.setOnAction { event ->
                try {
                    val file = File(it.path)
                    val fileResult = GsonEditor.absoluteRead<FileResult>(
                            file.absolutePath,
                            object : TypeToken<FileResult?>() {}.type
                    )

                    toFinalResult(event)
                    mainWindowStateMachine.currentState.setContent(scene, fileResult)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            hBox.children.add(button)
            HBox.setMargin(button, Insets(30.0))

            vbox.children.add(hBox)
            HBox.setHgrow(hBox, Priority.ALWAYS)
//            HBox.setMargin(hBox, Insets(10.0, 0.0, 10.0, 20.0))
        }

//        scroll.content = vbox
//        scroll.
    }

    private data class FilesDates(
            val path: String,
            val timestamp: String
    )
}