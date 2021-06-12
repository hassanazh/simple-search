package com.rocker

import com.rocker.service.FileReader

object Main extends App {

  println("Starting application")

  FileReader
    .readFiles(args)
    .fold(
        println,
        file => FileReader.iterate(FileReader.index(file, args))
      )

}
