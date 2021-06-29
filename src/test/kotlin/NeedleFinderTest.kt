import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Test
import java.io.File

class NeedleFinderTest {

    private val needleFinder = NeedleFinder()

    @Test
    fun `test needle finder`() {
        val path = "/tmp/somefile.txt"
        val numOfLines = 1000
        val lineLength = 1000
        val sortedStrings = (0..numOfLines)
            .map { RandomStringUtils.randomAlphabetic(lineLength) }
            .sorted()

        writeToFile(path, sortedStrings)

        (0..numOfLines).forEach {
            val currentString = sortedStrings[it]
            val currentStringPrefix = currentString.substring(0, 10)

            assert(needleFinder.findNeedleInner(path, currentStringPrefix) == currentString)
        }

        assert(needleFinder.findNeedleInner(path, "AAAAAAAAAA") == sortedStrings.first())
        assert(needleFinder.findNeedleInner(path, "zzzzzzzzzz") == "")

    }

    private fun writeToFile(path: String, strings: List<String>) {
        File(path).printWriter().use { out ->
            strings.forEach {
                out.println(it)
            }
        }
    }

}