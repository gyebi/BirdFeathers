
import android.content.Context
import com.example.birdfeathers.data.GrowthReminderData
import java.io.BufferedReader
import java.io.InputStreamReader


fun loadGrowthReminderDataFromAssets(context: Context, fileName: String): List<GrowthReminderData> {
    val list = mutableListOf<GrowthReminderData>()
    try {
        context.assets.open(fileName).use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).useLines { lines ->
                lines.drop(1).forEach { line -> // skip header
                    val parts = line.split(",")
                    if (parts.size >= 3) {
                        val dayNumber = parts[0].trim().toIntOrNull() ?: return@forEach
                        val birdType = parts[1].trim()
                        val intervention = parts[2].trim()
                        list.add(GrowthReminderData(dayNumber, birdType, intervention))
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return list
}
