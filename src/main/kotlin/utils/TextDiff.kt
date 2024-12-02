package utils

import com.github.difflib.DiffUtils
import com.github.difflib.patch.*

data class TextDiff(val left: String, val right: String) {

    val patch: Patch<String> by lazy {
        DiffUtils.diff(left.lines(), right.lines(), true)!!
    }

    val formatted by lazy { format() }

    private fun format(): String {
        var result = ""

        for (delta in patch.deltas) {
            when (delta) {
                is ChangeDelta -> {
                    result += delta.source.lines.joinToString(prefix = "- ", separator = "\n- ", postfix = "\n")
                    result += delta.target.lines.joinToString(prefix = "+ ", separator = "\n+ ", postfix = "\n")
                }

                is DeleteDelta -> {
                    result += delta.source.lines.joinToString(prefix = "- ", separator = "\n- ", postfix = "\n")
                }

                is EqualDelta -> {
                    result += delta.source.lines.joinToString(prefix = "  ", separator = "\n", postfix = "\n")
                }

                is InsertDelta -> {
                    result += delta.target.lines.joinToString(prefix = "+ ", separator = "\n+ ", postfix = "\n")
                }
            }
        }

        return result
    }

    companion object {

        fun of(left: String, right: String): TextDiff = TextDiff(left, right)

    }

}
