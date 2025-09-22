import Head from "next/head";
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import { useEffect, useState } from "react";
import axios from "axios";
import userService from "@/services/userService";

export default function Profile() {
    const [user, setUser] = useState({ username: "...", password: "...", email: "...", name: "...", role: "..." });
    const [isEditing, setIsEditing] = useState(false);
    const [editedUser, setEditedUser] = useState(user);

    useEffect(() => {
        const fetchUser = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                window.location.href = "/login";
                return;
            }

            const jwtPayload = userService.jwtPayload(token);
            let userData = await axios.get(`http://localhost:8080/users/${jwtPayload.sub}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            setUser(userData.data);
            setEditedUser(userData.data);
        };

        fetchUser();
    }, []);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEditedUser({ ...editedUser, [name]: value });
    };

    const handleSave = async () => {
        const token = localStorage.getItem("token");
        try {
            const res = await axios.put(
                `http://localhost:8080/users/${user.id}`,
                editedUser,
                { headers: { Authorization: `Bearer ${token}` } }
            );
// check
            const updatedUser = res.data;
            const isUsernameChanged = user.username !== updatedUser.username;
            const isPasswordChanged = user.password !== updatedUser.password;

            setUser(updatedUser);
            setIsEditing(false);

            if (isUsernameChanged || isPasswordChanged) {
                localStorage.removeItem("token");
                window.location.href = "/login";
            }
        } catch (err) {
            console.error("Update failed", err);
        }
    };

//Aufgabenliste
    const handleDelete = async () => {
        const confirmed = window.confirm("Are you sure you want to delete your profile? This cannot be undone.");
        if (!confirmed) return;

        const token = localStorage.getItem("token");
        try {
            await axios.delete(`http://localhost:8080/users/${user.id}`, {
                headers: { Authorization: `Bearer ${token}` },
            });

            localStorage.removeItem("token");
            window.location.href = "/login";
        } catch (err) {
            console.error("Delete failed", err);
            alert("Failed to delete profile.");
        }
    };


    return (
        <div className="home profile-page">
            <div className="profile-wrapper">
                <header className="profile-header">
                    <img
                        src={user.avatar || "/images/default-avatar.png"}
                        alt={`${user.name}'s avatar`}
                        className="profile-avatar"
                    />
                    <div className="profile-header-text">
                        <h1 className="profile-title"> Profile</h1>
                        <p className="profile-name">{user.name}</p>
                        <span className={`profile-role-badge profile-role--${user.role?.toLowerCase() || "default"}`}>
              {user.role}
            </span>
                    </div>
                </header>

                <dl className="profile-fields">
                    <div className="profile-field">
                        <dt>Name</dt>
                        <dd>
                            {isEditing ? (
                                <input type="text" name="name" value={editedUser.name} onChange={handleChange} />
                            ) : (
                                user.name
                            )}
                        </dd>
                    </div>

                    <div className="profile-field">
                        <dt>Username</dt>
                        <dd>
                            {isEditing ? (
                                <input type="text" name="username" value={editedUser.username} onChange={handleChange} />
                            ) : (
                                user.username
                            )}
                        </dd>
                    </div>

                    <div className="profile-field">
                        <dt>Password</dt>
                        <dd>
                            {isEditing ? (
                                <input type="text" name="password" value={editedUser.password} onChange={handleChange} />
                            ) : (
                                user.password
                            )}
                        </dd>
                    </div>

                    <div className="profile-field">
                        <dt>Email</dt>
                        <dd>
                            {isEditing ? (
                                <input type="email" name="email" value={editedUser.email} onChange={handleChange} />
                            ) : (
                                <a href={`mailto:${user.email}`} className="profile-email-link">
                                    {user.email}
                                </a>
                            )}
                        </dd>
                    </div>

                </dl>

                <div className="profile-ctas">
                    {isEditing ? (
                        <>
                            <button className="profile-btn profile-btn-primary" onClick={handleSave}>
                                Save
                            </button>
                            <button className="profile-btn profile-btn-secondary" onClick={() => setIsEditing(false)}>
                                Cancel
                            </button>
                        </>
                    ) : (
                        <>
                            <button className="profile-btn profile-btn-primary" onClick={() => setIsEditing(true)}>
                                Edit Profile
                            </button>
                            <button className="profile-btn profile-btn-danger" onClick={handleDelete}>
                                Delete Profile
                            </button>

                            <button className="profile-btn profile-btn-secondary" onClick={() => window.location.href = "/"}>
                                Back
                            </button>

                        </>
                    )}
                </div>
            </div>
        </div>
    );
}
