package com.example.mystoryapp1

import com.example.mystoryapp1.data.response.ListStoryItem

object DataDummy {
    fun generateDummyQuoteResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                id = "id_$i",
                name = "Name $i",
                description = "Description $i",
                photoUrl = "https://core.api.efishery.com/image/p/q100;/https://efishery.com/images/misi-global-hifi-1.png",
                createdAt = "2021-01-22T22:22:22Z",
                lat = i.toDouble() * 5,
                lon = i.toDouble() * 10
            )
            items.add(story)
        }
        return items
    }
}