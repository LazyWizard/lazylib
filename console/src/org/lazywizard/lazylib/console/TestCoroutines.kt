package org.lazywizard.lazylib.console

import kotlinx.coroutines.*
import org.lazywizard.console.BaseCommand
import org.lazywizard.console.Console

class TestCoroutines : BaseCommand {
    override fun runCommand(args: String, context: BaseCommand.CommandContext): BaseCommand.CommandResult {
        runBlocking {
            // this: CoroutineScope
            launch {
                delay(200L)
                println("Task from runBlocking")
            }

            coroutineScope {
                // Creates a new coroutine scope
                launch {
                    delay(500L)
                    println("Task from nested launch")
                }

                delay(100L)
                println("Task from coroutine scope") // This line will be printed before nested launch
            }

            println("Coroutine scope is over") // This line is not printed until nested launch completes
        }

        Console.showMessage("Finished coroutine!")
        return BaseCommand.CommandResult.SUCCESS
    }
}
