import java.math.BigInteger
import java.util.Random

object DiffieHellman {
    private val random = Random()

    fun privateKey(prime: BigInteger): BigInteger = randomNumber(prime - BigInteger.ONE) + BigInteger.ONE

    fun publicKey(p: BigInteger, g: BigInteger, privKey: BigInteger): BigInteger = g.modPow(privKey, p)

    fun secret(prime: BigInteger, publicKey: BigInteger, privateKey: BigInteger): BigInteger =
        publicKey.modPow(privateKey, prime)

    private fun randomNumber(upperBound: BigInteger): BigInteger =
        BigInteger(upperBound.bitLength(), random).mod(upperBound)
}
