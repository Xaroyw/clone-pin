package ca.nkrishnaswamy.picshare.repositories

import androidx.lifecycle.LiveData
import ca.nkrishnaswamy.picshare.data.db.DAOs.UserAccountDAO
import ca.nkrishnaswamy.picshare.data.models.UserModel

class SignedInUserAccountRepository(private val accountDao: UserAccountDAO) {

    fun getCurrentLoggedInUser() : LiveData<UserModel> {
        return accountDao.retrieveCurrentLoggedInUser()
    }

    fun logInUser(account: UserModel) {
        accountDao.logInUser(account)
    }

    fun signOut() {
        accountDao.signOut()
    }

}