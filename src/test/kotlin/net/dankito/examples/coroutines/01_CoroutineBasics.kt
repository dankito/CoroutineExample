package net.dankito.examples.coroutines

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory


/**
 * For describing texts see https://github.com/Kotlin/kotlinx.coroutines/blob/master/docs/basics.md
 */
class `01_CoroutineBasics` {

    companion object {
        private val log = LoggerFactory.getLogger(`01_CoroutineBasics`::class.java)
    }


    @Test
    fun firstExample() {
        GlobalScope.launch {
            log("I am in the Coroutine!")
            delay(500)
            log("After delay")
        }

        log("Main thread")
        Thread.sleep(1000) // Thread.sleep() is a blocking call
    }

    @Test
    fun firstExample_runBlocking() = runBlocking {
        GlobalScope.launch {
            log("I am in the Coroutine!")
            delay(500)
            log("After delay")
        }

        log("Main thread")
        delay(1000) // -> i can use a non blocking method call here
    }

    @Test
    fun firstExample_Job() = runBlocking {
        val job = GlobalScope.launch {
            log("I am in the Coroutine!")
            delay(500)
            log("After delay")
        }

        log("Main thread")
        job.join() // waits till launch() is done. Much better then waiting for a certain amount of time
    }


    @Test
    fun `Structured concurrency - Launch in same CoroutineScope`() = runBlocking {
        // We can launch coroutines in this scope without having to join them explicitly, because an outer coroutine
        // (runBlocking in our example) does not complete until all the coroutines launched in its scope complete
        launch {
            log("I am in the Coroutine!")
            delay(500)
            log("After delay")
        }

        log("Main thread")
    }

    // runBlocking waits for all its Coroutines to complete. But runBlocking blocks the current thread
    // coroutineScope just suspends, releasing the underlying thread for other usages.
    // Because of that difference, runBlocking is a regular function and coroutineScope is a suspending function.

    @Test
    fun `Structured concurrency - Create own CoroutineScope`() = runBlocking {
        // We can launch coroutines in this scope without having to join them explicitly, because an outer coroutine
        // (runBlocking in our example) does not complete until all the coroutines launched in its scope complete
        launch {
            delay(200)
            log("Task from runBlocking") // gets print before "Nested Coroutine in own scope"!
        }

        coroutineScope { // suspends main thread. log("Main thread") gets only executed when coroutineScope is completely done
            launch { 
                delay(500)
                log("Nested Coroutine in own scope")
            }

            delay(100)
            log("Task from own CoroutineScope")
        }

        log("Main thread") // gets print _after_ coroutineScope and all it's Coroutines are done
    }


    @Test
    fun `Suspending functions`() = runBlocking {
        launch { waitAndPrint(500, "Fire and forget 1") }
        val result = async { waitAndPrint(1000, "Wait for result") }
        launch { waitAndPrint(2000, "Fire and forget 2 - I get printed even though no one cares for me") }
        GlobalScope.launch { waitAndPrint(3000, "Fire and forget 3 - I never get printed as I am from a different scope runBlocking() is not waiting for") }

        result.await()
        log("Main done")
    }

    private suspend fun waitAndPrint(waitMillis: Long, message: String) {
        delay(waitMillis)

        log(message)
    }


    @Test
    fun `Coroutines ARE light-weight`() = runBlocking {
        // don't try this with threads
        repeat(100_000) { // launch a lot of coroutines
            launch {
                delay(1000L)
                print(".")
            }
        }
    }

    @Test
    fun `Global coroutines are like daemon threads`() = runBlocking {
        // Active coroutines that were launched in GlobalScope do not keep the process alive. They are like daemon threads. Therefore method quites after delay(1300L)
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // just quit after delay
    }


    private fun log(message: String) {
        log.info(message)
    }

}