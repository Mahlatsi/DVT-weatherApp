package com.`fun`.goweather.dao

import androidx.room.*
import com.`fun`.goweather.model.favorite.FavoriteList
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addData(favoriteList: FavoriteList): Completable

    @Query("select * from favoritelist")
    fun getFavoriteData(): Single<List<FavoriteList>>

    @Query("SELECT EXISTS (SELECT 1 FROM favoritelist WHERE id=:id)")
    fun isFavorite(id: Int): Single<Int>

    @Delete
    fun delete(favoriteList: FavoriteList?): Completable

}