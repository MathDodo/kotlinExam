package com.example.stuckoverflaw

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StuckoverflawApplication

fun main(args: Array<String>)
{
	runApplication<StuckoverflawApplication>(*args)
	{
		setBannerMode(Banner.Mode.OFF)
	}
}
