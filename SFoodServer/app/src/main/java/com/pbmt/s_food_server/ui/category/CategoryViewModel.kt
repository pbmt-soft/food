package com.pbmt.s_food_server.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pbmt.s_food_server.callback.ICategoryLoadCallBack
import com.pbmt.s_food_server.common.Common
import com.pbmt.s_food_server.model.CategoryModel

class CategoryViewModel : ViewModel(), ICategoryLoadCallBack {

    private  var categoryListMutableLiveData:MutableLiveData<List<CategoryModel>>?= null
    private  var messageError:MutableLiveData<String> = MutableLiveData()
    private val categoryCallBackListener: ICategoryLoadCallBack

    init {
        categoryCallBackListener=this
    }

    fun getCategoryList():MutableLiveData<List<CategoryModel>>
    {
        if (categoryListMutableLiveData ==null){
            categoryListMutableLiveData= MutableLiveData()
            messageError= MutableLiveData()
            loadCategoryList()
        }
        return categoryListMutableLiveData!!
    }
    fun getMessageError():MutableLiveData<String>{
        return  messageError
    }

     fun loadCategoryList() {
        val tempList=ArrayList<CategoryModel>()
        val categoryRef= FirebaseDatabase.getInstance().getReference(Common.CATEGORY_REF)
        categoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children){
                    val model= itemSnapshot.getValue<CategoryModel>(CategoryModel::class.java)
                    model!!.menu_id=itemSnapshot.key
                    tempList.add(model)
                }
                categoryCallBackListener.onCategoryLoadSuccess(tempList)
            }

            override fun onCancelled(error: DatabaseError) {
                categoryCallBackListener.onCategoryLoadFailed(error.message)
            }

        })
    }

    override fun onCategoryLoadSuccess(categoryModelList: List<CategoryModel>) {
        categoryListMutableLiveData!!.value=categoryModelList
    }

    override fun onCategoryLoadFailed(message: String) {
        messageError.value=message
    }
}