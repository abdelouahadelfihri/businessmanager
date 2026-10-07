package com.example.bizapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.bizapp.data.entity.*

// ThirdParty
@Dao
interface ThirdPartyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(t: ThirdParty): Long
    @Update suspend fun update(t: ThirdParty)
    @Delete suspend fun delete(t: ThirdParty)

    @Query("SELECT * FROM societe WHERE id = :id") suspend fun getById(id: Long): ThirdParty?
    @Query("SELECT * FROM societe ORDER BY nom") fun observeAll(): Flow<List<ThirdParty>>
    @Query("SELECT * FROM societe WHERE client IN (1,3) ORDER BY nom") fun observeCustomers(): Flow<List<ThirdParty>>
    @Query("SELECT * FROM societe WHERE fournisseur = 1 ORDER BY nom") fun observeSuppliers(): Flow<List<ThirdParty>>
    @Query("SELECT * FROM societe WHERE nom LIKE '%' || :q || '%' OR email LIKE '%' || :q || '%' OR phone LIKE '%' || :q || '%' ORDER BY nom")
    fun search(q: String): Flow<List<ThirdParty>>
}
