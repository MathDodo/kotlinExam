package com.example.stuckoverflaw

import org.springframework.data.repository.CrudRepository

interface PostRepository : CrudRepository<Post, Long>
{
    fun findBySlug(slug: String): Post?
    fun findAllByOrderByAddedAtDesc(): Iterable<Post>
}

interface UserRepository : CrudRepository<User, Long>
{
    fun findByLogin(login: String): User
}