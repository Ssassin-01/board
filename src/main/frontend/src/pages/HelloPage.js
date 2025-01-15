import React, { useEffect, useState } from "react";
import api from "../api/axios";

const HelloPage = () => {
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchHelloWorld = async () => {
      try {
        // 로컬 스토리지에서 토큰 가져오기
        const token = localStorage.getItem("token");

        const response = await api.get("/hello", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        setMessage(response.data);
      } catch (error) {
        console.error("Error fetching Hello World:", error);
        setMessage("Failed to fetch Hello World");
      }
    };

    fetchHelloWorld();
  }, []);

  return (
    <div>
      <h1>Hello Page</h1>
      <p>{message}</p>
    </div>
  );
};

export default HelloPage;
