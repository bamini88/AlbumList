package com.coding.albums.model

import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

class AlbumTest : TestCase() {
    private var album: Album? = null

    public override fun setUp() {
        album = Album()
        album!!.title = "Hello"
    }

    @Test
    fun testGetTitle() {
        Assert.assertNotNull(album?.title)
    }

    @Test
    fun testSetTitle() {
        Assert.assertNotNull(album?.title)
        println(album?.title)
        Assert.assertTrue(!album?.title.isNullOrEmpty())
    }

}