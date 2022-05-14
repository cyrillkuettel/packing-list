package ch.hslu.mobpro.packing_list.utilities

import ch.hslu.mobpro.packing_list.room.Item
import ch.hslu.mobpro.packing_list.room.Packlist

/**
 * [Packlist] objects used for tests.
 */
val testPacklists = arrayListOf(
    Packlist("1"),
    Packlist("2",),
    Packlist("3")
)
val testPacklist = testPacklists[0]

val testItems = arrayListOf(
    Item(1, 2, "testContent1"),
)
val testItem = testItems[0]
