package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// Contact
@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(c: Contact): Long
    @Update suspend fun update(c: Contact)
    @Delete suspend fun delete(c: Contact)

    @Query("SELECT * FROM socpeople WHERE id = :id") suspend fun getById(id: Long): Contact?
    @Query("SELECT * FROM socpeople ORDER BY lastname") fun observeAll(): Flow<List<Contact>>
    @Query("SELECT * FROM socpeople WHERE fk_soc = :thirdPartyId ORDER BY lastname")
    fun observeByThirdParty(thirdPartyId: Long): Flow<List<Contact>>
}
