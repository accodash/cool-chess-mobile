package pl.accodash.coolchess.database.repository

import pl.accodash.coolchess.database.dao.UserPreferenceDao
import pl.accodash.coolchess.database.entity.UserPreference

class UserPreferenceRepository(private val dao: UserPreferenceDao) {
    suspend fun getPreference(key: String): String? {
        return dao.getPreference(key)?.value
    }

    suspend fun setPreference(key: String, value: String) {
        dao.setPreference(UserPreference(key, value))
    }
}
