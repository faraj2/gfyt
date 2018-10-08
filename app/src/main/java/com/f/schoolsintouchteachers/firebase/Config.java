package com.f.schoolsintouchteachers.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Config {
    public static CollectionReference firestore= FirebaseFirestore.getInstance().collection("Schools");
    public static StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Schools");
    public static FirebaseAuth auth=FirebaseAuth.getInstance();
    public static DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Schools");

}
