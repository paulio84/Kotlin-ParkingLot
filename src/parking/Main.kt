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
                println(parkingLot?.park(Vehicle(registration, colour)) ?: "Sorry, a parking lot has not been created.")
            }
            "leave" -> {
                val parkingSpace = input[1].toInt()
                println(parkingLot?.leave(parkingSpace) ?: "Sorry, a parking lot has not been created.")
            }
            "reg_by_color" -> {
                val colour = input[1]
                println(parkingLot?.findRegByColour(colour) ?: "Sorry, a parking lot has not been created.")
            }
            "spot_by_color" -> {
                val colour = input[1]
                println(parkingLot?.findSpotByColour(colour) ?: "Sorry, a parking lot has not been created.")
            }
            "spot_by_reg" -> {
                val reg = input[1]
                println(parkingLot?.findSpotByReg(reg) ?: "Sorry, a parking lot has not been created.")
            }
            "status" -> println(parkingLot?.status() ?: "Sorry, a parking lot has not been created.")
            "exit" -> isExit = true
        }
    }
}

class ParkingLot(numSpaces: Int) {
    private val spaces: Collection<ParkingSpace>

    init {
        this.spaces = List(numSpaces) { ParkingSpace(it + 1) }.toSet()
    }

    fun park(vehicle: Vehicle): String {
        val index = this.findNextAvailableParkingSpace()

        return if (index >= 0) {
            val parkingSpace = this.spaces.elementAt(index)
            parkingSpace.vehicle = vehicle

            "${vehicle.colour} car parked in spot ${parkingSpace.spot}."
        } else {
            "Sorry, the parking lot is full."
        }
    }

    private fun findNextAvailableParkingSpace(): Int = this.spaces.indexOf(this.spaces.find { it.vehicle == null })

    fun leave(spot: Int): String {
        val parkingSpace = this.spaces.find { it.spot == spot }

        return parkingSpace?.vehicle?.let {
            parkingSpace.vehicle = null

            "Spot $spot is free."
        } ?: "There is no car in spot $spot."
    }

    fun status(): String {
        return if (this.spaces.isNotEmpty()) {
            var str = ""
            for (parkingSpace in this.spaces) {
                parkingSpace.vehicle?.let {
                    str += "${parkingSpace.spot} ${parkingSpace.vehicle?.registration} ${parkingSpace.vehicle?.colour}\n"
                }
            }

            str.trim()
        } else {
            "Parking lot is empty."
        }
    }

    fun findRegByColour(colour: String): String {
        val parkingSpaces = this.spaces.filter { it.vehicle?.colour.equals(colour, ignoreCase = true) }
        return if (parkingSpaces.isEmpty()) {
            "No cars with color ${colour.toUpperCase()} were found."
        } else {
            var str = ""
            for (vehicle in parkingSpaces) {
                str += if (str.isBlank()) vehicle.vehicle?.registration else ", ${vehicle.vehicle?.registration}"
            }
            str
        }
    }

    fun findSpotByColour(colour: String): String {
        val parkingSpaces = this.spaces.filter { it.vehicle?.colour.equals(colour, ignoreCase = true) }
        return if (parkingSpaces.isEmpty()) {
            "No cars with color ${colour.toUpperCase()} were found."
        } else {
            var str = ""
            for (vehicle in parkingSpaces) {
                str += if (str.isBlank()) vehicle.spot else ", ${vehicle.spot}"
            }
            str
        }
    }

    fun findSpotByReg(reg: String): String {
        val parkingSpace = this.spaces.find { it.vehicle?.registration.equals(reg, ignoreCase = true) }
        parkingSpace?.let {
            return "${parkingSpace.spot}"
        }

        return "No cars with registration number ${reg.toUpperCase()} were found."
    }
}

class ParkingSpace(val spot: Int, var vehicle: Vehicle? = null)

data class Vehicle(val registration: String, val colour: String)