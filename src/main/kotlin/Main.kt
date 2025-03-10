import java.awt.*
import java.text.NumberFormat
import javax.swing.*
import kotlin.concurrent.thread

fun main() {
    val frame = JFrame("The Bends")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(400,200)

    val label = JLabel("Загрузите аудио-файл для анализа.", SwingConstants.CENTER)
    label.font = Font("Arial", Font.PLAIN, 18)
    frame.add(label, BorderLayout.CENTER)

    val comboBox = JComboBox(FFTLibraryEnum.entries.toTypedArray())
    frame.add(comboBox, BorderLayout.NORTH)

    val fileChooser = JFileChooser();
    val loadButton = JButton("Загрузить файл")
    val numberFormat = NumberFormat.getInstance()
    val textFieldSemitone = JFormattedTextField(numberFormat)
    textFieldSemitone.columns = 10
    textFieldSemitone.toolTipText = "Введите количество полутонов"
    val labelSemitone = JLabel("Кол-во полутонов: ")

    val panel = JPanel()
    panel.add(labelSemitone)
    panel.add(textFieldSemitone)
    panel.add(loadButton)

    loadButton.addActionListener {
        val selectedItem = comboBox.selectedItem
        val returnValue = fileChooser.showOpenDialog(frame)
        if (textFieldSemitone.value is Number) {
            val semitoneValue = textFieldSemitone.text.toDouble()

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                val file = fileChooser.selectedFile
                label.text = "Обрабатывается: ${file.name}"
                thread {
                    try {
                        AudioUtils.analyzeAudioFile(file, label, selectedItem, semitoneValue)
                    } catch (e: Exception) {
                        SwingUtilities.invokeLater {
                            label.text = "Ошибка при обработке файла: ${e.message}"
                        }
                    }
                }
            }
        } else {
            label.text = "Неправильный формат данных. Полутона нужно вводить числами"
        }
    }

    frame.add(panel, BorderLayout.SOUTH)
    frame.isVisible = true
}
