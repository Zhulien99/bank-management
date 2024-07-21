package com.julien.bankmanagement.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.julien.bankmanagement.database.entities.UsersEntity

object PreferenceUtils {

    private fun getPreferences(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * Saves the provided key-value pair in shared preferences.
     *
     * @param key       The name of the preference
     * @param value     The new value of the preference
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not implemented yet!")
        }
    }

    /**
     * Returns the value on a given key.
     *
     * @param key           The preference name
     * @param defaultValue  Optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    private inline fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? =
        when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not implemented yet!")
        }

    //Token
    private const val USER = "key.userEntity"

    fun getUser(context: Context): UsersEntity? {
        val json = getPreferences(context).getString(USER, null)
        return if (json != null) {
            val gson = Gson()
            gson.fromJson(json, UsersEntity::class.java)
        } else {
            null
        }
    }

    fun saveUser(usersEntity: UsersEntity, context: Context) {
        val gson = Gson()
        val json = gson.toJson(usersEntity)
        getPreferences(context)[USER] = json
    }


    fun clearPreferences(context: Context){
        getPreferences(context).edit().remove(USER).apply()

    }
}
