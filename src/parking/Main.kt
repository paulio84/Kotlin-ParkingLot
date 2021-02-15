package parking

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val parkingLot = ParkingLot(numSpaces = 20)

    var input = scanner.nextLine()
    while (input.toLowerCase() != "exit") {
        when (input.substring(0, input.indexOf(' '))) {
            "park" -> {
                val (registration, colour) = input.substring(input.indexOf(' ') + 1).split(" ").toTypedArray()
                val response = parkingLot.park(Vehicle(registration, colour))
                println(response)
            }
            "leave" -> {
                val parkingSpace = input.substring(input.indexOf(' ') + 1).toInt()
                val response = parkingLot.leave(parkingSpace)
                println(response)
            }
        }
        input = scanner.nextLine()
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

    private fun findNextAvailableParkingSpace(): Int =
        this.spaces.indexOf(this.spaces.find { it.vehicle == null })

    fun leave(spot: Int): String {
        val parkingSpace = this.spaces.elementAt(spot - 1)

        return if (parkingSpace.vehicle != null) {
            parkingSpace.vehicle = null
            "Spot $spot is free."
        } else {
            "There is no car in spot $spot."
        }
    }
}

class ParkingSpace(var vehicle: Vehicle? = null)

data class Vehicle(val registration: String, val colour: String)