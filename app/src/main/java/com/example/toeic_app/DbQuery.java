package com.example.toeic_app;

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DbQuery {
    public static FirebaseFirestore g_firestore;
    public static List<CategoryModel>  g_catList = new ArrayList<>();


    // Lư thông tin userData vào FireBase
    public static void createUserData(String email,String name, MyCompleteListener myCompleteListener){
        Map<String, Object> userData = new ArrayMap<>();

        userData.put("email_id", email);
        userData.put("name", name);
        userData.put("total_score", 0);

         // Lấy bảng cần lưu
        DocumentReference userDoc = g_firestore.collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        WriteBatch batch = g_firestore.batch();

        batch.set(userDoc, userData);

        DocumentReference countDoc = g_firestore.collection("users").document("total_users");
        batch.update(countDoc, "count", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myCompleteListener.onSuccess();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }


    // Load tất cả các danh mục lên giao diện
    public static void loadCategories(MyCompleteListener completeListener){
        g_catList.clear();
        g_firestore.collection("quiz").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String , QueryDocumentSnapshot> docList = new ArrayMap<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            docList.put(doc.getId(), doc);
                        }
                        QueryDocumentSnapshot catListDoc = docList.get("categories");

                        long catCount = catListDoc.getLong("count"); // Lấy ra tổng

                        for(int i = 1; i <= catCount; i++){
                            String CatID = catListDoc.getString("cat" + String.valueOf(i)+ "_id");
                            QueryDocumentSnapshot catDoc = docList.get(CatID);
                            int noOfTest = catDoc.getLong("no_of_tests").intValue();
                            String catName = catDoc.getString("name");
                            g_catList.add(new CategoryModel( CatID, catName, noOfTest));
                        }

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

}
