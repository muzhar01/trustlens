package com.example.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.example.data.model.ChatMessageEntity
import com.example.data.model.ComplianceActionEntity
import com.example.data.model.ComplianceDocumentEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET complianceRiskScore = :newScore WHERE id = 1")
    suspend fun updateRiskScore(newScore: Int)

    @Query("UPDATE user_profile SET isTwoFactorActive = :isActive WHERE id = 1")
    suspend fun updateTwoFactor(isActive: Boolean)

    @Query("UPDATE user_profile SET languageCode = :lang WHERE id = 1")
    suspend fun updateLanguage(lang: String)
}

@Dao
interface DocumentDao {
    @Query("SELECT * FROM compliance_documents ORDER BY id ASC")
    fun getAllDocuments(): Flow<List<ComplianceDocumentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: ComplianceDocumentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(docs: List<ComplianceDocumentEntity>)

    @Update
    suspend fun updateDocument(doc: ComplianceDocumentEntity)

    @Delete
    suspend fun deleteDocument(doc: ComplianceDocumentEntity)

    @Query("SELECT * FROM compliance_documents WHERE id = :id LIMIT 1")
    suspend fun getDocumentById(id: Int): ComplianceDocumentEntity?
}

@Dao
interface ActionDao {
    @Query("SELECT * FROM compliance_actions WHERE isDismissed = 0 ORDER BY id ASC")
    fun getActiveActions(): Flow<List<ComplianceActionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: ComplianceActionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(actions: List<ComplianceActionEntity>)

    @Query("UPDATE compliance_actions SET isDismissed = 1 WHERE id = :id")
    suspend fun dismissAction(id: Int)
}

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatHistory(): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()
}

@Database(
    entities = [
        UserProfileEntity::class,
        ComplianceDocumentEntity::class,
        ComplianceActionEntity::class,
        ChatMessageEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun documentDao(): DocumentDao
    abstract fun actionDao(): ActionDao
    abstract fun chatDao(): ChatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trustlens_compliance.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
