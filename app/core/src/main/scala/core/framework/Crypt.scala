package core.framework

import scala.concurrent.*

case class CryptKeyValuePair(
  val privateKey: Array[Byte],
  val publicKey: Array[Byte]
)

trait Crypt:
  def generateKey: Future[CryptKeyValuePair]

  def sign(key: Array[Byte], content: Array[Byte]): Future[Array[Byte]]
  def verify(key: Array[Byte], content: Array[Byte], signature: Array[Byte]): Future[Boolean]
  
  def sign(key: Array[Byte], content: String): Future[Array[Byte]] =
    sign(key, content.getBytes())

  def verify(key: Array[Byte], content: String, signature: Array[Byte]): Future[Boolean] =
    verify(key, content.getBytes(), signature)
