/*
 * IdeaVim - Vim emulator for IDEs based on the IntelliJ platform
 * Copyright (C) 2003-2019 The IdeaVim authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

@file:Suppress("RemoveCurlyBracesFromTemplate")

package org.jetbrains.plugins.ideavim.action.motion.leftright

import com.maddyhome.idea.vim.command.CommandState
import com.maddyhome.idea.vim.helper.StringHelper.parseKeys
import com.maddyhome.idea.vim.option.Options
import com.maddyhome.idea.vim.option.Options.KEYMODEL
import org.jetbrains.plugins.ideavim.VimTestCase

class MotionShiftRightActionHandlerTest : VimTestCase() {
    fun `test visual right`() {
        Options.getInstance().getListOption(Options.KEYMODEL)!!.set("startsel")

        doTest(parseKeys("<S-Right>"),
                """
                A Discovery

                I ${c}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                """.trimIndent(),
                """
                A Discovery

                I ${s}f${c}o${se}und it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                """.trimIndent(),
                CommandState.Mode.VISUAL, CommandState.SubMode.VISUAL_CHARACTER
        )
    }

    fun `test visual right twice`() {
        Options.getInstance().getListOption(Options.KEYMODEL)!!.set("startsel")

        doTest(parseKeys("<S-Right><S-Right>"),
                """
                A Discovery

                I ${c}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                """.trimIndent(),
                """
                A Discovery

                I ${s}fo${c}u${se}nd it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                """.trimIndent(),
                CommandState.Mode.VISUAL, CommandState.SubMode.VISUAL_CHARACTER
        )
    }

    fun `test select right`() {
        Options.getInstance().getListOption(Options.KEYMODEL)!!.set("startsel")
        Options.getInstance().getListOption(Options.SELECTMODE)!!.set("key")

        doTest(parseKeys("<S-Right>"),
                """
                A Discovery

                I ${c}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                """.trimIndent(),
                """
                A Discovery

                I ${s}f${c}${se}ound it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                """.trimIndent(),
                CommandState.Mode.SELECT, CommandState.SubMode.VISUAL_CHARACTER
        )
    }

    fun `test select right twice`() {
        Options.getInstance().getListOption(Options.KEYMODEL)!!.set("startsel")
        Options.getInstance().getListOption(Options.SELECTMODE)!!.set("key")

        doTest(parseKeys("<S-Right><S-Right>"),
                """
                A Discovery

                I ${c}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                """.trimIndent(),
                """
                A Discovery

                I ${s}fo${c}${se}und it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                """.trimIndent(),
                CommandState.Mode.SELECT, CommandState.SubMode.VISUAL_CHARACTER
        )
    }

    fun `test simple motion char mode`() {
        doTest(parseKeys("gh", "<S-Right>"),
                """
                A Discovery

                ${c}I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                """
                A Discovery

                ${s}I $c${se}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_CHARACTER)
    }

    fun `test at the lineend char mode`() {
        doTest(parseKeys("gh", "<S-Right>"),
                """
                A Discovery

                I found it in a legendary la${c}nd
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                """
                A Discovery

                I found it in a legendary la${s}nd$c$se
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_CHARACTER)
    }

    fun `test out of line char mode`() {
        doTest(parseKeys("gh", "<S-Right>".repeat(2)),
                """
                A Discovery

                I found it in a legendary lan${c}d
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                """
                A Discovery

                I found it in a legendary lan${s}d$c$se
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_CHARACTER)
    }

    fun `test file end char mode`() {
        doTest(parseKeys("gh", "<S-Right>".repeat(2)),
                """
                A Discovery

                I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass$c.""".trimIndent(),
                """
                A Discovery

                I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass$s.$c$se""".trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_CHARACTER)
    }

    fun `test file char mode multicaret`() {
        doTest(parseKeys("gh", "<S-Right>".repeat(2)),
                """
                A Discovery

                I ${c}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass$c.""".trimIndent(),
                """
                A Discovery

                I ${s}fou$c${se}nd it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass$s.$c$se""".trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_CHARACTER)
    }

    fun `test simple motion line mode`() {
        doTest(parseKeys("gH", "<S-Right>"),
                """
                A Discovery

                ${c}I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                """
                A Discovery

                ${s}I$c found it in a legendary land
                ${se}all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_LINE)
    }

    fun `test lineend line mode`() {
        doTest(parseKeys("gH", "<S-Right>"),
                """
                A Discovery

                I found it in a legendary lan${c}d
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                """
                A Discovery

                ${s}I found it in a legendary land$c
                ${se}all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_LINE)
    }

    fun `test out of line line mode`() {
        doTest(parseKeys("gH", "<S-Right>".repeat(2)),
                """
                A Discovery

                I found it in a legendary lan${c}d
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                """
                A Discovery

                ${s}I found it in a legendary land$c
                ${se}all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_LINE)
    }

    fun `test fileend line mode`() {
        doTest(parseKeys("gH", "<S-Right>"),
                """
                A Discovery

                I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass$c.""".trimIndent(),
                """
                A Discovery

                I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                ${s}hard by the torrent of a mountain pass.$c$se""".trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_LINE)
    }

    fun `test line mode multicaret`() {
        doTest(parseKeys("gH", "<S-Right>"),
                """
                A Discovery

                I found ${c}it in ${c}a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass$c.""".trimIndent(),
                """
                A Discovery

                ${s}I found i${c}t in a legendary land
                ${se}all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                ${s}hard by the torrent of a mountain pass.$c$se""".trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_LINE)
    }

    fun `test simple motion block mode`() {
        doTest(parseKeys("g<C-H>", "<S-Right>"),
                """
                A Discovery

                ${c}I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                """
                A Discovery

                ${s}I $c${se}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_BLOCK)
    }

    fun `test at the lineend block mode`() {
        doTest(parseKeys("g<C-H>", "<S-Right>"),
                """
                A Discovery

                I found it in a legendary la${c}nd
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                """
                A Discovery

                I found it in a legendary la${s}nd$c$se
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_BLOCK)
    }

    fun `test out of line block mode`() {
        doTest(parseKeys("g<C-H>", "<S-Right>".repeat(2)),
                """
                A Discovery

                I found it in a legendary lan${c}d
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                """
                A Discovery

                I found it in a legendary lan${s}d$c$se
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.
                    """.trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_BLOCK)
    }

    fun `test file end block mode`() {
        doTest(parseKeys("g<C-H>", "<S-Right>".repeat(2)),
                """
                A Discovery

                I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass$c.""".trimIndent(),
                """
                A Discovery

                I found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass$s.$c$se""".trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_BLOCK)
    }

    fun `test to longer line block mode`() {
        doTest(parseKeys("g<C-H>", "<S-Down>", "<S-Right>".repeat(3)),
                """
                A Discovery

                I found it in a legendary lan${c}d
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.""".trimIndent(),
                """
                A Discovery

                I found it in a legendary lan${s}d$se
                all rocks and lavender and tu${s}fted$c$se grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.""".trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_BLOCK)
    }

    fun `test acontinuevisual`() {
        Options.getInstance().getListOption(KEYMODEL)!!.set("acontinuevisual")
        doTest(parseKeys("v", "<S-Right>".repeat(3)),
                """
                A Discovery

                I ${c}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.""".trimIndent(),
                """
                A Discovery

                I ${s}fou${c}n${se}d it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.""".trimIndent(),
                CommandState.Mode.VISUAL,
                CommandState.SubMode.VISUAL_CHARACTER)
    }

    fun `test no acontinueselect`() {
        Options.getInstance().getListOption(KEYMODEL)!!.set("")
        doTest(parseKeys("gh", "<S-Right>".repeat(3)),
                """
                A Discovery

                I ${c}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.""".trimIndent(),
                """
                A Discovery

                I ${s}found it in ${c}${se}a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.""".trimIndent(),
                CommandState.Mode.SELECT,
                CommandState.SubMode.VISUAL_CHARACTER)
    }

    fun `test no acontinuevisual`() {
        Options.getInstance().getListOption(KEYMODEL)!!.set("")
        doTest(parseKeys("v", "<S-Right>".repeat(3)),
                """
                A Discovery

                I ${c}found it in a legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.""".trimIndent(),
                """
                A Discovery

                I ${s}found it in ${c}a${se} legendary land
                all rocks and lavender and tufted grass,
                where it was settled on some sodden sand
                hard by the torrent of a mountain pass.""".trimIndent(),
                CommandState.Mode.VISUAL,
                CommandState.SubMode.VISUAL_CHARACTER)
    }
}