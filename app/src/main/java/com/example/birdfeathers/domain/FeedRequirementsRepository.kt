
import com.example.birdfeathers.entity.FeedRequirementEntity
import com.example.birdfeathers.domain.FeedRequirementDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedRequirementsRepository(
    private val feedRequirementDao: FeedRequirementDao,
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    // Insert a single feed requirement
    suspend fun insertFeedRequirement(requirement: FeedRequirementEntity) {
        feedRequirementDao.insertAll(listOf(requirement))
        // Optionally upload to Firebase:
        firestore.collection("feed_requirements")
            .add(requirement)
    }

    // Insert a list of feed requirements (bulk)
    suspend fun insertFeedRequirements(requirements: List<FeedRequirementEntity>) {
        feedRequirementDao.insertAll(requirements)
        // Optionally upload all to Firebase
        requirements.forEach { req ->
            firestore.collection("feed_requirements").add(req)
        }
    }

    // Get feed requirements for a bird type
    suspend fun getFeedByBirdType(birdType: String): List<FeedRequirementEntity> {
        return feedRequirementDao.getFeedByBirdType(birdType)
    }

    // Get all feed requirements
    suspend fun getAllFeedRequirements(): List<FeedRequirementEntity> {
        return feedRequirementDao.getAllFeedRequirements()
    }

    // (Optional) Download feed requirements from Firebase and save to Room
    fun syncFromFirebaseToRoom(onComplete: (() -> Unit)? = null) {
        firestore.collection("feed_requirements").get()
            .addOnSuccessListener { result ->
                val feedList = result.mapNotNull { it.toObject(FeedRequirementEntity::class.java) }
                CoroutineScope(Dispatchers.IO).launch {
                    feedRequirementDao.insertAll(feedList)
                    onComplete?.invoke()
                }
            }
            .addOnFailureListener {
                // Handle errors as needed
            }
    }
}
