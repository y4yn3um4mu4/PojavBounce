/*
 * This file is part of LiquidBounce (https://github.com/CCBlueX/LiquidBounce)
 *
 * Copyright (c) 2015 - 2026 CCBlueX
 *
 * LiquidBounce is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LiquidBounce is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LiquidBounce. If not, see <https://www.gnu.org/licenses/>.
 *
 */
package net.ccbluex.liquidbounce.features.feature.altmanager

import net.ccbluex.liquidbounce.integration.ui.altmanager.NativeAltManagerScreen
import net.ccbluex.liquidbounce.utils.client.mc
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.TitleScreen

/**
 * Adds an Alt Manager button to the top-right corner of the home/title screen
 * for quick access to account switching without opening the Integration Menu.
 * Mobile-safe variant for MobileGlues/PojavBounce.
 */
@Suppress("unused")
object AltManagerScreenButton {
    private const val BUTTON_WIDTH = 60
    private const val BUTTON_HEIGHT = 20
    private const val MARGIN = 10
    
    // Direct Hex Colors (ARGB Format) - avoids ARGB utility mapping errors
    private const val COLOR_NORMAL = 0xC81A1A2E.toInt()      // Dark Navy w/ Alpha
    private const val COLOR_HOVER = 0xC82A2A4E.toInt()       // Highlighted Navy w/ Alpha
    private const val COLOR_BORDER = 0xFF4A4A7A.toInt()      // Purple-grey border
    private const val COLOR_TEXT = 0xFFFFFFFF.toInt()        // Solid White

    private var lastScreenWidth = 0
    private var lastScreenHeight = 0
    private var buttonX = 0
    private var buttonY = 0

    /**
     * Render the Alt Manager button on the screen
     */
    @JvmStatic
    fun renderAltManagerButton(guiGraphics: GuiGraphics, screen: TitleScreen, mouseX: Int, mouseY: Int) {
        try {
            // Update button position if screen size changed
            if (screen.width != lastScreenWidth || screen.height != lastScreenHeight) {
                lastScreenWidth = screen.width
                lastScreenHeight = screen.height
                buttonX = screen.width - BUTTON_WIDTH - MARGIN
                buttonY = MARGIN
            }

            val isHovering = isMouseOverButton(mouseX, mouseY, screen.width)
            val backgroundColor = if (isHovering) COLOR_HOVER else COLOR_NORMAL

            // Draw button background using native vanilla fills
            guiGraphics.fill(buttonX, buttonY, buttonX + BUTTON_WIDTH, buttonY + BUTTON_HEIGHT, backgroundColor)

            // Draw button border
            guiGraphics.fill(buttonX, buttonY, buttonX + BUTTON_WIDTH, buttonY + 1, COLOR_BORDER)
            guiGraphics.fill(buttonX, buttonY + BUTTON_HEIGHT - 1, buttonX + BUTTON_WIDTH, buttonY + BUTTON_HEIGHT, COLOR_BORDER)
            guiGraphics.fill(buttonX, buttonY, buttonX + 1, buttonY + BUTTON_HEIGHT, COLOR_BORDER)
            guiGraphics.fill(buttonX + BUTTON_WIDTH - 1, buttonY, buttonX + BUTTON_WIDTH, buttonY + BUTTON_HEIGHT, COLOR_BORDER)

            // Draw button text
            val font = mc.font ?: return
            val text = "Alts"
            val textWidth = font.width(text)
            val textX = buttonX + (BUTTON_WIDTH - textWidth) / 2
            val textY = buttonY + (BUTTON_HEIGHT - 8) / 2
            
            guiGraphics.drawString(font, text, textX, textY, COLOR_TEXT, false)
        } catch (ignored: Throwable) {
            // Safe execution fallback
        }
    }

    /**
     * Handle click on the Alt Manager button
     */
    @JvmStatic
    fun handleButtonClick(mouseX: Int, mouseY: Int, screenWidth: Int): Boolean {
        return try {
            if (isMouseOverButton(mouseX, mouseY, screenWidth)) {
                mc.setScreen(NativeAltManagerScreen(null))
                true
            } else {
                false
            }
        } catch (ignored: Throwable) {
            false
        }
    }

    /**
     * Check if mouse is over the button
     */
    private fun isMouseOverButton(mouseX: Int, mouseY: Int, screenWidth: Int): Boolean {
        val x = screenWidth - BUTTON_WIDTH - MARGIN
        val y = MARGIN
        return mouseX in x..(x + BUTTON_WIDTH) && mouseY in y..(y + BUTTON_HEIGHT)
    }
}
