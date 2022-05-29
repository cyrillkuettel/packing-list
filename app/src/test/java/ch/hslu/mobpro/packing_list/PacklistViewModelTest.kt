/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.hslu.mobpro.packing_list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import ch.hslu.mobpro.packing_list.room.Packlist
import ch.hslu.mobpro.packing_list.util.MainCoroutineRule
import ch.hslu.mobpro.packing_list.util.getOrAwaitValue
import ch.hslu.mobpro.packing_list.viewmodels.PacklistViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import java.util.stream.Collectors

/**
 * Unit tests for the implementation of [TasksViewModel]
 */
@ExperimentalCoroutinesApi
class PacklistViewModelTest {

    // Subject under test
    private lateinit var tasksViewModel: PacklistViewModel

    // Use a fake repository to be injected into the ViewModel
    private lateinit var packlistRepository: FakeTestRepository

    // Set the main coroutines dispatcher for unit testing
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        // We initialise the tasks to 3, with one active and two completed
        packlistRepository = FakeTestRepository()
        val task1 = Packlist("Title1", "location1" ,"date1", -13070788)
        val task2 = Packlist("Title2","location1" ,"date1",-13070788)
        val task3 = Packlist("Title3","location1" ,"date1",-13070788)
        packlistRepository.insertPacklists(task1, task2, task3)

        tasksViewModel = PacklistViewModel(packlistRepository)
    }

    @Test
    fun createNewPackList_ShouldNavigateBackToMenu() = runTest {
        // When adding a new task
        tasksViewModel.insertNewPacklist(
            Packlist("new Packlist created in Menu",
            "location1" ,
            "date1", -13070788))

        // Then the event to navigate back to Menu is triggered
        val value = tasksViewModel._navigateBacktoMenu.getOrAwaitValue()

        assertEquals(value, true)

    }
}
