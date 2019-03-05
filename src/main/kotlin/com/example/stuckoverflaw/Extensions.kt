package com.example.stuckoverflaw

import java.time.LocalDateTime
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import java.util.*

fun LocalDateTime.format() = this.format(englishDateFormatter)

private val daysLookup = (1..31).associate { it.toLong() to getOrdinal(it) }

private val englishDateFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd")
        .appendLiteral(" ")
        .appendText(ChronoField.DAY_OF_MONTH, daysLookup)
        .appendLiteral(" ")
        .appendPattern("yyyy")
        .toFormatter(Locale.ENGLISH)

private fun getOrdinal(n: Int) = when {
    n in 11..13 -> "${n}th"
    n % 10 == 1 -> "${n}st"
    n % 10 == 2 -> "${n}nd"
    n % 10 == 3 -> "${n}rd"
    else -> "${n}th"
}

fun String.toSlug() = toLowerCase()
        .replace("\n", " ")
        .replace("[^a-z\\d\\s]".toRegex(), " ")
        .split(" ")
        .joinToString("-")
        .replace("-+".toRegex(), "-")

fun String.lix() : Int
{
    //Splitting to get the amount of words
    val words = split(' ')

    //Setting o for the amount of words
    val o : Int = words.count()

    //Creating p and l
    var p = 0f
    var l = 0

    //Iterating through the words in the string
    for (i in 0 until(o))
    {
        //Checking if the word is more than 6 characters (coma and dot are counted as characters)
        if(words[i].length > 6)
        {
            l++
        }

        //If there is a dot in the word then it should be at the end so checking the last character for being a dot
        if(words[i][words[i].length - 1] == '.')
        {
            p++
        }
    }

    if(o == 0 || p == 0f)
    {
        return 0
    }

    return (o/p + l*100f/o).toInt()
}