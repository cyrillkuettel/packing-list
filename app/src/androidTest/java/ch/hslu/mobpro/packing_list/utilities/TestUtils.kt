package ch.hslu.mobpro.packing_list.utilities

import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist

/**
 * [Packlist] objects used for tests.
 */
val testPacklists = arrayListOf(
    Packlist("testTitle1"),
    Packlist("testTitle2",),
    Packlist("testTitle3")
)
val testPacklist = testPacklists[0]

val testItems = arrayListOf(
    Item(1, 2, "testContent1"),
)
val testItem = testItems[0]
