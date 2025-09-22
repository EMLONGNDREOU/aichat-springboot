import axios from "axios";

const API_BASE = "http://localhost:8080";

const instanceAx = axios.create({
    baseURL: API_BASE,
    headers: {
        "Content-Type": "application/json",
    },
});

// ✅ Request interceptor: attach token
instanceAx.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers["Authorization"] = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

// ✅ Response interceptor: handle 401 globally
instanceAx.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            console.warn("Unauthorized (401) – logging out...");

            // Remove token
            localStorage.removeItem("token");

            // Redirect to login
            window.location.href = "/login";
        }
        return Promise.reject(error);
    }
);

export default instanceAx;
