import com.example.birdfeathers.entity.FeedRequirementEntity
import org.junit.Test

// A standalone fake repository, not extending your real one
class FakeFeedRequirementsRepository {
    private val broilerFeed = listOf(
        FeedRequirementEntity(0, "Starter", "Broiler", "Phase 1", 0.35),
        FeedRequirementEntity(0, "Grower", "Broiler", "Phase 2", 0.75),
        FeedRequirementEntity(0, "Finisher", "Broiler", "Phase 3", 2.6),
    )

    fun getFeedByBirdType(birdType: String): List<FeedRequirementEntity> {
        return when (birdType) {
            "Broiler" -> broilerFeed
            else -> emptyList()
        }
    }
}

// Minimal ViewModel for this test, using the fake repository
class TestFeedRequirementsViewModel(
    private val repo: FakeFeedRequirementsRepository
) {
    var numBirds = 50
    private val feedPrices = mapOf(
        "Starter" to 0.90,
        "Grower" to 0.80,
        "Finisher" to 0.75
    )
    private var feedRequirements: List<FeedRequirementEntity> = emptyList()

    fun loadFeedRequirements(birdType: String) {
        feedRequirements = repo.getFeedByBirdType(birdType)
    }

    fun feedBreakdown(): List<Triple<String, Double, Double>> {
        return feedRequirements.map {
            val price = feedPrices[it.nameStage] ?: 0.0
            val totalKg = it.kilosPerPhase * numBirds
            val totalCost = totalKg * price
            Triple(it.nameStage, totalKg, totalCost)
        }
    }

    val totalFeedKg: Double
        get() = feedRequirements.sumOf { it.kilosPerPhase } * numBirds

    val totalFeedCost: Double
        get() = feedRequirements.sumOf {
            val price = feedPrices[it.nameStage] ?: 0.0
            it.kilosPerPhase * numBirds * price
        }
}

class FeedRequirementsViewModelTest {
    @Test
    fun testFeedBreakdown() {
        val repo = FakeFeedRequirementsRepository()
        val viewModel = TestFeedRequirementsViewModel(repo)

        viewModel.loadFeedRequirements("Broiler")
        viewModel.numBirds = 50

        println("Feed Breakdown:")
        viewModel.feedBreakdown().forEach { (phase, totalKg, totalCost) ->
            println("$phase: ${"%.2f".format(totalKg)} kg, \$${"%.2f".format(totalCost)}")
        }
        println("Total Feed: ${"%.2f".format(viewModel.totalFeedKg)} kg")
        println("Total Feed Cost: \$${"%.2f".format(viewModel.totalFeedCost)}")
    }
}
