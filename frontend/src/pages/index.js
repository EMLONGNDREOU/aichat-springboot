import Head from "next/head";
import Image from "next/image";
import { Geist, Geist_Mono } from "next/font/google";
import styles from "@/styles/Home.module.css";

import {useCallback, useEffect, useRef, useState} from "react";
import axios from "axios";


const seedThreads = [
    { id: 1,   title: "General",         unread: 0 },
    { id: 2,   title: "", unread: 2 },
    { id: 3,   title: "", unread: 0 },
];

const seedMessages = {
    1: [
        { id: "m1", fromSelf: false, content: "Good morning team! ☕",  createdAt: Date.now() - 60_000 },
        { id: "m2", fromSelf: true,  content: "Morning! Ready for stand‑up.", createdAt: Date.now() - 30_000 },
    ],
    2: [

    ],
    3: [],
};



export default function Home() {


    /* refs */
    const messagesRef = useRef(null);

    /* state */
    const [threads, setThreads]           = useState([]);
    const [activeThreadId, setActiveThreadId] = useState(null);
    const [messages, setMessages]         = useState([]);
    const [draft, setDraft]               = useState("");


    useEffect(() => {
        // TODO load Threads from API and set them to state
        setThreads(seedThreads);

        // TODO load Messages of the latest thread
        setActiveThreadId(seedThreads[0].id);


    }, []);

    useEffect(() => {

        // TODO load Messages for active thread from API

        setMessages(seedMessages[activeThreadId]);

    }, [activeThreadId]);
/*
    useEffect(() => {
        const loadThreads = async () => {
            try {
                const token = localStorage.getItem("token");
                const resp = await axios.get("http://localhost:8080/chats", {
                    headers: { "Authorization": `Bearer ${token}` }
                });
                setThreads(resp.data);
                setActiveThreadId(resp.data[0]?.id || null);
            } catch (error) {
                console.error("Failed to load threads", error);
            }
        };

        loadThreads();
    }, []);

    useEffect(() => {
        const loadMessages = async () => {
            if (!activeThreadId) return;

            try {
                const token = localStorage.getItem("token");
                const resp = await axios.get(`http://localhost:8080/chats/${activeThreadId}/messages`, {
                    headers: { "Authorization": `Bearer ${token}` }
                });
                setMessages(resp.data);
            } catch (error) {
                console.error("Failed to load messages", error);
            }
        };

        loadMessages();
    }, [activeThreadId]);
*/
    useEffect(() => {
        if (messagesRef.current) {
            messagesRef.current.scrollTop = messagesRef.current.scrollHeight;
        }
    }, [messages]);

    /* derived helpers */
    const activeThread = threads.find(t => t.id === activeThreadId);

    /* ---- 3. switching threads ----------------------------------------- */
    const setActiveThread = (id) => {
        setActiveThreadId(id);
    };

    /* ---- 4. sending a message ----------------------------------------- */
    const handleSend = async (e) => {
        e.preventDefault();
        if (!draft.trim()) return;

        const tempId = `temp-${Date.now()}`;

        // Create optimistic message (before server response)
        const optimisticMsg = {
            id: tempId,
            content: draft,
            fromSelf: true,
            createdAt: Date.now()
        };
        setMessages(prev => [...prev, optimisticMsg]);
        setDraft("");

        try {
            const token = localStorage.getItem("token");

            // Send message to backend
            const resp = await axios.post("http://localhost:8080/messages", {
                content: draft,
                chatId: activeThreadId
            }, {
                headers: { "Authorization": `Bearer ${token}` },
            });

            const savedMsg = resp.data;

            setMessages(prev => [
                ...prev.filter(msg => msg.id !== tempId),
                savedMsg
            ]);

            if (savedMsg.autoResponse) {
                setMessages(prev => [...prev, {
                    id: `resp-${Date.now()}`,
                    fromSelf: false,
                    content: savedMsg.autoResponse,
                    createdAt: Date.now()
                }]);
            }

        } catch (error) {
            console.error("Failed to send message", error);
        }
    };


    return (
        <div className="home chat-page">
            {/* -- Sidebar ---------------------------------------------------------- */}
            <aside className="chat-sidebar">
                <header className="chat-sidebar-header">Chats</header>

                <ul className="chat-thread-list">
                    {threads.map(t => (
                        <li
                            key={t.id}
                            className={`chat-thread-item ${
                                t.id === activeThread.id ? "active" : ""
                            }`}
                            onClick={() => setActiveThread(t.id)}
                        >
                            <span className="thread-title">{t.title}</span>
                            {t.unread > 0 && (
                                <span className="thread-unread">{t.unread}</span>
                            )}
                        </li>
                    ))}
                </ul>
            </aside>

            {/* -- Chat pane -------------------------------------------------------- */}
            <section className="chat-container">
                <header className="chat-header">{activeThread?.title}</header>

                <div className="chat-messages" ref={messagesRef}>
                    {messages && messages.map(m => (
                        <div
                            key={m.id}
                            className={`chat-bubble ${m.fromSelf ? "self" : "other"}`}
                        >
                            <p>{m.content}</p>
                        </div>
                    ))}
                </div>

                <form className="chat-input-bar" onSubmit={handleSend}>
                    <input
                        className="chat-input"
                        type="text"
                        placeholder="Type a message…"
                        value={draft}
                        onChange={e => setDraft(e.target.value)}
                    />
                    <button type="submit" className="chat-send-btn">
                        Send
                    </button>
                </form>
            </section>
        </div>
    );
}
