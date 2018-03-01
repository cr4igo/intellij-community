// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.testGuiFramework.tests.community.focus

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.testGuiFramework.fixtures.IdeFrameFixture
import com.intellij.testGuiFramework.impl.GuiTestCase
import com.intellij.testGuiFramework.impl.GuiTestUtilKt
import com.intellij.testGuiFramework.tests.community.CommunityProjectCreator
import com.intellij.testGuiFramework.util.Key
import com.intellij.testGuiFramework.util.Key.ESCAPE
import com.intellij.testGuiFramework.util.Modifier
import com.intellij.testGuiFramework.util.plus
import org.fest.swing.core.SmartWaitRobot
import org.fest.swing.exception.ComponentLookupException
import org.fest.swing.timing.Pause
import org.junit.Assert
import org.junit.Test
import java.awt.Container
import java.awt.Window
import java.lang.Math.tan
import java.util.*
import javax.swing.JLabel
import javax.swing.KeyStroke

class GoToClassTwiceFocusTest : GuiTestCase() {

  private val typedString = "hefuihwefwehrf;werfwerfw"
  private val actionKeyStroke: KeyStroke by lazy { ActionManager.getInstance().getKeyboardShortcut("GotoClass")!!.firstKeyStroke }

  @Test
  fun testGoToClassFocusTwice() {
    CommunityProjectCreator.createCommandLineProject()
    Pause.pause(1000)
    ideFrame {
      focusOnEditor()
      for (i in 0..10) {
        openGoToClassSearchAndType(this@GoToClassTwiceFocusTest)
        focusOnEditor()
      }
    }
  }

  private fun startIntensiveCalcOnEdt() {
    for (i in 0..1000) ApplicationManager.getApplication().invokeLater { println(intensiveCpuCalc().toString()) }
  }

  private fun startIntensiveCalcOnParallel() {
    ApplicationManager.getApplication().executeOnPooledThread {
      for (i in 0..100) ApplicationManager.getApplication().executeOnPooledThread {
        for (k in 0..1000) println(intensiveCpuCalc().toString())
      }
    }
  }

  private fun intensiveCpuCalc(): Double {
    val rand = Random()
    val salt = rand.nextDouble()
    fun f(a: Double): Double = tan(rand.nextDouble() * tan(rand.nextDouble() * tan(rand.nextDouble() * tan(a + 0.001))))
    fun g(n: Int, a: Double): Double {
      var res = a
      for (i in 0..n) res = f(res)
      return res
    }
    return g(10000, salt)
  }

  private fun IdeFrameFixture.focusOnEditor() {
    editor {
      moveTo(89)
    }
  }

  private fun openGoToClassSearchAndType(guiTestCase: GuiTestCase) {

    val smartRobot = guiTestCase.robot() as SmartWaitRobot
    guiTestCase.shortcut(Modifier.CONTROL + Key.N, Modifier.META + Key.N)
    smartRobot.shortcutAndTypeString(actionKeyStroke, typedString, 100)
    Pause.pause(500)
    checkSearchWindow(guiTestCase)
    shortcut(ESCAPE)
  }

  private fun checkSearchWindow(guiTestCase: GuiTestCase) {
    val searchWindow = try {
      findSearchWindow()
    }
    catch (cle: ComponentLookupException) {
      guiTestCase.robot().waitForIdle()
      findSearchWindow()
    }
    with(guiTestCase) {
      val textfield = textfield("", searchWindow, guiTestCase.defaultTimeout)
      Assert.assertEquals(textfield.target().text, typedString)
    }
  }

  private fun findSearchWindow(): Container {
    fun checkWindowContainsEnterClassName(it: Window) = GuiTestUtilKt.findAllWithBFS(it,
                                                                                     JLabel::class.java).firstOrNull { it.text == "Enter class name:" } != null
    return Window.getWindows()
             .filterNotNull()
             .firstOrNull { checkWindowContainsEnterClassName(it) } ?: throw ComponentLookupException(
      "Unable to find GoToClass search window")
  }

}