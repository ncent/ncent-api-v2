package main.services.user_account

import kotlinserverless.framework.services.SOAResult
import kotlinserverless.framework.services.SOAResultType
import kotlinserverless.framework.services.SOAServiceInterface
import main.daos.*
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Validate the accuracy of the passed crypto pub/priv in the UserAccount
 */
object ValidateCryptoKeyPairService: SOAServiceInterface<CryptoKeyPair> {
    override fun execute(caller: Int?, data: Any?, params: Map<String, String>?) : SOAResult<CryptoKeyPair> {
        var result = SOAResult<CryptoKeyPair>(
            SOAResultType.FAILURE,
            "",
            null
        )
        val publicKey = params!!["publicKey"]!!
        val privateKey = params!!["privateKey"]!!
        val cryptoKeyPair = CryptoKeyPair.find {
            CryptoKeyPairs.publicKey eq publicKey
            CryptoKeyPairs.encryptedPrivateKey eq CryptoKeyPair.encryptPrivateKey(privateKey)
        }
        if(cryptoKeyPair.empty()) {
            result.message = "Invalid key pair"
        } else {
            result.data = cryptoKeyPair.first()
            result.result = SOAResultType.SUCCESS
        }
        return result
    }
}