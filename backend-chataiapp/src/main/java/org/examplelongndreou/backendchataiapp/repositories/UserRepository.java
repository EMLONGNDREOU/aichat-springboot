package org.examplelongndreou.backendchataiapp.repositories;

import org.examplelongndreou.backendchataiapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM public.users WHERE username = :username")

    public User findUserByUsername(@Param("username") String username);

    User findByEmail(String email);

    Optional<User> findById(Long id);

    User findByUsernameAndPassword(String username, String password);

   /* default void deleteById(Long id) {

    }

    */


    /**
    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "INSERT INTO public.users (username, email, password, name) " +
                    "VALUES (:username, :email, :password, :name) " +
                    "ON CONFLICT (email) DO UPDATE SET " +
                    "username = EXCLUDED.username, " +
                    "password = EXCLUDED.password, " +
                    "name = EXCLUDED.name " +
                    "RETURNING *"
    )
    User insertUser(@Param("username") String username,
                    @Param("email") String email,
                    @Param("password") String password,
                    @Param("name") String name);

     */
}

