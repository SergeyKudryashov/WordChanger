package com.getmati.wordchanger.services

import android.accessibilityservice.AccessibilityService
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeInfo.*

private val WORDS_TO_CHANGE = arrayOf("bitcoin", "crypto", "money")
const val CHANGED_TEXT = "hidden" + " "

class WordDetectorAccessibilityService : AccessibilityService() {

    override fun onInterrupt() {
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val source: AccessibilityNodeInfo = event.source ?: return
        val inputText = source.text ?: return

        val lastInputWord = inputText.split(" ").lastOrNull() ?: return
        if (WORDS_TO_CHANGE.contains(lastInputWord)) {
            val indexOfWord = inputText.lastIndexOf(lastInputWord)
            source.performAction(
                ACTION_SET_SELECTION,
                Bundle().apply {
                    putInt(ACTION_ARGUMENT_SELECTION_START_INT, indexOfWord)
                    putInt(ACTION_ARGUMENT_SELECTION_END_INT, indexOfWord + lastInputWord.length)
                }
            )
            addToClipboard()
            source.performAction(ACTION_PASTE)
        }
    }

    private fun addToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        clipboard?.setPrimaryClip(ClipData.newPlainText(CHANGED_TEXT, CHANGED_TEXT))
    }
}