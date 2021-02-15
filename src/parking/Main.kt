package parking

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    var parkingLot: ParkingLot? = null

    var isExit = false
    while (!isExit) {
        val input = scanner.nextLine().split(" ").toTypedArray()
        when (input[0]) {
            "create" -> {
                val numSpaces = input[1].toInt()
                parkingLot = ParkingLot(numSpaces)

                println("Created a parking lot with $numSpaces spots.")
            }
            "park" -> {
                val registration = input[1]
                val colour = input[2]
                val response =
                    parkingLot?.park(Vehicle(registration, colour)) ?: "Sorry, a parking lot has not been created."

                println(response)
            }
            "leave" -> {
                val parkingSpace = input[1].toInt()
                val response = parkingLot?.leave(parkingSpace) ?: "Sorry, a parking lot has not been created."

                println(response)
            }
            "status" -> {
                println(parkingLot?.status() ?: "Sorry, a parking lot has not been created.")
            }
            "exit" -> isExit = true
        }
    }
}

class ParkingLot(numSpaces: Int) {
    private val spaces: Collection<ParkingSpace>

    init {
        this.spaces = List(numSpaces) { ParkingSpace() }.toSet()
    }

    fun park(vehicle: Vehicle): String {
        val spot = findNextAvailableParkingSpace()

        return if (spot >= 0) {
            this.spaces.elementAt(spot).vehicle = vehicle
            "${vehicle.colour} car parked in spot ${spot + 1}."
        } else {
            "Sorry, the parking lot is full."
        }
    }

    private fun findNextAvailableParkingSpace(): Int = this.spaces.indexOf(this.spaces.find { it.vehicle == null })

    private fun isEmpty(): Boolean = this.spaces.count { it.vehicle != null } == 0

    fun leave(spot: Int): String {
        val parkingSpace = this.spaces.elementAt(spot - 1)

        return parkingSpace.vehicle?.let {
            parkingSpace.vehicle = null
            "Spot $spot is free."
        } ?: "There is no car in spot $spot."
    }

    fun status(): String {
        if (this.isEmpty()) return "Parking lot is empty."

        var str = ""
        for ((index, space) in this.spaces.withIndex()) {
            space.vehicle?.let {
                str += "${index + 1} ${space.vehicle?.registration} ${space.vehicle?.colour}\n"
            }
        }

        return str.trim()
    }
}

class ParkingSpace(var vehicle: Vehicle? = null)

data class Vehicle(val registration: String, val colour: String)