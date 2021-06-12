package com.rocker.service

import java.io.File

import scala.annotation.tailrec
import scala.collection.immutable.ListMap
import scala.io.StdIn.readLine
import scala.util.Try

object FileReader {

  sealed trait ReadFileError

  case object MissingPathArg extends ReadFileError

  case class NotDirectory(error: String) extends ReadFileError

  case class FileNotFound(t: Throwable) extends ReadFileError

  case class Data(fileName: String, listOfWords: List[String])

  case class Index(content: List[Data])

  def readFiles(directoryName: Array[String]): Either[ReadFileError, List[File]] = {
    for {
      path <- directoryName.headOption.toRight(MissingPathArg)
      file <- Try(new File(path))
        .fold(
          throwable => Left(FileNotFound(throwable)),
          file =>
            if (file.isDirectory) Right(file.listFiles.filter(f => f.isFile && f.getName.endsWith(".txt")).toList)
            else Left(NotDirectory(s"Path [$path] is not a directory"))
        )
    } yield file
  }

  def index(files: List[File], directoryName: Array[String]): Index = {
    val numberOfFiles = files.size

    val data = for {
      path <- directoryName.headOption
      _ = println(s"$numberOfFiles files read in directory $path ")

      listOfData = files.map(f => {
        val fileName = f.getName
        Data(fileName, scala.io.Source.fromFile("%s/%s".format(path, fileName)).getLines.flatMap(_.split("\\W+")).toList)
      })
    } yield listOfData
    if (data.isDefined) {
      Index(data.get)
    } else {
      Index(List.empty)
    }
  }

  private def calculatePercentage(matchedKeywordsSize: Int, keywordsSize: Int): Float =
    BigDecimal((matchedKeywordsSize / keywordsSize.toFloat) * 100).setScale(2, BigDecimal.RoundingMode.HALF_UP).toFloat

  @tailrec
  def iterate(indexedFiles: Index): Unit = {
    println(s"search> ")
    readLine() match {
      case ":quit" => println("Thank you!")
      case value if value.isEmpty =>
        iterate(indexedFiles)
      case searchString =>
        val keywordsResult = matchKeyWords(searchString, indexedFiles)
        if (!keywordsResult.exists(_._2 != 0f)) {
          println("no matches found")
        } else {
          keywordsResult.foreach(file => println(s"${file._1}: " + f"${file._2}" + "%"))
        }
        iterate(indexedFiles)
    }
  }

  def matchKeyWords(searchString: String, indexedFiles: Index): ListMap[String, Float] = {
    val keywords = searchString.split("\\s+")
    val matchedKeyWords = indexedFiles.content.map(f => {
      f.fileName -> keywords.filter(w => f.listOfWords.contains(w))
    }).toMap

    ListMap(matchedKeyWords
      .map(data => data._1 -> calculatePercentage(data._2.length, keywords.length))
      .toSeq
      .sortWith(_._2 > _._2): _*)
      .take(10)
  }
}
