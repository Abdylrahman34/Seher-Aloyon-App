package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Maison Aster", appName)
  }

  @Test
  fun `validate username rules`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val manager = com.example.data.FirebaseManager(context)

    // Invalid usernames
    assertEquals("Username must be at least 3 characters", manager.validateUsername("ab"))
    assertEquals("Username can only contain letters, numbers, and underscores", manager.validateUsername("user@name"))

    // Valid usernames
    assertEquals(null, manager.validateUsername("aster_vip"))
    assertEquals(null, manager.validateUsername("seher2026"))
  }
}
