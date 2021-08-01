package ca.nkrishnaswamy.picshare.viewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ca.nkrishnaswamy.picshare.data.db.DAOs.UserAccountDAO
import ca.nkrishnaswamy.picshare.data.db.roomDbs.CurrentLoggedInUserCache
import ca.nkrishnaswamy.picshare.data.models.UserModel
import ca.nkrishnaswamy.picshare.data.models.UserPost
import ca.nkrishnaswamy.picshare.data.models.relations.SignedInAccountWithUserPosts
import ca.nkrishnaswamy.picshare.repositories.SignedInUserAccountRepository

class SignedInUserViewModel(application: Application) : AndroidViewModel(application) {

    private val userAccountDao : UserAccountDAO = CurrentLoggedInUserCache.getInstance(application).userAccountDAO()
    private val repository = SignedInUserAccountRepository(userAccountDao)
    private var livedataUser : LiveData<UserModel> = repository.getCurrentLoggedInUser()
    private var livedataPostList : LiveData<List<SignedInAccountWithUserPosts>> = repository.getPosts()

    fun getCurrentLoggedInUser(): LiveData<UserModel> {
        return livedataUser
    }

    suspend fun updateUser(context: Context, account: UserModel, idToken: String) : Boolean {
        return repository.updateUser(context, account, idToken)
    }

    suspend fun logInUser(context: Context, idToken: String) : Boolean {
        return repository.logInUser(context, idToken)
    }

    suspend fun registerUser(context: Context, account: UserModel, idToken: String) : Boolean {
        return repository.registerUser(context, account, idToken)
    }

    private fun deleteAccountFromCache(context : Context) {
        deleteAllPosts()
        repository.deleteAccountFromCache(context)
    }

    fun logOutUser(context: Context) {
        deleteAccountFromCache(context)
    }

    suspend fun deleteAccount(context: Context, idToken: String) : Boolean {
        deleteAccountFromCache(context)
        return repository.deleteAccountFromServer(idToken)
    }

    fun addPost(post : UserPost) {
        repository.addPost(post)
    }

    fun getPosts(): LiveData<List<SignedInAccountWithUserPosts>> {
        return livedataPostList
    }

    fun deletePost(post: UserPost) {
        repository.deletePost(post)
    }

    private fun deleteAllPosts() {
        repository.deleteAllPosts()
    }
}