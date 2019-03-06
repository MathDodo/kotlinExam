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
class HtmlController(private val postRepo : PostRepository, private val userRepo : UserRepository)
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
    fun sequencesSubmit(@ModelAttribute sequenceForm: SequenceForm) : ModelAndView
    {
        var mv = ModelAndView()

        var id = sequenceForm.sequenceID.toInt()
        var size = sequenceForm.sequenceSize.toInt()

        var sequence = generateSequence(id) {it + id}

        mv.addObject("sequence", "Result: " + sequence.take(size).joinToString())

        mv.viewName = "sequencer"

        return mv
    }

    @GetMapping("/register")
    fun register(model: Model): String
    {
        model["title"] = "Stuckoverflaw register"

        return "register"
    }

    @PostMapping("/register")
    fun registerSubmit(@ModelAttribute registerForm: RegisterForm) : ModelAndView
    {
        var mv = ModelAndView()

        if(registerForm.firstname != "" && registerForm.lastname != "" && registerForm.login != "")
        {
            userRepo.save(User(registerForm.login, registerForm.firstname, registerForm.lastname, registerForm.description))
        }

        mv.viewName = "register"

        return mv
    }

    @GetMapping("/postwriting")
    fun postwriting(model: Model): String
    {
        model["title"] = "Stuckoverflaw write post"

        return "writepost"
    }

    @PostMapping("/postwriting")
    fun postwritingSubmit(@ModelAttribute postWriting: PostWriting) : ModelAndView
    {
        var mv = ModelAndView()

       if(postWriting.title != "" && postWriting.content != "" && postWriting.headline != "")
       {
            postRepo.save(Post(postWriting.title, postWriting.headline, postWriting.content, userRepo.findByLogin("dodo")))
       }

        mv.viewName = "writepost"

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

    data class SequenceForm(val sequenceID: Number, val sequenceSize: Number)

    data class RegisterForm(val firstname : String, val lastname : String, val login : String, val description : String?)

    data class PostWriting( var title: String, var headline: String, var content: String)
}