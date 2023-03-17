import scala.io.StdIn.readLine
import scala.io.Source.fromInputStream
import scala.util.Random

object Wordle {

  val ANSI_GREEN = "\u001B[32m"
  val ANSI_YELLOW = "\u001B[33m"
  val ANSI_RESET = "\u001B[0m"

  def main(args: Array[String]): Unit = {

    val words = fromInputStream(getClass.getResourceAsStream("possible_words.txt"))
      .mkString.split("\n").map(_.trim.toUpperCase)
    val correctWord = words(Random.nextInt(words.length))

    acceptGuess(6, words, correctWord)
  }

  def makeGuess(): String = {
    val guess = readLine().trim.toUpperCase.toString()
    return guess
  }
  def guessHasWrongLength(guess: String): Boolean = {
    if (guess.length != 5 || !guess.matches("[A-Z]+")) {
      true
    }
    return false
  }

  def isNotAcceptableWord(guess: String, words: Array[String]): Boolean = {
    if(!words.contains(guess)){
      return true
    }
    return false
  }
  def isCorrectWord(guess: String, correctWord: String): Boolean = {
    if(guess == correctWord){
      return true
    }
    return false
  }
  def hasNoMoreAttempts(attemptsLeft: Int): Boolean = {
    if(attemptsLeft == 1){
      return true
    }
    return false
  }

  def printWordWithHints(guess: String, correctWord: String): String = {
    val hintWord =
      guess.zipWithIndex.map { letter =>
        val color =
          if (letter._1 == correctWord(letter._2))
            Some(ANSI_GREEN)
          else if (correctWord.contains(letter._1))
            Some(ANSI_YELLOW)
          else
            None
        color.map(c => s"$c${letter._1}$ANSI_RESET").getOrElse(letter._1)
      }.mkString
    return hintWord
  }

  def acceptGuess(attemptsLeft: Int, words:  Array[String], correctWord: String): Unit = {

    print(s"Enter your guess ($attemptsLeft attempts left): ")
    var guess: String = makeGuess()

    if (guessHasWrongLength(guess)) { //have method for testing length
      println("Exactly 5 letters please!")
      acceptGuess(attemptsLeft, words, correctWord)
    }
    else if (isNotAcceptableWord(guess, words)) { //have method for testing if wrong word
      println("That is not a word!")
      acceptGuess(attemptsLeft, words, correctWord)
    }
    else {
      if (isCorrectWord(guess,correctWord)) { //have test for checking for correct word
        println("Correct!")
      }
      else {
        if (hasNoMoreAttempts(attemptsLeft)) { //method for checking amount left
          println(s"No more guesses! The word was ${correctWord}. Try again :-)")
        }
        else {
          println(printWordWithHints(guess, correctWord))
          acceptGuess(attemptsLeft - 1, words, correctWord)
        }
      }
    }
  }
}
