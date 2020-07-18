package au.com.arvis.webcapture.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import au.com.arvis.webcapture.data.AppDatabase
import au.com.arvis.webcapture.model.db.WebPageCapture
import au.com.arvis.webcapture.model.viewmodel.CaptureHistoryViewModel
import au.com.arvis.webcapture.repository.WebPageCaptureRepository
import au.com.arvis.webcapture.utils.MainCoroutineScopeRule
import au.com.arvis.webcapture.utils.getValue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.equalTo
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryViewModelTest {

    private lateinit var database: AppDatabase
    private lateinit var viewModel: CaptureHistoryViewModel

    private val webPageCapture1 = WebPageCapture(12345678, "https://www.google.com", "file:///images/test.jpg")
    private val webPageCapture2 = WebPageCapture(12345677, "https://www.android.com", "file:///images/test1.jpg")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineScope = MainCoroutineScopeRule()

    @Before
    fun setup() = runBlocking{
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        val webPageCaptureDao = database.webPageCaptureDao()
        val webPageCaptureRepository = WebPageCaptureRepository.getInstance(database.webPageCaptureDao())
        viewModel = CaptureHistoryViewModel(webPageCaptureRepository)

        webPageCaptureDao.addWebPageCapture(webPageCapture1)
        webPageCaptureDao.addWebPageCapture(webPageCapture2)
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun testGetHistory() = coroutineScope.runBlockingTest{
        viewModel.getFullHistory()

        val history = getValue(viewModel.webPageCaptureHistory)
        Assert.assertThat(history?.size, equalTo(2))
    }

    @Test
    fun testDeleteHistory() = coroutineScope.runBlockingTest {

        viewModel.deleteHistory(webPageCapture2)

        val history = getValue(viewModel.webPageCaptureHistory)
        Assert.assertThat(history?.size, equalTo(1))
        Assert.assertThat(history?.get(0), equalTo(webPageCapture1))
    }

}