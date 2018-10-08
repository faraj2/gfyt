package com.f.schoolsintouchteachers.models.realmObjects;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MyMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion==0){
            schema.get("Post1").addField("clap",boolean.class)
                    .addField("claps",Integer.class);
            oldVersion++;
        }
        if (oldVersion==1){
            schema.get("Pst").addPrimaryKey("id");
            oldVersion++;
        }
        if (oldVersion==2){
            schema.get("Pst").addField("posting",boolean.class);
            oldVersion++;
        }

    }
}
