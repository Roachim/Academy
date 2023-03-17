import org.scalatest.funsuite.AnyFunSuite

import java.net.{URL, URLConnection}
import java.nio.file.Paths
import org.scalamock.proxy
import org.scalamock.proxy.ProxyMockFactory

import java.io.ByteArrayInputStream

class PDFDownloaderTest extends AnyFunSuite with ProxyMockFactory {

//check if AL link works

  //check if AM link works

  //download PDF
  test("downloadIfFileNewOrUpdated_Download1PDF_ReturnTrue") { //can i test without downloading or creating a file?
    //Arrange
    //val connection = new URL("http://arpeissig.at/wp-content/uploads/2016/02/D7_NHB_ARP_Final_2.pdf").openConnection()
    //val connectionmock = mock[URLConnection]

    //(connectionmock.getInputStream _).().returns(status.ok)
    val destination = "C:\\Users\\KOM\\Desktop\\Opgaver\\Unittest Datasolvr\\dfghjk"
    val filename = "TestFile"
    val file = Paths.get(destination, filename + ".pdf").toFile
    val contentLength = 1
    val expectedResult = true
    //Act
    def testInputStreamProvider(connection: URLConnection) = new ByteArrayInputStream("test".getBytes)
    val result = PDFDownloader.downloadIfFileNewOrUpdated(file, contentLength, null, testInputStreamProvider )
    //Assert
    assert(expectedResult == result)
  }

  //Name PDF using BRN number

  //Make a readme with a list of downloaded pdf's

  //Be quick


}
