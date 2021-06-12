package com.rocker.service

import com.rocker.service.FileReader.{MissingPathArg, NotDirectory, index, matchKeyWords, readFiles}
import org.scalatest.EitherValues
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.File

class FileReaderTest extends AnyFlatSpec with EitherValues with Matchers {
  it should "return error for empty argument" in {
    readFiles(Array.empty).left.value shouldBe MissingPathArg
  }

  it should "return only txt files" in {
    readFiles(Array("src/test/resources/foo/bar")).value.length shouldBe 2
  }

  it should "return error for directory not found" in {
    readFiles(Array("/wrongPath")).left.value shouldBe NotDirectory("Path [/wrongPath] is not a directory")
    readFiles(Array("src/test/resources/foo/bar/sample3.conf")).left.value shouldBe NotDirectory("Path [src/test/resources/foo/bar/sample3.conf] is not a directory")
  }

  it should "return index of 2 contents" in {
    val files = List(new File("src/test/resources/foo/bar/sample1.txt"), new File("src/test/resources/foo/bar/sample2.txt"))
    index(files, Array("src/test/resources/foo/bar")).content.length shouldBe 2
  }

  it should "return no matches found" in {
    val files = List(new File("src/test/resources/foo/bar/sample1.txt"), new File("src/test/resources/foo/bar/sample2.txt"))
    val keywordsResult = matchKeyWords("Q R S T", index(files, Array("src/test/resources/foo/bar")))
    keywordsResult.exists(_._2 != 0f) shouldBe false
  }

  it should "return sample1.txt first" in {
    val files = List(new File("src/test/resources/foo/bar/sample1.txt"), new File("src/test/resources/foo/bar/sample2.txt"))
    val keywordsResult = matchKeyWords("A B C E F G", index(files, Array("src/test/resources/foo/bar")))
    keywordsResult.size shouldBe 2
    keywordsResult.head._1 shouldBe "sample1.txt"
    keywordsResult.head._2 shouldBe 83.33f
    keywordsResult.tail.head._1 shouldBe "sample2.txt"
    keywordsResult.tail.head._2 shouldBe 50.0f
  }

  it should "return sample2.txt first" in {
    val files = List(new File("src/test/resources/foo/bar/sample1.txt"), new File("src/test/resources/foo/bar/sample2.txt"))
    val keywordsResult = matchKeyWords("E F G H I L", index(files, Array("src/test/resources/foo/bar")))
    keywordsResult.size shouldBe 2
    keywordsResult.head._1 shouldBe "sample2.txt"
    keywordsResult.head._2 shouldBe 100.0f
    keywordsResult.tail.head._1 shouldBe "sample1.txt"
    keywordsResult.tail.head._2 shouldBe 33.33f
  }
}
