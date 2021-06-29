import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.lang.StringBuilder

class NeedleFinder {

    companion object {
        private const val NEW_LINE = '\n'
    }

    fun findNeedleInner(filePath: String, key: String): String {
        val file = File(filePath)
        FileInputStream(file).use {
            return findNeedleInner(it, key, start = 0L, end = file.length())
        }
    }

    private fun findNeedleInner(fileInputStream: FileInputStream, key: String, start: Long, end: Long): String {
        if (start <= end) {
            val middle = (start + end) / 2
            val line = getNextLine(fileInputStream, middle)

            return when {
                line == key -> {
                    line
                }
                line > key -> {
                    findNeedleInner(fileInputStream, key, start, middle - 1)
                }
                else -> {
                    findNeedleInner(fileInputStream, key, middle + 1, end)
                }
            }
        }

        return getNextLine(fileInputStream, start)
    }

    private fun getNextLine(fileInputStream: FileInputStream, startOffset: Long): String {
        val bufferedFileInputStream = seekToNextNewLine(fileInputStream, startOffset)

        return getStringUntilNewLine(bufferedFileInputStream)
    }

    private fun seekToNextNewLine(fileInputStream: FileInputStream, startOffset: Long): BufferedInputStream {
        fileInputStream.channel.position(startOffset)

        val bufferedFileInputStream = BufferedInputStream(fileInputStream)

        if (startOffset == 0L) {
            return bufferedFileInputStream
        }

        while (true) {
            val nextChar = bufferedFileInputStream.read()
            if (nextChar == -1 || nextChar.toChar() == NEW_LINE) {
                return bufferedFileInputStream
            }
        }
    }

    private fun getStringUntilNewLine(bufferedFileInputStream: BufferedInputStream): String {
        val stringBuilder = StringBuilder()
        while (true) {
            val nextChar = bufferedFileInputStream.read()
            if (nextChar == -1 || nextChar.toChar() == NEW_LINE) {
                return stringBuilder.toString()
            }
            stringBuilder.append(nextChar.toChar())
        }
    }
}