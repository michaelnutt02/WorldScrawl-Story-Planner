package com.example.profilepage

data class ProfileDetail(var type: TYPE, var title: String = "", var body: String = "", var isSelected: Boolean = false) {
    enum class TYPE {
        SINGLE,
        PARAGRAPH,
        CATEGORY,
        TAGS
    }
}
