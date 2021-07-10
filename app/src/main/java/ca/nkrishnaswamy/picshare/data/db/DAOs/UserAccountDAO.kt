package ca.nkrishnaswamy.picshare.data.db.DAOs

import androidx.lifecycle.LiveData
import androidx.room.*
import ca.nkrishnaswamy.picshare.data.models.UserModel
import ca.nkrishnaswamy.picshare.data.models.UserPost
import ca.nkrishnaswamy.picshare.data.models.relations.SignedInAccountWithUserPosts

@Dao
interface UserAccountDAO {

    @Query("SELECT * FROM signedInAccount LIMIT 1")
    fun retrieveCurrentLoggedInUser() : LiveData<UserModel>

    @Insert
    fun logInUser(currentUser: UserModel)

    @Query("DELETE FROM signedInAccount")
    fun signOut()

    @Update
    fun updateUser(currentUser: UserModel)

    @Insert
    fun insertPost(post: UserPost)

    @Transaction
    @Query("SELECT * FROM signedInAccount")
    fun getUserModelWithUserPosts() : LiveData<List<SignedInAccountWithUserPosts>>

}