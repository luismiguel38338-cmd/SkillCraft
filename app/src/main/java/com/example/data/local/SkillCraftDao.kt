package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillCraftDao {

    // User Profile
    @Query("SELECT * FROM user_profile WHERE id = 'primary_user' LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfile)

    // Projects
    @Query("SELECT * FROM projects ORDER BY difficulty ASC, title ASC")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :projectId LIMIT 1")
    fun getProjectById(projectId: String): Flow<Project?>

    @Query("SELECT * FROM projects WHERE status = 'IN_PROGRESS' LIMIT 1")
    fun getActiveProject(): Flow<Project?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<Project>)

    @Update
    suspend fun updateProject(project: Project)

    // Tasks
    @Query("SELECT * FROM project_tasks WHERE projectId = :projectId ORDER BY orderIndex ASC")
    fun getTasksForProject(projectId: String): Flow<List<ProjectTask>>

    @Query("SELECT * FROM project_tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: String): ProjectTask?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<ProjectTask>)

    @Update
    suspend fun updateTask(task: ProjectTask)

    // Achievements
    @Query("SELECT * FROM achievements")
    fun getAllAchievements(): Flow<List<UserAchievement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<UserAchievement>)

    @Update
    suspend fun updateAchievement(achievement: UserAchievement)

    // Daily Challenges
    @Query("SELECT * FROM daily_challenges")
    fun getDailyChallenges(): Flow<List<DailyChallenge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenges(challenges: List<DailyChallenge>)

    @Update
    suspend fun updateChallenge(challenge: DailyChallenge)

    // Certificates
    @Query("SELECT * FROM certificates ORDER BY issueDate DESC")
    fun getAllCertificates(): Flow<List<Certificate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCertificate(certificate: Certificate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCertificates(certificates: List<Certificate>)

    // Community Posts
    @Query("SELECT * FROM community_posts ORDER BY id DESC")
    fun getCommunityPosts(): Flow<List<CommunityPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunityPosts(posts: List<CommunityPost>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunityPost(post: CommunityPost)

    @Update
    suspend fun updateCommunityPost(post: CommunityPost)

    // AI Chat Messages
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<AiChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: AiChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearChatMessages()
}
