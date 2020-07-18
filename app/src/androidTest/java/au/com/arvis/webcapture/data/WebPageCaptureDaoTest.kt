package au.com.arvis.webcapture.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import au.com.arvis.webcapture.model.db.WebPageCapture
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.equalTo
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WebPageCaptureDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var webPageCaptureDao: WebPageCaptureDao
    private val webPageCapture1 = WebPageCapture(12345678, "https://www.google.com", "file:///images/test.jpg")
    private val webPageCapture2 = WebPageCapture(12345677, "https://www.android.com", "file:///images/test1.jpg")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDatabase() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        webPageCaptureDao = database.webPageCaptureDao()

        webPageCaptureDao.addWebPageCapture(webPageCapture1)
        webPageCaptureDao.addWebPageCapture(webPageCapture2)
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun testGetWebPageCaptures() = runBlockingTest {
        val webPageCaptureList = webPageCaptureDao.getWebPageCaptures()
        Assert.assertThat(webPageCaptureList.size, equalTo(2))

        Assert.assertThat(webPageCaptureList[0], equalTo(webPageCapture1))
        Assert.assertThat(webPageCaptureList[1], equalTo(webPageCapture2))
    }

    @Test
    fun testRemoveWebPageCapture() = runBlockingTest {
        webPageCaptureDao.deletePageCaptureById(webPageCapture1)
        val webPageCaptureList = webPageCaptureDao.getWebPageCaptures()
        Assert.assertThat(webPageCaptureList.size, equalTo(1))
        Assert.assertThat(webPageCaptureList[0], equalTo(webPageCapture2))
    }

    @Test
    fun testSearchWebPageCapture() = runBlockingTest {
        val searchResult = webPageCaptureDao.searchWebPageCapturesByUrl("%a%")
        Assert.assertThat(searchResult.size, equalTo(1))
        Assert.assertThat(searchResult[0], equalTo(webPageCapture2))
    }
}