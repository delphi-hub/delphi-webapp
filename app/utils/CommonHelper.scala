
package utils

trait CommonHelper {
    def addHttpProtocolIfNotExist(url: String): String ={
      var tempUrl:String = url
      val exist = url.contains("http://")
      if(! exist) tempUrl = "http://" + url
      tempUrl
    }
}
