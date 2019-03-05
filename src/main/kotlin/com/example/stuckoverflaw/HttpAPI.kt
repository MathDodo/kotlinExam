package com.example.stuckoverflaw

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/post")
class PostController(private val postRepo: PostRepository)
{
    @GetMapping("/")
    fun findAll() = postRepo.findAllByOrderByAddedAtDesc()

    @GetMapping("/{slug}")
    fun findOne(@PathVariable slug: String) =
            postRepo.findBySlug(slug) ?: throw IllegalArgumentException("Wrong post slug provided")

}

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepo: UserRepository)
{

    @GetMapping("/")
    fun findAll() = userRepo.findAll()

    @GetMapping("/{login}")
    fun findOne(@PathVariable login: String) = userRepo.findByLogin(login)
}
