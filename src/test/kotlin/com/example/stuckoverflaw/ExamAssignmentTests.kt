package com.example.stuckoverflaw

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

class ExamAssignmentTests
{
    @Test
    fun `Lix test`()
    {
        val content = "Kotlin is a language that supports both imperative, functional and object oriented programming. The official site is full of documentation."

        assertThat(content.lix()).isEqualTo(50)
    }
}