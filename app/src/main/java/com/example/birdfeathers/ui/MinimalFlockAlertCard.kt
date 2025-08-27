
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.birdfeathers.app.theme.FarmGreenDark
import com.example.birdfeathers.data.FlockArrival
import com.example.birdfeathers.data.GrowthReminderData
import com.example.birdfeathers.entity.toDomain
import com.example.birdfeathers.viewmodel.FlockArrivalViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@Composable
fun MinimalFlockAlertCard(
    viewModel: FlockArrivalViewModel,
    scheduleFileName: String = "vaccine_schedule.csv"
) {
    val context = LocalContext.current
    val flockArrival = viewModel.allFlockArrivals.collectAsState(initial = emptyList())
    val allFlocks = flockArrival.value.map { it.toDomain() }
    val reminders = remember { loadGrowthReminderDataFromAssets(context, scheduleFileName) }

    if (allFlocks.isEmpty()) {
        Text("No flocks available")
        return
    }

    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ISO_DATE

    val totalTodayInterventions = allFlocks.sumOf { flock ->
        val arrivalDate = runCatching { LocalDate.parse(flock.arrivalDate, formatter) }.getOrNull()
        val daysSinceArrival = arrivalDate?.let { ChronoUnit.DAYS.between(it, today).toInt() + 1 } ?: 0

        reminders.count {
            it.birdType.equals(flock.birdType, ignoreCase = true) &&
                    it.dayNumber == daysSinceArrival
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Flock Alerts | $totalTodayInterventions" ,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
            color = FarmGreenDark

        )

        allFlocks.sortedByDescending { it.arrivalDate }.forEach { flock ->
            FlockArrivalCard(flock = flock, reminders = reminders)
        }
    }
}

@Composable
fun FlockArrivalCard(
    flock: FlockArrival,
    reminders: List<GrowthReminderData>
) {
    val formatter = DateTimeFormatter.ISO_DATE
    val arrivalDate = runCatching { LocalDate.parse(flock.arrivalDate, formatter) }.getOrNull()
    val today = LocalDate.now()
    val daysSinceArrival = if (arrivalDate != null) ChronoUnit.DAYS.between(arrivalDate, today).toInt() + 1 else 0

    val todayInterventions = reminders.filter {
        it.birdType.equals(flock.birdType, ignoreCase = true) &&
                it.dayNumber == daysSinceArrival
    }

    val countInterventions = reminders.count{
            it.birdType.equals(flock.birdType, ignoreCase = true) &&
                    it.dayNumber == daysSinceArrival
                }


    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("Farm: ${flock.farmName} | Coop: ${flock.coopNumber}")
            Text("Arrival: ${flock.arrivalDate} | Bird Type: ${flock.birdType}")
            Text("Days since arrival: $daysSinceArrival | Flock Count: ${flock.dayOldChicks}")
            Spacer(Modifier.height(8.dp))
            Text("Today's Intervention:", style = MaterialTheme.typography.titleMedium)

            if (todayInterventions.isNotEmpty()) {
                todayInterventions.forEach {
                    Text("• ${it.intervention}", style = MaterialTheme.typography.bodyMedium, color = FarmGreenDark)
                }
            } else {
                Text("No intervention for today.")
            }
        }
    }
}
