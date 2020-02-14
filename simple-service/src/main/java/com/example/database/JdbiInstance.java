package com.example.database;

import com.example.model.DatabaseCredential;
import org.jdbi.v3.core.Jdbi;

public class JdbiInstance {
    protected static Jdbi jdbi;

    public static Jdbi getInstance() {
        if (jdbi == null) {
            jdbi = Jdbi.create(
                    "jdbc:mysql://" + DatabaseCredential.getHost() + ":" + DatabaseCredential.getPort() + "/" + DatabaseCredential.getDatabase() + "?serverTimezone=UTC",
                    DatabaseCredential.getUser(),
                    DatabaseCredential.getPassword()
            );
        }

        return jdbi;
    }
}
