class Player(var points: Int = 0, var name: String = "")

fun boardConstruct(table: Array<Array<String>>): Array<Array<String>> {
    for (i in table.indices) {
        for (j in 1 until table[0].size) {
            when(i) {
                0 -> table[i][j] += if(j % 2 == 0) (j / 2).toString() else " "
                in 1 until table.size - 1 -> table[i][j] += if(j % 2 == 1) "║" else " "
                table.size - 1 -> {
                    table[i][j] += if(j == 1) {
                        "╚"
                    } else if(j % 2 == 0) {
                        "═"
                    } else if(j == table[0].size - 1) {
                        "╝"
                    } else "╩"
                }
            }
        }
    }
    return table
}

fun printTable(table: Array<Array<String>>) {
    for (element in table) {
        for (j in 0 until table[0].size) {
            print(element[j])
        }
        println()
    }
}

fun changeTable(table: Array<Array<String>>, changeIndex: Int, row: Int, numberGame: Int): Array<Array<String>> {
    val point = if (numberGame % 2 == 1) if (changeIndex % 2 == 1) "o" else "*" else if (changeIndex % 2 == 0) "o" else "*"
    var i = table.size - 2
    while(true) {
        if (i < 1) {
            println("Column $row is full")
            table[0][0] = "0"
            break
        }
        if (table[i][row * 2] == "o" || table[i][row * 2] == "*") {
            i--
        } else {
            table[i][row * 2] = point
            break
        }
    }
    return table
}

fun singleOrMulti(): Int {
    var numberOfGames: String
    while (true) {
        println("Do you want to play single or multiple games?\nFor a single game, input 1 or press Enter\nInput a number of games:")
        numberOfGames = readLine()!!
        if (numberOfGames.isEmpty()) return 1
        try {
            return if (numberOfGames.toInt() > 0) numberOfGames.toInt() else {
                println("Invalid input")
                continue
            }
        } catch (e: Exception) {
            println("Invalid input")
        }
    }
}

fun winningCondition(table: Array<Array<String>>): Boolean{
    var winningCondition = false
    var counterFor4: String
    var counterFor4Old = ""
    var counterFor4Old2 = ""
    var counterFor4Old3 = ""
    var counter = 0
    var counter2 = 0
    var counter3 = 0

    for (i in 0 until table[0].size step 2) {
        for (j in 1 until table.size - 1) {
            counterFor4 = table[j][i]
            if(counterFor4 == counterFor4Old && (counterFor4 == "*" || counterFor4 == "o")) {
                counter++
            } else counter = 0
            counterFor4Old = counterFor4
            if(counter == 3) {
                winningCondition = true
                break
            }
        }
    }
    counter = 0
    counterFor4Old = ""
    for (i in 1 until table.size - 1) {
        for (j in 0 until table[0].size step 2) {
            counterFor4 = table[i][j]
            if(counterFor4 == counterFor4Old && (counterFor4 == "*" || counterFor4 == "o")) {
                counter++
            } else counter = 0
            counterFor4Old = counterFor4

            if(counter == 3) {
                winningCondition = true
                break
            }
            for (k in 0..100) {
                if (i + k >= table.size - 1 || j + k * 2 >= table[0].size - 1) {
                    counter2 = 0
                    counterFor4Old2 = ""
                    continue
                }
                counterFor4 = table[i + k][j + k * 2]
                if(counterFor4 == counterFor4Old2 && (counterFor4 == "*" || counterFor4 == "o")) {
                    counter2++
                } else counter2 = 0
                counterFor4Old2 = counterFor4
                if (counter2 == 3) {
                    winningCondition = true
                    break
                }
            }
            for (s in 0..100) {
                if (i + s >= table.size - 1 || j - s * 2 <= 1) {
                    counter3 = 0
                    counterFor4Old3 = ""
                    continue
                }
                counterFor4 = table[i + s][j - s * 2]
                if(counterFor4 == counterFor4Old3 && (counterFor4 == "*" || counterFor4 == "o")) {
                    counter3++
                } else counter3 = 0
                counterFor4Old3 = counterFor4
                if (counter3 == 3) {
                    winningCondition = true
                    break
                }
            }
        }
    }
    return winningCondition
}

fun main() {
    println("Connect Four")
    println("First player's name:")
    var Player1 = Player(name = readLine()!!)
    println("Second player's name:")
    var Player2 = Player(name = readLine()!!)
    var boardSize = listOf<Int>(6, 7)
    var input: String
    var controlString = Regex("1?2?3?.X1?2?3?.")
    var changeIndex = 0
    var winningCondition: Boolean

    while (true) {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")
        input = readLine()!!.replace(" ", "").replace("\t", "").replace("x", "X")
        if (input.isEmpty()) break
        if (!input.matches(controlString)) {
            println("Invalid input")
            continue
        }
        boardSize = input.split("X").map { it.toInt() }
        if (boardSize.maxOrNull()!! <= 9 && boardSize.minOrNull()!! >= 5 || input.isEmpty()) {
            break
        } else if (boardSize[0] < 5 || boardSize[0] > 9) {
            println("Board rows should be from 5 to 9")
        } else println("Board columns should be from 5 to 9")
    }

    val numberOfGames = singleOrMulti()

    println("${Player1.name} VS ${Player2.name}")
    println("${boardSize[0]} X ${boardSize[1]} board")
    if(numberOfGames == 1) println("Single game") else println("Total $numberOfGames games")

    var table = Array(boardSize[0] + 2) {Array(boardSize[1] * 2 + 2) { "" } }
    table = boardConstruct(table)

    for (i in 1..numberOfGames) {
        if (numberOfGames != 1) println("Game #$i")
        printTable(table)
        changeIndex = 0
        while(true) {
            println("${if(i % 2 == 1) if(changeIndex++ % 2 == 0) Player1.name else Player2.name else if(changeIndex++ % 2 == 0) Player2.name else Player1.name}'s turn:")
            val row = readLine()!!
            if(row == "end") {
                println("Game over!")
                break
            }
            try{
                row.toInt()
            } catch (e: Exception) {
                println("Incorrect column number")
                changeIndex--
                continue
            }
            if (row.toInt() < 1 || row.toInt() > boardSize[1]) {
                println("The column number is out of range (1 - ${boardSize[1]})")
                changeIndex--
                continue
            }
            table = changeTable(table, changeIndex, row.toInt(), i)
            if(table[0][0] == "0") {
                table[0][0] = " "
                changeIndex--
                continue
            }

            winningCondition = winningCondition(table)
            printTable(table)

            if(winningCondition) {
                println("Player ${if (i % 2 == 1) {
                    if(--changeIndex % 2 == 0) {
                        Player1.points += 2
                        Player1.name
                    } else {
                        Player2.points += 2
                        Player2.name
                    }
                } else if(--changeIndex % 2 == 0) {
                    Player2.points += 2
                    Player2.name
                } else {
                    Player1.points += 2
                    Player1.name
                } } won")
                break
            }
            if(changeIndex == boardSize[0] * boardSize[1]) {
                Player1.points++
                Player2.points++
                println("It is a draw")
                break
            }
        }
        if (numberOfGames > 1) println("Score\n${Player1.name}: ${Player1.points} ${Player2.name}: ${Player2.points}")
        table = boardConstruct(Array(boardSize[0] + 2) {Array(boardSize[1] * 2 + 2) { "" } })
    }
    println("Game over!")
}

