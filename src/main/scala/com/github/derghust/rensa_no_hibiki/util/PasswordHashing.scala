package com.github.derghust.rensa_no_hibiki.util

import de.mkammerer.argon2.Argon2Factory

object PasswordHashing:
  val argon2 = Argon2Factory.create();

  /** Hash data with argon2 algorithm.
    *
    * Example:
    *
    * {{{
    *   val myHashedPassword = PasswordHashing.hash("secret")
    * }}}
    *
    * @param data
    *   Data in a String format.
    * @param iterations
    *   Number of iterations. Default 10.
    * @param memory
    *   Number of memory. Default 65536.
    * @param parallelism
    *   Number of parallelism. Default 1.
    * @return
    *   Return Hashed data in a String format.
    */
  def hash(
    data: String,
    iterations: Int = 10,
    memory: Int = 65536,
    parallelism: Int = 1
  ): String =
    val convertedData = data.toCharArray
    val hash          = argon2.hash(iterations, memory, parallelism, convertedData)
    argon2.wipeArray(convertedData);
    hash

  /** Verify if data and hash are equally hashed.
    *
    * Example:
    *
    * {{{
    *   PasswordHashing.verify("secret", "SOME_HASH_DATA").toEither.map(log("yeah"))
    * }}}
    *
    * @param data
    *   Data in a String format.
    * @param hash
    *   Hashed data in a String format.
    * @return
    *   Return true for equal hash, otherwise return false.
    */
  def verify(data: String, hash: String): Boolean =
    argon2.verify(hash, data.toCharArray)
