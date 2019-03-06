package com.example.stuckoverflaw

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SiteConfig
{
    @Bean
    fun databaseInitializer(userRepo: UserRepository,
                            postRepo: PostRepository) = ApplicationRunner {

        val smaldini = userRepo.save(User("smaldini", "Stefani", "Maldini"))
        val dodo = userRepo.save(User("dodo", "Mathias", "Larsen", "That is me"))

        postRepo.save(
                    Post(
                        title = "Java popularity",
                        headline = "Java is popular",
                        content = "Java is a very popular language used in all kinds of applications.",
                        author = smaldini
                        )

                     )

        postRepo.save(
                    Post(
                        title = "Kotlin description",
                        headline = "Kotlin documentation",
                        content = "Kotlin is a language that supports both imperative, functional and object oriented programming. The official site is full of documentation.",
                        author = smaldini
                        )
                     )
    }
}