package com.example.stuckoverflaw

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class HtmlController(private val postRepo : PostRepository, private  val userRepo : UserRepository)
{
    @GetMapping("/")
    fun home(model: Model) : String
    {
        model["title"] = "Stuckoverflaw"
        model["posts"] = postRepo.findAllByOrderByAddedAtDesc().map { it.render() }
        //The name of the mustache file
        return "home"
    }

    @GetMapping("/post/{slug}")
    fun article(@PathVariable slug: String, model: Model): String {
        val post = postRepo
                .findBySlug(slug)
                ?.render()
                ?: throw IllegalArgumentException("Wrong post slug provided")
        model["title"] = post.title
        model["post"] = post
        //Getting the amount of words by getting the list, from the split method, where I split with the char of space
        model["wordCount"] = post.content.split(' ').count()

        //Getting the lix value of the content
        val contentLix = post.content.lix()

        //Getting the category from the lix value
        var category = getCategory(contentLix)

        model["lixCount"] = contentLix.toString() + " " + category
        return "post"
    }

    @GetMapping("/users")
    fun users(model: Model): String
    {
        model["title"] = "Stuckoverflaw users"
        model["users"] = userRepo.findAll().map { it }

        return "users"
    }

    @GetMapping("/sequences")
    fun sequencer(model: Model): String
    {
        model["title"] = "Stuckoverflaw sequences"

        return "sequencer"
    }

    @PostMapping("/sequences")
    fun sequencesSubmit(@ModelAttribute theForm: TheForm) : ModelAndView
    {
        var mv = ModelAndView()

        //mv.addObject("number",theForm.sequenceID)

        var id = theForm.sequenceID.toInt()
        var size = theForm.sequenceSize.toInt()

        var sequence = generateSequence(id) {it + id}

        mv.addObject("sequence", "Result: " + sequence.take(size).joinToString())

        mv.viewName = "sequencer"

        return mv
    }


    fun getCategory(contentLix : Int) : String
    {
        //Switch case on contentLix with ranges and zero which is returned when there is text or no dots
        return when(contentLix)
        {
            0 -> "(Something went wrong, either the content is an empty string or it has no dots.)"
            in 1..24 -> "(Easy to read.)"
            in 25..34 -> "(Easy for experienced readers.)"
            in 35..44 -> "(For experienced readers.)"
            in 45..54 -> "(Hard to read)"
            else -> "(Really hard to read.)"
        }
    }

    fun Post.render() = RenderedPost(
            slug,
            title,
            headline,
            content,
            author,
            addedAt.format()
    )

    data class RenderedPost(
            val slug: String,
            val title: String,
            val headline: String,
            val content: String,
            val author: User,
            val addedAt: String)

    data class TheForm(val sequenceID: Number, val sequenceSize: Number)
}