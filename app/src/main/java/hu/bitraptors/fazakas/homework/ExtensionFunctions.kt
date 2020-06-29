package hu.bitraptors.fazakas.homework

fun Double.format(digits: Int) = "%.${digits}f".format(this)