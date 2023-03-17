import org.scalatest.funsuite.AnyFunSuite

class WordleTest extends AnyFunSuite {

  /*Rule 1: must be guessed in 6 attempts or less
  Rule 2: Every word that is entered must be in the word list
  Rule 3: If the letter is correct, the color will turn green
  Rule 4: If the letter is correct, but at the wrong place, the letter turns yellow
  Rule 5: An incorrect letter turns grey
  Rule 6: Letters can be used more than 1 time
  * */

  //Rule 1 test case
  //Test for guessing correct on the 6 attempt
  //test for using more then 6 attempts
test("hasNoMoreAttempts_AttemptsAre1_ReturnTrue"){
  //Arrange
  val attempts = 1
  val expectedResult = true
  //Act
  val result = Wordle.hasNoMoreAttempts(attempts)
  //Assert
  assert(expectedResult == result)
}

  //Rule 2 test case
  //Test for using every word on the list
  //test for using a word on the list
  //Test for using a word not in the list
  test("isNotAcceptableWord_UsingNotAcceptableWord_ReturnTrue") {
    //Arrange
    val wordArray = Array("AHEAD", "ANTIC", "ASHEN", "PROOF", "SHIED", "ALIGN")
    val word = "SHORT"
    val expectedResult = true

    //Act
    val result = Wordle.isNotAcceptableWord(word, wordArray)
    //Assert
    assert(expectedResult == result)
  }

  //Rule 3 test case
  //Test for if a correct letter turns green
  test("printWordWithHints_InputRightLettersAtRightPlace_ReturnGreenLetters") { //bad test, also uses the color grey on expected result
    //Arrange
    val ANSI_RESET = "\u001B[0m"
    val ANSI_GREEN = "\u001B[32m"
    val correctWord = "HELLO"
    val word = "HELLO"

    val expectedResult = word.zipWithIndex.map { letter =>
      val color = Some(ANSI_GREEN)
      color.map(c => s"$c${letter._1}$ANSI_RESET").getOrElse(letter._1)
    }.mkString

    //Act
    val result = Wordle.printWordWithHints(word, correctWord)
    //Assert
    assert(expectedResult == result)
  }
  //Rule 4 test case
  //Test for a right letter being at the wrong place turns yellow
  test("printWordWithHints_InputRightLettersAtWrongPlace_ReturnYellowLetters") { //bad test, also uses the color grey on expected result
    //Arrange
    val ANSI_RESET = "\u001B[0m"
    val ANSI_YELLOW = "\u001B[33m"
    val correctWord = "AEIOU"
    val word = "OUAEI"

    val expectedResult = word.zipWithIndex.map { letter =>
      val color = Some(ANSI_YELLOW)
      color.map(c => s"$c${letter._1}$ANSI_RESET").getOrElse(letter._1)
    }.mkString

    //Act
    val result = Wordle.printWordWithHints(word, correctWord)
    //Assert
    assert(expectedResult == result)
  }


  //Rule 5 test case
  //test for an incorrect letters turning grey
  test("printWordWithHints_InputWrongLetters_ReturnGreyLetter") {
    //Arrange
    val correctWords = "ARRAY"
    val word = "HELLO"
    val expectedResult = "HELLO"

    //Act
    val result = Wordle.printWordWithHints(word, correctWords)
    //Assert
    assert(expectedResult == result)
  }
  //Rule 6 test case
  //test for a word containing the same letter twice
  //Test for a word containing the same letter 3 times
  test("guessHasWrongLength_UsingAWordWith2SameLetters_ReturnTrue") {
    //Arrange
    val word = "START"
    val expectedResult = false

    //Act
    val result = Wordle.guessHasWrongLength(word)
    //Assert
    assert(expectedResult == result)
  }


}
