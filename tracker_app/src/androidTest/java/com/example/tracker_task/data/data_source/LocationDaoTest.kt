package com.example.tracker_task.data.data_source

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.tracker_task.kotlin.data.data_source.LocationDaoKt
import com.example.tracker_task.kotlin.data.data_source.LocationDatabaseKt
import com.example.tracker_task.kotlin.domain.model.MyLocationKt
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LocationDaoTest {

    private lateinit var database: LocationDatabaseKt
    private lateinit var dao: LocationDaoKt

    private val location1 = MyLocationKt(1, 49.988358, 36.232845)
    private val location2 = MyLocationKt(2, 49.988358, 36.232845)

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocationDatabaseKt::class.java
        ).allowMainThreadQueries().build()

        dao = database.dao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_insertLocationToDatabase() {
        dao.insert(location1)
        assertThat(dao.getAllLocations()).contains(location1)
        assertThat(dao.getAllLocations()).doesNotContain(location2)
        dao.insert(location2)
        assertThat(dao.getAllLocations()).contains(location1)
        assertThat(dao.getAllLocations()).contains(location2)
    }

    @Test
    fun test_deleteAllLocationsFromDatabase() {
        dao.insert(location1)
        dao.insert(location2)
        assertThat(dao.getAllLocations().size).isEqualTo(2)
        dao.deleteAll()
        assertThat(dao.getAllLocations().size).isEqualTo(0)
    }
}