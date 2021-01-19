package com.example.worldscrawl

import kotlin.math.pow

class MathFact(base:Double, exponent:Double) {
    private val question:String = "${base.toInt()} ^ ${exponent.toInt()}"

    private val review:String = "$question = ${base.pow(exponent).toInt()}"

    private val answer:String = "Answer: $question = ${base.pow(exponent).toInt()}"

    private var underReview:Boolean = false

    private var isSelected:Boolean = false

    fun getQuestion():String{
        return question
    }

    fun getReview():String{
        return review
    }

    fun getAnswer():String{
        return answer
    }

    fun isSelected():Boolean{
        return isSelected
    }

    fun isUnderReview():Boolean{
        return underReview
    }

    fun toggleReview(){
        underReview = !underReview
    }

    fun toggleSelect(){
        isSelected = !isSelected
    }

    fun setSelect(truthVal:Boolean){
        isSelected = truthVal
    }




}