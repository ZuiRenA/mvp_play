package project.shen.mvp_play.model

data class Response <T> (
    val code: Int,
    val result: T,
    val msg: String = ""
)