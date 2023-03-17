import org.apache.commons.io.{FileUtils, IOUtils}
import org.apache.poi.ss.usermodel.{Workbook, WorkbookFactory}

import java.io.{File, InputStream}
import java.net.{URL, URLConnection}
import java.nio.file.Paths
import java.sql.Connection
import java.util.concurrent.atomic.AtomicInteger
import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.immutable.ParSeq
import scala.jdk.CollectionConverters._
import scala.util.Try

case class DownloadElement(brNum: String, url: String, alternateUrl: String, downloaded: Boolean)

object PDFDownloader {

  val columnALIndex = 37
  val columnAMIndex = 38
  val destination2 = "/Users/mhornbech/Desktop/pdfs" // <------------------- won't work here?
  val destination = "/Users/*/Desktop/pdfs"

  def main(args: Array[String]): Unit = {

    val workbook = createWorkbookFromXLSX("GRI_2017_2020 (1).xlsx")
    val rows = makeListFromWorkbook(workbook)

    val rowsProcessed = new AtomicInteger(0)
    val results = rows.par.map { row =>
      val status = Try(downloadPdf(row.url, row.brNum)).orElse(Try(downloadPdf(row.alternateUrl, row.brNum))).toOption.nonEmpty
      val count = rowsProcessed.addAndGet(1)
      if (count % 10 == 0) {
        println(s"Processed $count rows of ${rows.length} (${Math.floor(count.toDouble*100/rows.length).toInt}%)")
      }
      row.copy(downloaded = status)
    }

    writeToFile(results)
  }

  def writeToFile(results:  ParSeq[DownloadElement]): Unit= {
    FileUtils.writeStringToFile(
      Paths.get(destination, "status.csv").toFile,
      "Navn;Status\n" + results.map(r => s"${r.brNum};${if (r.downloaded) "Downloadet" else "Ikke downloadet"}").mkString("\n"),
      "UTF-8")
  }

  def createWorkbookFromXLSX(xlsxFileName: String): Workbook = {
    val workbook = WorkbookFactory.create(getClass.getResourceAsStream(xlsxFileName))
    return workbook
  }

  def makeListFromWorkbook(workbook: Workbook): List[DownloadElement] = {
    val reVal = workbook.sheetIterator().next().rowIterator().asScala.drop(1).map { r =>
      DownloadElement(
        r.getCell(0).getStringCellValue,
        r.getCell(columnALIndex).getStringCellValue,
        r.getCell(columnAMIndex).getStringCellValue,
        downloaded = false)
    }.toList

    return reVal
  }

  def downloadPdf(url: String, filename: String) = {

    val connection = openConnection(url)

    lazy val contentLength = connection.getContentLength //use getContentLengthLong?
    val file = Paths.get(destination, filename + ".pdf").toFile
//    if (!file.exists() || (contentLength != -1 && FileUtils.sizeOf(file) != contentLength)) {
//      FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(connection.getInputStream))
//    }
    def myProvider(c: URLConnection) = c.getInputStream
    downloadIfFileNewOrUpdated(file, contentLength, connection, myProvider)

    filename
  }

  def openConnection(url: String): URLConnection = {
    val connection = new URL(url).openConnection()
    connection.setConnectTimeout(10000)
    connection.setReadTimeout(10000)

    if (connection.getContentType == "application/pdf") //hey hvorfor fejl når pdf? Vil vi ikke have pdf?
      throw new Exception("Url does not point to a PDF file")

    return connection
  }

  def downloadIfFileNewOrUpdated(file: File, contentLength: Int, connection: URLConnection,
                                 inputStreamProvider: URLConnection => InputStream, WriteByteArray: (File, URLConnection) => Unit): Boolean = {
    if (!file.exists() || (contentLength != -1 && FileUtils.sizeOf(file) != contentLength)) {
      FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(inputStreamProvider(connection)))
      WriteByteArray(file, IOUtils.toByteArray(inputStreamProvider(connection)))
      return true
    }
    return false
  }
}
