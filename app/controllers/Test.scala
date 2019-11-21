import java.io._

import org.apache.commons._
import org.apache.http._
import org.apache.http.client._
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import java.util.ArrayList

import org.apache.http.message.BasicNameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import com.google.gson.Gson
import scalaj.http.{Http, HttpOptions}

class Person(){

//object HttpJsonPostTest extends App {

  // create our object as a json string
  //val spock = new Person("[using KeyStore]=25")
  //val spockAsJson = new Gson().toJson(spock)
 // println(spockAsJson)

  // add name value pairs to a post object
  //val post = new HttpPost("https://delphi.cs.uni-paderborn.de/api/search")
  //val nameValuePairs = new ArrayList[NameValuePair]()
 // nameValuePairs.add(new BasicNameValuePair("JSON", spockAsJson))
 // post.setEntity(new UrlEncodedFormEntity(nameValuePairs))

  // send the post request
 // val client = new DefaultHttpClient
 // val response = client.execute(post)
 // println("--- HEADERS ---")
  //response.getAllHeaders.foreach(arg => println(arg))

  val result = Http("https://delphi.cs.uni-paderborn.de/api/search").postData("""{"query":"[using KeyStore]=25"}""")
    .header("Content-Type", "application/json")
    .header("Charset", "UTF-8")
    .option(HttpOptions.readTimeout(10000)).asString

  println(result)


}